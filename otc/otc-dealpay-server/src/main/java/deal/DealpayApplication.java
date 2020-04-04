package deal;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class DealpayApplication {
	public static void main(String[] args) {
		int port = 7010;
		new SpringApplicationBuilder(DealpayApplication.class).properties("server.port=" + port).run(args);
	}
}
