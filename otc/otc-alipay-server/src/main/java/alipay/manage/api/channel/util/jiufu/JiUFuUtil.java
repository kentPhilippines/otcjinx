package alipay.manage.api.channel.util.jiufu;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

import cn.hutool.core.util.ObjectUtil;

public class JiUFuUtil {
	public static final String APPID = "1006346282";
	public static final String KEY = "yhdbzeqlusdgchemxs";
	public static final String URL = "https://newapi.9pay.vip/unionOrder";
	public static final String SERVER_BANKTOBANK = "10101";
	public static final String SERVER_ALIAPY_BANK_SCAN = "10108";
	public static final String SERVER_ALIAPY_BANK_H5 = "10109";
	public static final String SERVER_YUNSAHNFUTOBANK = "10103";
	public static final String SERVER_WECHAR_BANK = "10114";
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
