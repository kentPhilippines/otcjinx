package alipay;

import alipay.manage.bean.DealOrder;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.*;

/**
 * <p>宝转宝支付接口服务</p>
 * @author K
 */
@SpringBootApplication
@EnableEurekaClient        //服务注册
@EnableFeignClients        //远程调用
@ServletComponentScan(basePackages = "alipay.*")
@ComponentScan(basePackages = "alipay.*") //注入扫描
@EnableTransactionManagement //事务
@EnableRedisHttpSession //redis   session 共享
@EnableAspectJAutoProxy(proxyTargetClass = true)
//@NacosPropertySource(dataId = "${nacos.config.data-id}",autoRefreshed = true)
public class AlipayApplication {
	public static void main(String[] args) {
		SpringApplication.run(AlipayApplication.class, args);
 	}

}
