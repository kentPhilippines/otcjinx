package alipay.manage.api.channel.deal.hongyun;

import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class HongYunNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();

    @RequestMapping(HongYunUtil.NOTIFY)
    public String notify(HttpServletRequest req,@RequestParam Map<String, Object> params
    ) {
        log.info("hongyun支付回调:{}", JSONUtil.toJsonStr(params));
        String clientIP = HttpUtil.getClientIP(req);
        String channelKey = getChannelKey(params.get("mchOrderNo")+"");
        String sign = params.get("sign")+"";
      //  String channelKey = "B9RDZZUOKAGGWOLBCKVZLYCVM3ERGIM4UDEHLKHKOX1H75Y0OOA1FSXVVBKDZATIN9URAFWZOGBA1WU3MU7Q7L37CZ8IGCPO8HS8YRZVVM2K4LOKPYS9HO3XIE3LSBYZ";
        params.remove("sign");
        String param = HongYunUtil.createParam(params);
        param = param + "&key=" + channelKey;
        log.info("【hongyun加密前参数：" + param + "】");
        String md5 = HongYunUtil.md5(param).toUpperCase();

        if(md5.equalsIgnoreCase(sign) && "2".equals(params.get("status")+"")) {
            Result result = dealpayNotfiy(params.get("mchOrderNo")+"", clientIP+"", "三方回调成功");
            return "success";
        }

        return "error";

    }
}