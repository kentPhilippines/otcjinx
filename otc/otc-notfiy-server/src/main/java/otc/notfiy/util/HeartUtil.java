package otc.notfiy.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import otc.common.RedisConstant;
import otc.notfiy.redis.RedisUtil;
@Component
public class HeartUtil {
	Logger log = LoggerFactory.getLogger(HeartUtil.class);
	static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	@Autowired RedisUtil redisUtil;
	public boolean testHeartbeat( Map<Object, Object> postjson) {
		log.info("<<<<<进入心跳检测");
		String MD5 = postjson.get("MD5")+"";
		boolean set = redisUtil.set(RedisConstant.Queue.HEARTBEAT+MD5, RedisConstant.Queue.HEARTBEAT+df.format(new Date()),60);//设置心跳过期时间1分钟
		log.info("心跳检测值："+RedisConstant.Queue.HEARTBEAT+MD5+"结果："+set);
		log.info("心跳检测结束>>>>");
		return set;
	}

}
