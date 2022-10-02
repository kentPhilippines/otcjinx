package alipay.config.task;

import alipay.config.listener.ServerConfig;
import alipay.manage.api.channel.amount.BalanceInfo;
import alipay.manage.api.channel.util.QueryBalanceTool;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import otc.result.Result;
import otc.util.DesUtil2;

import java.util.Date;
import java.util.List;

@Component
@Configuration
@EnableScheduling
public class TaskSelf {
    private static final Log log = LogFactory.get();
    @Autowired private ServerConfig serverConfig;
    @Autowired private ChannelQueryOrder channelQueryOrder;
    @Autowired private UserTask userTaskImpl;
    @Autowired private OrderTask orderTask;
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
        log.info("【撮合订单解锁】");
        orderTask.macthOrder();
        log.info("【开始订单解密】");
        orderTask.decryptStr();
    }


    @Scheduled(cron = "0/10 * * * * ?")
    public void  WitTask() {
        if(serverConfig.getServerPort() != 9010 ){
            log.info("当前任务端口号不正确");
            return;
        }
        log.info("【开始查询渠道代付数据】");
        channelQueryOrder.query();
    }

 //   @Scheduled(cron = "* */50 * * * ?")
    public void  pushChannelAmount() {
        if(serverConfig.getServerPort() != 9012 ){
            log.info("当前任务端口号不正确");
            return;
        }
        List<BalanceInfo> balance = QueryBalanceTool.findBalance();
        for (BalanceInfo info : balance){
            String msg = "";
            msg = "当前渠道为："+ info.getChannel() + " ，当前余额为："+info.getBalance() +"， 当前时间为："+info.getTime();
            push(msg);
        }

    }
    @Scheduled(cron = "0 0 1 * * ?")
    public void userTask() {
        if (serverConfig.getServerPort() != 9010) {
            log.info("当前任务端口号不正确");
            return;
        }
        log.info("【开始进行每日账户清算】");
        userTaskImpl.userAddTask();
        userTaskImpl.userTask();

    }



    void push(String msg) {
        ThreadUtil.execute(() -> {
            String url = "http://172.29.17.156:8889/api/send?text=";
            String test = msg + "，触发时间：" + DatePattern.NORM_DATETIME_FORMAT.format(new Date());
            test = HttpUtil.encode(test, "UTF-8");
            String id = "&id=";
            String ids = "-1001464340513,480024035";
            id += ids;
            String s = url + test + id;
            HttpUtil.get(s, 1000);
        });
    }
}
