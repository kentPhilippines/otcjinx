package test.number.channal;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class youkuaitong {

    private  final  static  String  privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDXleYMd6O20JyF4V45pNxJ4+zHb8+LKqm/ujVWor/JvFshLwpXOn6Qj2kYHa3qslMCQVySzr4DKLMM2SSxN6HnDYURvGBj2N8pYW5xNtsOzAp8mHykQV0w59lVV+a9Hv3NUnqf8vyqLMWvcaWoxp52QyK+Eh0B9KEkozwQcaI4m7ceCu6rJTNJ21tZK3/S3KA13mT0wJ87So2MK3itY213mm4mn/1n5AA8OzEoyo+kG84K8hX3RDLeIJI4g5cIarUlh/qcBxH3Tqx/wn4iLyhKabUdDJHxUobUsLgrD97yVj8fD/cL8Kiuf2bhPnqWh1gQbd32ZBrBaYwHNSleg3apAgMBAAECggEANOsnsBHCDwMAGODsNywfosee/ImWoWuUhWqY4y5J86QXnnqkiCrknGmHe5L1ePHS7G/M4IB9JdtcsB+xIQst48Bu1J6v7nJyvP6clI/Jj8VIIbNwjQU6ceHHeGp+ShgMZtUDIrYLjV42HE3CQz6V8SOjqJJbqno4//xNpx+tmUU0hX5I5nL+x3yFS/QyiOyhG4wyg7aCeYbaP1OPmsF5suLNd1QPOAMihP+oKXPdD079VJtw/Xtgfm72d6eggWfQygQgZEAGfuVEW2IKfyGyBRyy0nPd4h9nb1x0AQEex7Gpz7novMV+uLHRKX64IDEIlBpMP2QjRf7OyskUYHd3gQKBgQDrQLGujDJDO+WfNuOK+0/yKztC185vuyugy8muP0VcznNW4L53orIWws4Ae+UwY8VbFcDbAuMBbuSmAzDFIWt1l4gZzayqfi+8t9L5XuzbQWsmPhkPqyFK5NK52jOUFtSuU27D3KqaNce65YrWoKlmUOxkEk/6xJ2SwWPw3Dem8QKBgQDqmS2sUiDcQCFbb6T+pItTomIcHXiGKiapj91oXcXlsEo00nvoBNfdeTNwBnDQTEGPdxj8DMToRHOOyVE53VEKV7xmBxP5p9CTH1eMACgjAr6uths1KF2r7H7LETRkj4c0890G91mbOSJ0FxMkq4RGjH04vwKP00LmAgs5BoH7OQKBgFug6RPpOzKAw+ENifb6B5t1RzGzyI4wQr/wX+9kaWnKZ5YxFz6VufOvkHkTyKLeWWIuyN0E9NH8FUar+3TsnWRBzxrtxxDo8UL5/kxusqZ7hnZwvWYi62a2VXVaDbjiY5g6muviqPhDdjXPbhJGpXRalJgtocU0i4M7m+eWfn6hAoGBANTzyxPbUVzdEeqQQ7Oq5ZY7ltdyoQ8YgBP3NcIhLRy8k/+y+Sq6CsFN3bVZA1rxfamfMJzLcopsaIE6mXLvRTsgPTJYRnefL6P9FVlOYyC0wyaQw83TIISJnubybR7DcrZMj1xdd4eBq5a5w3TMBLyNlrXGHas02Es1m75d++txAoGANcMTEFaZpW/qnKaNCltimrkev2tDVkJO1dLo+/jcqRnDu5iBm/i+xoOUYssFSjo9R+WpcaoEQCcD0dRSM7o5zqP+ykkgYlCp1VxMUi71tsV+cfYgSFA2jzO47Zb2QjdOPgqe/y4XxwCXfp1PLJD0n+SyrwJgZWADbrW97QkptJk=";
    private  final  static  String  publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAriqb6/wy43hyWljbW5Bs0ZyrLHfmSijMNz0hX+rdsHHG59izADHn0wphFvFVWvcU2sEHxUaEcpjIaFqL06/imdSZFWAWu677je45rYvE+0mu6YkyDjdkvmYLfKyAfYbcWjG6RRIH2dU6ULZd9tYWM29qAQnsW9y5BSNNQlsQm8e0Q6zPT7LL091f1HnJmFCVvC0pkK/a2Tz9tldjmOVQSM5+auz/lgVFh5Xb8fOnK+ofumPpTfA3Ej9xGZJVAD522VTg9jgycDm5Ta10T0WFtme275cLdj7R5/DsRBYj4c7478nDXfqvjQVVhs5YQ0vHArZ7w3nb/OK2cfJg+FOvqwIDAQAB";

    public static void main(String[] args) {

        String payurl = "http://api.baotongpay.com/api/agentPay/draw";
        String tkmoney = 1000+""; // 金额
        String mchId = "90000287";// 商户id
        String orderid ="2121312321312223123";// 20位订单号 时间戳+6位随机字符串组成
        String notifyurl = "www.jinxing.com";// 通知地址
        String bankfullname = "日本京都银行社";
        String banknumber ="2316281218731827";
        String type = "1";
        String mail = "jinxin.server@gmail.com";
        String ifsc = "1";
        String branchName = "山西省临汾市";
        String mobile = "13542536722";
        StringBuffer s = new StringBuffer();
        s.append("mchId=" + mchId + "&");
        s.append("orderid=" + orderid + "&");
        s.append("tkmoney=" + tkmoney+ "&");
        s.append("banknumber=" + banknumber);
        String sign = RSASignature.sign(s.toString(),privateKey);
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("sign", sign);
        param.add("tkmoney", tkmoney);
        param.add("mchId", mchId);
        param.add("orderid", orderid);
        param.add("notifyurl", notifyurl);
        param.add("bankfullname", bankfullname);
        param.add("banknumber", banknumber);
        param.add("mail", mail);
        param.add("type", type);
        param.add("ifsc", ifsc);
        param.add("branchName", branchName);
        param.add("mobile", mobile);
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("请求前参数："+param);
        System.out.println("payurl："+payurl);

        String rString = restTemplate.postForObject(payurl, param, String.class);
        //{msg=提交成功。 code=0}
        //{msg=请联系客服添加ip白名单。 code=500}
        System.out.println(rString);
        JSONObject parseObj = JSONUtil.parseObj(rString);
        String object = parseObj.getStr("code");
    }
}
class RSASignature {

    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    /**
     * RSA签名
     *
     * @param content    待签名数据
     * @param privateKey 商户私钥
     * @param encode     字符集编码
     * @return 签名值
     */
    public static String sign(String content, String privateKey, String encode) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));

            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes(encode));

            byte[] signed = signature.sign();

            return Base64.encodeBase64String(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes());
            byte[] signed = signature.sign();
            return Base64.encodeBase64String(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * RSA验签名检查
     *
     * @param content   待签名数据
     * @param sign      签名值
     * @param publicKey 分配给开发商公钥
     * @param encode    字符集编码
     * @return 布尔值
     */
    public static boolean doCheck(String content, String sign, String publicKey, String encode) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decodeBase64(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update(content.getBytes(encode));

            boolean bverify = signature.verify(Base64.decodeBase64(sign));
            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean doCheck(String content, String sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decodeBase64(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update(content.getBytes());

            boolean bverify = signature.verify(Base64.decodeBase64(sign));
            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}