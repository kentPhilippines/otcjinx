package alipay;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.NumberUtil;

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
			int port = 0;
			int defaultPort = 9010;
	        Future<Integer> future = ThreadUtil.execAsync(() ->{
	                int p = 0;
	                Scanner scanner = new Scanner(System.in);
	                while(true) {
	                    String strPort = scanner.nextLine();
	                    if(!NumberUtil.isInteger(strPort)) {
	                        System.err.println("只能是数字");
	                        continue;
	                    }
	                    else {
	                        p = Convert.toInt(strPort);
	                        scanner.close();
	                        break;
	                    }
	                }
	                return p;
	        });
	            try {
					port=future.get(5,TimeUnit.SECONDS);
				} catch (InterruptedException | ExecutionException | TimeoutException e) {
					 port = defaultPort;
				}
	            
	        if(!NetUtil.isUsableLocalPort(port)) {
	            System.err.print("端口%d被占用了，无法启动%n"); 
	            port = ++defaultPort;
	        }
	        if(!NetUtil.isUsableLocalPort(port)) {
	        	System.err.print("端口%d被占用了，无法启动%n"); 
	        	port = ++defaultPort;
	        }
	        if(!NetUtil.isUsableLocalPort(port)) {
	        	System.err.print("端口%d被占用了，无法启动%n"); 
	        	port = ++defaultPort;
	        }
	        if(!NetUtil.isUsableLocalPort(port)) {
	        	System.err.print("端口%d被占用了，无法启动%n"); 
	        	port = ++defaultPort;
	        }
	        
	        new SpringApplicationBuilder(AlipayApplication.class).properties("server.port=" + port).run(args);
	}
}
