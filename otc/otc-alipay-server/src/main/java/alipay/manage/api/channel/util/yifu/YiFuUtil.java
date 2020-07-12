package alipay.manage.api.channel.util.yifu;

import cn.hutool.core.util.ObjectUtil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

public class YiFuUtil {
    public static final String APPID="-YbBmS2aFEfgBx4mnXI";
    public static final String URL="https://ef.sanbanye.com/pay";
    public static final String KEY="kRrgxUk0QCpxz1bS5QVRJXZKEFPGnrqD";
    public static final String CHANNEL_KUAIJIE="0202";
    public static final String CHANNEL_WANGYING="0202";
    public static final String CHANNEL_BANK="6";
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
