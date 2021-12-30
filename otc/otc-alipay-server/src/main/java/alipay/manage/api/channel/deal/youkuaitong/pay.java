package alipay.manage.api.channel.deal.youkuaitong;


import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import cn.hutool.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


public class pay {
	//下单
	synchronized void pay() {

		String payurl = "https://XXXXX.com/api/unifiedorder";
		String payAmount = "313"; // 金额
		String tradeType = "XXXXX"; // '银行编码
		String mchId = "10002";// 商户id
		String outTradeNo = generateOrderId();// 20位订单号 时间戳+6位随机字符串组成
		String nonceStr = UUID.randomUUID().toString();//
		String notifyUrl = "http://XXXX.com/";// 通知地址
	 
		String privateKey = "xxxxxxxxxxx";// 用户私钥
		StringBuffer s = new StringBuffer();
		s.append("mchId=" + mchId + "&");
		s.append("outTradeNo=" + outTradeNo + "&");
		s.append("payAmount=" + payAmount + "&");
		s.append("nonceStr=" + nonceStr + "&");
		s.append("tradeType=" + tradeType + "&");
		s.append("notifyUrl=" + notifyUrl);
		String sign = RSASignature.sign(s.toString(), privateKey);//

		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
	 
		param.add("sign", sign);
	 
		param.add("mchId", mchId);
		param.add("outTradeNo", outTradeNo);
		param.add("payAmount", payAmount);
		param.add("nonceStr", nonceStr);
		param.add("tradeType", tradeType);
		param.add("notifyUrl", notifyUrl);
	 

		RestTemplate restTemplate = new RestTemplate();
		String rString = restTemplate.postForObject(payurl, param, String.class);
		JSONObject josn = new JSONObject();
	/*	if (josn.getInteger("code") == 200) {
			//业务处理
		}*/

	}
	//回调
	String callBack(HttpServletRequest request) {
		JSONObject r = new JSONObject();
		try {
			String mchId = request.getParameter("mchId") ;
			String outTradeNo = request.getParameter("outTradeNo") ;
			String payAmount = request.getParameter("payAmount") ;
			String transactionId = request.getParameter("transactionId") ;
			String nonceStr = request.getParameter("nonceStr") ;
			String success = request.getParameter("success") ;
			String sign = request.getParameter("sign") ;
			StringBuffer s = new StringBuffer();
			s.append("mchId=" + mchId + "&");
			s.append("outTradeNo=" + outTradeNo+ "&");
			s.append("payAmount=" + payAmount + "&");
			s.append("transactionId=" + transactionId + "&");
			s.append("nonceStr=" + nonceStr+ "&");
			s.append("success=" + success);
//			s.append("key=" + member.getApikey());
			String publicKey="XXXXXXX";//平台公钥
			if (RSASignature.doCheck(s.toString(), sign, publicKey)) {
				//业务处理
			}
		} catch (Exception e1) {
		}
		return "OK";
	}
	public String generateOrderId() {
		String keyup_prefix = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String keyup_append = String.valueOf(new Random().nextInt(899999) + 100000);
		String pay_orderid = keyup_prefix + keyup_append;// 订单号
		return pay_orderid;
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