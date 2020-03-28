package otc.apk;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <p>队列数据独立服务</p>
 * @author K
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class ApkApplication {
	public static void main(String[] args) {
		int port = 1232;
		new SpringApplicationBuilder(ApkApplication.class).properties("server.port=" + port).run(args);
	}
}
