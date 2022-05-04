package alipay.manage.api.channel.deal;

import alipay.config.redis.RedisUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.util.ResultDeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.util.HashMap;
import java.util.Map;

@Component("RchargeOnlinePay")
public class RchargeOnlinePay extends PayOrderService {
    private static final String MARS = "SHENFU";
    @Autowired
    private RedisUtil redis;
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        String create = create(dealOrderApp, channel);
        Result result = createOrder(
                dealOrderApp
        );
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            orderEr(dealOrderApp, "错误消息：" + result.getMessage());
            return result;
        }
    }

    private Result createOrder(DealOrderApp dealOrderApp) {
        Map cardmap = new HashMap();
        cardmap.put("bank_name", "0");
        cardmap.put("card_no", "0");
        cardmap.put("card_user", "0");
        cardmap.put("oid_partner", "0");
        cardmap.put("money_order", dealOrderApp.getOrderAmount());
        cardmap.put("no_order", dealOrderApp.getOrderId());
        redis.hmset(MARS + dealOrderApp.getOrderId(), cardmap, 900000);
        return Result.buildSuccessResult( PayApiConstant.Notfiy.OTHER_URL + "/online?orderId=" + dealOrderApp.getOrderId()  );

    }
}
