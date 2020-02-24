package pay;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <p>四方接口服务</p>
 * @author K
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class Application {
	public static void main(String[] args) {
		int port = 6010;
		new SpringApplicationBuilder(Application.class).properties("server.port=" + port).run(args);
	}
}
