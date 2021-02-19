package test.number.channal;

import alipay.manage.api.channel.deal.shunYiFu.ShunXinFuUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.http.HttpUtil;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class

shunxin {

    public static void main(String[] args) {

        String partner = "1000043363";
        String service = "10108";
        String tradeNo = UUID.randomUUID().toString();
        String amount = "700.00";
        String notifyUrl = "www.baidu.com";
        String extra = tradeNo;
        String key = "&" + "zzwyolmbhyledwlnta";
        Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("partner", partner);
        map.put("resultType", "json");
        map.put("service", service);
        map.put("tradeNo", tradeNo);
        map.put("amount", amount);
        map.put("notifyUrl", notifyUrl);
        map.put("extra", extra);
        String param = ShunXinFuUtil.createParam(map);
        param += key;
        System.out.println("【顺心付签名参数为：" + param + "】");
        String s = ShunXinFuUtil.md5(param);
        map.put("sign", s);

        System.out.println("【顺心付请求前参数为：" + param + "】");

        String post = HttpUtil.post("https://lhapit2xp.pp2pay.cc/unionOrder", map);
        String s1 = HtmlUtil.removeHtmlTag(post, true, "head", "script");
        String s2 = StrUtil.removeSuffix(s1, "' >\n" +
                "</form>\n" +
                "\n" +
                "</body></html>\n");
        String s3 = StrUtil.removePrefixIgnoreCase(s2, "<html><body><form name='postSubmit' method='POST' action='");
        System.out.println(
                s3
        );
    }
}
