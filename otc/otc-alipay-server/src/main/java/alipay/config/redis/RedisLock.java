package alipay.config.redis;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
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
