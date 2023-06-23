package alipay.manage.api.channel.deal.caiyun;

import alipay.manage.api.channel.util.yifu.YiFuUtil;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
public class KddPayNotify extends NotfiyChannel {

    /**
     * 测试报文：{"merchantId":"200289","version":"1.0","orderNo":"J20220217182526265927355","amount":"5000","tradeDate":"20220217","tradeTime":"183528","resultCode":"0","sign":"d1487a8740f16b44ba3d0e1d42ff68a4"}
     * {"error_code":10001,"error_msg":"不要重复下订单哦！","request_url":"\/api\/order"}
     * @param request
     * @param params
     * @return
     */
    @RequestMapping(value = "/henglPayNotify")
    @ResponseBody
    public String chaofanPayNotify( HttpServletRequest request,@RequestParam Map<String,String> params) {
        log.info("kdd notify:{}", JSONUtil.toJsonStr(params));
//        String requestStr="{\"amount\":\"300.00\",\"body\":\"test\",\"channel\":\"alipayCodeSmall\",\"order_status\":\"1\",\"out_trade_no\":\"1671060097960382464\",\"trade_no\":\"1671060097960382464\",\"sign\":\"9442e429aae0db59c8001584dbbb5a5e\"}";
//        params = JSONUtil.toBean(requestStr,Map.class);
        String clientIP = HttpUtil.getClientIP(request);
        Map<String,String> ipmap = new HashMap<>();
        ipmap.put("43.225.47.22","43.225.47.22");
        ipmap.put("43.225.47.36","43.225.47.36");
        ipmap.put("43.225.47.39","43.225.47.39");
        ipmap.put("45.207.58.203","45.207.58.203");
        ipmap.put("8.217.66.247","8.217.66.247");

        String s = ipmap.get(clientIP);

          String password  = getChannelKey(params.get("out_trade_no"));
        String postSign= params.get("sign");
        String sign = createSign(params, password);
        if(postSign.equalsIgnoreCase(sign) && "1".equals(params.get("order_status")+"")) {
            Result result = dealpayNotfiy(params.get("out_trade_no")+"", s, "kdd回调成功");
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
