package com.pay.channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 支付demo
 */
public class TestOrder
{

    public static void main(String[] args) throws Exception
    {
        String key = "acd01a514ea08f30e4b38c131bf921bf";
        Map<String, String> map = new TreeMap();
        map.put("service", "pay.alipay.card");
        map.put("version", "1.0");
        map.put("merchantId", "200289");
        map.put("orderNo", "12018011512301500003");
        map.put("tradeDate", "20220127");
        map.put("tradeTime", "123015");
        map.put("amount", "5000");
        map.put("clientIp", "127.0.0.1");
        map.put("notifyUrl", "http://152.32.107.70:802/chaofanPayNotify");
        map.put("resultType", "json");
        map.put("sign", createSign(map, key));

        String reqUrl = "http://47.56.118.34:9100/gateway";
        System.out.println("reqUrl：" + reqUrl);

        CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
        String res = null;
        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            List<NameValuePair> nvps = new ArrayList();
            for (String k : map.keySet()) {
                nvps.add(new BasicNameValuePair(k, map.get(k)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            httpPost.addHeader("Connection", "close");
            client = HttpClient.createHttpClient();
            response = client.execute(httpPost);
            if (response != null && response.getEntity() != null) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                Map<String, String> resultMap = SignUtils.parseQuery(content);
                System.out.println("请求结果：" + content);

                if (resultMap.containsKey("sign")) {
                    if (!SignUtils.checkParam(resultMap, key)) {
                        res = "验证签名不通过";
                    }
                    else {
                        if (resultMap.get("repCode").equals("0001")) {
                            // 扫码使用此网址显示二维码
                            // 其他直接跳转至该网址
                            String resultUrl = resultMap.get("resultUrl");
                        }
                    }
                }
            }
            else {
                res = "操作失败";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            res = "系统异常";
        }
        finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
    }

    private static String createSign(Map map, String key)
    {
        Map<String, String> params = SignUtils.paraFilter(map);
        params.put("key", key);
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "UTF-8");

        return sign;
    }
}
