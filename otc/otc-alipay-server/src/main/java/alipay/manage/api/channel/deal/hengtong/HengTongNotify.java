package alipay.manage.api.channel.deal.hengtong;

import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class HengTongNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();


    @PostMapping(HengtongUtil.NOTIFY)
    public String notify(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String clientIP = HttpUtil.getClientIP(req);
        if (!"18.163.76.215".equals(clientIP)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + "18.163.76.215" + "】");
            log.info("【当前回调ip不匹配】");
            return "ip is error";
        }


        /**
         * paymentReference	M	商户订单号。等同于请求时的 paymentReference。注意核对签名，商户订单号等信息。
         * success	M	如果为true则为支付成功。
         * reference	M	本平台的订单号（提案编号）
         * amount	M	网站提交时的金额。
         * realAmount	M	实际支付金额
         * sign	M	签名，签名方式参考 签名说明。注意，签名全大写。
         */

        String paymentReference = req.getParameter("paymentReference");
        String success = req.getParameter("success");
        String reference = req.getParameter("reference");
        String amount = req.getParameter("amount");
        String realAmount = req.getParameter("realAmount");
        String sign = req.getParameter("sign");

        Map<String, Object> map = new HashMap<>();


        map.put("paymentReference", paymentReference);
        map.put("success", success);
        map.put("reference", reference);
        map.put("amount", amount);
        map.put("realAmount", realAmount);
        String param = HengtongUtil.createParam(map);
        String channelKey = getChannelKey(paymentReference);
        String s = HengtongUtil.md5(param + "&key=" + channelKey).toUpperCase();
        if (!sign.equals(s)) {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + s + "】");
            log.info("【签名失败】");
            return "sign is error";
        }
        if ("true".equals(success)) {
            Result dealpayNotfiy = dealpayNotfiy(paymentReference, clientIP, "HTpay话费回调成功");
            if (dealpayNotfiy.isSuccess()) {
                return "SUCCESS";
            }
        }
        return "error";
    }


}
