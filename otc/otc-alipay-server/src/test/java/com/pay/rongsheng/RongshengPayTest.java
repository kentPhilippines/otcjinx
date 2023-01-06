package com.pay.rongsheng;

import alipay.manage.api.channel.util.qiangui.MD5;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;

public class RongshengPayTest {
    public static void main(String[] args) {
        RongshengPayTest newXiangyunPayTest = new RongshengPayTest();
        newXiangyunPayTest.testwit();
    }

    public void testwit()
    {
        String key ="EKGOTRRKBC43D5WQ";
        String payurl = "http://195.130.202.43/cash";
        String money = "100.00"; // 金额
        String payType = "bankcard"; // '银行编码
        String user = "jinxin";// 商户id
        String osn = RandomStringUtils.randomNumeric(10);// 20位订单号 时间戳+6位随机字符串组成
        String notifyUrl = "http://34.92.251.112:9010/notfiy-api-pay/newXiangyunWit-notify";
        String name = "张三";
        String card = "123123";
        String bankCode = "工商银行";

        MultiValueMap<String, String> param= new LinkedMultiValueMap<String, String>();
        param.add("user", user);
        param.add("bank", bankCode);
        param.add("card", card);
        param.add("name", name);
        param.add("money", money);
        param.add("osn", osn);


        String originalStr = createParam(param.toSingleValueMap())+"&key="+key;
        System.out.println(originalStr);
        String sign = MD5.MD5Encode(originalStr);
        param.add("sign", sign);
        param.add("nurl", notifyUrl);
        System.out.println(JSONUtil.toJsonStr(param.toSingleValueMap()));
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(param, headers);
        String rString = restTemplate.postForObject(payurl, request, String.class);
        System.out.println(rString);
    }
    public static String createParam(Map<String, String> map) {
        try {
            if (map == null || map.isEmpty()) {
                return null;
            }
            Object[] key = map.keySet().toArray();
//            Arrays.sort(key);
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
