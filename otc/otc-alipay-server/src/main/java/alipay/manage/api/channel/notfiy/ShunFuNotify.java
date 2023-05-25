package alipay.manage.api.channel.notfiy;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 申付 代付回调处理类
 */
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class ShunFuNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();

    @RequestMapping("/ShunFu-notify")
    public String notify(HttpServletRequest req, HttpServletResponse res, @RequestParam Map<String,Object> requestParam) {
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【ShunFu-notify代付回调数据为：{}】",JSONUtil.toJsonStr(requestParam));
        log.info("【当前回调ip为：" + clientIP + "】");


        Map mapIp = PayUtil.ipMap;
        Object object = mapIp.get(clientIP);
        if (ObjectUtil.isNull(object)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + "34.92.61.182" + "】");
            log.info("【当前回调ip不匹配】");
            return "ip errer";
        }
//        Map<String,Object> requestParam = JSONUtil.toBean(json,Map.class);
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
        log.info("【shunfu代付回调数据为：{}】",JSONUtil.toJsonStr(requestParam));

        String sign = requestParam.get("sign")+"";
        String orderId = requestParam.get("order_no")+"";
        String tradeNo = requestParam.get("trade_no")+"";
        String orderStatus = requestParam.get("status")+"";
        String password = "b9aa8978ea84f179f83c9de8dab20dfe";
        String merchantCode = "ZRB646C4A680B1FB";
//        requestParam.remove("orderFee");
//        requestParam.remove("orderStatus");
        requestParam.remove("sign");
        //商户号+商户订单+平台订单+商户KEY进行MD5加密
        String original = merchantCode+orderId+tradeNo+password;

        log.info("【shunfu代付签名前参数：" + original + "】");
        String md5 = PayUtil.md5(original);
        if (sign.equalsIgnoreCase(md5)) {
            if ("2".equals(orderStatus)) {
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
