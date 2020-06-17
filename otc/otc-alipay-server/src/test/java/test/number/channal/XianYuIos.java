package test.number.channal;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import alipay.manage.bean.DealOrder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class XianYuIos {
	
	
	public static void main(String[] args) {

	   	String fxnotifyurl = "http://182.16.89.146:9010/notfiy-api-pay/xianyu-notfiy";
	    String fxbackurl = "http://182.16.89.146:9010/notfiy-api-pay/xianyu-notfiy";
	    String fxattch = "test";
	    String fxdesc = "desc";
	    String fxfee = "500";
	    String fxpay = "zfbh5";
	    String fxddh = System.currentTimeMillis()+""; //订单号
	    String fxid = "2020185";
	    String key = "pNgvmzYZcWpDfxLlYTyUFyhPtXPXPhrn";
	    //订单签名
	    String fxsign = md5(fxid + fxddh + fxfee + fxnotifyurl + key); 
	    fxsign = fxsign.toLowerCase();
	    Map<String, Object> reqMap = new HashMap<String, Object>();
	    reqMap.put("fxid", fxid);
	    reqMap.put("fxddh", fxddh);
	    reqMap.put("fxfee", fxfee);
	    reqMap.put("fxpay", fxpay);
	    reqMap.put("fxnotifyurl", fxnotifyurl);
	    reqMap.put("fxbackurl", fxbackurl);
	    reqMap.put("fxattch", fxattch);
	    reqMap.put("fxnotifystyle", "1" );
	    reqMap.put("fxdesc", fxdesc);
	    reqMap.put("fxip", "127.0.0.1");
	    reqMap.put("fxsign", fxsign);
	   System.out.println("【咸鱼支付宝扫码请求参数："+reqMap.toString()+"】");
	    // 支付请求返回结果
	    String result = null;
	    result = HttpUtil.post("https://csj.fenvun.com/Pay", reqMap);
	    JSONObject parseObj = JSONUtil.parseObj(result);
	    System.out.println("【咸鱼支付宝扫码返回："+parseObj.toString()+"】");
	    Object object = parseObj.get("status");
	
	}
	 public static String md5(String a) {
	    	String c = "";
	    	MessageDigest md5;
		   	String result="";
			try {
				md5 = MessageDigest.getInstance("md5");
				md5.update(a.getBytes("utf-8"));
				byte[] temp;
				temp=md5.digest(c.getBytes("utf-8"));
				for (int i=0; i<temp.length; i++)
					result+=Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			}
			return result;
	    }
}
