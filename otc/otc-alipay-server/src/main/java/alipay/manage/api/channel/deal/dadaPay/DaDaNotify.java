package alipay.manage.api.channel.deal.dadaPay;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class DaDaNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();
    @RequestMapping("/dadaPayNotify")
    public String notify(HttpServletRequest req, HttpServletResponse res ) {
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为：" + clientIP + "】");
        Map mapIp = PayUtil.ipMap;
        Object object = mapIp.get(clientIP);
        if (ObjectUtil.isNull(object)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + mapIp.toString() + "】");
            log.info("【当前回调ip不匹配】");
            return "ip errer";
        }


        String payOrderId = req.getParameter("payOrderId");
        String mchId = req.getParameter("mchId");
        String appId = req.getParameter("appId");
        String productId = req.getParameter("productId");
        String mchOrderNo = req.getParameter("mchOrderNo");
        String amount = req.getParameter("amount");
        String income = req.getParameter("income");
        String status = req.getParameter("status");
        String paySuccTime = req.getParameter("paySuccTime");
        String backType = req.getParameter("backType");
        String sign = req.getParameter("sign");
        Map<String, Object> map = new HashMap<>();
        map.put("payOrderId", payOrderId);
        map.put("mchId", mchId);
        map.put("appId", appId);
        map.put("productId", productId);
        map.put("mchOrderNo", mchOrderNo);
        map.put("amount", amount);
        map.put("income", income);
        map.put("status", status);
        map.put("paySuccTime", paySuccTime);
        map.put("backType", backType);
        log.info("【dada签名前参数：" + map + "】");
        String dpAyChannelKey = getDPAyChannelKey(mchOrderNo);
        String createParam = PayUtil.createParam(map);
        log.info(createParam);
        String md5 = PayUtil.md5(createParam + "&key="+dpAyChannelKey).toUpperCase(Locale.ROOT);
        if (sign.equals(md5)) {
            if ("2".equals(status)) {
                Result dealpayNotfiy = dealpayNotfiy(mchOrderNo, clientIP);
                if (dealpayNotfiy.isSuccess()) {
                    return  "success";
                }
            }
        } else {
            return "error";
        }
        return "end errer";
    }




}
