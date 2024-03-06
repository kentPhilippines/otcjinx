package alipay.manage.api.channel.deal.pipixia;

import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class PipixiaNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();


    @PostMapping(PipixiaUtil.NOTIFY)
    public String notify(HttpServletRequest req, HttpServletResponse res,@RequestParam Map<String,Object> decodeParamMap) throws IOException {
        log.info("pipixia 回调数据为:" + decodeParamMap.toString());
        String sign = decodeParamMap.get("sign").toString();
        String rk = decodeParamMap.get("rk").toString();
        decodeParamMap.remove("sign");



        String md5 = sign(decodeParamMap, getChannelKey(decodeParamMap.get("out_trade_no").toString()),rk);
        if (sign.equalsIgnoreCase(md5) && "success".equals(decodeParamMap.get("status")+"") ) {
            Result result = dealpayNotfiy(decodeParamMap.get("out_trade_no").toString(), HttpUtil.getClientIP(req));
            if (result.isSuccess()) {
                return "success";
            }
        } else {
            log.info("签名错误：我方签名:" + md5 + "  对方签名  ： " + sign);
        }
        return Result.buildFail().toString();
    }


    public static String sign(final Map<String, Object> hashMap, String token, String randKey) {
        Set<String> keySet = hashMap.keySet();
        String[] keyArray = keySet.toArray(new String[0]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyArray.length; i++) {
            if (String.valueOf(hashMap.get(keyArray[i])).length() > 0) {
                sb.append(keyArray[i]).append("=").append(hashMap.get(keyArray[i])).append("&");
            }
        }

        sb.append("key" + randKey + "=" + token);

        return MD5Util.encrypt(MD5Util.encrypt(sb.toString())).toUpperCase();
    }

}
