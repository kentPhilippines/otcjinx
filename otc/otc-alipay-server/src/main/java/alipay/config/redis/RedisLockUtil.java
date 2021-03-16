package alipay.config.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedisLockUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisLockUtil.class);
    public static final String AMOUNT_USER_KEY = "AMOUNT:USER:KEY:";
    @Resource
    private RedisCache cache;
    long waitTime = 3000;
    public void redisLock(String lock) {
        LOGGER.info("当前锁：{}", lock);
        boolean tryLock = cache.lock(lock);
        LOGGER.info("{}锁获取状态：{} ", lock, tryLock);
        long start = System.currentTimeMillis();
        while (!tryLock) {
            // 获取锁
            if (System.currentTimeMillis() - start > waitTime) {
                break;
            }
            tryLock = cache.lock(lock);
            if (tryLock) {
                LOGGER.info("{}重获锁状态：{} ", lock, tryLock);
            }
        }
    }

    public boolean isOk(String lock) {
        LOGGER.info("当前锁：{}", lock);
        boolean tryLock = cache.lock(lock);
        return tryLock;
    }


    public void unLock(String lock) {
        LOGGER.info("当前锁释放：{}", lock);
        cache.unlock(lock);
    }
}
