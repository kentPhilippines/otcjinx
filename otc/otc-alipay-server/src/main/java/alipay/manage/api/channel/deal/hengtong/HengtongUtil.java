package alipay.manage.api.channel.deal.hengtong;

import cn.hutool.core.util.ObjectUtil;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;

public class HengtongUtil {


    public static final String NOTIFY = "/hengtong-notify";
    public static final String D_NOTIFY = "/hengtong-d-notify";


    public static String createParam(Map<String, Object> map) {
        try {
            if (map == null || map.isEmpty())
                return null;
            Object[] key = map.keySet().toArray();
            Arrays.sort(key);
            StringBuffer res = new StringBuffer(128);
            for (int i = 0; i < key.length; i++)
                if (ObjectUtil.isNotNull(map.get(key[i])))
                    res.append(key[i] + "=" + map.get(key[i]) + "&");
            String rStr = res.substring(0, res.length() - 1);
            return rStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String md5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }


}
