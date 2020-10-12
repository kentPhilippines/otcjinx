package alipay.manage.api.channel.deal.haiwang;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
    public static final String PID = "65";
    public static final String KEY = "WjekVDjOYAaqBZAPqdbuFDjDxRznOCaE";
    public static final String URL = "http://api.lalarfb.cn/pay";
    public static final String TYPE = "card1";
    public static final String NOTIFY = "/haiwang-notify";

    public static String md5(String a) {
        String c = "";
        MessageDigest md5;
        String result = "";
        try {
            md5 = MessageDigest.getInstance("md5");
            md5.update(a.getBytes("utf-8"));
            byte[] temp;
            temp = md5.digest(c.getBytes("utf-8"));
            for (int i = 0; i < temp.length; i++)
                result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
        return result;
    }
}
