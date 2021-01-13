package alipay.manage.api.channel.deal.haiwang;

import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class HaiWangNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();

    @RequestMapping(Util.NOTIFY)
    public String notify(HttpServletRequest request, HttpServletResponse res,
                         @RequestBody String json) throws IOException {
        log.info("【收到海王回调】");
        String clientIP = HttpUtil.getClientIP(request);
        if (!"110.42.3.162".equals(clientIP)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + "110.42.3.16" + "】");
            log.info("【当前回调ip不匹配】");
            return "ip errer";
        }
        JSONObject rtJson = JSONUtil.parseObj(json);
        log.info("海王回调参数：" + rtJson.toString());
        Object sn = rtJson.get("sn");
        Object out_sn = rtJson.get("out_sn");
        Object money = rtJson.get("money");
        Object pay_type_group = rtJson.get("pay_type_group");
        Object trade_status = rtJson.get("trade_status");
        Object encryption = rtJson.get("encryption");
        if (ObjectUtil.isNull(sn) || ObjectUtil.isNull(out_sn)
                || ObjectUtil.isNull(money) || ObjectUtil.isNull(pay_type_group)
                || ObjectUtil.isNull(encryption)
                || ObjectUtil.isNull(trade_status)) {
            log.info("海王回调必传参数为空：");
            return "param errer";
        }
        String s = "sn=" + sn +
                "&out_sn=" + out_sn +
                "&money=" + money +
                "&pay_type_group=" + pay_type_group +
                "&trade_status=" + trade_status +
                "&key=" + Util.KEY;
        log.info("海王签名参数：" + s.toString());
        String sign = Util.md5(s).toUpperCase();
        if (sign.equals(encryption)) {
            log.info("【当前支付成功回调签名参数：" + encryption + "，当前我方验证签名结果：" + sign + "】");
            log.info("【签名成功】");
        } else {
            log.info("【当前支付成功回调签名参数：" + encryption + "，当前我方验证签名结果：" + sign + "】");
            log.info("【签名失败】");
            return "sign errer";
        }
        if ("TRADE_SUCCESS".equals(trade_status)) {
            Result dealpayNotfiy = dealpayNotfiy(out_sn.toString(), clientIP, "海王回调订单成功");
            if (dealpayNotfiy.isSuccess()) {
                log.info("【订单回调修改成功，订单号为 ：" + out_sn + " 】");
                return "success";
            }
        }
        return "";
    }
}
