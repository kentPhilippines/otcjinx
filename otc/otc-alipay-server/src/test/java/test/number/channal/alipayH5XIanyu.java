package test.number.channal;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;

public class alipayH5XIanyu {
	public static void main(String[] args) {
		/**
		 * 	fxid				商务号			是	唯一号，由穿山甲支付-csjPAY提供
			fxddh				商户订单号			是	仅允许字母或数字类型,不超过22个字符，不要有中文
			fxdesc				商品名称			是	utf-8编码
			fxfee				支付金额			是	请求的价格(单位：元) 可以0.01元
			fxnotifyurl			异步通知地址		是	异步接收支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
			fxbackurl			同步通知地址		是	支付成功后跳转到的地址，不参与签名。
			fxpay				请求类型 【支付宝扫码：zfbsm】【支付宝H5：zfbh5】	是	请求支付的接口类型。
			fxnotifystyle		异步数据类型		否	异步返回数据的类型，默认1 返回数据为表单数据（Content-Type: multipart/form-data），2 返回post json数据。
			fxattch				附加信息			否	原样返回，utf-8编码
			fxsmstyle			扫码模式			否	用于扫码模式（sm），仅带sm接口可用，默认0返回扫码图片，为1则返回扫码跳转地址。
			fxbankcode			银行类型			否	用于网银直连模式，请求的银行编号，参考银行附录,仅网银接口可用。
			fxfs				反扫付款码数字		否	用于用户被扫，用户的付款码数字,仅反扫接口可用。
			fxuserid			快捷模式绑定商户id	否	用于识别用户绑卡信息，仅快捷接口可用。
			fxsign				签名【md5(商务号+商户订单号+支付金额+异步通知地址+商户秘钥)】	是	通过签名算法计算得出的签名值。
			fxip				支付用户IP地址	是	用户支付时设备的IP地址
		 */
		/**
		 * 		money			是		订单金额
				part_sn			是		商家平台订单号
				notify			是		异步回调地址
				id				是		码商账号id
				sign			是		签名
		
		String money = "100";
		String part_sn = UUID.randomUUID().toString();
		String id = "id";
		String notify = "http://localhost:8084/qzf/notifyUrl.htm";
		
		
		
		 */
		
		
		 
		
		
		
		   	String fxnotifyurl = "http://182.16.89.146:9010/notfiy-api-pay/xianyu-notfiy";
		    String fxbackurl = "http://182.16.89.146:9010/notfiy-api-pay/xianyu-notfiy";
		    String fxattch = "CHESHICHESHICHESHI";
		    String fxdesc = "aDFJKGHAOIFDGIGIURIUGIJGDFJIKGASDKOJG";
		    String fxfee = "1000.00";
		    String fxpay = "zfbh5";
		    String fxddh =  "CH"+ IdUtil.objectId().toUpperCase(); //订单号
		    String fxid = "2020177";
		    String key = "AHFuoYCUgZcOdpectBxYiPElWMVGljbc";
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
		    reqMap.put("fxdesc", fxdesc);
		    reqMap.put("fxip", "182.16.89.146");
		    reqMap.put("fxsign", fxsign);
		    System.out.println(reqMap.toString());
		    // 支付请求返回结果
		    String result = null;
		    result = HttpUtil.post("https://csj.fenvun.com/Pay", reqMap);
		    System.out.println(result);
		    
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
