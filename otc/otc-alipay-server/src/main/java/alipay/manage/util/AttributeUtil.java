package alipay.manage.util;

import alipay.config.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *码商是否符合当前接单要求
 */
@Component
public class AttributeUtil {
    private   final String REDIS_KEY = "UAER_ATTRIBUTE_KEY:";
    @Autowired  RedisUtil redisUtil;
    /**
     * 清楚Redis中的数据
     */
    public void deleteRedis() {
        redisUtil.deleteKey(REDIS_KEY);
    }
}
