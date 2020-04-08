package alipay.config.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class LoginConfig extends WebMvcConfigurationSupport{
	@Autowired
	SubmitInterceptor loginInterceptor;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginInterceptor).addPathPatterns("/**").
		excludePathPatterns("/login","/static/**");
		super.addInterceptors(registry);
	}
	/**
	 * 	默认情况下，Spring Boot从类路径中名为/static（/public或/resources或/META-INF/resources）的目录或根目录提供静态内容ServletContext。
	 * 	它使用ResourceHttpRequestHandlerSpring MVC中的from，
	 * 	因此您可以通过添加自己WebMvcConfigurer的addResourceHandlers方法并覆盖该方法来修改该行为。
	 */
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		super.addResourceHandlers(registry);
	}
}
