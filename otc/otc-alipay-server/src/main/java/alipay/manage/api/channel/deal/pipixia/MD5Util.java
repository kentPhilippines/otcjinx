package alipay.manage.api.channel.deal.pipixia;

import java.security.MessageDigest;

/**
 * @author Justin Yi
 * @version 1.0
 * @classname MD5Util
 * @description TODO
 * @date 2022/3/8 10:27
 **/
public class MD5Util {

    /**
     * 转换成MD5
     * @param str 参数
     * @return
     */
    public static String encrypt(String str) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");

            md.update(str.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }

    }

}
