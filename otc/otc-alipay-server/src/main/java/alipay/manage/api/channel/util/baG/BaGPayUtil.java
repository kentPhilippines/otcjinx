package alipay.manage.api.channel.util.baG;

import org.apache.commons.codec.binary.Base64;
import otc.util.StringUtils;

import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import cn.hutool.http.HttpUtil;
public class BaGPayUtil {
    public static final String PRIVATE_KEY="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMDfWK+cCRqM9Qam ps49XpZ6+/MfvuRw8z/Hx+CQwkZ7R9hrDEwrtMoUVaUPEMcvtC5I6XuTR+X07hw8 dmRXPUXQHmpe8U5tkoaEl6XoBWt/w9KZXcDknOCfMjJYbIiKEsPRIu925pZUvGH3 JuMtPEH43C2YaHZS1vXqXeUc7t5RAgMBAAECgYEArEcT5ZBfxVqBBxbWykOg+JMT 97+0eEK31JGz5NAI6IH308USr/seOp7dPVluqCzhKbKw81PEFhCom4oaSlhufh8s Bfwor2dMxkEsa1sySi0szYIN5ZhOkwgmS9hQfPfQxZd/TLcqxBdh3BJvU5UqqRM9 nyK1EJo/uvMxYEhqD40CQQDn4dDG8r0Tm2496A0Ooyk85wjrxH3zA6gUxHQ2YZ5Z Smq5OSYi80N3GZMMJfNOINZL1G0g42NR1KVOfMvKbbfPAkEA1O7YOah/TtAZ84Xq CvR+1OmozSdQ79t1o6bO2U+vjvEtqZZ6GPRdBiGvQC2FbmI3biZpDOxgIKN7cJ1A cgdv3wJBANQpBbfyAtN8xBo6RjAdUy7ZCI2HY+HEd7ZApT/Yg2SZNRqx0lXqE9FW Aff8hSf33XrWKt8LjiUiFfnBL0jQqHsCQCHXlCYV0aYFDRrXPctf8IiGWn3Asext RNUtvdJsB8sAKfG6KM2uiNpgoCnjEkHo+kZXdHrJVr3ZPdU4KPX2mKECQHYhSozr ZOcBu0MQBgn1lQcxDAenAO2DkzIvpdMXzjcSZ6SmTxb+/lPxUnLRtHfQtCEs1lWQ ROm9gf0bMK7zfXA=";
    public static final String APPID = "114210057";
    public static final String PAY_TYPE = "copybank";
    private static final String SIGN_TYPE_RSA = "RSA";
    private static final String SIGN_TYPE_RSA2 = "RSA2";
    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    private static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = decryptBASE64(publicKey);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec);
        Signature signature = Signature.getInstance("SHA256WithRSA");
        signature.initVerify(publicKey2);
        signature.update(data);
        return signature.verify(decryptBASE64(sign));
    }
    public static String getURL(Map<String, String> paramsMap) {
        String url = null;
        if ((paramsMap != null) && (paramsMap.size() > 0)) {
            Iterator it = paramsMap.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                String value = paramsMap.get(key);
                if (url == null) {
                    url = key + "=" + value;
                } else {
                    url = url + "&" + key + "=" + value;
                }
            }
        }
        return url;
    }
    public static byte[] decryptBASE64(String key) {
        byte[] b = key.getBytes();
        Base64 base64 = new Base64();
        return base64.decode(b);
    }
    /**
     * 加密
     * @param pwd
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        Base64 base64 = new Base64();
        byte[] b = base64.encode(key);
        String s = new String(b);
        return s;
    }
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = decryptBASE64(privateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey2 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Signature signature = Signature.getInstance("SHA256WithRSA");
        signature.initSign(privateKey2);
        signature.update(data);
        return encryptBASE64(signature.sign());
    }
    public static PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws Exception {
        if (ins == null || StringUtils.isEmpty(algorithm)) {
            return null;
        }
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        byte[] encodedKey = readText(ins).getBytes();
        encodedKey = Base64.decodeBase64(encodedKey);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }
    public static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        StringWriter writer = new StringWriter();
        io(new InputStreamReader(ins), writer, -1);
        byte[] encodedKey = writer.toString().getBytes();
        encodedKey = Base64.decodeBase64(encodedKey);
        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }
    public static String getSignContent(Map<String, String> sortedParams) {
        StringBuffer content = new StringBuffer();
        // app_id,method,charset,sign_type,version,bill_type,timestamp,bill_date
        String[] sign_param = sortedParams.get("sign_param").split(",");// 生成签名所需的参数
        List<String> keys = new ArrayList<String>();
        for (int i = 0; i < sign_param.length; i++) {
            keys.add(sign_param[i]);
        }
        Collections.sort(keys);
        int index = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = sortedParams.get(key);
            if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                index++;
            }
        }
        return content.toString();
    }
    private static String readText(InputStream ins) throws IOException {
        Reader reader = new InputStreamReader(ins);
        StringWriter writer = new StringWriter();
        io(reader, writer, -1);
        return writer.toString();
    }
    private static void io(Reader in, Writer out, int bufferSize) throws IOException {
        if (bufferSize == -1) {
            bufferSize = DEFAULT_BUFFER_SIZE >> 1;
        }
        char[] buffer = new char[bufferSize];
        int amount;
        while ((amount = in.read(buffer)) >= 0) {
            out.write(buffer, 0, amount);
        }
    }
}
