package test.number.channal;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class shuadeduo {
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        String key = "Bearer wLMoIrTFGtsbIHM4FV3gYA3T1Z3XPNLKkegoxSzyrBc9tX6ezzVCVaZyJG0SYCkg";
        map.put("trade_no", StrUtil.uuid().toString());
        map.put("amount", "1000.00");
        map.put("notify_url", "http://starpay168.com:5055/api-alipay/");
        map.put("ip", "128.98.2.90");
        String result2 = HttpRequest.post("https://4536251.net/api/transaction")
                .header(Header.CONTENT_TYPE, "application/x-www-form-urlencoded")//头信息，多个头信息多次调用此方法即可
                .header("Authorization", key)//头信息，多个头信息多次调用此方法即可
                .form(map)//表单内容
                .timeout(20000)//超时，毫秒
                .execute().body();

        System.out.println(result2);
    }
}
