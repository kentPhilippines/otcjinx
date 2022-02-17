package alipay.manage.api.channel.deal.chaofan;

import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.util.chaofanpay.MD5;
import alipay.manage.util.chaofanpay.SignUtils;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
public class ChaoFanPayNotify extends NotfiyChannel {

    /**
     * 测试报文：{"service":"pay.alipay.card","version":"1.0","amount":"5000","clientIp":"127.0.0.1","notifyUrl":"http://localhost/response.html","merchantId":"200289","orderNo":"12018011512301500002","tradeDate":"20220127","tradeTime":"123015","sign":"578E3D3C792E17502CC0E51294930138"}
     * @param request
     * @param params
     * @return
     */
    @RequestMapping(value = "/chaofanPayNotify",method = RequestMethod.POST)
    @ResponseBody
    public String chaofanPayNotify( HttpServletRequest request,@RequestBody Map<String,String> params) {
        log.info("test111111111:{}", JSONUtil.toJsonStr(params));
        String clientIP = HttpUtil.getClientIP(request);
        Map<String,String> ipmap = new HashMap<>();
        ipmap.put("47.75.96.143","47.75.96.143");
        ipmap.put("47.56.118.34","47.56.118.34");
        String s = ipmap.get(clientIP);
        if(StrUtil.isEmpty(s)){
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + ipmap.toString()+ "】");
            log.info("【当前回调ip不匹配】");
            return "ip is error";
        }
        log.info("test111111111:{}", JSONUtil.toJsonStr(params));
        String password = getChannelKey(params.get("orderNo"));
        String sign = createSign(params, password);
        if(params.get("sign").toString().equalsIgnoreCase(sign) && "0".equals(params.get("resultCode")+"")) {
            Result result = dealpayNotfiy(params.get("orderNo")+"", params.get("clientIp")+"", "chaofan回调成功");
            //todo 这里处理业务
            return "success";
        }
        return "error";
    }

    private static String createSign(Map map, String key)
    {
        Map<String, String> params = SignUtils.paraFilter(map);
        params.put("key", key);
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "UTF-8");

        return sign;
    }
}
