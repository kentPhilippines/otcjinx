package test.number;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import otc.util.MapUtil;
import otc.util.RSAUtils;

public class MD5 {
	public static void main(String[] args) {
		String key = "A460F297FB7F4E048E1EB71E7A337B1F";
		 String dealKey = UUID.randomUUID().toString().replace("-", "").toUpperCase();
		 System.out.println(dealKey);
	}
 
}
