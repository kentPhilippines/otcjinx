package test.number.channal;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;

public class jiufu {
	public static void main(String[] args) {
		String partner = "1006346282";
		String service = "10109";//"10108" 宝转卡    10101 网关支付    10114
		String tradeNo = StrUtil.uuid();
		String amount = "1000";
		String notifyUrl = "www.baidu.com";
		String extra = "abc";
		String resultType = "json";
		String sign = "";
		String key = "yhdbzeqlusdgchemxs";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("partner", partner);
		map.put("service", service);
		map.put("tradeNo", tradeNo);
		map.put("amount", amount);
		map.put("notifyUrl", notifyUrl);
		map.put("resultType", resultType);
		map.put("extra", extra);
		String createParam = createParam(map);
		map.put("sign", md5(createParam+"&"+key));
		String post = HttpUtil.post("https://newapi.9pay.vip/unionOrder", map);
		System.out.println(post);
		
		
		
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
			String rStr = res.substring(0, res.length() - 1);
			return rStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
