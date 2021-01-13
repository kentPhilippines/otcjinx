package alipay.manage.api.channel.util.shenlaifu;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShenlaifuUtil {
    public static final String URL = "https://www.godpay.shop/load/pay";
    public static final String KEY = "ab90c5506ec5c5aa37e1e16c42f09c1c";
    public static final String APPID = "GP233554";
    public static String md5(String a) {
        String c = "";
        MessageDigest md5;
        String result="";
        try {
            md5 = MessageDigest.getInstance("md5");
            md5.update(a.getBytes("utf-8"));
            byte[] temp;
            temp=md5.digest(c.getBytes("utf-8"));
            for (int i = 0; i < temp.length; i++) {
                result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
        return result;
    }
}
