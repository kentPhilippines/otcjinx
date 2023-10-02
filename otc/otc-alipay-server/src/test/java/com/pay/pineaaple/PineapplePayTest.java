package com.pay.pineaaple;

import alipay.manage.api.channel.util.qiangui.MD5;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import otc.result.Result;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
@Slf4j
public class PineapplePayTest {
    public static void main(String[] args) {
        PineapplePayTest newXiangyunPayTest = new PineapplePayTest();
        log.info("{}", JSONUtil.toJsonStr(newXiangyunPayTest.testwit()));
        ;
    }
    public Result testwit()
    {
        String key ="95e8e9aaae939af7f28ea6811cd0d6cd";
        String payurl = "http://kspay251apicfgogogopay.koubofeixingq.com/api/pay/unifiedorder";
        String money = "100.00"; // 金额
        String payType = "CnyNumber"; // '银行编码
        String user = "310";// 商户id
        String osn = RandomStringUtils.randomNumeric(10);// 20位订单号 时间戳+6位随机字符串组成
        String notifyUrl = "http://34.92.251.112:9010/notfiy-api-pay/newXiangyunWit-notify";
        String name = "张三";
        String card = "6222250112341234";
        String bankCode = "ICBC";



        Map<String,Object> param = new HashMap<String,Object>();
        param.put("mchid", user);
        param.put("out_trade_no", osn);
        param.put("amount", money);
        param.put("channel", payType);
        param.put("return_url", notifyUrl);
        param.put("time_stamp", DateTime.now().toString("YYYYMMDDhhmmss"));
        param.put("notify_url", notifyUrl);
        param.put("body", "test");
        String originalStr = createParam(param)+"&key="+key;
        System.out.println(originalStr);
        String sign = MD5.MD5Encode(originalStr).toLowerCase();
        param.put("sign", sign);

        String reqUrl = payurl;
        log.info("reqUrl：" + reqUrl);

        String res = null;
        try {
            String post = HttpUtil.post(reqUrl, param);
            log.info("请求结果：" + post);
            if (StringUtils.isNotEmpty(post)) {
                JSONObject jsonObject = JSONUtil.parseObj(post);
                String code = jsonObject.getStr("code");
                if ("0".equals(code)) {
                    String resultUrl = JSONUtil.toBean(jsonObject.get("data")+"",Map.class).get("request_url")+"" ;
                    return Result.buildSuccessResult("支付处理中", resultUrl);
                }
            } else {
                res = "操作失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = "系统异常";
        }
        //{"code":0,"msg":"\u8bf7\u6c42\u6210\u529f","data":{"request_url":"http:\/\/101.34.216.142\/CnyNumber.php?is_bzk=0&account_name=ACSHKko9LrA2s3S9mPBJxA%3D%3D&bank_name=jomLb6rwrlgRoxxEWcqj9Q%3D%3D&account_number=6VR7arFw%2FBiVu9L2KzE8%2FrKFJlqU1nolGVuNLIBA3bQ%3D&trade_no=6123139761&order_pay_price=99.98&sign=7ce3f1fb4fbab6430f20f5e652bb391f&user=mxDcQroBKsZt2YGVYY9KlyBnk7k9n0YITC3MlLxlRmyO7niR5PElEmruMbEJ%2B0d5&remark=1427423&is_pay_name=1"}}
        return Result.buildFailMessage(res);
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
