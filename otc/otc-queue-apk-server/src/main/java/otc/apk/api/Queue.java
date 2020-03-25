package otc.apk.api;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateBetween;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.util.ObjectUtil;
import otc.api.alipay.Common;
import otc.apk.redis.RedisUtil;
import otc.apk.service.serviceFeign.AlipayServiceClienFeign;
import otc.apk.service.serviceFeign.ConfigServiceClientFeign;
import otc.bean.alipay.FileList;
import otc.bean.alipay.Medium;
import otc.bean.config.ConfigFile;
import otc.common.RedisConstant;
import otc.result.Result;
@Component
public class Queue {
	Logger log= LoggerFactory.getLogger(Queue.class);
    @Autowired AlipayServiceClienFeign alipayServiceClienFeignImpl;
    @Autowired ConfigServiceClientFeign configServiceClientFeignImpl;
    @Autowired RedisUtil redisUtil;
    private static final String REDISKEY_QUEUE = RedisConstant.Queue.QUEUE_REDIS;
    public Set<Object> getList(String[] codes) {
    	if(ObjectUtil.isNull(codes)) {
    		if (!redisUtil.hasKey(REDISKEY_QUEUE)) {
    			List<Medium> findIsDealMedium = alipayServiceClienFeignImpl.findIsDealMedium(Common.Medium.MEDIUM_ALIPAY);
    			log.info("findIsDealMedium 获取的值是：" +findIsDealMedium);
    			for (Medium medium : findIsDealMedium)
    				addNode(medium.getMediumNumber(),"");
    		}
    	return 	redisUtil.zRange(REDISKEY_QUEUE, 0, -1);
    	}
    	for(String code : codes) {
    		if (!redisUtil.hasKey(REDISKEY_QUEUE+code)) {
    			List<Medium> findIsDealMedium = alipayServiceClienFeignImpl.findIsDealMedium(Common.Medium.MEDIUM_ALIPAY,code);
    			log.info("findIsDealMedium 获取的值是：" +findIsDealMedium);
    			for (Medium medium : findIsDealMedium)
    				addNode(medium.getMediumNumber(),code);
    		}
    	}
    	Set<Object> zRange = null;
    	for(String code : codes) {
    		Set<Object> zRange2 = redisUtil.zRange(REDISKEY_QUEUE+code, 0, -1);
    		if(CollUtil.isEmpty(zRange))
    			zRange = zRange2;
    		else 
    			for(Object obj : zRange2)
    				zRange.add(obj);
		}
        return zRange;
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
    public boolean addNode(Object alipayAccount,String code) {
        return addNode(alipayAccount,null,code);
    }


    private static boolean checkNotNull(Object v) {
        if (v == null || "" == v)
            return false;
        return true;
    }
    /**
     * <p>删除队列元素</p>
     * @param alipayAccount
     * @param code 
     * @return
     */
    public boolean deleteNode(Object alipayAccount, String code) {
        return redisUtil.zRemove(REDISKEY_QUEUE+code, alipayAccount) > 0;
    }
    /**
     * <p>更新队列</p>
     * @param alipayAccount
     * @param qr
     * @return
     */
    public boolean updataNode(Object alipayAccount, FileList qr,String code) {
        if (deleteNode(alipayAccount,code))
            if (addNode(alipayAccount,qr,code))
                return true;
        return false;
    }
    public boolean updataNode(Object alipayAccount,String code) {
        if (deleteNode(alipayAccount,code))
            if (addNode(alipayAccount,code))
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
     * @param code 						队列code值
     * @return
     */
    public boolean addNode(Object alipayAccount, FileList qr,String code) {
        if (!checkNotNull(alipayAccount))
            return false;
        LinkedHashSet<TypedTuple<Object>> zRangeWithScores = redisUtil.zRangeWithScores(REDISKEY_QUEUE+code, 0, -1);//linkedhashset 保证set集合查询最快
        if (CollUtil.isEmpty(zRangeWithScores))
            redisUtil.zAdd(REDISKEY_QUEUE+code, alipayAccount.toString(), 10);
        Optional<TypedTuple<Object>> reduce = zRangeWithScores.stream().reduce((first, second) -> second);
        TypedTuple<Object> typedTuple = null;
        if (reduce.isPresent())
            typedTuple = reduce.get();
        if (ObjectUtil.isNull(typedTuple))
            return false;
        Double score = typedTuple.getScore() + 10;//预留10个操作空间
        if(ObjectUtil.isNotNull(qr)) {
            Timestamp createTime = (Timestamp) qr.getCreateTime();//创建时间   多少天之前 .
            Result dayCount = configServiceClientFeignImpl.getConfig(ConfigFile.ALIPAY,ConfigFile.Alipay.NEW_QRCODE_PRIORITY);
            Integer day = Integer.valueOf(dayCount.getResult().toString());//天数
            DateBetween create = DateBetween.create(createTime, new Date(), true);
            long between = create.between(DateUnit.DAY);
            if(between <= day) {// 置为队列中间
                List<TypedTuple<Object>> collect = zRangeWithScores.stream().collect(Collectors.toList());
                TypedTuple<Object> typedTuple2 = collect.get(zRangeWithScores.size()/2);
                score = typedTuple2.getScore();
                score-=0.1;
            }
        }
        return redisUtil.zAdd(REDISKEY_QUEUE + code, alipayAccount.toString(), score);
    }
}
