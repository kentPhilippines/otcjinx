package alipay.config.task;

import alipay.config.redis.RedisLockUtil;
import alipay.manage.api.channel.amount.recharge.usdt.UsdtPayOut;
import alipay.manage.service.WithdrawService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import otc.bean.dealpay.Withdraw;

@Component
@Configuration
@EnableScheduling
public class USDTTask {
    private static final Log log = LogFactory.get();
    @Autowired
    private UsdtPayOut usdtPayOutImpl;
    private static final String KEY = "findDealOrderLog";
    @Autowired
    private RedisLockUtil redisLockUtil;
    @Autowired
    WithdrawService withdrawServiceImpl;

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

    @Scheduled(cron = "0/15 * * * * ?")
    public void ethFee() {
        try {
            if (redisLockUtil.isOk(KEY)) {
                Withdraw wit = withdrawServiceImpl.findEthFee();
                if (null != null && StrUtil.isNotEmpty(wit.getOrderId())) {
                    usdtPayOutImpl.autoWitUSDT(wit.getOrderId());
                } else {
                    log.info("【当前不存在未结算订单】");
                }
            }
            ;
        } catch (Exception e) {
        } finally {
            redisLockUtil.unLock(KEY);
        }

    }

}
