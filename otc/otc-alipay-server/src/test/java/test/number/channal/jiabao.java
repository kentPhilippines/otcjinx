package test.number.channal;

import alipay.manage.api.channel.deal.jiabao.JiaobaoUtil;
import alipay.manage.api.channel.deal.jiabao.RSAUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class jiabao {
    private static final Log log = LogFactory.get();
    private static DecimalFormat dcf = new DecimalFormat("#.00");
    private static String merchantPrivateKey = "6cb94440755c4090a4e37f16e9f726cb";
    private static String merchantCode = "jx888m1";
    private String newLoadPayPublicKey;//请求串加密密钥

    public static void main(String[] args) {
        sendPayRequest();
    }

    //发送充值申请
    public static String sendPayRequest(
    ) {

        String amount = "200.00";
        String notify = "www.baidu.com";
        String channel = "AlipayBank";
        String url = "https://api.yushiyp.com/pay/center/deposit/apply";
        String merchantTradeNo = "test" + System.currentTimeMillis();
        HashMap<String, Object> map = new HashMap<>();
        map.put("merchantCode", merchantCode);
        map.put("merchantTradeNo", merchantTradeNo);
        map.put("userId", merchantCode); //玩家 用户ID
        map.put("amount", amount);
        map.put("notifyUrl", notify);//服务器回调地址
        //       map.put("terminalType",2);
        map.put("channel", channel);
        String signStr = RSAUtil.createParam(map);
        log.info("signStr ====== {}", signStr);
        String sign = RSAUtil.md5(signStr.toString() + merchantPrivateKey);
        map.put("sign", sign);
        //     map.put("configCode", configCode);
// "{\"merchantCode\":\"jx888m1\",\"amount\":\"200.00\",\"merchantTradeNo\":\"test1603698321780\",\"channel\":\"AlipayBank\",\"sign\":\"9ebf9c3b3f8658c4b796905e3b0f496c\",\"notifyUrl\":\"www.baidu.com\",\"userId\":\"jx888m1\",\"terminalType\":2}\n"
//
        JSONObject merJson = new JSONObject();
        merJson.put("merchantCode", merchantCode);
        merJson.put("content", "" + JSONUtil.parseObj(map) + "");
        merJson.put("signType", "md5");
        log.info("请求参数：{}", merJson.toString());
        String result = JiaobaoUtil.sendHttpRequest(url, merJson.toString());
        log.info("返回结果：{}", result);
        JSONObject resJSON = JSONUtil.parseObj(result);
        if (resJSON.getStr("status").equalsIgnoreCase("FAIL")) {
            return result;
        }
        JSONObject data = JSONUtil.parseObj(resJSON.getStr("data"));
        String resContent = data.getStr("content");
        JSONObject resData = JSONUtil.parseObj(resContent);
        String payUrl = resData.getStr("payUrl");
        Map<String, Object> returnMap = new TreeMap<>();
        returnMap.put("merchantCode", resData.getStr("merchantCode"));
        returnMap.put("merchantTradeNo", merchantTradeNo);
        returnMap.put("tradeNo", resData.getStr("tradeNo"));
        returnMap.put("payUrl", payUrl);
        log.info(payUrl);
        return payUrl;
    }

    @PostMapping("/notify")
    @ResponseBody
    public String notify(@RequestBody JSONObject jsonObject) {
        log.info("收到回调内容：{}", jsonObject);
        merchantCode = jsonObject.getStr("merchantCode");
        String content = jsonObject.getStr("content");
        JSONObject ntobj = JSONUtil.parseObj(content);
        Map<String, Object> notifyMap = new TreeMap<>();
        notifyMap.put("amount", ntobj.getStr("amount"));
        notifyMap.put("merchantCode", ntobj.getStr("merchantCode"));
        notifyMap.put("merchantTradeNo", ntobj.getStr("merchantTradeNo"));
        notifyMap.put("tradeStatus", ntobj.getStr("tradeStatus"));
        notifyMap.put("tradeNo", ntobj.getStr("tradeNo"));
        notifyMap.put("notifyTime", ntobj.getStr("notifyTime"));

        StringBuffer signStr = new StringBuffer();
        for (Map.Entry<String, Object> m : notifyMap.entrySet()) {
            if (m.getValue() != null)
                signStr.append(m.getValue());
        }

        log.info("收到回调内容：签名字符串：{}", signStr);
        String s = RSAUtil.md5(signStr + merchantPrivateKey);


        // 处理到账 业务

        log.info("处理结果 ：SUCCESS");
        //处理 成功，响应此内容
        return "SUCCESS";
    }

}
