package test.number;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import otc.util.MapUtil;
import otc.util.RSAUtils;

public class MD5 {
	public static void main(String[] args) {
		String key = "amount_fee=38.000000&amount_str=2000.000000&business_type=11&create_time=2020-05-23 01:52:04&for_trade_id=&input_charset=utf-8&modified_time=2020-05-23 01:54:20&out_trade_no=5EC81143900859E058EA66F3&remark=&request_time=2020-05-23 01:54:20&status=1&trade_id=MM2020052301174890";
		String a ="n - 当前验签错误，我方验签串aaea17931649d3f71d3cb044a88e20cf，对方验签串：b134b54b7afb454d4698892970aa0f04\r\n";
		String md5 = md5(key+"&afdfasdf16541asdf51asd6f621sd");
		System.out.println(md5);
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
	   	String result=""; 
		try {
			md5 = MessageDigest.getInstance(ENCODE_TYPE);
			md5.update(a.getBytes(UTF_8));
			byte[] temp;
			temp=md5.digest(c.getBytes(UTF_8));
			for (int i=0; i<temp.length; i++)
				result+=Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
		}
		return result;
    }
}
