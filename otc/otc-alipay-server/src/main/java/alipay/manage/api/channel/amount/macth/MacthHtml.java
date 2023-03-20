package alipay.manage.api.channel.amount.macth;

import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.util.ResultDeal;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

@Component("MacthHtml")
public class MacthHtml extends PayOrderService {
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        /**
         * 1，根据建立的数据库得出的用户数据判断这个卡商是否要走 撮合交易
         *          1》获取到这个会员的计算数据得到
         *          2》根据计算数据得到相应支付情况
         *
         */












        return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl( PayApiConstant.Notfiy.OTHER_URL+"/macth?orderId=" + dealOrderApp.getOrderId()));
    }
}
