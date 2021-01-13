package alipay.manage.api.channel.deal.jiabao;

import cn.hutool.core.util.ObjectUtil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * 获取RSA公私钥 地址：http://web.chacuo.net/netrsakeypair
 * Java 版本 填写 （ 生成密钥位数：1024  密钥格式： PKCS#8   证书密码：不用填 ）
 * 非Java 版本 填写 （ 生成密钥位数：1024  密钥格式： PKCS#1   证书密码：不用填 ）
 */
public class RSAUtil {
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

    public static String createParam(HashMap<String, Object> decodeParamMap) {
        try {
            if (decodeParamMap == null || decodeParamMap.isEmpty()) {
                return null;
            }
            Object[] key = decodeParamMap.keySet().toArray();
            Arrays.sort(key);
            StringBuffer res = new StringBuffer(128);
            for (int i = 0; i < key.length; i++) {
                if (ObjectUtil.isNotNull(decodeParamMap.get(key[i]))) {
                    res.append(decodeParamMap.get(key[i]));
                }
            }
            String rStr = res.toString();
            return rStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}