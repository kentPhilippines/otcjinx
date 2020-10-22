
package test.number;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import otc.util.MapUtil;
import otc.util.encode.XRsa;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class witTest {
	public static void main(String[] args) {
		new witTest().deal();
		//相应结果集：{"success":true,"message":"支付处理中","result":{"sussess":true,"cod":0,"openType":1,"returnUrl":"http://api.tjzfcy.com/gateway/bankgateway/payorder/order/60326816340490956.html"},"code":null}
		//相应结果集：{"success":true,"message":"支付处理中","result":{"sussess":true,"cod":0,"openType":1,"returnUrl":"http://api.tjzfcy.com/gateway/bankgateway/payorder/order/60326822046537022.html"},"code":1}


		//	HttpUtil.get("127.0.0.1:9010/pay/testWit?orderNo=W1598355653114710115");
	}


	public static String getKeyedDigestUTF8(String strSrc) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(strSrc.getBytes("UTF8"));
			String result = "";
			byte[] temp;
			temp = md5.digest("".getBytes("UTF8"));
			for (int i = 0; i < temp.length; i++)
				result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
			return result;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String createParam(Map<String, Object> map) {
		try {
			if (map == null || map.isEmpty())
				return null;
			Object[] key = map.keySet().toArray();
			Arrays.sort(key);
			StringBuffer res = new StringBuffer(128);
			for (int i = 0; i < key.length; i++)
				if (ObjectUtil.isNotNull(map.get(key[i])))
					res.append(key[i] + "=" + map.get(key[i]) + "&");
			String rStr = res.substring(0, res.length() - 1);
			return rStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	void wit() {
		SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");
		String userid = "CX888";
		String key = "AF2A874BF0AF49BBA136EB738E857EF6";//交易密钥
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGH6UIF1tms5pAmXQaPpwiprfF8m5AWlxAED4wjbHKYsdRXAxJaVJTkwvfDr+QAfJaa7YY4h33uVjMmqoxJ8rkHaT0w9l2fi6xReAWmhPJ6CDUN0zCF5mwawLDfw1ivYdfz9ZJCJhS8MmPNVTxPd1+5Tl9nZ7kSu5dxgZpuadilwIDAQAB";


		Map<String, Object> objectToMap = new HashMap<>();
		objectToMap.put("appid", userid);
		objectToMap.put("apporderid", "123712343627d1dg112");
		objectToMap.put("ordertime", d.format(new Date()) + "");
		objectToMap.put("amount", "10");
		objectToMap.put("acctno", "test123123123123");
		objectToMap.put("acctname", "zhangsan");
		objectToMap.put("bankcode", "ICBC");
		objectToMap.put("notifyurl", "http://www.baodu.com");
		String createParam = createParam(objectToMap);
		System.out.println("签名前请求串：" + createParam);
		String md5 = getKeyedDigestUTF8(createParam + key);
		System.out.println("签名：" + md5);
		objectToMap.put("sign", md5);
		String createParam2 = createParam(objectToMap);
		System.out.println("加密前字符串：" + createParam2);
		XRsa rsa = new XRsa(publicKey);
		String publicEncrypt = rsa.publicEncrypt(createParam2);
		System.out.println("加密后字符串：" + publicEncrypt);
		Map<String, Object> postMap = new HashMap<String, Object>();
		postMap.put("cipherText", publicEncrypt);
		postMap.put("userId", userid);
		System.out.println("请求参数：" + postMap.toString());
		String post = HttpUtil.post("http://starpay168.com:5055/api-alipay/deal/wit", postMap);
		System.out.println("相应结果集：" + post);


	}

	void deal() {
		SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");
		String userid = "2u7rMduh";
		String key = "bc64a6067ae6e7edcfd8c62ad3472d46";//交易密钥
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCIU7OCf+4N47fbJMCpd68GileM6vz02Bi+nTrvuzn7uAn/g89WGE+/aYuFHzAOCsrZ17y26RkQimjbWISEkDd4izP0+w7D0qZCb2czE+UeV7Qvt/DuXpY+yGM1+EAnP/xTr4qmXAZDn3e+xSzU8VMDD/3cuGH6X0CbmQZeK+VVqQIDAQAB";
		Deal deal = new Deal();
		deal.setAmount("100");//金额
		deal.setAppId(userid);//商户号
		deal.setApplyDate(d.format(new Date()));
		deal.setNotifyUrl("http://starpay168.com:5055");
		deal.setPageUrl("http://starpay168.com:5055");
		deal.setOrderId(IdUtil.objectId());
		deal.setPassCode("ALIPAYTOBANK");
		deal.setSubject("订单交易");
		deal.setUserid("ASDSADASDS");  //to userid
		Map<String, Object> objectToMap = MapUtil.objectToMap(deal);
		String createParam = createParam(objectToMap);
		System.out.println("签名前请求串：" + createParam);
		String md5 = getKeyedDigestUTF8(createParam + key);
		System.out.println("签名：" + md5);
		deal.setSign(md5);
		Map<String, Object> objectToMap2 = MapUtil.objectToMap(deal);
		String createParam2 = createParam(objectToMap2);
		System.out.println("加密前字符串：" + createParam2);
		XRsa rsa = new XRsa(publicKey);
		String publicEncrypt = rsa.publicEncrypt(createParam2);
		System.out.println("加密后字符串：" + publicEncrypt);
		Map<String, Object> postMap = new HashMap<String, Object>();
		postMap.put("cipherText", publicEncrypt);
		postMap.put("userId", userid);
		System.out.println("请求参数：" + postMap.toString());
		String post = HttpUtil.post("http://starpay168.com:5055/api-alipay/deal/pay", postMap);
		System.out.println("相应结果集：" + post);

	}

}

class Deal {
	private String appId;
	private String orderId;
	private String notifyUrl;
	private String amount;
	private String passCode;
	private String sign;
	private String applyDate;
	private String subject;
	private String userid;
	private String pageUrl;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPassCode() {
		return passCode;
	}

	public void setPassCode(String passCode) {
		this.passCode = passCode;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	@Override
	public String toString() {
		return "Deal{" +
				"appId='" + appId + '\'' +
				", orderId='" + orderId + '\'' +
				", notifyUrl='" + notifyUrl + '\'' +
				", pageUrl='" + pageUrl + '\'' +
				", amount='" + amount + '\'' +
				", passCode='" + passCode + '\'' +
				", sign='" + sign + '\'' +
				", applyDate='" + applyDate + '\'' +
				", subject='" + subject + '\'' +
				", userid='" + userid + '\'' +
				'}';
	}
}
//amount=100&appId=borui123&applyDate=20200922173742&notifyUrl=http://starpay168.com:5055&orderId=5f69c5e7476ee6c666a28842&passCode=ALIPAY_SCAN&subject=订单交易&userid=freebuf002
//amount=100&appId=borui123&applyDate=20200922174347&notifyUrl=http://starpay168.com:5055&orderId=5f69c753476e6dc4e28774fe&passCode=ALIPAY_SCAN&sign=a9e5f599625ba25bf7753fce17924ef0&subject=订单交易&userid=freebuf002

//amount=100&appId=borui123&applyDate=20200922174347&notifyUrl=http://starpay168.com:5055&orderId=5f69c753476e6dc4e28774fe&passCode=ALIPAY_SCAN&subject=订单交易&userid=freebuf002
//amount=100&appId=borui123&applyDate=20200922173742&notifyUrl=http://starpay168.com:5055&orderId=5f69c5e7476ee6c666a28842&passCode=ALIPAY_SCAN&subject=订单交易&userid=freebuf002
