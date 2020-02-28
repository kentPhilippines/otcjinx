package alipay.config.annotion;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * <p>当出现版本号冲突时候的重试注解</p>
 * @author K
 */
@Retention(RetentionPolicy.RUNTIME)//运行时异常
public @interface IsTryAgain {
}
