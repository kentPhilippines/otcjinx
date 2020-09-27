package test.number.appdeal;

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

public class bage123 {
    public static void main(String[] args) {
        run();
    }


    static void run() {
        SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");
        String userid = "bage123";
        String key = "B90C15BEE46148E896EF729A4BF36650";//交易密钥
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCN4KC12pit2XPR65LwhbbWJiTwayrlPHCap9nMyXuCU3fx4p+Z9oiWKxkjMowT0nYdl2YPec1ovH7jVo6QwTepJBTJLfX92MbQl8LhVojCet33nV0wkEg+1kHOdyYWmIB/xxBv1MIbJ6Gx0XV3DdzfhWRDlVUnUmjpRNXWHpMWvQIDAQAB";
        String a = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBgveeFzs3TKSdfMH9Z5uZ5aAThUaSaCLjabXIyAzpbmz1SzbCc6YNXAEYwDvXkztTzbks6jQbt61ib1Uuy4z123wEYk3p4IyFMKfEquPAauj7yTybME0J23rmpXDgXLsX5vO2LB1P9pcv1HiJG/403wYiebnLOfB1w/20qtRnyQIDAQAB";


        Deal deal = new Deal();
        deal.setAmount("100");//金额
        deal.setAppId(userid);//商户号
        deal.setApplyDate(d.format(new Date()));
        deal.setNotifyUrl("http://starpay168.com:5055");
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
