package alipay.manage.api.channel.amount.recharge.usdt;

import alipay.manage.api.config.PayOrderService;
import org.springframework.stereotype.Component;
import otc.bean.dealpay.Withdraw;
import otc.result.Result;

@Component("UsdtWit")
public class UsdtWit extends PayOrderService implements USDT {
    @Override
    public Result withdraw(Withdraw wit) {
        return Result.buildSuccessMessage("USDT代付处理中");
    }
}
