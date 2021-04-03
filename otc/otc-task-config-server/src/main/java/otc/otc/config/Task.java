package otc.otc.config;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import otc.otc.feign.AlipayServiceClien;
import otc.otc.feign.FileServiceClien;
import otc.otc.feign.QueueServiceClien;

/**
 * <p>核心定时任务类</p>
 * @author kent
 * @date 2020-3-31 23:53:00
 */
@Component
@Configuration
@EnableScheduling
public class Task {
	private static final Log log = LogFactory.get();
	@Autowired QueueServiceClien queueServiceClienImpl;
	@Autowired FileServiceClien fileServiceClienImpl;
	@Autowired AlipayServiceClien alipayServiceClienImpl;
	/**
	 * 每分钟踢人
	 */
	@Scheduled(cron = "0 */1 * * * ?")
    public void queue() {
        queueServiceClienImpl.task();
    }

    /**
     * 凌晨一点执行任务
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void userTask() {
        alipayServiceClienImpl.userTask();
    }

    @Scheduled(cron = "*/10 * * * * ?")
    public void orderTask() {
        log.info("订单结算啊");
        alipayServiceClienImpl.orderTask();
    }

    /**
     * 10秒图片剪裁
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void file() {
        fileServiceClienImpl.task();
    }
}
