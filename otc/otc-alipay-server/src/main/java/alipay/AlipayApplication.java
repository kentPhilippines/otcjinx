package alipay;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>宝转宝支付接口服务</p>
 * @author K
 */
@SpringBootApplication
@EnableEurekaClient		//服务注册
@EnableFeignClients		//远程调用
@ServletComponentScan(basePackages = "alipay.*")
@ComponentScan(basePackages = "alipay.*") //注入扫描
@EnableTransactionManagement //事务
//@EnableConfigurationProperties //读取外部配置
@EnableRedisHttpSession //redis   session 共享
public class AlipayApplication {
	public static void main(String[] args) {
		int port = 9010;
		new SpringApplicationBuilder(AlipayApplication.class).properties("server.port=" + port).run(args);
	}
}
