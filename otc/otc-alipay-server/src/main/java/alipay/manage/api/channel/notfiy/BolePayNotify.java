package alipay.manage.api.channel.notfiy;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 申付 代付回调处理类
 */
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class BolePayNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();

    @RequestMapping("/BolePay-notify")
    public String notify(HttpServletRequest req, HttpServletResponse res, @RequestParam Map<String,Object> requestParam) {
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【bole代付回调数据为：{}】",JSONUtil.toJsonStr(requestParam));
        log.info("【当前回调ip为：" + clientIP + "】");


        Map mapIp = PayUtil.ipMap;
        Object object = mapIp.get(clientIP);
        if (ObjectUtil.isNull(object)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + "66.203.152.213" + "】");
            log.info("【当前回调ip不匹配】");
            return "ip errer";
        }
        /**
         * 商户单号	appOrderNo
         * 平台单号	orderNo
         * 支付完成时间	orderTime
         * 商户号	appId
         * 订单金额	orderAmt
         * 订单手续费	orderFee
         * 支付状态	orderStatus
         * 签名	sign
         */
        log.info("【bole代付回调数据为：{}】",JSONUtil.toJsonStr(requestParam));

        String sign = requestParam.get("sign")+"";
        String orderId = requestParam.get("appOrderNo")+"";
        String orderStatus = requestParam.get("orderStatus")+"";
        String password = "BB8E7131F29F110BB8E3BE5B6C3A936E";
//        requestParam.remove("orderFee");
//        requestParam.remove("orderStatus");
        requestParam.remove("sign");
        String createParam = PayUtil.createParam(requestParam);
        log.info("【bole代付签名前参数：" + createParam + "】");
        String md5 = PayUtil.md5(createParam +"&key="+ password);
        if (sign.equalsIgnoreCase(md5)) {
            if ("02".equals(orderStatus)) {
                Result result = witNotfy(orderId, clientIP);
                if (result.isSuccess()) {
                    return "success";
                }
            } else    {
                witNotSuccess(orderId);
            }
        } else {
            return "error";
        }
        return "end errer";
    }
}
