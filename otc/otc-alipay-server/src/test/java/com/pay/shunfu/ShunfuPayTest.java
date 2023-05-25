package com.pay.shunfu;

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

public class ShunfuPayTest {
    public static void main(String[] args) {
        ShunfuPayTest newXiangyunPayTest = new ShunfuPayTest();
        newXiangyunPayTest.testwit();
    }
    public void testwit()
    {
        String key ="b9aa8978ea84f179f83c9de8dab20dfe";
        String payurl = "https://pay.shunpay.cc/proxy/SpecialOrder";
        String money = "100.00"; // 金额
        String payType = "bankcard"; // '银行编码
        String user = "ZRB646C4A680B1FB";// 商户id
        String osn = RandomStringUtils.randomNumeric(10);// 20位订单号 时间戳+6位随机字符串组成
        String notifyUrl = "http://34.92.251.112:9010/notfiy-api-pay/newXiangyunWit-notify";
        String name = "张三";
        String card = "6222250112341234";
        String bankCode = "ICBC";

        /**
         * 商户号	pid	N	平台生成的商户PID
         * 订单号	order_no	N	商户提供的订单号
         * 订单金额	amount	N	生成订单的代付金额
         * 银行编码	bank_code	N	银行编码，参见银行附录
         * 银行卡号	bank_number	N	银行卡号，用户收款的银行卡号
         * 真实姓名	realname	N	绑定银行的真实姓名
         * 省	bank_province	Y	银行所属省份
         * 市	bank_city	Y	银行所属市
         * 支行	bank_branch	Y	银行支行名称
         * 回调地址	notify_url	N	支付成功或失败以后的回传地址
         * 签名	sign	N	参数签名方式
         */
        MultiValueMap<String, String> param= new LinkedMultiValueMap<String, String>();
        param.add("pid", user);
        param.add("order_no", osn);
        param.add("amount", money);
        param.add("bank_code", bankCode);
        param.add("bank_number", card);
        param.add("realname", name);
        param.add("notify_url", notifyUrl);


        String originalStr = key+ createParam(param.toSingleValueMap())+key;
        System.out.println(originalStr);
        String sign = MD5.MD5Encode(originalStr).toLowerCase();
        param.add("sign", sign);

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
