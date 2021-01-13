package alipay.manage.api.channel.util.xianyu;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class XianYuUtil {
	public static final String KEY = "pNgvmzYZcWpDfxLlYTyUFyhPtXPXPhrn";
	public static final String APPID = "2020185";
	public static final String URL = "https://api.shoudanbao.net/Pay";
	public static final String IOSH5 = "zfbh5";
	public static final String SCANH5 = "zfbsm";
	public static final String BANK = "bank";

	public static String md5(String a) {
		String c = "";
		MessageDigest md5;
		String result = "";
		try {
            md5 = MessageDigest.getInstance("md5");
            md5.update(a.getBytes("utf-8"));
            byte[] temp;
            temp = md5.digest(c.getBytes("utf-8"));
            for (int i = 0; i < temp.length; i++) {
                result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			}
			return result;
	    }
}
