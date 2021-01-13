package alipay.manage.api.channel.notfiy;

import alipay.manage.api.channel.util.jiufu.JiUFuUtil;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class JiuFuDealNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();

    @PostMapping("/jiufu-notfiy")
    public String notify(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为：" + clientIP + "】");
        Map mapip = new HashMap();
        mapip.put("45.192.182.53", "45.192.182.53");
        mapip.put("46.137.208.154", "46.137.208.154");
        Object object = mapip.get(clientIP);
        if (ObjectUtil.isNull(object)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + mapip.toString() + "】");
            log.info("【当前回调ip不匹配】");
            return "ip errer";
        }
        String signType = req.getParameter("signType");
        String charset = req.getParameter("charset");
        String amount = req.getParameter("amount");
        String status = req.getParameter("status");
        String statusStr = req.getParameter("statusStr");
        String outTradeNo = req.getParameter("outTradeNo");
        String extra = req.getParameter("extra");
        String sign = req.getParameter("sign");
        Map<String, Object> map = new HashMap();
        map.put("amount", amount);
        map.put("status", status);
        map.put("statusStr", statusStr);
        map.put("outTradeNo", outTradeNo);
        map.put("extra", extra);
        String createParam = JiUFuUtil.createParam(map);
        log.info("【豪富返回参数加密验签串：" + createParam + "】");
        String md5 = JiUFuUtil.md5(createParam + "&" + JiUFuUtil.KEY);
        if (md5.equals(sign)) {
            log.info("【当前验签正确：" + map.toString() + "】");
        } else {
            log.info("当前验签错误，我方验签串" + md5 + "，对方验签串：" + sign + "");
            return "errer";
        }
        if ("1".equals(status)) {
            Result dealpayNotfiy = dealpayNotfiy(outTradeNo, clientIP, "玖富回调成功");
            if (dealpayNotfiy.isSuccess()) {
                return "success";
            }
        }
        return "end errer";
    }
}
