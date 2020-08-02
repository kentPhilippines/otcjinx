package test.number;

import alipay.manage.api.channel.util.yifu.YiFu02Util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AmountTest {
	public static void main(String[] args) {
		Map<String,Object> yifuMap = new HashMap<>();
		yifuMap.put("order_id", "C1596119675803817527");
		yifuMap.put("user_id", "2u7rMduh");
		yifuMap.put("amount", "100000");
		yifuMap.put("transact_id", "aca047bcdebb68598f5f719b9e86f8e4");
		yifuMap.put("real_amount", "100000");
		yifuMap.put("pay_time", "2020-07-30 22:35:25");
		yifuMap.put("status","1");//7693189d0ed57388f8ab71464a1e8f29
		String param = YiFu02Util.createParam(yifuMap);
		System.out.println(param);
		String s = YiFu02Util.md5(param + "key=" + YiFu02Util.KEY);
		System.out.println(s);

	}

}
