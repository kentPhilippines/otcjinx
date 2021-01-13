package otc.util;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import otc.exception.BusinessException;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;


public class RSAUtils {
    public static final String CHARSET = "UTF-8";   //设置以UTF-8编码
    public static final String RSA_ALGORITHM = "RSA"; //采用RSA加解密算法
    public static final Integer KEY_SIZE = 1024;
    public static final String RSA_ALGORITHM_SIGN = "SHA256WithRSA";
    private static final Log log = LogFactory.get();
    /**
     * 随机生成密钥对
     */

    /**
     * 公钥对象
     *
     * @param publicKey
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过X509编码的Key指令获得公钥对象
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            X509EncodedKeySpec x509Key = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
            RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509Key);
            return key;
        } catch (Exception e) {
            throw new RuntimeException("得到公钥时异常", e);
        }
    }

    /**
     * 私钥对象
     *
     * @param privateKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过PKCS8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PKCS8EncodedKeySpec pkcs8Key = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8Key);
        return key;

    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicEncrypt(String data, String publicKey) {
        try {
            RSAPublicKey rsaPublicKey = getPublicKey(publicKey);
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), rsaPublicKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串" + data + "时异常", e);
        }
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     */
    public static String privateDecrypt(String data, String privateKey) {
        try {
            RSAPrivateKey rsaPrivateKey = getPrivateKey(privateKey);
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), rsaPrivateKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 私钥加密
     *
     * @param data       加密字符串
     * @param privateKey 私钥字符串
     * @return 密文
     */
    public static String privateEncrypt(String data, String privateKey) {
        try {
            RSAPrivateKey rsaPrivateKey = getPrivateKey(privateKey);
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, rsaPrivateKey);
          
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), rsaPrivateKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("解密字符串" + data + "时异常", e);
        }
    }

    /**
     * 公钥解密
     *
     * @param data      加密字符串
     * @param publicKey 公钥字符串
     * @return 解密字符串
     */
    public static String publicDecrypt(String data, String publicKey) {
        try {
            RSAPublicKey rsaPublicKey = getPublicKey(publicKey);
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, rsaPublicKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), rsaPublicKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串" + data + "时异常", e);
        }
    }

    /**
     * RSA加密算法对于加密的长度是有要求的。一般来说，加密时，明文长度大于加密钥长度-11时，明文就要进行分段；解密时，密文大于解密钥长度时，密文就要进行分段（以字节为单位）
     *
     * @param cipher
     * @param opmode
     * @param datas
     * @param keySize
     * @return
     */
    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock = 0;
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        }
        byte[] resultDatas = out.toByteArray();
        IOUtils.closeQuietly(out);
        return resultDatas;
    }


    /**
     * 签名
     *
     * @param content
     * @return
     */
    public String sign(String content, String privateKey) {
        try {
            //sign
            Signature signature = Signature.getInstance(RSA_ALGORITHM_SIGN);
            signature.initSign(getPrivateKey(privateKey));
            signature.update(content.getBytes(CHARSET));
            return Base64.encodeBase64URLSafeString(signature.sign());
        } catch (Exception e) {
            throw new RuntimeException("签名字符串[" + content + "]时遇到异常", e);
        }
    }

    /**
     * 验签的方法
     *
     * @param content
     * @param sign
     * @return
     */
    public boolean verify(String content, String sign, String publicKey) {
        try {
            Signature signature = Signature.getInstance(RSA_ALGORITHM_SIGN);
            signature.initVerify(getPublicKey(publicKey));
            signature.update(content.getBytes(CHARSET));
            return signature.verify(Base64.decodeBase64(sign));
        } catch (Exception e) {
            throw new RuntimeException("验签字符串[" + content + "]时遇到异常", e);
        }
    }


	private static final String UTF_8 = "utf-8";
	private static final String ENCODE_TYPE = "md5";
    /**
     * md5加密
     * @return
     */
    public static String md5(String a) {
    	String c = "";
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

    /**
     * 将参数用公钥进行加密，平台内部调用
     *
     * @param map        map参数
     * @param privateKey 私钥
     * @return 返回map
     */
    public static String getEncryptPublicKey(Map<String, Object> map, String privateKey) {
        //拼接参数
        String urlParam = MapUtil.createParam(map);
        //私钥解密密文得到字符串参数
        String cipherText = publicEncrypt(urlParam, privateKey);
        //调用方法转成map
        if (StringUtils.isEmpty(cipherText)) {
            throw new BusinessException("加密字符串为空");
        }
        return cipherText;
    }

    /**
     * 将解密的密文转成map返回 所有的验证都在调用前完成
     *
     * @param cipherText 密文
     * @param privateKey 私钥
     * @return 返回map
     */
    public static Map<String, Object> retMapDecode(String cipherText, String privateKey) {
        //私钥解密密文得到字符串参数
        String urlParam = privateDecrypt(cipherText, privateKey);
        //调用方法转成map
        if (StringUtils.isEmpty(urlParam)) {
            throw new BusinessException("解密字符串为空");
        }
        return MapUtil.paramToMap(urlParam);
    }

    /**
     * 商户参数私钥解密方法，所有验证在调用前完成
     *
     * @param cipherText 商户传过来的密文
     * @param privateKey 解密私钥
     * @return 返回map
     */
    public static Map<String, Object> getDecodePrivateKey(String cipherText, String privateKey) {
        String urlParam = privateDecrypt(cipherText, privateKey);
        log.info("【解密数据：" + urlParam + "】");
        if (StringUtils.isEmpty(urlParam)) {
            throw new BusinessException("解密字符串为空,请核对密钥正确醒和确认好当前加密方法是否有误");
        }
        return MapUtil.paramToMap(urlParam);
    }

 /*   //调用demo
    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        JSON json = new JSONObject();
        //获取密钥
        List<String> list = genKeyPair();
        String publicKey = list.get(0);
        String privateKey = list.get(1);
        System.out.println("生成的公钥是=" + publicKey);
        System.out.println("生成的私钥是=" + privateKey);
        Map<String, Object> param = Maps.newHashMap();
        param.put("userId", "abcdedfdfd001");
        param.put("orderId", "898989888889");
        param.put("userName", "密电码009");
        param.put("sign", "dfsafdafdsafdsafdsa");
        param.put("createTime", "2020-03-04 10:03:03");
        param.put("remark", "这是我的测试数据");
        //平台自身加密后传后台数据
        String encrypt = getEncryptPublicKey(param, SystemConstants.INNER_PLATFORM_PUBLIC_KEY);
        System.out.println("alipay生成密文传给admin的值=" + encrypt);
        Map<String, Object> stringObjectMap = retMapDecode(encrypt, SystemConstants.INNER_PLATFORM_PRIVATE_KEY);
        System.out.println("admin传过来的密文，解密后的值=" + stringObjectMap.toString());
    }
    */

}
