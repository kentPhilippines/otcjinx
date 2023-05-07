package com.pay.yifu;

import alipay.manage.api.channel.util.qiangui.MD5;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@Slf4j
public class YifuPayTest {
    public static void main(String[] args) throws IOException {
        YifuPayTest newXiangyunPayTest = new YifuPayTest();
        newXiangyunPayTest.testwit();
    }
    public void testwit() throws IOException {
        String key ="x9V51wel7n55Q2N16V461C1LE84aU9I5";
        String payurl = "https://api.zhou--jiang.com/merchant/recharge/save-order";
        String money = "120.00"; // 金额
        String payType = "1"; // '银行编码
        String user = "gdfjnn7i";// 商户id
        String osn = RandomStringUtils.randomNumeric(10);// 20位订单号 时间戳+6位随机字符串组成
        String notifyUrl = "http://34.92.251.112:9010/notfiy-api-pay/yfNotify";


        MultiValueMap<String, String> param= new LinkedMultiValueMap<String, String>();
        param.add("merchantNo", user);
        param.add("customerNo", osn);
        param.add("userName", RandomStringUtils.randomAlphabetic(6));
        param.add("amount", money);
        param.add("returnType", "1");
        param.add("stype", payType);
        param.add("timestamp", new Date().getTime()+"");
        param.add("notifyUrl", notifyUrl);
        param.add("payIp", "127.0.0.1");

        String originalStr = createParam(param.toSingleValueMap())+"&key="+key;
        System.out.println(originalStr);
        String sign = MD5.MD5Encode(originalStr);
        param.add("sign", sign);

        param.add("depositName", RandomStringUtils.randomAlphabetic(5));
        param.add("realName", RandomStringUtils.randomAlphabetic(5));



        String requestJson = JSONUtil.toJsonStr(param.toSingleValueMap());
        log.info(requestJson);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(requestJson,mediaType);
        Request request = new Request.Builder()
                .url(payurl)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string().toString());
    }

    public static String createParam(Map<String, String> map) {
        try {
            if (map == null || map.isEmpty()) {
                return null;
            }
            Object[] key = map.keySet().toArray();
            Arrays.sort(key);
            StringBuffer res = new StringBuffer(128);
            for (int i = 0; i < key.length; i++) {
                if (ObjectUtil.isNotNull(map.get(key[i]))) {
                    res.append(key[i] + "=" + map.get(key[i]) + "&");
                }
            }
            String rStr = res.substring(0, res.length() - 1);
            return rStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
