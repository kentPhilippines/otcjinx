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
public class CGPayNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @RequestMapping("/CGPay-notify")
    public String notify(HttpServletRequest req, HttpServletResponse res , @RequestBody Map<String, String> params) throws IOException {
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

        String customer_id = params.get("customer_id");
        String order_id = params.get("order_id");
        String sign = params.get("sign");
        String transaction_id = params.get("transaction_id");
        String order_amount = params.get("order_amount");
        String status = params.get("status");
        String real_amount = params.get("real_amount");
        String message = params.get("message");
        Map mappp = new HashMap();
        mappp.put("customer_id", customer_id);
        mappp.put("order_id", order_id);
        mappp.put("transaction_id", transaction_id);
        mappp.put("order_amount", order_amount);
        mappp.put("status", status);
        mappp.put("real_amount", real_amount);
        mappp.put("message", message);
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
        if("30000".equals(status)){
            Result dealpayNotfiy = dealpayNotfiy(transaction_id, clientIP);
            if (dealpayNotfiy.isSuccess()) {
                return  "success";
            }
        }
        return "error";
    }
}


/**
 * customer_id 是 平台分配商户编号 (Integer)
 * order_id 是 商户订单号 (string)
 * transaction_id 是 平台订单号 (string)
 * order_amount 是 提单⾦额 (float)
 * real_amount 是 玩家实际⽀付⾦额(float)
 * sign 否 请参考签名算法
 * status 是 订单⽀付状态，30000成功，50000原路退款
 * message 是 ⽀付结果描述
 * extra 否 额外参数
 * extra.refund 否
 * 原路退款相关资讯
 * refund_status：退款状态
 * 1:退款中
 * 2:退款成功
 * 3:退款失败
 * bank_account_name：户名
 * bank_name：银⾏名称
 * bank_no：银⾏帐号
 * bank_province：银⾏省份
 * bank_city：银⾏城市
 * bank_sub_branch：银⾏⽀⾏
 * phone：⼿机号
 */
