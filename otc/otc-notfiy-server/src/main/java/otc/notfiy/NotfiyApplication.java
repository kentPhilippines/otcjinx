package otc.notfiy;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class NotfiyApplication {
	public static void main(String[] args) {
		int port = 12312;
		new SpringApplicationBuilder(NotfiyApplication.class).properties("server.port=" + port).run(args);
	}

}
