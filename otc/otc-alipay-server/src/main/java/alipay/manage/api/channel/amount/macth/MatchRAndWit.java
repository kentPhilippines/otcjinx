package alipay.manage.api.channel.amount.macth;


import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import org.springframework.stereotype.Component;
import otc.result.Result;


/**
 * 撮合订单交易
 */
@Component("MatchRAndWit")
public class MatchRAndWit extends PayOrderService {

    /**
     * 1,撮合订单首先建立 交易 预订单
     * 2,查看是否有符合出款要求的出款订单
     * 3,存在符合要求的出款订单，进行撮合标记
     * 4,返回出款页面的链接
     */

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {




        return  Result.buildFailMessage("暂无支付资源");
    }
}
