package test.number.channal;

import alipay.manage.api.channel.util.baG.BaGPayUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.JsonObject;
import otc.util.MapUtil;
import java.util.*;

public class baG {
    static void test(){
        /**
         * 商户号	    merchant_id	    是	10000098	String(32)	分配唯一的商户标识
         * 订单号	    order_no	    是	201911111234567890	String(28)	商户订单号（每个订单号必须唯一）不可重复不可带特殊字符与中文。不可超过32位
         * 金额	        amount	        是	100.00	float	金额 以元为单位 两位小数点
         * 支付渠道	    method	        是	wechat	String	注意：请查看支付渠道或者咨询客服
         * 异步地址	    notify_url	    是	https://www.baidu.com	String	回调地址(支付成功时，会通知该地址订单状态成功)
         * 成功跳转地址	return_url	    否	https://www.baidu.com	String	跳转地址(支付成功时，会跳转该地址)
         * 版本号	    version	        是	2.0	String	版本号
         * 签名值	    sign	        是	xxxxxxxxxxxxxx	String	RSA2签名值
         */


    }
    private static final String SIGN_TYPE_RSA = "RSA";

    private static final String SIGN_TYPE_RSA2 = "RSA2";

    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    private static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public static void main(String[] args) {
        try {
            String appid = "114210057";
            String orderI = System.currentTimeMillis()/1000+"";
            String amount = "100.00";
            String paytype = "copybank";
            Map<String, Object> paramsMap = new LinkedHashMap<String, Object>();
            paramsMap.put("merchant_id",appid);
            paramsMap.put("order_no", orderI);
            paramsMap.put("amount", amount);
            paramsMap.put("method", paytype);
            paramsMap.put("notify_url",  "http://www.baidu.com" );
            paramsMap.put("return_url",  "http://www.baidu.com" );
            paramsMap.put("version", "2.0");
            String param = MapUtil.createParam(paramsMap);
            //   String paramsString = getURL(paramsMap);
            System.out.println("paramsString" +param);
            String PRIVATE_KEY="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMDfWK+cCRqM9Qam ps49XpZ6+/MfvuRw8z/Hx+CQwkZ7R9hrDEwrtMoUVaUPEMcvtC5I6XuTR+X07hw8 dmRXPUXQHmpe8U5tkoaEl6XoBWt/w9KZXcDknOCfMjJYbIiKEsPRIu925pZUvGH3 JuMtPEH43C2YaHZS1vXqXeUc7t5RAgMBAAECgYEArEcT5ZBfxVqBBxbWykOg+JMT 97+0eEK31JGz5NAI6IH308USr/seOp7dPVluqCzhKbKw81PEFhCom4oaSlhufh8s Bfwor2dMxkEsa1sySi0szYIN5ZhOkwgmS9hQfPfQxZd/TLcqxBdh3BJvU5UqqRM9 nyK1EJo/uvMxYEhqD40CQQDn4dDG8r0Tm2496A0Ooyk85wjrxH3zA6gUxHQ2YZ5Z Smq5OSYi80N3GZMMJfNOINZL1G0g42NR1KVOfMvKbbfPAkEA1O7YOah/TtAZ84Xq CvR+1OmozSdQ79t1o6bO2U+vjvEtqZZ6GPRdBiGvQC2FbmI3biZpDOxgIKN7cJ1A cgdv3wJBANQpBbfyAtN8xBo6RjAdUy7ZCI2HY+HEd7ZApT/Yg2SZNRqx0lXqE9FW Aff8hSf33XrWKt8LjiUiFfnBL0jQqHsCQCHXlCYV0aYFDRrXPctf8IiGWn3Asext RNUtvdJsB8sAKfG6KM2uiNpgoCnjEkHo+kZXdHrJVr3ZPdU4KPX2mKECQHYhSozr ZOcBu0MQBgn1lQcxDAenAO2DkzIvpdMXzjcSZ6SmTxb+/lPxUnLRtHfQtCEs1lWQ ROm9gf0bMK7zfXA=";
            String sign =  BaGPayUtil.sign(param.getBytes("UTF-8"),PRIVATE_KEY);
            System.out.println("sign" +sign);


            Map jsobObject = new HashMap();
            jsobObject.put("merchant_id", appid);
            jsobObject.put("order_no", orderI);
            jsobObject.put("amount",amount);
            jsobObject.put("method", paytype);
            jsobObject.put("notify_url", "http://www.baidu.com");
            jsobObject.put("return_url", "http://www.baidu.com");
            jsobObject.put("version", "2.0");
            jsobObject.put("sign", sign);
            JSONObject jsonObject2 = JSONUtil.parseObj(jsobObject);
            System.out.println(jsonObject2.toString());
            String result2 = HttpUtil.post("https://www.yfyfyfypay.com/gateway/build",jsonObject2.toString());
            System.out.println(result2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
