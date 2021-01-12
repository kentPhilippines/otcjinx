package alipay.manage.util;

import alipay.config.redis.RedisUtil;
import alipay.manage.service.MediumService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateBetween;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;
import otc.api.alipay.Common;
import otc.bean.alipay.FileList;
import otc.bean.alipay.Medium;
import otc.common.RedisConstant;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class QueueQrcodeUtil {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    MediumService mediumService;
    @Autowired
    SettingFile settingFile;
    private static final String REDISKEY_QUEUE = RedisConstant.Queue.QUEUE_REDIS;
    
    
    public Set<Object> getList() {
        if (!redisUtil.hasKey(REDISKEY_QUEUE)) {
            List<Medium> findIsDealMedium = mediumService.findIsDealMedium(Common.MEDIUM_ALIPAY);
            for (Medium medium : findIsDealMedium) {
                addNode(medium.getMediumNumber());
            }
        }
        return redisUtil.zRange(REDISKEY_QUEUE, 0, -1);
    }
    
    public boolean addNode(Object alipayAccount) {
    	return addNode(alipayAccount,null);
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
        LinkedHashSet<TypedTuple<Object>> zRangeWithScores = redisUtil.zRangeWithScores(REDISKEY_QUEUE, 0, -1);
        //linkedhashset 保证set集合查询最快
        if (CollUtil.isEmpty(zRangeWithScores)) {
            redisUtil.zAdd(REDISKEY_QUEUE, alipayAccount.toString(), 10);
        }
        Optional<TypedTuple<Object>> reduce = zRangeWithScores.stream().reduce((first, second) -> second);
        TypedTuple<Object> typedTuple = null;
        if (reduce.isPresent()) {
            typedTuple = reduce.get();
        }
        if (ObjectUtil.isNull(typedTuple)) {
            return false;
        }
        Double score = typedTuple.getScore() + 10;//预留10个操作空间
        if (ObjectUtil.isNotNull(qr)) {
            Timestamp createTime = (Timestamp) qr.getCreateTime();//创建时间   多少天之前 .
            String dayCount = settingFile.getName(SettingFile.NEW_QRCODE_PRIORITY);
            Integer day = Integer.valueOf(dayCount);//天数
            DateBetween create = DateBetween.create(createTime, new Date(), true);
            long between = create.between(DateUnit.DAY);
            if (between <= day) {// 置为队列中间
                List<TypedTuple<Object>> collect = zRangeWithScores.stream().collect(Collectors.toList());
                TypedTuple<Object> typedTuple2 = collect.get(zRangeWithScores.size() / 2);
                score = typedTuple2.getScore();
                score -= 0.1;
            }
        }
        return redisUtil.zAdd(REDISKEY_QUEUE, alipayAccount.toString(), score);
    }
    
    private static boolean checkNotNull(Object v) {
        if (v == null || "" == v)
            return false;
        return true;
    }
}
