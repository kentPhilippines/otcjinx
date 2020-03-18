package test.number;

import java.util.Map;

import otc.util.encode.XRsa;

public class RsaTet {
	
	public static void main(String[] args) {
		Map<String, String> createKeys = XRsa.createKeys(512);
    	System.out.println("公钥："+createKeys.get("publicKey"));
    	System.out.println("私钥："+createKeys.get("privateKey"));
    	System.out.println("公钥加密");
    	String asas = "accountId=AC1000&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&backCard=65465443213213&userName=%E7%9C%9F%E5%AE%9E%E6%95%B0%E6%8D%AE%E6%B5%8B%E8%AF%95&userId=test&ipAddr=127.0.0.1";
    	XRsa aa = new XRsa(createKeys.get("publicKey"), createKeys.get("privateKey"));
    	System.out.println("明文长度："+asas.length());
    	String publicEncrypt = aa.publicEncrypt(asas);
    	System.out.println("公钥加密后的内容："+publicEncrypt);
    	System.out.println("密文长度："+publicEncrypt.length());
    	String privateEncrypt = aa.privateEncrypt("accountId=AC1000&amount=200&backCard=65465443213213&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&userName=%E7%9C%9F%E5%AE%9E%E6%95%B0%E6%8D%AE%E6%B5%8B%E8%AF%95&userId=test&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&amount=200&ipAddr=127.0.0.1");
    	System.out.println("私钥加密后的内容："+privateEncrypt);
    	//String privateDecrypt = aa.privateDecrypt(privateEncrypt);
    //	System.out.println("私钥解密私钥的加密内容："+privateDecrypt);
    	String privateDecrypt2 = aa.privateDecrypt(publicEncrypt);
    	System.out.println("私钥解密公钥内容："+privateDecrypt2);
    	String publicDecrypt = aa.publicDecrypt(privateEncrypt);
    	System.out.println("公钥解密私钥加密后内容："+publicDecrypt);

	}

}
