package test.number.channal;

import alipay.manage.api.channel.deal.haiwang.Util;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

public class haiwang {
    public static void main(String[] args) {
        String pid = Util.PID;
        String money = "100.00";
        String sn = "1273612ss9926w919";
        String pay_type_group = Util.TYPE;
        String notify_url = "http://haiwang.com";
        String key = Util.KEY;
        String s = "pid=" + pid + "&money=" + money + "&sn=" + sn + "&pay_type_group=" +
                pay_type_group + "&notify_url=" + notify_url + "&key=" + key;
        System.out.println("签名参数：" + s.toString());
        String sign = Util.md5(s).toUpperCase();
        Map<String, Object> map = new HashMap<>();
        map.put("pid", pid);
        map.put("money", money);
        map.put("sn", sn);
        map.put("pay_type_group", pay_type_group);
        map.put("notify_url", notify_url);
        map.put("sign", sign);
        System.out.println("请求参数：" + map.toString());
        String post = HttpUtil.post(Util.URL, map);
        System.out.println(post);
        String code = JSONUtil.parseObj(post).getStr("code");
        if (code.equals("1")) {
            JSONObject jsonObject = JSONUtil.parseObj(post);
            String data = jsonObject.getStr("data");
            JSONObject jsonObject1 = JSONUtil.parseObj(data);
            String code_url = jsonObject1.getStr("code_url");
            System.out.println("支付连接：" + code_url);
        }
    }
}
