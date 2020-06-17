package test.number.channal;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.netflix.ribbon.proxy.annotation.Http;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;

public class YiFu {
	public static void main(String[] args) {
		/**
		 * 	app_id				是		是			APP_ID
			channel				是		是			通道编号，见下方目录
			out_trade_no		是		是			外部订单号
			money				是		是			金额，单位元，两位小数点
			callback_url		是		是			异步回调地址
			terminalType		否		否			终端类型：Android、IOS、PC、Others
			sign				是		否			签名：详见下方签名方式
		 */
		
		test();
	}

	private static void test() {
		String key = "kRrgxUk0QCpxz1bS5QVRJXZKEFPGnrqD";
		String app_id = "-YbBmS2aFEfgBx4mnXI" ;
		String channel = "1011";
		String out_trade_no = StrUtil.uuid();
		String money = "100";
		String callback_url = "https://www.uz-pay.com/Api/collection/query";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("app_id", app_id);
		map.put("channel", channel);
		map.put("out_trade_no", out_trade_no);
		map.put("money", money);
		map.put("callback_url", callback_url);
		String createParam = createParam(map);
		String md5 = md5(createParam + "&key="+key);
		String sign = md5.toUpperCase();
		map.put("sign", sign);
		String post = HttpUtil.post("https://ef.sanbanye.com/pay", map);
		System.out.println(post);
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
	public static String createParam(Map<String, Object> map) {
		try {
			if (map == null || map.isEmpty())
				return null;
			Object[] key = map.keySet().toArray();
			Arrays.sort(key);
			StringBuffer res = new StringBuffer(128);
			for (int i = 0; i < key.length; i++) 
				if(ObjectUtil.isNotNull(map.get(key[i])))
					res.append(key[i] + "=" + map.get(key[i]) + "&");
			return res.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
