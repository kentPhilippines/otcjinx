package com.pay.leli;

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

public class LeliPayTest {
    public static void main(String[] args) {
        LeliPayTest newXiangyunPayTest = new LeliPayTest();
//        newXiangyunPayTest.testcard();
        newXiangyunPayTest.testalipay();
    }
    public void testcard()
    {
        String key ="a1a49bbb60f756715ed7df3c32d23838";
        String payurl = "https://paygate.lelipay.com:9043/lelipay-gateway-onl/txn";
        String money = "10000"; // 金额
        String payType = "401"; // '银行编码
        String user = "988001944000367";// 商户id
        String osn = RandomStringUtils.randomNumeric(10);// 20位订单号 时间戳+6位随机字符串组成
        String notifyUrl = "http://34.92.251.112:9010/notfiy-api-pay/newXiangyunWit-notify";
        String name = "张三";
        String card = "123123";
        String bankCode = "工商银行";

        MultiValueMap<String, String> param= new LinkedMultiValueMap<String, String>();
        param.add("txnType", "01");
        param.add("txnSubType", "21");
        param.add("secpVer", "icp3-1.1");
        param.add("secpMode", "perm");
        param.add("macKeyId", user);
        param.add("orderDate", DateTime.now().toString("yyyyMMdd"));
        param.add("orderTime", DateTime.now().toString("HHmmss"));
        param.add("merId", user);
        param.add("orderId", osn);
        param.add("payerId", RandomStringUtils.randomNumeric(16));
        param.add("pageReturnUrl", notifyUrl);
        param.add("notifyUrl", notifyUrl);
        param.add("productTitle", "test");
        param.add("txnAmt", money);
        param.add("currencyCode", "156");
        param.add("timeStamp", DateTime.now().toString("yyyyMMddHHmmss"));
        param.add("cardType", "DT01");
        param.add("bankNum", "01020000");
        param.add("accName", "王小二");
        param.add("sthtml", "1");
        String originalStr = createParam(param.toSingleValueMap())+"&k="+key;
        System.out.println(originalStr);
        String sign = MD5.MD5Encode(originalStr);
        param.add("mac", sign.toLowerCase());



        System.out.println(JSONUtil.toJsonStr(param.toSingleValueMap()));
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(param, headers);
        String rString = restTemplate.postForObject(payurl, request, String.class);
        System.out.println(rString);
        /**
         * {
         *   "orderId" : "5659810205",
         *   "secpVer" : "icp3-1.1",
         *   "mac" : "19b74b8f38d9e3685ef144121879025f",
         *   "extInfo" : "http://paygate.lelipay.com:9090/lelipay-gateway-onl/sthtml/ht202303250027540422zkzl/do",
         *   "timeStamp" : "20230325230702",
         *   "txnStatusDesc" : "交易處理中",
         *   "orderTime" : "230653",
         *   "txnStatus" : "01",
         *   "macKeyId" : "988001944000367",
         *   "respMsg" : "成功",
         *   "secpMode" : "perm",
         *   "merId" : "988001944000367",
         *   "currencyCode" : "156",
         *   "orderDate" : "20230325",
         *   "respCode" : "0000",
         *   "txnAmt" : "10000",
         *   "txnId" : "202303250000005102437370"
         * }
         */
    }

    public void testalipay()
    {
        String key ="a1a49bbb60f756715ed7df3c32d23838";
        String payurl = "https://paygate.lelipay.com:9043/lelipay-gateway-onl/txn";
        String money = "110000"; // 金额
        String payType = "401"; // '银行编码
        String user = "988001944000367";// 商户id
        String osn = RandomStringUtils.randomNumeric(10);// 20位订单号 时间戳+6位随机字符串组成
        String notifyUrl = "http://34.92.251.112:9010/notfiy-api-pay/newXiangyunWit-notify";
        String name = "张三";
        String card = "123123";
        String bankCode = "工商银行";

        MultiValueMap<String, String> param= new LinkedMultiValueMap<String, String>();
        param.add("txnType", "01");
        param.add("txnSubType", "32");
        param.add("secpVer", "icp3-1.1");
        param.add("secpMode", "perm");
        param.add("macKeyId", user);
        param.add("orderDate", DateTime.now().toString("yyyyMMdd"));
        param.add("orderTime", DateTime.now().toString("HHmmss"));
        param.add("merId", user);
        param.add("orderId", osn);
        param.add("payerId", RandomStringUtils.randomNumeric(16));
        param.add("pageReturnUrl", notifyUrl);
        param.add("notifyUrl", notifyUrl);
        param.add("productTitle", "test");
        param.add("txnAmt", money);
        param.add("currencyCode", "156");
//        param.add("cardType", "DT01");
//        param.add("bankNum", "01020000");
        param.add("timeStamp", DateTime.now().toString("yyyyMMddHHmmss"));
        param.add("sthtml", "1");

        String originalStr = createParam(param.toSingleValueMap())+"&k="+key;
        System.out.println(originalStr);
        String sign = MD5.MD5Encode(originalStr);
        param.add("mac", sign.toLowerCase());
        System.out.println(JSONUtil.toJsonStr(param.toSingleValueMap()));
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(param, headers);
        String rString = restTemplate.postForObject(payurl, request, String.class);
        System.out.println(rString);
        /**
         * {
         *   "orderId" : "7739768914",
         *   "codeImgUrl" : "https://kmwerdd.com/orders/f2321b5c-131e-401f-9d6b-a372d5f956dd?token=eyJhbGciOiJSUzI1NiJ9.eyJvcmRlcl9pZCI6ImYyMzIxYjVjLTEzMWUtNDAxZi05ZDZiLWEzNzJkNWY5NTZkZCJ9.ZGuYxKb142Q10aTsjWmu7FeavUqV9YBMxj3eppwAGu9RPl-5w68UJoTOlatcR6QukaRMSCLPq_Y8dn8bMd4n5BcJApyjlnLtQp5dmk2UwZGriYSslSN40XYnyC8WkybXI-MgtNcwc9a3ysIISK9GCbumq0mnDwzBzX_f39A1jkFmphCqfhvkLfFdVIIv1g6KjC5rpByNwgrlsmhb5S2wgTMUlD_zOJ_M7Ot3cpaVdyHNn7eOZrjEtmd_O1Escvs1T6no_i5U-MDBT8EP7tnNHpQWI_hcxhnq5ypHZaGJvmCML7e-jE69fucPp_oOYqFqeGV4ts9-TucdhogCu91pXA",
         *   "secpVer" : "icp3-1.1",
         *   "mac" : "ea6a3307465bc7d35a376a6b4bcceec7",
         *   "extInfo" : "",
         *   "timeStamp" : "20230325231617",
         *   "txnStatusDesc" : "订单建立",
         *   "orderTime" : "231608",
         *   "txnStatus" : "01",
         *   "macKeyId" : "988001944000367",
         *   "respMsg" : "成功",
         *   "secpMode" : "perm",
         *   "merId" : "988001944000367",
         *   "currencyCode" : "156",
         *   "orderDate" : "20230325",
         *   "respCode" : "0000",
         *   "txnAmt" : "110000",
         *   "txnId" : "202303250000005102480967"
         * }
         */
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
