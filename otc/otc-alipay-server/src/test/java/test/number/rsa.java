package test.number;


import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import otc.util.RSAUtils;

import java.security.PrivateKey;
import java.util.Map;

public class rsa {


    public static void main(String[] args) {
        String pri = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIqrMd7wkHewsXSNxqpkm0BQj9ioG72p8oxQDch79E0bNoHA/XbtSCURedtVr2LeQhgYETKTOeRaQMn3CPv8aerScZhPaPn/sgglIwKjASFei9XTyG9qrA9B6fGB8afmslfcguci/derdgOrIAdEY5yaZc0qmijgMyBWMSBJQgVXAgMBAAECgYAjY18dyvzDXtdarFfX4kbwmd6r5vL1ayqX/lIWKPQGvXwijvLgZ9uyggw8Gljg/WGWroWtB3+NtC62nBGx5Q/OWlVzjd396ARF0meFyVI4p6gZJH9q9/LG7uONrW3iF5cjlO7on+tO55jKbHdi3ShQRQCFxq4RyYZEwVWNeQgZAQJBAMTz7iIuw0BhUx7hz5Kb9orTQ88aqLvDq39ryr8adH01VsLzpt5s2nhZhlPiaRZ/6dHMQS8NWLtehjNU9993oNcCQQC0Pfn21dZ4QpTuii122zX409l5kL97zbJ0h5okJnrqnBq01fVMsfJNjNamXM6tUA+0PtQ50ad7HKkU0by1Ua+BAkEAnd0veLKmygm4iD5TFJ5yROAFsBEcQjQaWG3b+sWHrXx9aJHHpOz3SBPp+X624NPyZLklXkOCZdzkEG0AqVvHzQJBAKZ/yfTWvHQDLYjdY/ozrbvaFz9RE1HORleXHDDgyxMGeTUroJnkl6xDudWcTd+T4a3jjDCNF2WGqDpvNR9aAgECQFBeNQ2MfkHsNtHLnXomzslmyVpknVZ//Hx0dqmWBWOrMM6+GeYRxyakBWxahxQMfco7R0Vmkf74X+0WwwTkSMY=";
        String pul = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKqzHe8JB3sLF0jcaqZJtAUI/YqBu9qfKMUA3Ie/RNGzaBwP127UglEXnbVa9i3kIYGBEykznkWkDJ9wj7/Gnq0nGYT2j5/7IIJSMCowEhXovV08hvaqwPQenxgfGn5rJX3ILnIv3Xq3YDqyAHRGOcmmXNKpoo4DMgVjEgSUIFVwIDAQAB";
        RSA rsa = new RSA(pri,pul);
        System.out.println(pri);
        Map<String, Object> decodePrivateKey = RSAUtils.getDecodePrivateKey("V057Uc+HuhnisMW+8Zm454JeOKEbU9SFK+cWbR9tgxVTXUpZtMTnE+NrC31/v47Tal78gFyiuxb/rkUxl21I5VvYnvs2+7lH5O16gDx+ds/1PZYWTUWPqwNIgdF0GWSMvAPk5ioAqBxp/4BG778azde82v2yQriPRNlvp84cHNVc0ggAJqYUA0RpnN0+LbxM1Gom2mbkBb5EYRzysfXAfww9/DQlsvU8Mv93QblFqoihhiipjMMkE78es18deZqGp/ECS4ENUfEueZg5ZpChmrYtozyeDqn80+XMlA3wph1OOl5LVOf9N05Sl8FqsFqvhyzCrjqOD2fd0d4PbOYLhXaoEGJJB6HnM8/bcm301393gtetAZLSOsOPIzIpG6WqeEgHpWumBPLHkSiJ3WVKxoUHGTHYKqLbf94baLV0dSiY7mov/y09Mee5uf+Llz/HpUtPI9afhiDdonX9mI8DGTpZoZEKqGN5AP480N/rwDoB04M0f9p68pe4VOTnRSt1cSvkAIO9p9a4szluWpkwn3jMx8+BpGI3AmKuY+kK9H/phWAQbrFNWjYME6cGpK0GDMsUNtIOxrNlkpjh4KnG1g9uFWJ0IreM+Z/LKHBO6kfUdhILb/7Zy5FIIKwaKrbxPtY78GwuKavD7iybjqc7XSsS7aDz/Tj6QzpwpBisSiU=", pri);
        System.out.println(decodePrivateKey.toString());
        //获得私钥
        PrivateKey privateKey = rsa.getPrivateKey();
        String privateKeyBase64 = rsa.getPrivateKeyBase64();
        System.out.println(privateKeyBase64);
//获得公钥
        rsa.getPublicKey();
        String publicKeyBase64 = rsa.getPublicKeyBase64();
        System.out.println(publicKeyBase64);
//公钥加密，私钥解密
        byte[] encrypt = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
        byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);

//Junit单元测试
//Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));

//私钥加密，公钥解密
        byte[] encrypt2 = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
        byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);

//Junit单元测试
//Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt2, CharsetUtil.CHARSET_UTF_8));

    }
}
