package otc.otc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
	@Autowired QueueServiceClien queueServiceClienImpl;
	@Scheduled(cron = "0 */1 * * * ?")
	public void queue() {
		queueServiceClienImpl.task();
	}
	
	
}
