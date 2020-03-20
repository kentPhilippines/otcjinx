package alipay.manage.util;
import alipay.config.redis.RedisUtil;
import alipay.manage.bean.FileList;
import alipay.manage.bean.Medium;
import alipay.manage.service.FileListService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateBetween;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;
import otc.api.alipay.Common;
import otc.common.RedisConstant;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class QueueQrcodeUtil {
    Logger log= LoggerFactory.getLogger(QueueQrcodeUtil.class);
    @Autowired
    FileListService fileListService;
    @Autowired
    SettingFile settingFile;
    @Autowired
    RedisUtil redisUtil;
    private static final String REDISKEY_QUEUE = RedisConstant.Queue.QUEUE_REDIS;
    public Set<Object> getList() {
        if (!redisUtil.hasKey(REDISKEY_QUEUE)) {
            List<Medium> findIsDealMedium = fileListService.findIsDealMedium(Common.MEDIUM_ALIPAY);
            log.info("findIsDealMedium 获取的值是：" +findIsDealMedium);
            for (Medium medium : findIsDealMedium)
                addNode(medium.getMediumNumber());
        }
        return redisUtil.zRange(REDISKEY_QUEUE, 0, -1);
    }
    /**
     * <p>获取队列尾列下标</p>
     * @return
     */
    public Integer getRear() {
        return Double.valueOf(redisUtil.zSize(REDISKEY_QUEUE) - 1).intValue();
    }
    /**
     * <p>获取index所对应的元素，这种随机访问队列相对于普通队列的最重要的作用</p>
     * @param index 需要访问的元素序号（从队首到队尾的序号，队首序号为0）
     * @return index所对应的元素，index超出队列的长度则返回null
     */
    public Object get(int index) {
        return CollUtil.getFirst(redisUtil.zRange(REDISKEY_QUEUE, index, index++));
    }
    /**
     * <p>出列</p>
     *
     * @return K
     */
    public Object pop() {
        if (!redisUtil.hasKey(REDISKEY_QUEUE))
            return null;
        Set<Object> zRange = redisUtil.zRange(REDISKEY_QUEUE, 0, 1);
        redisUtil.zRemoveRange(REDISKEY_QUEUE, 0, 1);
        return CollUtil.getFirst(zRange);
    }
    public boolean addNode(Object alipayAccount) {
        return addNode(alipayAccount,null);
    }


    private static boolean checkNotNull(Object v) {
        if (v == null || "" == v)
            return false;
        return true;
    }
    private void clear() {
        redisUtil.del(REDISKEY_QUEUE);
    }
    private boolean isEmpty() {
        return redisUtil.hasKey(REDISKEY_QUEUE);
    }
    /**
     * <p>删除队列元素</p>
     * @param alipayAccount
     * @return
     */
    public boolean deleteNode(Object alipayAccount) {
        return redisUtil.zRemove(REDISKEY_QUEUE, alipayAccount) > 0;
    }
    /**
     * <p>更新队列</p>
     * @param alipayAccount
     * @param qr
     * @return
     */
    public boolean updataNode(Object alipayAccount, FileList qr) {
        if (deleteNode(alipayAccount))
            if (addNode(alipayAccount,qr))
                return true;
        return false;
    }
    public boolean updataNode(Object alipayAccount) {
        if (deleteNode(alipayAccount))
            if (addNode(alipayAccount))
                return true;
        return false;
    }
    public boolean addNodeRightToLeft(Object alipayAccount) {
        return addNodeRightToLeft(alipayAccount, null);
    }
    /**
     * 	<p>根据score值从右往左入列</p>
     * @param alipayAccount
     * @return
     */
    public boolean addNodeRightToLeft(Object alipayAccount, Object index) {
        if (!checkNotNull(alipayAccount))
            return false;
        LinkedHashSet<TypedTuple<Object>> zRangeWithScores = redisUtil.zRangeWithScores(REDISKEY_QUEUE, 0, -1);
        if (CollUtil.isEmpty(zRangeWithScores))
            redisUtil.zAdd(REDISKEY_QUEUE, alipayAccount.toString(), 10);
        Double score = 0.00;
        if (checkNotNull(index)) {
            Object[] str = zRangeWithScores.toArray();
            List<Object> list = CollUtil.toList(str);
            @SuppressWarnings("unchecked")
            TypedTuple<Object> member = (TypedTuple<Object>) list.get(Integer.parseInt(index.toString()));
            score = member.getScore() - 0.1;
        } else {
            //获取队列首个元素的score值
            TypedTuple<Object> typedTuple = CollUtil.getFirst(zRangeWithScores);
            Double score2 = typedTuple.getScore();
            score = score2 - 0.1;//预留10个操作空间
        }
        if (score == 0.00)
            return false;
        return redisUtil.zAdd(REDISKEY_QUEUE, alipayAccount.toString(), score);
    }
    /**
     * <p>支付宝入列</p>
     * @param alipayAccount				支付宝账户号
     * @param qr						二维码具体参数
     * @return
     */
    public boolean addNode(Object alipayAccount, FileList qr) {
        if (!checkNotNull(alipayAccount))
            return false;
        LinkedHashSet<TypedTuple<Object>> zRangeWithScores = redisUtil.zRangeWithScores(REDISKEY_QUEUE, 0, -1);//linkedhashset 保证set集合查询最快
        if (CollUtil.isEmpty(zRangeWithScores))
            redisUtil.zAdd(REDISKEY_QUEUE, alipayAccount.toString(), 10);
        Optional<TypedTuple<Object>> reduce = zRangeWithScores.stream().reduce((first, second) -> second);
        TypedTuple<Object> typedTuple = null;
        if (reduce.isPresent())
            typedTuple = reduce.get();
        if (ObjectUtil.isNull(typedTuple))
            return false;
        Double score = typedTuple.getScore() + 10;//预留10个操作空间
        if(ObjectUtil.isNotNull(qr)) {
            Timestamp createTime = (Timestamp) qr.getCreateTime();//创建时间   多少天之前 .
            String dayCount = settingFile.getName(settingFile.NEW_QRCODE_PRIORITY);
            Integer day = Integer.valueOf(dayCount);//天数
            DateBetween create = DateBetween.create(createTime, new Date(), true);
            long between = create.between(DateUnit.DAY);
            if(between <= day) {// 置为队列中间
                List<TypedTuple<Object>> collect = zRangeWithScores.stream().collect(Collectors.toList());
                TypedTuple<Object> typedTuple2 = collect.get(zRangeWithScores.size()/2);
                score = typedTuple2.getScore();
                score-=0.1;
            }
        }
        return redisUtil.zAdd(REDISKEY_QUEUE, alipayAccount.toString(), score);
    }
}
