package test.number;

import otc.util.RSAUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class MD5 {
	public static void main(String[] args) {
	/*	String key = md5(IdUtil.objectId().toUpperCase() + IdUtil.objectId().toUpperCase()).toUpperCase();

		List<String> strings = RSAUtils.genKeyPair();
		String s = strings.get(0);
		String a = strings.get(1);

		System.out.println(
				key
		);
		System.out.println(
				s
		);
		System.out.println(
				a
		);*/

        String s = "U9Nl2X6da/9NLv/1sinAf2TARkIGHuTzxKO9Yv9YLziHPDjck3Vi/TeDFhQWqBDe54W9Ae21Ueaea1VT0C/KGV7m09Me506Df/2i1CfJx/RklSGoq8WNnlFWUnLSci64pCdPihbdpN6InFcCvYc9FJvXdiyybpCanKtyzJO1AimfHNgq9tQOud3tPm1mCMSCnKNwuNY2nFQNx1cp7S8YGHrxiJZlcRzGylmOitWEIUIShtFDXdjGGMkyYmwBJUmhbiwwQEkdp0TgE9NPN42lYdqEoZ5mYZDNxYWfuRdEsp7uS07cSJBNpLaE/bwLvR6LvXuUXWrbIsivplg==";
        Map<String, Object> decodePrivateKey = RSAUtils.getDecodePrivateKey(s, "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAIhTs4J/7g3jt9skwKl3rwaKV4zq/PTYGL6dOu+7Ofu4Cf+Dz1YYT79pi4UfMA4KytnXvLbpGRCKaNtYhISQN3iLM/T7DsPSpkJvZzMT5R5XtC+38O5elj7IYzX4QCc//FOviqZcBkOfd77FLNTxUwMP/dy4YfpfQJuZBl4r5VWpAgMBAAECgYBmTuJ1ukpN9CqDdJZ918rrTqEbqTKDETWov1h+lvSR2vDt596OzxnyHWROWDR5JVJMn+IJJu/fgO/mRQzWa2CkpMdS2m3gNyoi8RK5buQHcUkcYo/uoDbYMjKzvFAFvSIdLm805RMnDmXToS31dF/JKN+aui5DsosM1hKPKFTMvQJBANOZ0LAhDshpa3qFol3KEkT8sWvcRe0s7cCYOWSy0MeXkK7VhIv3gFunMhuYzKzVOLQM3dQ7NxzYLIYpIkefjSMCQQCk7oh9vzJbSJ4/F1yvBOiJXkPPSwy+Wu7OQxPxF+QH/xr/GjUN6LaRIY42pyMBEFtQ/b6yzWiD+4QnTBMFrxzDAkEAiom+VFKQdy5uK/2qXHWcmIZJMzE4pxwvXVVM3l0kzEizdYReD7Wv2FITQHY+Kzo+scNqyTUr1DrEi+IKi5IuuQJBAJwtK543MI3z47VGgANTGIcLdhaSsIyIOt2h8v43azyPOpcZz+dN16LyLWJfPexB+tZkkPFrRG7F+kvj45dcTFUCQQCsyBFJIO6WIE2HiMTCFUQdFM1qTX0FGJBHS+jNrFPTd6r9hW9uSPd9fnSuq3MECFf50CHGeEH2xz6ZqHtAXGZP");
        System.out.println(decodePrivateKey.toString());
    }

	private static final String UTF_8 = "utf-8";
	private static final String ENCODE_TYPE = "md5";
    /**
     * md5加密
     * @param str
     * @return
     */
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
