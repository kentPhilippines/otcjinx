package alipay.config.redis;


import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCache {
    public static final Log log = LogFactory.get();
    private static final String LOCK_KEY = "redis-lock:";
    private static final Long DEFAULT_TRY_LOCK_MILLIS = TimeUnit.SECONDS.toMillis(3);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public String evic(String key) {
        this.redisTemplate.delete(key);
        return key;
    }

    public Long evic(Collection keys) {
        return this.redisTemplate.delete(keys);
    }

    public Long increase(String key) {
        return this.redisTemplate.opsForValue().increment(key, 1);
    }

    public Long increase(String key, long delta) {
        return this.redisTemplate.opsForValue().increment(key, delta);
    }

    public Long decrease(String key) {
        return this.redisTemplate.opsForValue().increment(key, -1);
    }

    public Long decrease(String key, long delta) {
        return this.redisTemplate.opsForValue().increment(key, delta);
    }

    public Boolean existed(String key) {
        return Optional.ofNullable(this.redisTemplate.hasKey(key)).orElse(false);
    }

    public <T> T get(String key) {
        return (T) this.redisTemplate.opsForValue().get(key);
    }

    public <T> T cache(String key, T value) {
        this.redisTemplate.opsForValue().set(key, value);
        return value;
    }

    public <T> T updateCache(String key, T value) {
        if (existed(key)) {
            this.redisTemplate.opsForValue().set(key, value);
        }
        return value;
    }

    public <T> T cache(String key, T value, long last) {
        this.redisTemplate.opsForValue().set(key, value, last, TimeUnit.SECONDS);
        return value;
    }

    public <T> T cache(String key, T value, long last, TimeUnit unit) {
        this.redisTemplate.opsForValue().set(key, value, last, unit);
        return value;
    }

    public <T> Set<T> listGetAll(String key) {
        return (Set<T>) this.redisTemplate.opsForSet().members(key);
    }

    public <T> T listAdd(String key, T value) {
        this.redisTemplate.opsForSet().add(key, value);
        return value;
    }

    public Long listSize(String key) {
        return this.redisTemplate.opsForSet().size(key);
    }

    public <T> T listRemove(String key, T value) {
        this.redisTemplate.opsForSet().remove(key, value);
        return value;
    }

    public Long listRemove(String key, Object... values) {
        return this.redisTemplate.opsForSet().remove(key, values);
    }

    public Long listClear(String key) {
        long count = this.redisTemplate.opsForSet().size(key);
        this.redisTemplate.opsForSet().pop(key, count);
        return count;
    }

    public Boolean listContains(String key, Object value) {
        return this.redisTemplate.opsForSet().isMember(key, value);
    }

    public <T> T zsetAddOrUpdate(String key, T value, double score) {
        this.redisTemplate.opsForZSet().add(key, value, score);
        return value;
    }

    public <T> Set<T> zsetGetFromTop(String key, int n) {
        if (n <= 0) {
            return (Set<T>) this.redisTemplate.opsForZSet().range(key, 0, -1);
        } else {
            return (Set<T>) this.redisTemplate.opsForZSet().range(key, 0, n);
        }
    }

    public <T> Set<T> zsetGetByScore(String key, double score) {
        return (Set<T>) this.redisTemplate.opsForZSet().rangeByScore(key, score, Integer.MAX_VALUE);
    }

    public Long zsetIndex(String key, Object value) {
        return this.redisTemplate.opsForZSet().rank(key, value);
    }

    public Double zsetScore(String key, Object value) {
        return this.redisTemplate.opsForZSet().score(key, value);
    }

    public Long zsetRemove(String key, Object... value) {
        return this.redisTemplate.opsForZSet().remove(key, value);
    }

    public Long zsetSize(String key) {
        return this.redisTemplate.opsForZSet().size(key);
    }

    public boolean lock(String key) {
        String lock = LOCK_KEY + key;
        String millis = System.currentTimeMillis() + DEFAULT_TRY_LOCK_MILLIS + 1 + "";
        Boolean r = redisTemplate.opsForValue().setIfAbsent(lock, millis);
        if (null != r && r) {
            return true;
        }
        String curVal = this.get(lock);
        // 过期了允许从新获取锁
        if (StringUtils.isNotEmpty(curVal) && Long.parseLong(curVal) < System.currentTimeMillis()) {
            this.cache(lock, millis);
            return true;
        }
        return false;
    }

    public void unlock(String key) {
        String lock = LOCK_KEY + key;
        log.info("【删除key：" + lock + "】");
        this.evic(lock);
    }

    public void publish(String queue, Object msg) {
        this.redisTemplate.convertAndSend(queue, msg);
    }

    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }
}
