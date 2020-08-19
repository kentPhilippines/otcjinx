package test.number;

import otc.util.encode.XRsa;

import java.util.Map;

public class RsaTet {

	public static void main(String[] args) {
		test1();
		// 	test();
	}

	private static void test1() {
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBgveeFzs3TKSdfMH9Z5uZ5aAThUaSaCLjabXIyAzpbmz1SzbCc6YNXAEYwDvXkztTzbks6jQbt61ib1Uuy4z123wEYk3p4IyFMKfEquPAauj7yTybME0J23rmpXDgXLsX5vO2LB1P9pcv1HiJG/403wYiebnLOfB1w/20qtRnyQIDAQAB";
		String privatekey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIGC954XOzdMpJ18wf1nm5nloBOFRpJoIuNptcjIDOlubPVLNsJzpg1cARjAO9eTO1PNuSzqNBu3rWJvVS7LjPXbfARiTengjIUwp8Sq48Bq6PvJPJswTQnbeualcOBcuxfm87YsHU/2ly/UeIkb/jTfBiJ5ucs58HXD/bSq1GfJAgMBAAECgYAL8KEXiBjDfmNmyYuw6w5jX9IkOpNJCCS/Ro2l1xupoa6V5rtDrhnO/X50Y7SgqUg876h0xZrMO2DWxGDcEZQLLKaeNNLB1XehgF+J+Tffwbo1eoMpH4DnTp8fVU5gWSei55GMN/xzp1qph+06b74wQ2hJq6ig49xVU6VYxErDAQJBANJPQ6dfbzbF/D9WmXiFRKf87gXDnVTyQ/tmVTCns5VV0M+E6rDs/E/9pmt7emXKvDbzv+3V4hQ1EJEg5da1RbkCQQCdpfrNBm7KQPGDBqreOoMmk/UoimD5hVznAsZQ3ko0Hi2eUehQBxLVMYXpKw5U5JP1wVPjf7mAsOwFacxNPDqRAkADGMGxRDl5//5P3HGUEbpKEvJaSWAWsR6JJB+bAM0nJMVXWOivxD2O2/hIWuAZgZu1327zDJQwoftld6uKts6ZAkAtEvHcgQRYS61B2zwrgetRsmgcCUSk0x625jIxmPz6Xc6JP73+c6dM0XYKLsdQOnKbh4UmvLQbOXqiKZfCVYAhAkBNsuCHrdgjN6mQNI6Xeo6+v8mjeQJ0XnPpMpbbq5E6bTpQGeNlirs6bonn09gWdv3ItYlreaLs08CeBSXby/6U";


		String password = "RA4iBtDbYSgZ0hD5mjA2HlU4076IEnz0NPXeFt312LqrupTkpdSTKjrdmnxfyzq08/9Q4J8CwM9xartq6T8iLjSZiWT osQ0Att3 kwQTj/lzaf PYatxuTuYmcWfQI/v1WYCwo8HMn6TUFZe3YnhY82Xxk LIezfbSAKo1gw4gyYr5CTbg3VGnzhhFZYzrw9YfnNmwdkXakGtC4iUQN/x61 eosSIGGs3j6lbDoMtaebLSIExrD/wwaJWzHCoyH8CnMeBZ3EWd6K5DfdfInUlgfk4anr6NmoqoPSAifJ1hFdH6Q1j0eQB nRMnCqU5AhPcq9yW0qXaD/8lHplgSqTDp7FJiRT6PaKgVgcmxPXbcg6Cm0/Sm5T7agjkw54o 7jXING1weHOZTmlWXq3Vncx eiM4YNMAIM8QiXBnR7cFAvaXoewXechk6VytlX1YsMov/zKw5PW5n39YEyPfHEfhcv0Mb7mVgGyTpbQIMhuL1S RrNG3rTA42zGvxWqh";
		String password1 = "RA4iBtDbYSgZ0hD5mjA2HlU4076IEnz0NPXeFt312LqrupTkpdSTKjrdmnxfyzq08/9Q4J8CwM9xartq6T8iLjSZiWT+osQ0Att3+kwQTj/lzaf+PYatxuTuYmcWfQI/v1WYCwo8HMn6TUFZe3YnhY82Xxk+LIezfbSAKo1gw4gyYr5CTbg3VGnzhhFZYzrw9YfnNmwdkXakGtC4iUQN/x61+eosSIGGs3j6lbDoMtaebLSIExrD/wwaJWzHCoyH8CnMeBZ3EWd6K5DfdfInUlgfk4anr6NmoqoPSAifJ1hFdH6Q1j0eQB+nRMnCqU5AhPcq9yW0qXaD/8lHplgSqTDp7FJiRT6PaKgVgcmxPXbcg6Cm0/Sm5T7agjkw54o+7jXING1weHOZTmlWXq3Vncx+eiM4YNMAIM8QiXBnR7cFAvaXoewXechk6VytlX1YsMov/zKw5PW5n39YEyPfHEfhcv0Mb7mVgGyTpbQIMhuL1S+RrNG3rTA42zGvxWqh";


		XRsa rsa = new XRsa(publicKey, privatekey);

		String asas = rsa.privateDecrypt(password);
		System.out.println("解密结果：" + asas);
		System.out.println("明文长度：" + asas.length());
		String publicEncrypt = rsa.publicEncrypt(asas);
		System.out.println("公钥加密后的内容：" + publicEncrypt);
		System.out.println("密文长度：" + publicEncrypt.length());
		String privateEncrypt = rsa.privateEncrypt(asas);
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
