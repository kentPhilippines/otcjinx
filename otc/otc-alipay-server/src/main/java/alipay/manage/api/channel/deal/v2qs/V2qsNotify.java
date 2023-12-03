package alipay.manage.api.channel.deal.v2qs;

import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.util.CheckUtils;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
public class V2qsNotify extends NotfiyChannel {
    @RequestMapping("/V2qs-pay")
    public String notify(HttpServletRequest req, HttpServletResponse res) throws IOException {
        log.info("【收到V2qsNotify手动支付成功回调】");
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为：" + clientIP + "】");
        String apporderid = req.getParameter("apporderid");
        String tradesno = req.getParameter("tradesno");
        String status = req.getParameter("status");
        String amount = req.getParameter("amount");
        String appid = req.getParameter("appid");
        String sign = req.getParameter("sign");
        Map<String, Object> mapParar = new HashMap<String, Object>();
        mapParar.put("apporderid", apporderid);
        mapParar.put("tradesno", tradesno);
        mapParar.put("status", status);
        mapParar.put("amount", amount);
        mapParar.put("appid", appid);
        mapParar.put("statusdesc", "成功");
        String channelKey = super.getChannelKey(apporderid);
        String md5 = CheckUtils.getSign(mapParar, channelKey);
        if (md5.equals(sign)) {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名成功】");
        } else {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名失败】");
            return "sign is error";
        }
        Result dealpayNotfiy = dealpayNotfiy(apporderid, clientIP);
        if (dealpayNotfiy.isSuccess()) {
            return  "success";
        }
        return "error";
    }
}
