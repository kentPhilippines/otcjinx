package alipay.manage.api.channel.deal.xiangyun;

import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class XiangYunNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;

    public static String createParam(Map<String, Object> map) {
        try {
            if (map == null || map.isEmpty()) {
                return null;
            }
            Object[] key = map.keySet().toArray();
            Arrays.sort(key);
            StringBuffer res = new StringBuffer(128);
            for (int i = 0; i < key.length; i++) {
                if (ObjectUtil.isNotNull(map.get(key[i]))) {
                    res.append(key[i] + "=" + map.get(key[i]) + "&");
                }
            }
            String rStr = res.substring(0, res.length() - 1);
            return rStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String md5(String a) {
        String c = "";
        MessageDigest md5;
        String result = "";
        try {
            md5 = MessageDigest.getInstance("md5");
            md5.update(a.getBytes("utf-8"));
            byte[] temp;
            temp = md5.digest(c.getBytes("utf-8"));
            for (int i = 0; i < temp.length; i++) {
                result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
        return result;
    }

    @RequestMapping("/xiangyun-notfiy")
    public String notify(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【收到祥云支付回调通知】");
        Map mapip = new HashMap();
        mapip.put("47.52.104.61", "47.52.104.61");
        mapip.put("54.179.55.153", "54.179.55.153");
        Object object = mapip.get(clientIP);
        if (ObjectUtil.isNull(object)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + mapip.toString() + "】");
            log.info("【当前回调ip不匹配】");
            return "ip is error";
        }
        /**
         * signType	str	是	否	默认值,md5(不参与验签)
         * charset	str	是	否	默认值：utf-8(不参与验签)
         * amount	str	是	是	交易金额
         * status	str	是	是	订单状态
         * statusStr	str	是	是	订单状态说明
         * outTradeNo	str	是	是	商户订单号
         * extra	str	否	是	创单时存在传递了该参数则参与验签
         * sign	str	是	否	签名(不参与验签)
         */
        String signType = req.getParameter("signType");
        String charset = req.getParameter("charset");
        String amount = req.getParameter("amount");
        String status = req.getParameter("status");//1  成功
        String statusStr = req.getParameter("statusStr");
        String outTradeNo = req.getParameter("outTradeNo");
        String extra = req.getParameter("extra");
        String sign = req.getParameter("sign");
        Map map = new HashMap();
        map.put("amount", amount);
        map.put("status", status);
        map.put("statusStr", statusStr);
        map.put("outTradeNo", outTradeNo);
        map.put("extra", extra);
        log.info("【祥云支付回调通知加签参数：" + map.toString() + "】");
        String param = createParam(map);
        log.info("【祥云支付回调通知加签串：" + param + "】");
        String channelKey = getChannelKey(outTradeNo);
        String md5 = md5(param + "&" + channelKey);
        if (md5.equals(sign)) {
            log.info("【验签成功】");
        } else {
            log.info("【验签失败， 对方系统签名为：" + sign + "】");
            return "sign is error";
        }
        if ("1".equals(status)) {
            Result result = dealpayNotfiy(outTradeNo, clientIP, "祥云支付成功");
            if (result.isSuccess()) {
                return "success";
            }
        }
        return "error";
    }
}
