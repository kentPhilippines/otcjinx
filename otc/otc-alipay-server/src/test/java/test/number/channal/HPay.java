package test.number.channal;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;

import java.text.SimpleDateFormat;
import java.util.*;

public class HPay<merchId> {
    private static SimpleDateFormat d = new SimpleDateFormat("YYYYMMDDhhmmss");

    public static void main(String[] args) {
        pay();
        //curType=CNY&merchId=662022111914064912&money=888&notifyUrl=http://www.sdadsa.cn/sda/asda&orderId=6f896593-bef2-400a-a557-0356c09f54e9&payName=李逵&payType=306&reType=INFO&signType=MD5&time=202211325015349&userId=李逵&md5ey=s66202P0-licy872&SGTSU11191406>>4912adsd
        //curType=CNY&merchId=123123&money=100&notifyUrl=www.baidu.com&orderId=1 2312312&payName=张三 &payType=66&reType=LINK&signType=MD5&time=20220905002841&userId=1&md5ey= PSnb9LnXabju0XIVbfQDFmMmLZiXw0pu
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


    static void pay() {
        String merchId = "662022111914064912";
        String money = "888";
        String userId = "李逵";
        String orderId = "76996922226969861198696";
        String time = d.format(new Date());
        String notifyUrl = "http://www.aaa.cn";
        String payType = "223";
        String curType = "CNY";
        String reType = "INFO";
        String signType = "MD5";
        String payName = userId;
        ;
        Map<String, Object> map = new HashMap<>();
        map.put("merchId", merchId);
        map.put("money", money);
        map.put("userId", userId);
        map.put("orderId", orderId);
        map.put("time", time);
        map.put("notifyUrl", notifyUrl);
        map.put("payType", payType);
        map.put("curType", curType);
        map.put("reType", reType);
        map.put("signType", signType);
        map.put("payName", payName);
        String param = createParam(map);
        System.out.println("恒支付签名参数：" + param);
        System.out.println(param);
        String k = param + "&md5_key=" + "8f8addf8eb98542cf0cbfd6cb857d36c";
        System.out.println(k);
        String md5 = PayUtil.md5(k);
        String sign = md5;
        System.out.println(md5);
        map.put("sign", sign);
        System.out.println(map.toString());
        String post1 = HttpUtil.post("http://api.hengzf.net/gateway/pay/index", map);
        System.out.println("恒支付响应参数：" + post1);
    }

    static void wit() {
   /*
    merchId √ ALL ALL 商户号
    money √ ALL ALL 代付金额 或 USDT 个数
    userId √ ALL ALL 商户平台会员唯一标识(可 md5 加密的值),如果无法传 平台用户唯一标识，请传 随机 数， 注： 识别恶意支付， 提高成功率
    orderId √ ALL ALL 订单号 curType √ ALL ALL 币种简称，见 1.3 币种列表
    bankType √ ALL ALL 账号类型，0-银行卡对私 1- 银行卡对公 3-USDT
    payType √ ALL ALL 代付类型，见 1.4 代付类型
    name × ALL（Except USDT） 0 或 1（必填） 收款银行卡姓名
    bank_key × ALL（Except USDT） PHP（必填） 0 或 1（必填） 银行简称,见对应币种银行列 表(简称)，例如：ABC ；若银 行列表无对应简称，统一传入： OTHER
    bank × ALL（Except USDT） 0 或 1（必填） 银行名称,见对应币种银行列 表(名称)，例如：中国农业银 行；若银行列表里无对应银行， 传入需代付的银行名称即可
    card × ALL（Except USDT） 0 或 1（必填） 收款银行卡号
    bankBranch × ALL（Except USDT） 0 或 1 支行名称
    bankNum × INR（必填） 0 或 1（必填） ISFC 编号
    usdtType × USDT 3 USDT 代付收款地址协 议,USDT-ERC20,USDT-TRC20 usdtAddress × USDT 3 USDT 代付收款地址
    time √ ALL ALL 请求时间，时间格式： YYYYMMDDhhmmss 14 位数字， 精确到秒。如：20221024145654
    notifyUrl √ ALL ALL 异步通知接口地址，用于通知 支付结果
    signType √ ALL ALL 签名类型 MD5 mobile × ALL（Except USDT） 0 或 1 手机号
    sign √ ALL ALL 签名，生成方式见备注
    */


        String merchId = "662022111914064912";
        String money = "1000";
        String userId = "李逵";
        String orderId = "60000001112";
        String bankType = "0";
        String payType = "88";
        String curType = "CNY";
        String name = "李逵";
        String bank_key = "OTHER";
        String bank = "北京银行";
        String card = "66202000000000000001";
        String time = d.format(new Date());
        String notifyUrl = "http://www.bas.cn";
        String signType = "MD5";
        Map<String, Object> map = new HashMap<>();
        map.put("merchId", merchId);
        map.put("money", money);
        map.put("userId", userId);
        map.put("curType", curType);
        map.put("orderId", orderId);
        map.put("time", time);
        map.put("notifyUrl", notifyUrl);
        map.put("payType", payType);
        map.put("bankType", bankType);
        map.put("name", name);
        map.put("signType", signType);
        map.put("bank_key", bank_key);
        map.put("bank", bank);
        map.put("card", card);
        String param = createParam(map);
        System.out.println("恒支付签名参数：" + param);
        System.out.println(param);
        String k = param + "&md5_key=" + "8f8addf8eb98542cf0cbfd6cb857d36c";
        System.out.println(k);
        String md5 = PayUtil.md5(k);
        String sign = md5;
        System.out.println(md5);
        map.put("sign", sign);
        System.out.println(map.toString());
        String post1 = HttpUtil.post("http://api.hengzf.net/gateway/repay/index", map);
        System.out.println(post1);

    }
}
