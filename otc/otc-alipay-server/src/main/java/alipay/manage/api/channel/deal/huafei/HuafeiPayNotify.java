package alipay.manage.api.channel.deal.huafei;

import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.util.chaofanpay.MD5;
import alipay.manage.util.chaofanpay.SignUtils;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Controller
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
public class HuafeiPayNotify extends NotfiyChannel {

    /**
     * 测试报文：{"merchantId":"200289","version":"1.0","orderNo":"J20220217182526265927355","amount":"5000","tradeDate":"20220217","tradeTime":"183528","resultCode":"0","sign":"d1487a8740f16b44ba3d0e1d42ff68a4"}
     * {"error_code":10001,"error_msg":"不要重复下订单哦！","request_url":"\/api\/order"}
     * @param request
     * @param params
     * @return
     */
    @RequestMapping(value = "/huafeiPayNotify",method = RequestMethod.POST)
    @ResponseBody
    public String chaofanPayNotify( HttpServletRequest request,@RequestParam Map<String,String> params) {
        log.info("huafeiPayNotify:{}", JSONUtil.toJsonStr(params));
        String clientIP = HttpUtil.getClientIP(request);
        Map<String,String> ipmap = new HashMap<>();
        ipmap.put("34.150.111.99","34.150.111.99");
        String s = ipmap.get(clientIP);
        if(StrUtil.isEmpty(s)){
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + ipmap.toString()+ "】");
            log.info("【当前回调ip不匹配】");
            return "ip is error";
        }
        String password = getChannelKey(params.get("out_trade_no"));
        String sign = createSign(params, password.split(",")[0],password.split(",")[1]);
        if(params.get("sign").toString().equalsIgnoreCase(sign) && "CODE_SUCCESS".equals(params.get("callbacks")+"")) {
            Result result = dealpayNotfiy(params.get("out_trade_no")+"", "127.0.0.1", "huafei回调成功");
            return "success";
        }
        return "error";
    }

    private static String createSign(Map map, String key,String token)
    {
        Map<String, String> params = alipay.manage.api.channel.deal.chaofan.SignUtils.paraFilter(map);

        //params.put("key", key);
        //params.put("token", token);
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        alipay.manage.api.channel.deal.chaofan.SignUtils.buildPayParams1(buf, params, false);
        String preStr = buf.toString()+key;
        log.info("notify preStr:{}",preStr);
        String sign = alipay.manage.api.channel.deal.chaofan.MD5.sign(preStr, "UTF-8");

        return sign;
    }
   /* private static String createSign(Map map, String key)
    {
        Map<String, String> params = SignUtils.paraFilter(map);
        params.put("key", key);
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "UTF-8");

        return sign;
    }*/
}
