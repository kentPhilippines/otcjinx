package test.number.channal;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import otc.util.MapUtil;
import otc.util.encode.XRsa;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DEAL {
    static SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");

    public static void main(String[] args) throws Exception {
        test();
    }

    static void test() throws Exception {
        String key = "2593114D4AE7469FB0089F434B94AC6F";//������Կ
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCe772h6hRLDhNsAbkUHMJHiEBezSaBqwoRRiC5slEM7AWEsNo3/RooO9wGRSqrRj9yCOdD0NDvLtTt9MMcZ3Z+fS54h18OI8dKCu+VW8rkzo1BN+Bj4uJh2KUEbx6/F6bwCb2OHTBjh9Pn1bw27cdbjSuiSKlNOQEgGnCbtTCKxwIDAQAB";
        Deal deal = new Deal();
        deal.setAmount("100");//���
        deal.setAppId("AsgRTDFY");//�̻���
        deal.setApplyDate(d.format(new Date()));
        deal.setNotifyUrl("www.baidu.com");
        deal.setOrderId(IdUtil.objectId());
        deal.setPassCode("TEST_ALIPAY_SCAN");
        deal.setSubject("测试");
        deal.setUserid("asdas");
        Map<String, Object> objectToMap = MapUtil.objectToMap(deal);
        String createParam = MapUtil.createParam(objectToMap);
        System.out.println("ǩ��ǰ���󴮣�" + createParam);
        String md5 = md5(createParam + key);
        System.out.println("ǩ����" + md5);
        deal.setSign(md5);
        Map<String, Object> objectToMap2 = MapUtil.objectToMap(deal);
        String createParam2 = MapUtil.createParam(objectToMap2);
        System.out.println("����ǰ�ַ�����" + createParam2);
        XRsa rsa = new XRsa(publicKey);
        String publicEncrypt = rsa.publicEncrypt(createParam2);
        System.out.println("���ܺ��ַ�����" + publicEncrypt);
        Map<String, Object> postMap = new HashMap<String, Object>();
        postMap.put("cipherText", publicEncrypt);
        postMap.put("userId", "AsgRTDFY");
        String post = HttpUtil.post("127.0.0.1:9010/deal/pay", postMap);
        System.out.println("��Ӧ�������" + post);

    }

    public static String md5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
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
    private String pageUrl;
    private String amount;
    private String passCode;
    private String sign;
    private String applyDate;
    private String subject;
    private String userid;

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
}
