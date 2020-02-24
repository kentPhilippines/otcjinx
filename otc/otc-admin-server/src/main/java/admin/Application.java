package admin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;



/**
 * <strong><p>总后台管理</p></strong>
 * @author K
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableDiscoveryClient
public class Application {
	static Logger log = LoggerFactory.getLogger(Application.class);
	public static void main(String[] args) {
		   int port = 8010;
		/*   Future<Integer> execAsync = ThreadUtil.execAsync(()->{
			   //TODO 当前位置需求为  自动检测端口是否被占用   占用端口号+1     每台服务限  10个端口
			   boolean flag = true;
			   if(flag)
					return 10000;
				return 10000;
		   });
		   try {
			Integer integer = execAsync.get();
			port = integer;
		} catch (InterruptedException | ExecutionException e) {
			log.info("当前服务端口获取异常");
			port+=10;
		} 
		*/
	  new SpringApplicationBuilder(Application.class).properties("server.port=" + port).run(args);
	}

}
