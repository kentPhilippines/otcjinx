package test.number;

import otc.util.encode.XRsa;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class RsaTet {

    private static final String UTF_8 = "utf-8";
    private static final String ENCODE_TYPE = "md5";

    public static void main(String[] args) {

        XRsa rsa = new XRsa(" MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5mV07mTwh3PViM+UxBInwaORI\n" +
                "IHlufTmaipgxJoQyQksHKpi9yABbBO2hLqjfdNLM/jNG0iRMYnkM699iRrCph3qG\n" +
                "ut5xut1hVaBl1ohQPZBHYaKFX+YCYCgcUxK3mAOv5kIM2THHR10MH2nH52DV2GPW\n" +
                "NFg2bqmbJdU7XqIoFQIDAQAB\n","MIICdAIBADALBgkqhkiG9w0BAQEEggJgMIICXAIBAAKBgQC5mV07mTwh3PViM+Ux\n" +
                "BInwaORIIHlufTmaipgxJoQyQksHKpi9yABbBO2hLqjfdNLM/jNG0iRMYnkM699i\n" +
                "RrCph3qGut5xut1hVaBl1ohQPZBHYaKFX+YCYCgcUxK3mAOv5kIM2THHR10MH2nH\n" +
                "52DV2GPWNFg2bqmbJdU7XqIoFQIDAQABAoGALAxsBjMOBHNugYoTgRi0rCwY0c1z\n" +
                "RHfP7g+qoy+i/FKjf+XqLlCDVRbMVw694ZrWVPSXst045DASzrBEagyg7JOcDR11\n" +
                "0Jqf0YhO4tl0/rDLkIC3wVYRGmnGj2N1nYqwwf8+PYIr3G6LzlaRmALP17GmOSu9\n" +
                "63b51N1pntF/x4ECQQDMGZV3RcBZsmD7Ch1fwLtTa+S5hne3+eMBx+uJvPAUxjqp\n" +
                "kxurMD375mQ6Bahj7pmmLCXvNkGaRVfVdjrJhrpxAkEA6MtrO1GfbqhADBrKSQbY\n" +
                "y0tONdDFG8j1as11MFm+8vUYA7L6hYLkeEKqGGoaxENrA2j8T2r7CU1BlO4wn6nx\n" +
                "5QJAY4qavggTzutTpzwApYGfGwmFDQLLArxp/aVed9fhE5C2bZvko7IsmN19xlqa\n" +
                "DNsCruvbOHayuiXVt7Mo6r690QJAUUeE3yc2hUpVYmSQMEMluoVGf1+vEm5t0aZ+\n" +
                "viz3kJimk9QPyEHVR2/vN6sEQXZQjNZBEfGGSM8ikL9BVt3dSQJBAMfsJQzJXDfo\n" +
                "XKLQ/cAnkXTWyCsk1clZ6x096hjgumBV/HoGr1fdQZMBVT1Avw36jPzhyMK1PfLi\n" +
                "cD75Ri+n9Nk=\n" );


        String publicEncrypt = rsa.publicEncrypt("111111112211111111222222211111111222222211111111222222211111111222222211111111222222211111111222222211111111222222211111111222222211111111222222211111111222222211111111222222211111111222222211111111222222211111111222222211111111222222211111111222222211111111222222222222");
        System.out.println(publicEncrypt);

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

    private static void test1() {
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCvRvYBBFtPRCxDb+OedlBzzJSYaPnROvE5Xq0H3IuyxoweFoOrMGmlY79LWSXzTV20x5XRgREunFT5DVXZd96h9xpWQ6As/JkG1H8RsmzrWxhZilub3Nh39zAMoX9PoZ8X7+HKVOsUnvPSvw6JIO8duZKMh8wwIDgryPjHnYx3vQIDAQAB";


        String privatekey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAK9G9gEEW09ELENv4552UHPMlJho+dE68TlerQfci7LGjB4Wg6swaaVjv0tZJfNNXbTHldGBES6cVPkNVdl33qH3GlZDoCz8mQbUfxGybOtbGFmKW5vc2Hf3MAyhf0+hnxfv4cpU6xSe89K/Dokg7x25koyHzDAgOCvI+MedjHe9AgMBAAECgYBPuTYR1lp13mtHrS/aFrSXRzlbbXwbl/S6hH6tcbo08awvz0vKZLXY3VrYOc9SMCFsJc/WQB+BlOftdAxVYvlm6/8d/+l9O08Udni+Axs/DXjkWwgnc0Z9CMTy29UEe3+AfJy+casW13C/gSiHJycVqciVHboz1ZgF4w8GSZNw4QJBAP9Fsocuo9FmUZr06gz+xqwJyjZallAyzUYYzyIjRDnMoLIjVewMWLTej4NzLETawWNsdP6jm5BhOgfTyTZ6KGUCQQCvxuGyxx016coAxWqkgCJBfdPepPrWIqDUnGaIa8Q5REQBDyFIYrgCySOUB1zlk2nNvBdK6yDXFkSplGuTq+B5AkEAxU46yk/bYPvD8ZCB4199cfXzLQXhjsVlclu0pf86Oa6XOnkjbVqxqcMojAKWYfXvagineTY+BF+R+gDRUmEjHQJBAJL/eIpA+A25OSSR9FAjlAI/DysXv2nKxcNoFFsr/IqDq3dTRts5K3ew7B8EVUzsjcPu82/E2M7nIX7Lz3dMeskCQHERgD5Pu/EhexxaSg4TnkTS+CL4PGDrviFDVYD7dsInCPKJzeArdbZvdo3c88KvPo8uI3wrHMo7AAY++XmBAHc=";
        String password = "345678";

        XRsa rsa = new XRsa(publicKey, privatekey);
/*
        String asas = rsa.privateDecrypt(password);
        System.out.println("解密结果：" + asas);
        System.out.println("明文长度：" + asas.length());*/
        String publicEncrypt = rsa.publicEncrypt(password);
        System.out.println("公钥加密后的内容：" + publicEncrypt);
        System.out.println("密文长度：" + publicEncrypt.length());
        String privateEncrypt = rsa.privateEncrypt(password);
		System.out.println("私钥加密后的内容：" + privateEncrypt);
		String privateDecrypt2 = rsa.privateDecrypt(publicEncrypt);
		System.out.println("私钥解密公钥内容：" + privateDecrypt2);
		String publicDecrypt = rsa.publicDecrypt(privateEncrypt);
		System.out.println("公钥解密私钥加密后内容：" + publicDecrypt);

	}

	static void test() {
		Map<String, String> createKeys = XRsa.createKeys(1024);
		System.out.println("公钥：" + createKeys.get("publicKey"));
		System.out.println("私钥：" + createKeys.get("privateKey"));
		System.out.println("公钥加密");
		String asas = "accountId=AC1000&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&backCard=65465443213213&userName=%E7%9C%9F%E5%AE%9E%E6%95%B0%E6%8D%AE%E6%B5%8B%E8%AF%95&userId=test&ipAddr=127.0.0.1";
		XRsa aa = new XRsa(createKeys.get("publicKey"), createKeys.get("privateKey"));
		System.out.println("明文长度：" + asas.length());
		String publicEncrypt = aa.publicEncrypt(asas);
		System.out.println("公钥加密后的内容：" + publicEncrypt);
		System.out.println("密文长度：" + publicEncrypt.length());
		String privateEncrypt = aa.privateEncrypt("accountId=AC1000&amount=200&backCard=65465443213213&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&userName=%E7%9C%9F%E5%AE%9E%E6%95%B0%E6%8D%AE%E6%B5%8B%E8%AF%95&userId=test&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&ipAddr=127.0.0.1");
		System.out.println("私钥加密后的内容：" + privateEncrypt);
		//String privateDecrypt = aa.privateDecrypt(privateEncrypt);
		//	System.out.println("私钥解密私钥的加密内容："+privateDecrypt);
		String privateDecrypt2 = aa.privateDecrypt(publicEncrypt);
		System.out.println("私钥解密公钥内容：" + privateDecrypt2);
		String publicDecrypt = aa.publicDecrypt(privateEncrypt);
		System.out.println("公钥解密私钥加密后内容：" + publicDecrypt);
	}
}
