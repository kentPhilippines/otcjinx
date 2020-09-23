
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
		new witTest().run();
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

	void run() {
		SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");
		String key = "1FFE92CF583A451585181C958EE44775";//交易密钥
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKdv2iandXVj8MjY44RwsqwBwmu4gcnd2Fnu4SEGXwLL9TeKJgfvrzALYH2kDVWHzqNCu3VO37wRx3uKR1h1hmauHTeyadS4DrnzxSD1Wa1BO5yjYO3U+g11Jb8gO9HKeA/8SC7E2OhVHvpy6SHm0kl4LzmYNugEgPU/0DeVgSTQIDAQAB";
		Deal deal = new Deal();
		deal.setAmount("100");//金额
		deal.setAppId("borui123");//商户号
		deal.setApplyDate(d.format(new Date()));
		deal.setNotifyUrl("http://starpay168.com:5055");
		deal.setOrderId(IdUtil.objectId());
		deal.setPassCode("ALIPAY_SCAN");
		deal.setSubject("订单交易");
		deal.setUserid("freebuf002");  //to userid
		Map<String, Object> objectToMap = MapUtil.objectToMap(deal);
		String createParam = MapUtil.createParam(objectToMap);
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
		postMap.put("userId", "borui123");
		String post = HttpUtil.post("http://starpay168.com:5055/api-alipay/deal/pay", postMap);
		System.out.println("相应结果集：" + post);

	}

}

class Deal {
	/**
	 * ######################
	 * �ֶ���		�Ƿ����ǩ��   �Ƿ����
	 * appid			��			��		24		�̻��ţ����磺11396	�̻����׺�
	 * orderid			��			��		128		�����ţ�����Ψһ	ϵͳ��֤��ʽ appid+orderid����֤Ψһ�ԡ�
	 * notifyurl		��			��		96		�ص���ַ	�ص���ַ������72���ַ�
	 * pageUrl			��			��		124		ͬ�����ص�ַ��H5���Ʒ�ش���	֧���ɹ�����ת�ĵ�ַ
	 * amount			��			��		-		֧�����, ��λ  �� Ԫ
	 * passcode		��			��		24		ͨ�����룺PAY1005��֧����ɨ�롿	��Ʒ���ͱ�š���ѯ��Ӫ��
	 * rsasign			��			��		-		ǩ���ַ������ܣ����ܷ�ʽ�ο�ƽ̨��Demo	ǩ��
	 * userid			��			��		124		�����û�Ψһ��ʶ	������Ϊ�̻�����ұ�ʶ��������߳ɹ��ʣ�
	 * bankCode 		��			��		24		���б���	�磺 bankCode=ICBC������֧��ʱ����
	 * subject 		��			��		124		��Ʒ����
	 * applydate		��			��		-		����ʱ�䣬ʱ���ʽ��yyyyMMddHHmmss	��ʱ���ʽ���������쳣��
	 */
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
