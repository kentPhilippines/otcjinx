package alipay.manage.api.channel.amount.recharge;


import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.util.ResultDeal;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

@Component("NameInput")
public class NameInput extends PayOrderService {
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入输入姓名转卡渠道：" + dealOrderApp.getOrderId() + "】");
        return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrlAndPayInfo(PayApiConstant.Notfiy.OTHER_URL + "/pay?orderId=" + dealOrderApp.getOrderId() + "&type=203",""));
    }











}
