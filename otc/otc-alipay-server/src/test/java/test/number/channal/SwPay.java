package test.number.channal;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.codec.digest.HmacUtils;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class SwPay {


    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
        wit(args);
    }

    public static void wit(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {

//        AccessKey(商户号):
//        dmLlkzZkaaseYgN1z6e2cly5
//        SecretKey(MD5密钥):
//        3vY6jOl0pMIXrxal5013Um72g288eyCrNVR4zpZ2
        String signKey = "3vY6jOl0pMIXrxal5013Um72g288eyCrNVR4zpZ2";
        String key = "dmLlkzZkaaseYgN1z6e2cly5";
        String order_no = RandomUtil.randomNumbers(9);
        String amount = "100";
        String ip = "127.0.0.1";
        String mid = "98";
        String time = System.currentTimeMillis() / 1000 + "";
        String notify_url = "http://localhost:8080";
        Map headers = new HashMap();
        headers.put("Content-Type", "application/json");
        Map<String, String> body = new HashMap();
        body.put("OrderNo", order_no);
        body.put("Timestamp", time );
        body.put("AccessKey",key);
        body.put("PayChannelId","13");
        body.put("Payee", "test");
        body.put("PayeeNo", "1234");
        body.put("PayeeAddress", "testadd");
        body.put("Amount", amount);
        body.put("CallbackUrl", notify_url);
        String param1 = createParam(body)+"&SecretKey="+signKey;
        System.out.println("加密前串为：" + param1);
        String sign1 = md5( param1);
        body.put("Sign", sign1.toLowerCase());
        JSONObject param = JSONUtil.parseFromMap(body);
        System.out.println("请求参数为：" + param);
        HttpResponse execute = HttpRequest.post("https://merchant.yixindaifu.xyz/api/pay/submit")
                .addHeaders(headers).body(param).execute();
        String ruselt = execute.body().toString();
        System.out.println(ruselt);
        //{"Data":{"MerchantOrderNo":"861878262","OrderNo":"YX23081614270340585","Payee":"test","PayeeNo":"1234","PayeeAddress":"testadd","OrderStatus":1,"Amount":100.0},"Code":0,"Message":""}
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
            for (int i = 0; i < temp.length; i++)
                result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
        return result;
    }

    public static String sign(String secretKey, String data) {

        byte[] bytes = HmacUtils.hmacSha1(secretKey, data);
        return Base64.getEncoder().encodeToString(bytes);

    }
    public static String createParam(Map<String, String> map) {
        try {
            if (map == null || map.isEmpty())
                return null;
            Object[] key = map.keySet().toArray();
            Arrays.sort(key);
            StringBuffer res = new StringBuffer(128);
            for (int i = 0; i < key.length; i++)
                if (ObjectUtil.isNotNull(map.get(key[i])))
                    res.append(key[i] + "=" + map.get(key[i]) + "&");
            String rStr = res.substring(0, res.length() - 1);
            return rStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
