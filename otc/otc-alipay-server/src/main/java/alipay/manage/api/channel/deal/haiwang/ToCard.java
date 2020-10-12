package alipay.manage.api.channel.deal.haiwang;

import alipay.manage.api.config.PayOrderService;
import alipay.manage.api.feign.ConfigServiceClient;
import alipay.manage.bean.DealOrderApp;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.bean.config.ConfigFile;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;

/**
 * 海王转卡
 */
@Component("haiwangTocard")
public class ToCard extends PayOrderService {
    private static final Log log = LogFactory.get();
    @Autowired
    ConfigServiceClient configServiceClientImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入海王转银行卡】");
        String create = create(dealOrderApp, channel);
        if (StrUtil.isNotBlank(create)) {
            log.info("【本地订单创建成功，开始请求远程三方支付】");
            Result config = configServiceClientImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.SERVER_IP);
            log.info("【回调地址ip为：" + config.toString() + "】");
            String url = createOrder(dealOrderApp, config.getResult() + PayApiConstant.Notfiy.NOTFIY_API_WAI + "/haofu-notfiy", dealOrderApp.getOrderAmount(), create);
            if (StrUtil.isBlank(url)) {
                log.info("【豪富支付宝转卡支付失败，订单号为：" + create + "】");
            } else {
                return Result.buildSuccessResultCode("支付处理中", url, 1);
            }
        }
        return Result.buildFailMessage("支付失败");

    }

    private String createOrder(DealOrderApp dealOrderApp, String notifys, BigDecimal orderAmount, String orderId) {
        String pid = Util.PID;
        int i = orderAmount.intValue();
        String money = i + ".00";
        String sn = orderId;
        String pay_type_group = Util.TYPE;
        String notify_url = notifys;
        String key = Util.KEY;
        String s = "pid=" + pid + "&money=" + money + "&sn=" + sn + "&pay_type_group" +
                pay_type_group + "&notify_url=" + notify_url + "&key=" + key;
        String sign = Util.md5(s).toUpperCase();


        return "";
    }
}
