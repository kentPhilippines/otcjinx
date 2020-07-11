package alipay.config.redis;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {

    /**
     * 最长等待时间
     *
     * @return
     */
    long waitTime() default 3000L;

    /**
     * 额外lock key
     * @return
     */
    String extraKey() default "";
}
