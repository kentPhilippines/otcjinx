package test.number;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	public static void main(String[] args) {
	/*	for (int a = 0; a <= 5; a++) {
			long l = System.currentTimeMillis();
			String post = HttpUtil.post("http://api.tailipower.com/app/api/rechargeOrder/pay07Notify", "");
			System.out.println("请求返回：" + post);
			long l2 = System.currentTimeMillis();
			long oo = l2 - l;
			System.out.println("请求时间" + oo);
		}
*/
    }

	private static final String UTF_8 = "utf-8";
	private static final String ENCODE_TYPE = "md5";
    /**
     * md5加密
     * @param str
     * @return
     */
    public static String md5(String a) {
		String c = "";
		MessageDigest md5;
		String result = "";
		try {
			md5 = MessageDigest.getInstance(ENCODE_TYPE);
			md5.update(a.getBytes(UTF_8));
			byte[] temp;
			temp = md5.digest(c.getBytes(UTF_8));
			for (int i = 0; i < temp.length; i++)
				result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
		}
		return result;
    }
}
