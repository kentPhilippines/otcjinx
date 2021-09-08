package alipay.config.task;

import alipay.config.listener.ServerConfig;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import otc.result.Result;

@Component
@Configuration
@EnableScheduling
public class TaskSelf {
    private static final Log log = LogFactory.get();
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    UserTask userTaskImpl;
    @Autowired
    OrderTask orderTask;
    @Scheduled(cron = "0/20 * * * * ?")
    public void orderTask() {
        if(serverConfig.getServerPort() != 9010 ){
            log.info("当前任务端口号不正确");
            return;
        }
        log.info("【开始进行每10秒订单清算】");
        orderTask.orderTask();
    }
    @Scheduled(cron = "0/5 * * * * ?")
    public void orderWitTask() {
        if(serverConfig.getServerPort() != 9010 ){
            log.info("当前任务端口号不正确");
            return;
        }
        log.info("【开始进行10秒代付订单推送】");
        orderTask.orderWitTask();
    }


    @Scheduled(cron = "0/10 * * * * ?")
    public void  WitTask() {
        if(serverConfig.getServerPort() != 9011 ){
            log.info("当前任务端口号不正确");
            return;
        }
        log.info("【开始进行10秒代付订单推送】");
    }




}
