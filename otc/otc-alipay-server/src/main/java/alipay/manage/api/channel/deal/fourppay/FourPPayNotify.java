package alipay.manage.api.channel.deal.fourppay;

import alipay.manage.api.channel.util.yifu.YiFuUtil;
import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class FourPPayNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @RequestMapping("/fourPPayNotify")
    @ResponseBody
    public String chaofanPayNotify( HttpServletRequest request,@RequestBody Map<String,String> params) {
        log.info("fourPPayNotify notify:{}", JSONUtil.toJsonStr(params));
//        String requestStr="{\"amount\":\"300.00\",\"body\":\"test\",\"channel\":\"alipayCodeSmall\",\"order_status\":\"1\",\"out_trade_no\":\"1671060097960382464\",\"trade_no\":\"1671060097960382464\",\"sign\":\"9442e429aae0db59c8001584dbbb5a5e\"}";
//        params = JSONUtil.toBean(requestStr,Map.class);
        String clientIP = HttpUtil.getClientIP(request);

        String orderId = params.get("payment_cl_id");
        String password = getChannelKey(orderId);
//        this.getChannelKey("");
        String postSign= params.get("sign");
        String sign = createSign(params, password);
        if(postSign.equalsIgnoreCase(sign) && "2".equals(params.get("status")+"")) {
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
        String preStr = YiFuUtil.createParam(map)+""+key;
        log.info("before sign：{}",preStr);
        String sign = YiFuUtil.md5(preStr).toLowerCase();

        return sign;
    }
}
