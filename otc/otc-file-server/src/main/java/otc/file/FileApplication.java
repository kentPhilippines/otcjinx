package otc.file;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class FileApplication {
	public static void main(String[] args) {
		int port = 7676;
		new SpringApplicationBuilder(FileApplication.class).properties("server.port=" + port).run(args);
	}
}
