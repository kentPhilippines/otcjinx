package alipay.manage.api.channel.deal.anxin;

import alipay.manage.api.channel.deal.anxin.util.PayDigestUtil;
import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.service.UserInfoService;
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
public class AnXinPayNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();

    @RequestMapping("/anxin-bank-notify")
    public String notify(HttpServletRequest req, HttpServletResponse res , @RequestBody Map<String, String> params) throws IOException {
        log.info("【收到安心支付成功回调】");
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
         * {"tradeTime":"20230317221907","amount":100,"mchId":1320,"
         * orderNo":"C6S1S9S179","amountOriginal"
         * :100,"mchOrderNo":"C6S1S9S179","sign":"2E635612AFA67CDED0564E2744032D82"
         * ,"backType":2,"type":1,"status":1}
         */

        String tradeTime = req.getParameter("tradeTime");
        String amount = req.getParameter("amount");
        String mchId = req.getParameter("mchId");
        String orderNo = req.getParameter("orderNo");
        String amountOriginal =  req.getParameter("amountOriginal");
        String mchOrderNo = req.getParameter("mchOrderNo");
        String sign =  req.getParameter("sign");
        String backType =  req.getParameter("backType");
        String status =  req.getParameter("status");
        String type =  req.getParameter("type");

        Map mappp = new HashMap();
        mappp.put("tradeTime", tradeTime);
        mappp.put("amount", amount);
        mappp.put("mchId", mchId);
        mappp.put("orderNo", orderNo);
        mappp.put("status", status);
        mappp.put("amountOriginal", amountOriginal);
        mappp.put("mchOrderNo", mchOrderNo);
        mappp.put("backType", backType);
        mappp.put("type", type);
        String channelKey = getChannelKey(mchOrderNo);
        String reqSign = PayDigestUtil.getSign(map, channelKey);
        if (reqSign.equals(sign)) {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + reqSign + "】");
            log.info("【签名成功】");
        } else {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + reqSign + "】");
            log.info("【签名失败】");
            return "sign is error";
        }
        if("1".equals(status)){
            Result dealpayNotfiy = dealpayNotfiy(mchOrderNo, clientIP);
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
