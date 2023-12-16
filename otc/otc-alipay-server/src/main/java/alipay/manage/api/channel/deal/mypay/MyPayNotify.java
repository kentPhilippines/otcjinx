package alipay.manage.api.channel.deal.mypay;

import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.util.RSAUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;
import otc.util.MapUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class MyPayNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();


    @RequestMapping(MyPayUtil.NOTIFY)
    public String notify(HttpServletRequest req, HttpServletResponse res,@RequestBody Map<String,Object> decodeParamMap) throws Exception {
        log.info("mypay 回调数据为:" + decodeParamMap.toString());
        String sign = decodeParamMap.get("sign").toString();
        decodeParamMap.remove("sign");
        boolean md5 = RSAUtil.verifyByPublicKeyStr( MyPayUtil.PUBLIC_KEY,MapUtil.createParam(decodeParamMap),sign);
        if (md5 && "1".equals(decodeParamMap.get("status")+"") ) {
            Result result = dealpayNotfiy(decodeParamMap.get("merchantOrderNo").toString(), HttpUtil.getClientIP(req));
            if (result.isSuccess()) {
                return "success";
            }
        } else {
            log.info("签名错误：我方签名:" + md5 + "  对方签名  ： " + sign);
        }
        return Result.buildFail().toString();
    }

}
