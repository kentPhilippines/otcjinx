package alipay.manage.api.channel.deal.pineaple;

import alipay.manage.api.channel.util.yifu.YiFuUtil;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
public class PineapplePayNotify extends NotfiyChannel {

    /**
     * 测试报文：{"merchantId":"200289","version":"1.0","orderNo":"J20220217182526265927355","amount":"5000","tradeDate":"20220217","tradeTime":"183528","resultCode":"0","sign":"d1487a8740f16b44ba3d0e1d42ff68a4"}
     * {"error_code":10001,"error_msg":"不要重复下订单哦！","request_url":"\/api\/order"}
     * @param request
     * @param params
     * @return
     */
    @RequestMapping(value = "/pineapplePayNotify")
    @ResponseBody
    public String chaofanPayNotify( HttpServletRequest request,@RequestParam Map<String,String> params) {
        log.info("pineapplePayNotify notify:{}", JSONUtil.toJsonStr(params));
//        String requestStr="{\"amount\":\"300.00\",\"body\":\"test\",\"channel\":\"alipayCodeSmall\",\"order_status\":\"1\",\"out_trade_no\":\"1671060097960382464\",\"trade_no\":\"1671060097960382464\",\"sign\":\"9442e429aae0db59c8001584dbbb5a5e\"}";
//        params = JSONUtil.toBean(requestStr,Map.class);
        String clientIP = HttpUtil.getClientIP(request);

        String orderId = params.get("out_trade_no");
         String password = getChannelKey(orderId);
//        this.getChannelKey("");
        String postSign= params.get("sign");
        String sign = createSign(params, password);
        if(postSign.equalsIgnoreCase(sign) && "1".equals(params.get("order_status")+"")) {
            Result result = dealpayNotfiy(orderId+"", clientIP, "三方回调成功");
            return "success";
        }
        return "error";
    }


    private static String createSign(Map map, String key)
    {
        map.remove("sign");
        Map<String, String> params = map;
//        params.put("key", key);
//        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
//        SignUtils.buildPayParams(buf, params, false);
        String preStr = YiFuUtil.createParam(map)+"key="+key;
        log.info("before sign：{}",preStr);
        String sign = YiFuUtil.md5(preStr).toLowerCase();

        return sign;
    }
}
