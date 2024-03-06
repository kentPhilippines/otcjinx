package alipay.manage.api.channel.deal.cangqiong;

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
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class CangQiongNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();


    @PostMapping(CangQiongUtil.NOTIFY)
    public String notify(HttpServletRequest req, HttpServletResponse res,@RequestParam Map<String,Object> decodeParamMap) throws IOException {
        log.info("chaoqiong 回调数据为:" + decodeParamMap.toString());
        String sign = decodeParamMap.get("sign").toString();
        decodeParamMap.remove("sign");
        String md5 = SanYangUtil.encodeSign(decodeParamMap, getChannelKey(decodeParamMap.get("out_trade_no").toString()));
        if (sign.equalsIgnoreCase(md5) && "1".equals(decodeParamMap.get("order_status")+"") ) {
            Result result = dealpayNotfiy(decodeParamMap.get("out_trade_no").toString(), HttpUtil.getClientIP(req));
            if (result.isSuccess()) {
                return "SUCCESS";
            }
        } else {
            log.info("签名错误：我方签名:" + md5 + "  对方签名  ： " + sign);
        }
        return Result.buildFail().toString();
    }

}
