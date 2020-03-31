package otc.apk.util;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import otc.apk.feign.AlipayServiceClien;
import otc.apk.redis.RedisUtil;
import otc.bean.alipay.Medium;
import otc.common.RedisConstant;
import otc.result.Result;
import otc.util.RSAUtils;

public class HeartUtil {
	@Autowired RedisUtil redisUtil;
    private static final String REDISKEY_QUEUE = RedisConstant.Queue.QUEUE_REDIS;//队列储存数据
    private static final String DATA_QUEUE_HASH = RedisConstant.Queue.MEDIUM_HASH;//本地数据储存键
    private static final String HEARTBEAT =  RedisConstant.Queue.HEARTBEAT;//心跳数据标记
    private static final Log log = LogFactory.get();
    @Autowired AlipayServiceClien AlipayServiceClienImpl;
	public void clickHeart() {
		/**
		 * ####################################
		 * 1,拿到所有队列数据
		 */
		log.info("【当前模糊匹配键值："+REDISKEY_QUEUE+"】");
		Set<String> keys = redisUtil.keys(REDISKEY_QUEUE);
		log.info("【当前收款媒介个数："+keys.size()+"】");
		for(String  key: keys) {
			log.info("【当前检查key ："+key+"】");
			 LinkedHashSet<TypedTuple<Object>> withScores = redisUtil.zRangeWithScores(key, 0, -1); 
			 for(TypedTuple tuple : withScores) {
				 Object value = tuple.getValue();
				 Double score = tuple.getScore();
				 log.info("【当前队列元素：value："+value+"，score："+score+"】");
				 String md5 = RSAUtils.md5( DATA_QUEUE_HASH+value);
				 log.info("【当前本地数据储存键："+md5+"】");
				 Medium med = (Medium)redisUtil.hget(DATA_QUEUE_HASH,md5);
				 String phone = med.getMediumPhone();
				 log.info("【当前收款媒介绑定手机号："+phone+"】");
				 String md52 = RSAUtils.md5( HEARTBEAT+value);
				 /**
				  * 验证当前媒介是否在redis中
				  */
				 boolean hasKey = redisUtil.hasKey(md52);
				 if(!hasKey) {//不在的时候 讲该媒介踢出队列
					 Result offMediumQueue = AlipayServiceClienImpl.offMediumQueue(value.toString());
					 if(offMediumQueue.isSuccess())
						 log.info("【已将收款媒介："+value.toString()+"，成功踢出队列】");
				 }
			 }
		}
		
	}
	
	
	
	
	
	
	
}
