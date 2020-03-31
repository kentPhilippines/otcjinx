package alipay.config.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * <p>防重复提交</p>
 * @author K
 * <p><strong>当前注解要与@LogMonitor注解  结合使用才有效</strong></p>
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Submit {
	boolean required();
}
