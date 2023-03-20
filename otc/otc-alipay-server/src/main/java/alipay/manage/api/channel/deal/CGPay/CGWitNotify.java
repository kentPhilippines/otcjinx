package alipay.manage.api.channel.deal.CGPay;

import alipay.manage.api.channel.deal.anxin.util.PayDigestUtil;
import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.service.UserInfoService;
import alipay.manage.util.CheckUtils;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class CGWitNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @RequestMapping("/CGWit-noyfit")
    public String notify(HttpServletRequest req, HttpServletResponse res, @RequestBody Map<String, String> params) throws IOException {
        log.info("【收到CG支付成功回调】");
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为：" + clientIP + "】");
        Map map = PayUtil.ipMap;
        Object object = map.get(clientIP);
        if (ObjectUtil.isNull(object)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + map.toString() + "】");
            log.info("【当前回调ip不匹配】");
            return "ip errer";
        }


        /**
         * customer_id 是 平台分配商户编号
         * order_id 是 商户订单号
         * amount 是 订单⾦额
         * datetime 是 ⽇期时间
         * sign 否 请参考签名算法
         * transaction_id 是 平台订单号
         * transaction_code 是 订单⽀付状态，30000为成功，40000为驳回
         * transaction_msg 是 结果描述
         */
        String customer_id = params.get("customer_id");
        String order_id = params.get("order_id");
        String amount = params.get("amount");
        String datetime = params.get("datetime");
        String sign = params.get("sign");
        String transaction_id = params.get("transaction_id");
        String transaction_code = params.get("transaction_code");
        String transaction_msg = params.get("transaction_msg");

        Map mappp = new HashMap();

        mappp.put("customer_id", customer_id);
        mappp.put("order_id", order_id);
        mappp.put("datetime", datetime);
        mappp.put("amount", amount);
        mappp.put("transaction_id", transaction_id);
        mappp.put("customer_id", customer_id);
        mappp.put("transaction_code", transaction_code);
        mappp.put("transaction_msg", transaction_msg);
        String channelKey = getChannelKey(order_id);
        String reqSign = PayDigestUtil.getSign(map, channelKey);
        reqSign = reqSign.toUpperCase(Locale.ROOT);
        if (reqSign.equals(sign)) {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + reqSign + "】");
            log.info("【签名成功】");
        } else {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + reqSign + "】");
            log.info("【签名失败】");
            return "sign is error";
        }

        if("30000".equals(transaction_code)){
            Result dealpayNotfiy = dealpayNotfiy(order_id, clientIP);
            if (dealpayNotfiy.isSuccess()) {
                return "OK";
            }
        }else if ("40000".equals(transaction_code)){
            witNotSuccess(order_id);
        }
        return "";
    }
}
