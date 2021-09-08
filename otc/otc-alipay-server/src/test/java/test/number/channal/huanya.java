package test.number.channal;

import alipay.manage.api.channel.deal.huanya.HuanYaUtil;
import alipay.manage.api.channel.util.shenfu.PayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import otc.common.PayApiConstant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class huanya {
    private static SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");
    private static String name = "付款人：";
    private static String appid = "202104191433446914";
    private static String pasword = "8708fwe0fhiuhsdhcsdu0dshcuhhcakdcadc00ehc0";
    private static String payurl = "http://hfsfapi.aa10six.com/gateway/bankgateway/pay";

    public static void main(String[] args) {
        //info_order=info_order
        // &money_order=2000
        // &name_goods=huafei
        // &no_order=J20210908114839448803471
        // &notify_url=http://47.243.66.246:23762/notfiy-api-pay/huiutongfu-notify
        // &oid_partner=202104191433446914
        // &pay_name=马双云
        // &pay_type=203
        // &sign_type=MD5
        // &time_order=20210908114839
        // &user_id=61383297e4b04f5e0dad4a29
        Map<String, Object> map = new HashMap();
        map.put("oid_partner", appid);
        map.put("notify_url", "http://47.243.66.246:23762/notfiy-api-pay/huiutongfu-notify");
        map.put("sign_type", "MD5");
        map.put("user_id", "61383297e4b04f5e0dad4a29");
        map.put("no_order", "J20210908114839448803471");
        map.put("time_order",  "20210908114839");
        map.put("money_order", "2000");
        map.put("name_goods", "huafei");
        map.put("pay_type",  "203");
        map.put("info_order", "info_order");
        if(StrUtil.isNotEmpty("付款人：马双云 ")) {
            String[] split = "付款人：马双云 ".split(name);
            String payName = split[1];
            map.put("pay_name", payName);
        }
        String createParam = PayUtil.createParam(map);
        System.out.println("【绅付转卡请求参数：" + createParam + "】");
        String md5 = PayUtil.md5(createParam + pasword);
        map.put("sign", md5);
        String post = HttpUtil.post( payurl, map);
        System.out.println("【绅付转卡返回数据：" + post + "】");






        // 【绅付请求参数：info_order=info_order&money_order=12580.00&name_goods=huafei&no_order=J20210908110614172719994&notify_url=http://47.243.66.246:23762/notfiy-api-pay/huiutongfu-notify&oid_partner=202104191433446914&pay_name=严建良&pay_type=203&sign_type=MD5&time_order=20210908110614&user_id=613828a6e4b0e1e4163333c0】
        // 【绅付请求参数：info_order=info_order&money_order=12580.00&name_goods=huafei&no_order=J20210908110614172719994&notify_url=http://47.243.66.246:23762/notfiy-api-pay/huiutongfu-notify&oid_partner=202104191433446914&pay_name=李文龙&pay_type=203&sign_type=MD5&time_order=20210908113429&user_id=61382f453170f4271ed33169】

        //info_order=info_order&money_order=2000&name_goods=huafei&no_order=J20210908114839448803471&notify_url=http://47.243.66.246:23762/notfiy-api-pay/huiutongfu-notify&oid_partner=202104191433446914&pay_name=马双云&pay_type=203&sign_type=MD5&time_order=20210908114839&user_id=61383297e4b04f5e0dad4a29
        //info_order=info_order&money_order=2000&name_goods=huafei&no_order=J20210908114839448803471&notify_url=http://47.243.66.246:23762/notfiy-api-pay/huiutongfu-notify&oid_partner=202104191433446914&pay_name=马双云&pay_type=203&sign_type=MD5&time_order=20210908114839&user_id=61383297e4b04f5e0dad4a29】
    }
}
