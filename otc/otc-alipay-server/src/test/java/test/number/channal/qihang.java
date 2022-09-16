package test.number.channal;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.codec.digest.HmacUtils;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


public class qihang {

    @Test
    public void checkOrangeSign () {
      String xml  = "<SuccessResponse><code>200</code><message>OK</message><data>success</data></SuccessResponse>";
      JSONObject jsonObject = JSONUtil.xmlToJson(xml);
      Object successResponse = jsonObject.get("SuccessResponse");
      System.out.println(successResponse);


    }
    @Test
    public void checkOrangeSign11 () {
        //import org.apache.commons.codec.digest.HmacUtils;

        String secretKey = "82c8a4bc8b81ed850ba1fecea0452529516cacd5d0d88fe9e4088c275732";
        String signStr = "amount=4000&bank_code=ABC&card_no=498090660322621333&holder_name=王好看&ip=127.0.0.1&mid=80&notify_url=http://localhost:8080&order_no=569002936&time=1660982997";
                        //amount=4000&bank_code=ABC&card_no=498090660322621333&holder_name=王好看&ip=127.0.0.1&mid=80&notify_url=http://localhost:8080&order_no=569002936&time=1660982997

        // 利用 apache 工具类 HmacUtils
        byte[] bytes = HmacUtils.hmacSha1(secretKey, signStr);
        String sign = Base64.getEncoder().encodeToString(bytes);

        System.out.println(sign);// A87/JDISQ2PVGSW8tonLimSuP0k=
    }
    public static void test(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
        String secretKey = "82c8a4bc8b81ed850ba1fecea0452529516cacd5d0d88fe9e4088c275732";
        String signStr = "amount=4000&bank_code=ABC&card_no=62234234234242463456567&holder_name=王好看&ip=127.0.0.1&mid=80&notify_url=http://localhost:8080&order_no=1618aa46-1f3e-4872-8461-96b01d8686d7&time=1660978354";

        String sign = sign(secretKey , signStr );
        System.out.println(sign);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
        pay(args);
    }

    public static void pay(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
        //52b8605da4ee112f449e2b3550a6260e45382f97
        //05b64abf4fd8d4bf57e2010ae22b7cc42e04b4d9d42460e1e1e1d69798b2
        String signKey = "05b64abf4fd8d4bf57e2010ae22b7cc42e04b4d9d42460e1e1e1d69798b2";
        String key = "52b8605da4ee112f449e2b3550a6260e45382f97";
        String order_no = RandomUtil.randomNumbers(9);
        String amount = "100";
        String ip = "127.0.0.1";
        String mid = "98";
        String time = System.currentTimeMillis() / 1000 + "";
        String notify_url = "http://localhost:8080";
        Map headers = new HashMap();
        headers.put("Authorization", "api-key " + key.trim());
        headers.put("Content-Type", "application/json");
        Map<String, String> body = new HashMap();
        body.put("order_no", order_no);
        body.put("gateway", "usdt");
        body.put("coin_type", "USDT_TRC20");
        body.put("amount", amount);
        body.put("ip", ip);

        body.put("time", time);
        body.put("mid", mid);
        body.put("notify_url", notify_url);
        String param1 = createParam(body);
        System.out.println("加密前串为：" + param1);
        String sign1 = sign( signKey.trim(),param1);
        body.put("sign", sign1);
        JSONObject param = JSONUtil.parseFromMap(body);
        System.out.println("请求参数为：" + param);
        HttpResponse execute = HttpRequest.post("https://api.ykt66.cc/api/v1/deposits")
                .addHeaders(headers).body(param).execute();
        String ruselt = execute.body().toString();
        System.out.println(ruselt);
    }

    public static void wit(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
        String signKey = "82c8a4bc8b81ed850ba1fecea0452529516cacd5d0d88fe9e4088c275732";
        String key = "bce8ae69874a73a537369836c5fc93af3d7db440";
        String order_no = RandomUtil.randomNumbers(9);
        String bank_code = "ABC";
        String amount = "100";
        String card_no = RandomUtil.randomNumbers(11)+"11111111";
        String ip = "127.0.0.1";
        String sign = "";
        String mid = "80";
        String time = System.currentTimeMillis() / 1000 + "";
        String notify_url = "http://localhost:8080";
        String holder_name = "王好看";
        Map headers = new HashMap();
        headers.put("Authorization", "api-key " + key.trim());
        headers.put("Content-Type", "application/json");
        Map<String, String> body = new HashMap();
        body.put("order_no", order_no);
        body.put("bank_code", bank_code);
        body.put("amount", amount);
        body.put("card_no", card_no);
        body.put("ip", ip);

        body.put("time", time);
        body.put("mid", mid);
        body.put("notify_url", notify_url);
        body.put("holder_name", holder_name);
        String param1 = createParam(body);
        System.out.println("加密前串为：" + param1);
        String sign1 = sign( signKey.trim(),param1);
        body.put("sign", sign1);
        JSONObject param = JSONUtil.parseFromMap(body);
        System.out.println("请求参数为：" + param);
        HttpResponse execute = HttpRequest.post("https://api.ykt66.cc/api/v1/transfers")
                .addHeaders(headers).body(param).execute();
        String ruselt = execute.body().toString();
        System.out.println(ruselt);
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
