package alipay.manage.api.channel.deal.fcpay;

import alipay.manage.api.channel.util.yifu.YiFuUtil;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class FcPayNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();

    @RequestMapping("/fcpay-notify")
    public String notify(HttpServletRequest req, @RequestBody Map<String, Object> params
    ) {
        log.info("fcpay支付回调paramForm:{}", JSONUtil.toJsonStr(params));
        String clientIP = HttpUtil.getClientIP(req);

        String channelKey = getChannelKey(params.get("callerOrderId") + "");
        String sign = params.get("sign") + "";
        params.remove("sign");

        String param = params.keySet().stream()
                .sorted()
                .map(s -> s + "" + params.getOrDefault(s, ""))
                .collect(Collectors.joining(""));

        System.out.println(param);
        String md5 = YiFuUtil.md5(channelKey + param + channelKey);

        log.info("【fcpay加密前参数：" + param + "】");

        if (md5.equalsIgnoreCase(sign) && "AP".equalsIgnoreCase(params.get("orderStatus") + "")) {
            Result result = dealpayNotfiy(params.get("callerOrderId") + "", clientIP + "", "三方回调成功");
            return "OK";
        }

        return "error";

    }
}