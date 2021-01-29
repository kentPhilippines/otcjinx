package alipay.manage.api.channel.deal.shunYiFu;

import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class ShunXinFuNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @PostMapping("/shunxinfu-source-notify")
    public String notify(HttpServletRequest req, HttpServletResponse res) {
        log.info("【收到顺心付支付成功回调】");
        String clientIp = HttpUtil.getClientIP(req);
        log.info("【收到顺心付支付回调通知】");
        Map mapip = new HashMap();
        mapip.put("54.251.214.162", "54.251.214.162");
        Object object = mapip.get(clientIp);
        if (ObjectUtil.isNull(object)) {
            log.info("【当前回调ip为：" + clientIp + "，固定IP登记为：" + mapip.toString() + "】");
            log.info("【当前回调ip不匹配】");
            return "ip is error";
        }
        /**
         * signType str 是 否 默认值,md5(不参与验签)
         * charset str 是 否 默认值：utf-8(不参与验签)
         * amount str 是 是 交易⾦额
         * status str 是 是 订单状态
         * statusStr str 是 是 订单状态说明
         * outTradeNo str 是 是 商户订单号
         * extra str 否 是 创单时存在传递了该参数则参与验签
         * sign str 是 否 签名(不参与验签)
         */
        String signType = req.getParameter("signType");
        String charset = req.getParameter("charset");
        String sign = req.getParameter("sign");

        String amount = req.getParameter("amount");
        String status = req.getParameter("status");
        String statusStr = req.getParameter("statusStr");
        String outTradeNo = req.getParameter("outTradeNo");
        String extra = req.getParameter("extra");
        Map<String, Object> param = new HashMap<>();
        param.put("amount", amount);
        param.put("status", status);
        param.put("statusStr", statusStr);
        param.put("outTradeNo", outTradeNo);
        param.put("extra", extra);
        String param1 = ShunXinFuUtil.createParam(param);
        String channelKey = getChannelKey(outTradeNo);
        channelKey = "&" + channelKey;
        param1 += channelKey;
        log.info("【顺心付签名参数为：" + param1 + "】");
        String s = ShunXinFuUtil.md5(param1);

        if (s.equals(sign)) {
            log.info("【验签成功】");
        } else {
            log.info("【验签失败， 对方系统签名为：" + sign + "，我方签名串为：" + s + "】");
            return "sign is error";
        }
        if ("1".equals(status)) {
            Result result = dealpayNotfiy(outTradeNo, clientIp, "顺心支付成功");
            if (result.isSuccess()) {
                return "success";
            }
        }
        return "error";
    }
}
