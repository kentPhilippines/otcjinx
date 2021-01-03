package alipay.manage.api.channel.deal;

import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import org.springframework.stereotype.Component;
import otc.result.Result;

@Component("RchargePay")
public class RchargePay extends PayOrderService {
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        String create = create(dealOrderApp, channel);
        return Result.buildSuccessMessage("支付处理中");
    }
}
