package alipay.config.task;

import alipay.manage.api.channel.amount.recharge.usdt.UsdtPayOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class USDTTask {
    @Autowired
    private UsdtPayOut usdtPayOutImpl;

    @Scheduled(cron = "0/5 * * * * ?")
    public void usdt() {
        usdtPayOutImpl.findDealOrderLog();
    }

}
