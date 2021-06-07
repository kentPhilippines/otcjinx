package test.number;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.DES;
import cn.hutool.http.HttpUtil;
import otc.util.RSAUtils;

import java.nio.charset.Charset;

public class des {
    static String desPassword = "C3AD125D8DDA4030A5A71D16";
    static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCZhADxkdiJFcFaqamlhcxE+bzekfJJFH/qm6sSXg88J+L5q1uboF7LVhtx7t0oz855PED77GsuVbwYhx42ztr4DsU4+5YJEJ/OQL0In3zOkU58mCeTGBbdeoR3DxjBBIkrmC8p6FjdeGw75Gf2YAnImYji+OOv9SX7+kWYBm/yZwIDAQAB";

    public static void main(String[] args) {
        String pass = "";
        String salt = "";
        if (desPassword.length() != 24) {
            String s = StrUtil.subWithLength(desPassword, 0, 24);
            pass = s;
        } else {
            pass = desPassword;
        }
        salt = desPassword.substring(4, 12);
        System.out.println(salt);
        DES des = new DES(Mode.CFB, Padding.PKCS5Padding, pass.getBytes(), salt.getBytes());
        String s = des.encryptHex("appId=wxc888&orderId=947ECF100003&notifyUrl=http://pay.allpayzs.com/Payment/Callback/10234/v2&pageUrl=https://www.awcwin8.com&amount=100.00&passCode=wy&bankCode=wy&subject=subject&applyDate=20210522173602&userid=219730b8c6d86e7b7ddcb3dfd526aa41&sign=6ccfe908676ab4ed2be146d328b39cf4\n");
        System.out.println("des加密密文为：");
        System.out.println(s);
        String decode = decode(s);
        System.out.println(decode);
    }

    public static String decode(String cipher) {
        /**
         * 加密mode 为  cfb  , 补码方式为 PKCS5Padding  , 偏移量为
         */
        String pass = "";
        String salt = "";
        if (desPassword.length() != 24) {
            String s = StrUtil.subWithLength(desPassword, 0, 24);
            pass = s;
        } else {
            pass = desPassword;
        }
        salt = desPassword.substring(4, 12);

        DES des = new DES(Mode.CFB, Padding.PKCS5Padding, pass.getBytes(), salt.getBytes());
        String data = "";
        String str = HttpUtil.decode(StrUtil.str(des.decrypt(cipher), "utf-8"), Charset.defaultCharset());//这里即为 用户需要加密的内容
        System.out.println("解密des密文为：");
        System.out.println(str);
        try {
            data = RSAUtils.publicEncrypt(str, publicKey);
        } catch (Exception e) {
            return "加密失败";
        }
        return data;
    }
}
