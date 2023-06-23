package com.pay.caiyun;

import alipay.manage.api.channel.util.qiangui.MD5;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.DateTime;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;
public class CaiyunPayTest {
    public static void main(String[] args) {
        CaiyunPayTest newXiangyunPayTest = new CaiyunPayTest();
//        newXiangyunPayTest.testcard();
        newXiangyunPayTest.testcard();
    }
    public void testcard()
    {
        String key ="eac29f43410e8cecc210605ce52fb994";
        String payurl = "http://hgapi2.facaishop.com/v1/order/create";
        String money = "800.00"; // 金额
        String payType = "QX801705"; //
        String user = "35508142";// 商户id
        String osn = RandomStringUtils.randomNumeric(10);// 20位订单号 时间戳+6位随机字符串组成
        String notifyUrl = "http://34.92.251.112:9010/notfiy-api-pay/caiyun-notify";

        MultiValueMap<String, String> param= new LinkedMultiValueMap<String, String>();
        param.add("merchant_no", user);
        param.add("pay_code", payType);
        param.add("order_amount", money);
        param.add("order_no", osn);
        param.add("callback_url", notifyUrl);
        param.add("ts", DateTime.now().getMillis()+"");

        String originalStr = createParam(param.toSingleValueMap())+"&key="+key;
        System.out.println(originalStr);
        String sign = MD5.MD5Encode(originalStr);
        param.add("sign", sign.toLowerCase());



        System.out.println(JSONUtil.toJsonStr(param.toSingleValueMap()));
        RestTemplate restTemplate = new RestTemplate();
        String rString = restTemplate.postForObject(payurl, param.toSingleValueMap(), String.class);
        System.out.println(rString);
        //{"success":true,"code":200,"msg":"","data":{"merchant_no":"35508142","order_no":"0526224879","platform_no":"2023041917573825861448173","order_amount":"799.99","pay_code":"QX801705","qr_code":"http:\/\/pay.fangzuhg2.com\/c\/api\/pay?osn=2023041917573825861448173","pay_url":"http:\/\/pay.fangzuhg2.com\/c\/api\/pay?osn=2023041917573825861448173"}}

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
            //log.info(rStr);
            return rStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
