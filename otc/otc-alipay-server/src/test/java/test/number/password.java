package test.number;

import otc.result.Result;
import otc.util.encode.HashKit;

public class password {
	public static void main(String[] args) {
		Result encodePassword = HashKit.encodePassword("21873136853", "123456", "80yqwzp37x");
		System.out.println(encodePassword.toString());
	}

}
