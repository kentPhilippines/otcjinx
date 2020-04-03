package otc.file;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import cn.hutool.core.util.NetUtil;


@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class FileApplication {
	public static void main(String[] args) {
		int defaultPort = 7676;
		while(!NetUtil.isUsableLocalPort(defaultPort)) {
			System.err.print("端口%d被占用了，无法启动%n");
			 defaultPort += 1;
		}
		new SpringApplicationBuilder(FileApplication.class).properties("server.port=" + defaultPort).run(args);
	}
}
