package alipay.manage.api.channel.deal.jiabao;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Map;

public class JiaobaoUtil {

    // 发送HTTP请求代码
    public static String sendHttpRequest(String url, String data) {
        String result = null;
        try {
            String body = HttpRequest.post(url)
                    .header(Header.CONTENT_TYPE, "application/json;charset=UTF-8")//头信息，多个头信息多次调用此方法即可
                    .header("ali-cdn-real-ip", "103")//头信息，多个头信息多次调用此方法即可
                    .body(data)
                    .timeout(20000)//超时，毫秒
                    .execute().body();
            return body;
        } catch (Exception e) {
            return null;
        }
    }

    // 签名
    public static String getSign(Map<String, Object> map, String merchantKey) {
        StringBuffer signStr = new StringBuffer();
        for (Map.Entry<String, Object> m : map.entrySet()) {
            if (m.getValue() != null)
                signStr.append(m.getValue());
        }
//		signStr.append(merchantKey);
        return signStr.toString();
    }

    // MD5加密
    public static String sign(String source) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = source.getBytes(Charset.forName("UTF-8"));
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}
