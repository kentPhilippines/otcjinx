package test.number.channal;

import alipay.manage.api.channel.deal.haiwang.Util;

import java.util.HashMap;
import java.util.Map;

public class haiwang {
    public static void main(String[] args) {
        String pid = Util.PID;
        String money = "100.00";
        String sn = "12736129926919";
        String pay_type_group = Util.TYPE;
        String notify_url = "http://haiwang.com";
        String key = Util.KEY;
        String s = "pid=" + pid + "&money=" + money + "&sn=" + sn + "&pay_type_group" +
                pay_type_group + "&notify_url=" + notify_url + "&key=" + key;
        String sign = Util.md5(s).toUpperCase();
        Map<String, String> map = new HashMap<>();

        map.put("pid", pid);
        map.put("money", money);
        map.put("sn", sn);
        map.put("pay_type_group", pay_type_group);
        map.put("notify_url", notify_url);
        map.put("sign", sign);

    }
}
