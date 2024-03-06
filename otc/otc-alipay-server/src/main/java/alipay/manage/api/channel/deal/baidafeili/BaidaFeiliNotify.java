package alipay.manage.api.channel.deal.baidafeili;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * code        √ 状态标识 0000-成功
 * msg         √ 信息说明
 * sign        √ 签名
 * merchId     √ 商户号
 * money       √ 订单金额
 * tradeNo     √ 平台交易号
 * orderId     √ 订单号
 * time        √ 请求时间，时间格式：YYYYMMDDhhmmss 14 位数 字，精确到秒。如：20221024145654
 * signType    √ 签名类型 MD5
 * payType     √ 支付类型
 * curType     √ 币种简称
 * floatMoney  √ 浮动金额（实际到账金额以该金额为准）
 */
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class BaidaFeiliNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();

    @PostMapping("/BaidaFeiliPay-notify")
    public Map notify(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Map respo = new HashMap();
        log.info("【收到 百达斐丽 支付成功回调】");
        InputStream inputStream = req.getInputStream();
        String body;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
        body = stringBuilder.toString();
        JSON parse = JSONUtil.parse(body);
        JSONObject parseObj = JSONUtil.parseObj(parse);

        Set<String> keySet = parseObj.keySet();
        log.info("【收到 百达斐丽 支付成功请求，当前请求参数为：" + parseObj + "】");
        Map<String, Object> decodeParamMap = new ConcurrentHashMap();
        for (String key : keySet) {
            decodeParamMap.put(key, parseObj.getObj(key));
        }
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为：" + clientIP + "】");
        log.info("【转换为map为：" + decodeParamMap.toString() + "】");
        String sign = (String) decodeParamMap.get("sign");
        decodeParamMap.remove("sign");
        String createParam =  SanYangUtil.createParam(decodeParamMap);
        String oid_partner = parseObj.getStr("orderId");//获取渠道商户Id
        String channelKey = super.getChannelKey(oid_partner);
        String md5 = PayUtil.md5(createParam +"&md5_key=" + channelKey);
        if (md5.equals(sign)) {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名成功】");
        } else {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名失败】");
            respo.put("code", 9999);
            respo.put("msg", " sign is errer ");
            return respo;
        }

        Result dealpayNotfiy = dealpayNotfiy((String) decodeParamMap.get("orderId"), clientIP);
        if (dealpayNotfiy.isSuccess()) {
            respo.put("code", 0000);
            respo.put("msg", " 成功 ");
            return respo;
        }
        respo.put("code", 9999);
        respo.put("msg", " is errer ");
        return respo;
    }
}
