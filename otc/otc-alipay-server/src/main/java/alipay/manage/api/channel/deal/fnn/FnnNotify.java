package alipay.manage.api.channel.deal.fnn;

import alipay.manage.api.channel.util.yifu.YiFuUtil;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.assertj.core.util.Lists;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class FnnNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();

    @RequestMapping("/fnn-notify")
    public String notify(HttpServletRequest req,@RequestParam Map<String, Object> params
    ) {
        log.info("fnn支付回调:{}", JSONUtil.toJsonStr(params));
        String clientIP = HttpUtil.getClientIP(req);
        List list = Lists.newArrayList("13.214.136.125".split(","));
//        Map mapip = new HashMap();
//        mapip.put("13.231.125.35", "13.231.125.35");
//        Object object = mapip.get(clientIP);
//        if (!list.contains(clientIP)) {
//            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + list.toString() + "】");
//            log.info("【当前回调ip不匹配】");
//            return "ip is error";
//        }
        String channelKey = getChannelKey(params.get("sh_order")+"");
        String sign = params.get("sign")+"";
        params.remove("sign");
        String param = YiFuUtil.createParam(params);
        param = param + "key=" + channelKey;
        log.info("【fnn加密前参数：" + param + "】");
        String md5 = YiFuUtil.md5(param).toUpperCase();

        if(md5.equalsIgnoreCase(sign) && "success".equals(params.get("status")+"")  ) {
            Result result = dealpayNotfiy(params.get("sh_order")+"", clientIP+"", "三方回调成功");
            return "success";
        }

        return "error";

    }
}