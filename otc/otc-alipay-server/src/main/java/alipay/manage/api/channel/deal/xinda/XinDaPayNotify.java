package alipay.manage.api.channel.deal.xinda;

import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class XinDaPayNotify extends NotfiyChannel {
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

    @RequestMapping("/xd-pay-notify")
    public String notify(HttpServletRequest req, HttpServletResponse res, @RequestBody Map<String, Object> requestParams) {
        log.info("收到xd付款回调，数据为:" + requestParams.toString());
        Object sign = requestParams.get("sign");
        requestParams.remove("sign");
        String param = createParam(requestParams);
        String key = getChannelKey((String) requestParams.get("tradeNo"));
        String s = param + "&key=" + key;
        String Sign = md5(s);
        log.info("获取key = " + key);
        log.info("签名为:" + s);
        if (sign.toString().equals(Sign)) {
            if ("1".equals(requestParams.get("status").toString())) {
                Result result = dealpayNotfiy(requestParams.get("tradeNo").toString(), HttpUtil.getClientIP(req), "支付成功");
                if (result.isSuccess()) {
                    return "SUCCESS";
                }
            }
        } else {
            log.info("【验签失败， 对方系统签名为：" + sign + "】");
            log.info("【验签失败， 我方系统签名为：" + s + "】");
        }
        return "error";
    }


    @RequestMapping("/xd-wit-notify")
    public String notifyWit(HttpServletRequest req, HttpServletResponse res, @RequestBody Map<String, Object> requestParams) {
        log.info("收到xd付款回调，数据为:" + requestParams.toString());
        Object sign = requestParams.get("sign");
        requestParams.remove("sign");
        String param = createParam(requestParams);
        String key = getDPAyChannelKey((String) requestParams.get("tradeNo"));
        String s = param + "&key=" + key;
        String Sign = md5(s);
        log.info("获取key = " + key);
        log.info("签名为:" + s);
        if (sign.toString().equals(Sign)) {
            if ("1".equals(requestParams.get("status").toString())) {
                Result result = witNotfy(requestParams.get("tradeNo").toString(), HttpUtil.getClientIP(req));
                if (result.isSuccess()) {
                    return "SUCCESS";
                }
            }
        } else {
            log.info("【验签失败， 对方系统签名为：" + sign + "】");
            log.info("【验签失败， 我方系统签名为：" + s + "】");
        }
        return "error";
    }


}



