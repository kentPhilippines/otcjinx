package otc.zuul;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import cn.hutool.core.util.NetUtil;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
@EnableDiscoveryClient
public class ZuulApplication {
    public static void main(String[] args) {
        int port = 5055;
        while(!NetUtil.isUsableLocalPort(port)) {
            System.err.print("端口%d被占用了，无法启动%n"); 
            port += 1;
        }
        new SpringApplicationBuilder(ZuulApplication.class).properties("server.port=" + port).run(args);
    }
}