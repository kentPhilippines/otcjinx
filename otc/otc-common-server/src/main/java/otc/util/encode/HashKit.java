package otc.util.encode;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import otc.result.Result;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashKit {
	private static final String UTF_8 = "utf-8";
	private static final String ENCODE_TYPE = "md5";
	static Logger log = LoggerFactory.getLogger(HashKit.class);


	/**
	 * 生成随机盐
	 */
    public static String randomSalt() {
    	String randomString = RandomUtil.randomString(10);
        return randomString;
    }
	
	
	
	
	
	public static  Result encodePassword(String username ,String password , String salt ) {
		if (StrUtil.isBlank(username) || StrUtil.isBlank(password) || StrUtil.isBlank(salt)) {
			return Result.buildFailMessage("必传参数为空");
		}
		return Result.buildSuccessResult(forE(number(username), username + password, salt));
	}
	static int number(String c) {
		char[] charArray = c.toCharArray();
		char a = charArray[0];
		char d = charArray[charArray.length - 1];
		int o = a - d;
		int p = (o < 0) ? -o : o;
		boolean flag = true;
		if (p > 15) {
			while (flag) {
				p -= 2;
				if (p < 15) {
					flag = false;
				}
			}
		}
		return p;
	}
	/**
	 * <p>生成秘钥</p>
	 * @param c			加密次数
	 * @param a			加密值
	 * @param b			加密盐
	 * @return
	 */
	 static String forE(int c , String a, String b ) {
		 c--;
		 String createPassword = createPassword(a, b);
		 if (c == 0) {
			 return createPassword;
		 }
		 return forE(c, createPassword, b);
	 }
	/**
	 * <p>MD5加密</p>
	 * @param a
	 * @param c
	 * @return
	 */
	static String createPassword(String a,String c){
		   	MessageDigest md5;
		   	String result="";
			try {
				md5 = MessageDigest.getInstance(ENCODE_TYPE);
				md5.update(a.getBytes(UTF_8));
				byte[] temp;
				temp = md5.digest(c.getBytes(UTF_8));
				for (int i = 0; i < temp.length; i++) {
					result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
				}
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
				log.info("获取秘钥失败");
			}
			return result;
	}
}
