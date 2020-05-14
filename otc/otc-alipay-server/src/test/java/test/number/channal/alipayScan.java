package test.number.channal;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.lang.UUID;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;

public class alipayScan {
	public static void main(String[] args) {
		test();
	}
	static void test(){ 
		String apiurl = "http://www.qsy123.cn/api/orders/index.html"; // API下单地址
		String key = "zfZ2BTd6PHKvwCxU"; // 商户密钥
		String bankco = "alipay";
		String moneys = "200";
		String orderId = UUID.randomUUID().toString();
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("mch_id", "3362"); // 商户号
		parameterMap.put("type", bankco ); // 支付类型
		parameterMap.put("price",  moneys ); // 金额
		parameterMap.put("out_order_id", orderId); // 商户号
		parameterMap.put("notifyurl", "www.baidu.com"); // 异步通知地址
		parameterMap.put("returnurl","www.baidu.com"); // 同步通知地址
		parameterMap.put("extend", "312|xxx"); // 附加数据
		String stringSignTemp = "extend="+parameterMap.get("extend")+"&mch_id="+parameterMap.get("mch_id")+"&notifyurl="+parameterMap.get("notifyurl")+"&out_order_id="+parameterMap.get("out_order_id")+"&price="+parameterMap.get("price")+"&returnurl="+parameterMap.get("returnurl")+"&type="+parameterMap.get("type")+"&key="+key;
		parameterMap.put("sign", md5(stringSignTemp).toUpperCase()); // 附加数据
		String jsonString = HttpUtil.post(apiurl, parameterMap);
		System.out.println(jsonString);
	}
	 public static String md5(String a) {
	    	String c = "";
	    	MessageDigest md5;
		   	String result="";
			try {
				md5 = MessageDigest.getInstance("md5");
				md5.update(a.getBytes("utf-8"));
				byte[] temp;
				temp=md5.digest(c.getBytes("utf-8"));
				for (int i=0; i<temp.length; i++)
					result+=Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			}
			return result;
	    }

}
