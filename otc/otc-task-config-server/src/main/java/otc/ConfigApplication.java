package otc;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import cn.hutool.core.util.NetUtil;
/**
 * <p>全局定时任务执行和全局配置中心</p>
 * <p>这里不采取cloud配置中心因为安全原因和考虑到网络原因</p>
 * <p>定时任务这里全部采取接口调用的方式</p>
 * <p></p>
 * @author K
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class ConfigApplication {
	public static void main(String[] args) {
		int defaultPort = 2232;
		while(!NetUtil.isUsableLocalPort(defaultPort)) {
			System.err.print("端口%d被占用了，无法启动%n");
			defaultPort += 1;
		}
		new SpringApplicationBuilder(ConfigApplication.class).properties("server.port=" + defaultPort).run(args);
	}
}
