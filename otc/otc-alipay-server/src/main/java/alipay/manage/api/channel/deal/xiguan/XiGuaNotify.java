package alipay.manage.api.channel.deal.xiguan;

import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class XiGuaNotify extends NotfiyChannel {
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

    @RequestMapping("/xiguan-notfiy")
    public String notify(HttpServletRequest req, HttpServletResponse res, @RequestBody Map<String, Object> requestParams) throws IOException {
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【收到西瓜支付回调通知，参数为："+requestParams.toString()+"】");
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
        Object amount = requestParams.get("amount");
        Object deptId = requestParams.get("deptId");
        Object merchantOrderNo = requestParams.get("merchantOrderNo");
        Object orderNo = requestParams.get("orderNo");
        Object orderStatus = requestParams.get("orderStatus");
        Object sign = requestParams.get("sign");
        requestParams.remove("sign");
        String merchantOrderNo1 = getChannelKey(merchantOrderNo.toString());
        String param1 = createParam(requestParams);
        log.info("加密前串为：" + param1);
        String sign1 = md5(param1 + "&key=" + merchantOrderNo1);
        if (sign1.equals(sign)) {
            Result result = dealpayNotfiy(merchantOrderNo.toString(), clientIP, "祥云支付成功");
            if (result.isSuccess()) {
                return "success";
            }
        } else {
            log.info("【验签失败， 对方系统签名为：" + sign + "】");
            return "sign is error";
        }
        return "sign is error";
    }
}
