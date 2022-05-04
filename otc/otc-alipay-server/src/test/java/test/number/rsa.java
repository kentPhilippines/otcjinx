package test.number;


import alipay.manage.api.channel.deal.jiabao.RSAUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import otc.util.RSAUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.List;
import java.util.Map;

public class rsa {
    private static final String UTF_8 = "utf-8";
    private static final String ENCODE_TYPE = "md5";

    public static void main(String[] args) throws IOException {
        String key  = "RDBECCGUN2OQ6NMS8WUDOZGAXME9UYPA0RJPTR89COWMISQ2X2JWHRE4YHX2FMCUGOPVF5KGOIO7ZKWXOCNDIFKIWPDK9LGI4OWJCOTDWFTWU1KVZ3KLTDONCD364FC3";

        String s = md5("agentpayOrderId=G01202202181914103240004&fee=1800&status=2&key="+key).toUpperCase();
        String s1 = md5("6214680066592096") ;
        BigDecimal divide = new BigDecimal(40000).divide(new BigDecimal(100));

        System.out.println(s);
        System.out.println(s1);
        System.out.println(divide);


    }

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
