package alipay.manage.api.channel.deal.Hpay;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class HenPayNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @PostMapping("/hengPay-bank-notify")
    public Map notify(HttpServletRequest req, HttpServletResponse res) throws IOException {
        log.info("【收到恒支付成功回调】");
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
        } catch (
                IOException ex) {
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
        log.info("【收到恒支付成功请求，当前请求参数为：" + parseObj + "】");
        Map<String, Object> decodeParamMap = new ConcurrentHashMap();
        for (String key : keySet) {
            decodeParamMap.put(key, parseObj.getObj(key));
        }
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为：" + clientIP + "】");

        log.info("恒支付回调参数：" + decodeParamMap.toString());
        Object merchId = decodeParamMap.get("merchId");
/*
        Object money = decodeParamMap.get("money");
        Object curType = decodeParamMap.get("curType");
        Object time = decodeParamMap.get("time");
        Object tradeNo = decodeParamMap.get("tradeNo");
        Object signType = decodeParamMap.get("signType");
        Object payType = decodeParamMap.get("payType");
        Object code = decodeParamMap.get("code");
        Object msg = decodeParamMap.get("msg");
        */
        Object sign = decodeParamMap.get("sign");
        Object orderId = decodeParamMap.get("orderId");
        decodeParamMap.remove("sign");
        String createParam = PayUtil.createParam(decodeParamMap);
        String channelKey = super.getChannelKey(orderId.toString());
        String md5 = PayUtil.md5(createParam + "&md5_key=" + channelKey);
        if (md5.equals(sign.toString())) {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名成功】");
        } else {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名失败】");
            Map<String, Object> back = new HashMap<>();
            back.put("code", "9999");
            back.put("msg", "sign is errer  [我方验签错误]");
            return back;
        }

        Result dealpayNotfiy = dealpayNotfiy(orderId.toString(), clientIP);
        if (dealpayNotfiy.isSuccess()) {
            Map<String, Object> back = new HashMap<>();
            back.put("code", "0000");
            back.put("msg", "交易成功");
            return back;
        }
        Map<String, Object> back = new HashMap<>();
        back.put("code", "9999");
        back.put("msg", "  is errer  ");
        return back;
    }
}
