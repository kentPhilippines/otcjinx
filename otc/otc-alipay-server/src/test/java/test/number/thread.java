package test.number;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import cn.hutool.http.HttpUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class thread {
	static Lock lock = new ReentrantLock();

	public static void main(String[] args) {
        String pid = "ZRB615074FAB0D2A";
        String amount = 150 + ".00";
        String order_no = "2121121211112121212121212122121212121";
        String pay_type =  "603";
        String notify_url = "notify";
        Map map = new HashMap();
        map.put("pid", pid);
        map.put("order_no", order_no);
        map.put("amount", amount);
        map.put("pay_type", pay_type);
        map.put("notify_url", notify_url);

        System.out.println("【新盛话费加签参数：" + map + "】");
        String createParam = PayUtil.createParam(map);
        String md5 = PayUtil.md5(createParam + "&key=" + "317138754d41cee1325d30fcc4b18b7c");
        map.put("sign", md5);
        map.put("return_type", "json");
        String post = HttpUtil.post( "https://pay.xshengs.com"+"/pay/order", map);
        System.out.println("【新盛话费返回数据：" + post + "】");
    }


}

