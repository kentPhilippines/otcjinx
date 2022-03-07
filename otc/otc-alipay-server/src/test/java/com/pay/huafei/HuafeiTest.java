package com.pay.huafei;

import com.pay.channel.HttpClient;
import com.pay.channel.MD5;
import com.pay.channel.SignUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.*;

public class HuafeiTest {

    /**
     * 密鑰：SCSD7lwUYyv2xVCU9grlV2ByuRtJPmv3LbiGcEF6SLUWsao8k7rc8Y8HtWYgVgs3
     *
     * token：P7q2kLuGb0K0QQu7LGSkds7XG5Q0772m
     *
     * 100226
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        String key = "SCSD7lwUYyv2xVCU9grlV2ByuRtJPmv3LbiGcEF6SLUWsao8k7rc8Y8HtWYgVgs3";
        String token = "P7q2kLuGb0K0QQu7LGSkds7XG5Q0772m";
        Map<String, String> map = new TreeMap();
        map.put("mch_id", "100226");
        map.put("total", "100");
        map.put("out_trade_no", "12018011512301500007");
        map.put("timestamp", new Date().getTime()/1000+"");
        map.put("type", "9018");
        map.put("return_url", "127.0.0.1");
        map.put("notify_url", "http://152.32.107.70:802/chaofanPayNotify");
        map.put("sign", createSign(map, key,token));
        map.put("request_token", token);

        String reqUrl = "http://34.150.111.99/api/order";
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

    private static String createSign(Map map, String key,String token)
    {
        Map<String, String> params = SignUtils.paraFilter(map);
        //params.put("key", key);
        //params.put("token", token);
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams1(buf, params, false);
        String preStr = buf.toString()+key+token;
        System.out.println(preStr);
        String sign = MD5.sign(preStr, "UTF-8");

        return sign;
    }
    //mch_id100005notify_urlwww.baidu.comout_trade_noE20201124162543244030return_urlwww.baidu.comtimestamp1606206344total99typealipay_wap + secret(秘钥) + request_token（请求Token）
    //mch_id100226notify_urlhttp://152.32.107.70:802/chaofanPayNotifyout_trade_no12018011512301500007request_tokenP7q2kLuGb0K0QQu7LGSkds7XG5Q0772mreturn_url127.0.0.1timestamp1646580672total100type9018SCSD7lwUYyv2xVCU9grlV2ByuRtJPmv3LbiGcEF6SLUWsao8k7rc8Y8HtWYgVgs3P7q2kLuGb0K0QQu7LGSkds7XG5Q0772m
}
