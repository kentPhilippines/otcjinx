package com.pay.newxiangyun;

import alipay.manage.api.channel.util.qiangui.MD5;
import cn.hutool.core.util.ObjectUtil;
import com.pay.shanhe.RSASignature;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class NewXiangyunPayTest {
    public static void main(String[] args) {
        NewXiangyunPayTest newXiangyunPayTest = new NewXiangyunPayTest();
        newXiangyunPayTest.test();
    }
    public void test()
    {
        String key ="MLPK0BBPcZiuqEcQfhdhDkl2ReVcckACsZZBOg6LbUUA3uDF28yV0jij4jdBBcwt";
        String payurl = "https://api.topmav.com/pay/unifiedorder";
        String payurl1 = "http://localhost:8081/forwardForAll?url="+payurl;
        String totalAmt = "500"; // 金额
        String payType = "bankcard"; // '银行编码
        String storeCode = "1000517";// 商户id
        String storeOrderNo = RandomStringUtils.randomNumeric(10);// 20位订单号 时间戳+6位随机字符串组成
        String backUrl = "http://XXXX.com/";// 通知地址
        String notifyUrl = "http://XXXX.com/";// 通知地址
        String playerName = "张三";// 通知地址

        String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCF3utQV6OJHCzwHnSRAay1ra3y8kzPbanfadbWiotZyVSZPcP5oFdg1JhQ4gwwMZBhnAn/0MozO0XeakY+ey6IeIyHdpoeamDLCop/DT3PQXSDhFpzSPvdKnB6U6izCLLlrYX15j3EQ5JNh4WgCFbI1nuLZJCJANjamIJMLIi9B0cpKLewDTjCFSb7KS60WZ2IY3NviGEGAc6aYUWVzPWsoIrrb/8QAmTXMRxe28aGgIurzh0QGd/CIXrT6ERIum2Q5i3uluw/MLDIuzmto+91RBiQg6Ybv2PgwaM8Sj6xs/zESG1OkeuucIFqGTV7FZbrjFy1DD5eGWwgBgUu6DJxAgMBAAECggEAC1t2K0LEJQW26kgrK4Iw3Nj3QP09dvuexc185Iase9mQy89pqOvpfdWLdpE253/M9/r8/i8AeIg2zT/G5dYdhIZ1pahyOtJbk945Eb5V2Bd9gwrfgZhXhdPr5vTiAw5h3wpxqwL1iokRCHLO61zJwBAyOwk1Gepwe1sk8sF0eFTcsUV0tHDvCD4WpkltrpUl4krkKVy04t+dDtdVfWgJRMhD7kfWLQnpO2/CpSvXHtjzy2zkbY/alt3pGmk5NAxg6Y41zDlZ0t2FlniukpIX36kpFRUbYigH0ydzTPyrHKP6rie1RfBxomOKIJYcodZ8hm7EeM/pUB5+3jifQzQAAQKBgQD/Nsp2bFSzffiPM+CKqYWN6WEpmzppobmlu3V1dhmxZkMd0HD0EjmW046TY+2OA6iulqj99uGH8N19jcCjZ/YztWsjI6bn6RdPbTzB2dQhezEp6IxteNiN/5mY2kJ5OJYSzkoG2igkT4sO4Wuv5txI//ue0N5DnH7sB0KolphocQKBgQCGSHZKYsVAsx2/ugUNBSxCHDXtjBGjbabT2qrI6DGrjtu8XdyU5Xg3e6t2tEcpp05ZYbqtKrQZoECs9b+WsJ/mVoxiWMoq0YZjwIAT2yXihf9gL/fvvOWjo07Jt9sycWxnGZz/y3KjgsGCjRZCit8imOeJmwugJFQxbAhbrqFqAQKBgQCdsA/mncaz5Jioeog5hMa1SUa0e2nbDIb1ZN8NRXxYhPPhPwIbfAtDKtIENZD41z/pJ8Ogr2LoKKXhxq0JCdowVt6spoGg9v9lHvyUVe/hBAn7d+kUVr+O9SfFLs41sgXf9r/8fdyhmtgzyIpN9BmVrTyeAzXhlpdBZLcGDY57sQKBgG9VqwX3qHYBTBwHnmJyNITHoQrIRGOM6XHjbhBPO8dzFcqyw82MCEVwOvSbehkWB0biWYVzz4kjrhv5URUeciTnA1QTK+Oeft873rUX0OxkjxzwCJBzvK4VG6Dx2EKVefxtZxdKVk0tf0W/toILY//qaKZVr6jiFhA38yIYwuoBAoGAUJ4tTS8NiB3vCYV9uLzTGU+UyAIzCzObXjbkb7DwperzrawPHQh/iTFPHhQQHyVjp1yQaYONNmuhLZTjIbu7XwlfQvpDaSEY9/pBcgRvR5MFjHpIK7ACRhiBJhFXfbvq0b3zbTVY5lFvUQDzH3beZU5o6PZtSesLJZwLcXi2hjE=";// 用户私钥
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("storeCode", storeCode);
        param.add("storeOrderNo", storeOrderNo);
        param.add("payType", payType);
        param.add("totalAmt", totalAmt);
        param.add("backUrl", backUrl);
        param.add("notifyUrl", notifyUrl);
        param.add("playerName", playerName);

        String originalStr = createParam(param.toSingleValueMap())+"&key="+key;
        String sign = MD5.MD5Encode(originalStr);
        param.add("sign", sign);

        RestTemplate restTemplate = new RestTemplate();
        String rString = restTemplate.postForObject(payurl1, param.toSingleValueMap(), String.class);
        System.out.println(rString);
    }
    public void testwit()
    {
        String key ="MLPK0BBPcZiuqEcQfhdhDkl2ReVcckACsZZBOg6LbUUA3uDF28yV0jij4jdBBcwt";
        String payurl = "https://api.topmav.com/withdraw/unifiedorder";
        String transAmt = "100"; // 金额
        String payType = "bankcard"; // '银行编码
        String storeCode = "1000517";// 商户id
        String storeOrderNo = RandomStringUtils.randomNumeric(10);// 20位订单号 时间戳+6位随机字符串组成
        String notifyUrl = "http://34.92.251.112:9010/notfiy-api-pay/newXiangyunWit-notify";
        String accountName = "张三";
        String accountNo = "123123";
        String bankCode = "ICBC";

        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("storeCode", storeCode);
        param.add("storeOrderNo", storeOrderNo);
        param.add("accountName", accountName);
        param.add("accountNo", accountNo);
        param.add("transAmt", transAmt);
        param.add("notifyUrl", notifyUrl);
        param.add("bankCode", bankCode);
        param.add("bankBranchName", "工商银行");
        param.add("bankAccountType", "1");
        param.add("province", "湖南");
        param.add("city", "长沙");
        param.add("userIdCard", "123");
        param.add("userMobile", "1234");
        param.add("transMemo", "5555");

        String originalStr = createParam(param.toSingleValueMap())+"&key="+key;
        System.out.println(originalStr);
        String sign = MD5.MD5Encode(originalStr);
        param.add("sign", sign);

        RestTemplate restTemplate = new RestTemplate();
        String rString = restTemplate.postForObject(payurl, param.toSingleValueMap(), String.class);
        System.out.println(rString);
    }
    public static String createParam(Map<String, Object> map) {
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
