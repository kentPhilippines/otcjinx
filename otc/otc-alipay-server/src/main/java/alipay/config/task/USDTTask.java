package alipay.config.task;

import alipay.config.redis.RedisLockUtil;
import alipay.manage.api.channel.amount.recharge.usdt.UsdtPayOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Configuration
@EnableScheduling
public class USDTTask {
    @Autowired
    private UsdtPayOut usdtPayOutImpl;
    private static final String KEY = "findDealOrderLog";
    @Autowired
    private RedisLockUtil redisLockUtil;

    @Scheduled(cron = "0/30 * * * * ?")
    public void usdt() {
        try {
            if (redisLockUtil.isOk(KEY)) {
                usdtPayOutImpl.findDealOrderLog();
            }
            ;
        } catch (Exception e) {
        } finally {
            redisLockUtil.unLock(KEY);
        }

    }

}
