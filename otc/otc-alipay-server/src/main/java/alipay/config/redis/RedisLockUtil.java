package alipay.config.redis;

import cn.hutool.core.thread.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedisLockUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisLockUtil.class);
    @Resource
    private RedisCache cache;

    public void redisLock(String lock) {
        LOGGER.info("当前锁：{}", lock);
        boolean tryLock = cache.lock(lock);
        LOGGER.info("{}锁获取状态：{} ", lock, tryLock);
        while (!tryLock) {
            // 获取锁
            long l = System.currentTimeMillis();
            Long a = l % 2;
            if (a == 0) {
                a = a + 3;
            }
            ThreadUtil.sleep(20 + a);
            tryLock = cache.lock(lock);
            LOGGER.info("【不断重复请求当前锁：" + lock + "，当前锁状态：" + tryLock + "】");
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
