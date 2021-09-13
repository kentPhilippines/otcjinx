package test.number.channal;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class amount {

    private static String getNowDateStr() {
        return DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMAT);
    }

    public static void main(String[] args) {

        Map<String, Object> map = new HashMap<String, Object>();
        String oid_partner =  "202109092149374993";
        String notify_url = "test";
        String no_order =  "202109092149374993";
        Integer money_order = 1500;
        String acct_name =  "测试";
        String card_no = "202109092149374993";
        String bank_name = "测试银行";
        String time_order = getNowDateStr();
        map.put("oid_partner", oid_partner);
        map.put("notify_url", notify_url);
        map.put("no_order", no_order);
        map.put("channel", "4");
        map.put("money_order", money_order);
        map.put("acct_name", acct_name);
        map.put("card_no", card_no);
        map.put("bank_name", bank_name);
        map.put("time_order", time_order);
        map.put("sign_type", "MD5");

        String createParam = PayUtil.createParam(map);
        String md5 = PayUtil.md5(createParam + "789okjhy789okjuy7890plkju890olkju789okjhy789ok");
        map.put("sign", md5);
        System.out.println("【当前众邦代付请求参数为：" + map.toString() + "】");
        String post = HttpUtil.post( "http://ypsapi.uuusix.com/gateway/pay", map, 2000);
        System.out.println("【众邦代付响应参数为：" + post + "】");

    }
}
