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
public class AnXinWitNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();
    @RequestMapping("/anxin-wit-noyfit")
    public String notify(HttpServletRequest req, HttpServletResponse res) throws IOException {
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
        String mchId = req.getParameter("mchId");
        String mchOrderNo = req.getParameter("mchOrderNo");
        String amount = req.getParameter("amount");
        String orderNo = req.getParameter("orderNo");
        String status = req.getParameter("status");
        String backType = req.getParameter("backType");
        String tradeTime = req.getParameter("tradeTime");
        String remark = req.getParameter("remark");
        String sign = req.getParameter("sign");
        Map mappp = new HashMap();
        mappp.put("mchId", mchId);
        mappp.put("mchOrderNo", mchOrderNo);
        mappp.put("amount", amount);
        mappp.put("orderNo", orderNo);
        mappp.put("status", status);
        mappp.put("backType", backType);
        mappp.put("tradeTime", tradeTime);
        mappp.put("remark", remark);
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

        if("2".equals(status)){
            Result dealpayNotfiy = dealpayNotfiy(mchOrderNo, clientIP);
            if (dealpayNotfiy.isSuccess()) {
                return "success";
            }
        }else if ("3".equals(status)){
            witNotSuccess(mchOrderNo);
        }
        return "";
    }
}
