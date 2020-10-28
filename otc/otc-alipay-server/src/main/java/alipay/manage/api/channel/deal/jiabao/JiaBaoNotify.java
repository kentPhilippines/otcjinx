package alipay.manage.api.channel.deal.jiabao;

import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class JiaBaoNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();

    @RequestMapping("/jiabao-source-notify")
    public String notify(HttpServletRequest req, HttpServletResponse res, @RequestBody JSONObject jsonObject) {
        String clientIP = HttpUtil.getClientIP(req);
        if (!clientIP.equals("47.242.26.125")) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + "47.242.26.125" + "】");
            log.info("【当前回调ip不匹配】");
            return "ip is error";
        }
        if (StrUtil.isBlank(jsonObject.toString())) {
            log.info("【参数获取错误】");
            return "param is error";
        }
        log.info("收到回调内容：{}", jsonObject);
        String content = jsonObject.getStr("content");
        JSONObject ntobj = JSONUtil.parseObj(content);
        HashMap<String, Object> notifyMap = new HashMap<>();
        notifyMap.put("amount", ntobj.getStr("amount"));
        notifyMap.put("merchantCode", ntobj.getStr("merchantCode"));
        notifyMap.put("merchantTradeNo", ntobj.getStr("merchantTradeNo"));
        notifyMap.put("tradeStatus", ntobj.getStr("tradeStatus"));
        notifyMap.put("tradeNo", ntobj.getStr("tradeNo"));
        notifyMap.put("notifyTime", ntobj.getStr("notifyTime"));
        String channelKey = super.getChannelKey(ntobj.getStr("merchantTradeNo"));
        String signStr = RSAUtil.createParam(notifyMap);
        log.info("signStr ====== {}", signStr);
        log.info("收到回调内容：签名字符串：{}", signStr);
        String md5 = RSAUtil.md5(signStr + channelKey);
        String sign = ntobj.getStr("sign");
        if (md5.equals(sign)) {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名成功】");
        } else {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名失败】");
            return "sign is error";
        }
        if ("PAYMENT_SUCCESS".equals(ntobj.getStr("tradeStatus"))) {
            Result result = dealpayNotfiy(ntobj.getStr("merchantTradeNo"), clientIP, "家宝回调成功");
            if (result.isSuccess()) {
                return "SUCCESS";
            }
        }
        return "error";
    }
}