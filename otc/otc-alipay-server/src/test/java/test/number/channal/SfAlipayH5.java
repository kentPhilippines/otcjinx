package test.number.channal;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.netflix.ribbon.proxy.annotation.Http;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import otc.api.alipay.Common;
import otc.util.number.GenerateOrderNo;

public class SfAlipayH5 {
	  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    // 获取当前时间年月日时分秒毫秒字符串
	    private static String getNowDateStr() {
	        return sdf.format(new Date());
	    }
	public static void main(String[] args) throws NoSuchAlgorithmException {
		 String AuthorizationURL = "http://34.92.76.25/Pay_Index.html";
		    String merchantId = "200602537";
		    String keyValue = "hjisna4yigfbaux4c2rth0frwco8md3j" ;
		    String     pay_bankcode=null;
		         pay_bankcode="922";   //'银行编码
		 String    pay_memberid=merchantId;//商户id
		 String    pay_orderid=	GenerateOrderNo.Generate("C");	//20位订单号 时间戳+6位随机字符串组成
		 System.out.println("订单号:"+pay_orderid);
		 System.out.println("长度:"+pay_orderid.length());
		 String    pay_applydate=getNowDateStr();//yyyy-MM-dd HH:mm:ss
		 String pay_amount = "500";
		 String pay_notifyurl = "http://starpay77.com/index";
		 String pay_callbackurl = "http://starpay77.com/index";
		 Map<String, Object> map = new HashMap<String, Object>();
		 map.put("pay_memberid" , pay_memberid) ;
		 map.put("pay_applydate" , pay_applydate) ;
		 map.put("pay_orderid" , pay_orderid) ;
		 map.put("pay_bankcode" , pay_bankcode) ;
		 map.put("pay_notifyurl" , pay_notifyurl) ;
		 map.put("pay_callbackurl" , pay_callbackurl) ;
		 map.put("pay_amount" , pay_amount) ;
		 String createParam = createParam(map);
		 System.out.println("签名前加密串："+createParam);
		 String pay_md5sign =  md5(createParam+"&key="+keyValue).toUpperCase();
		 map.put("pay_productname" , "测试") ;
		 map.put("pay_md5sign" , pay_md5sign) ;
		 System.out.println(map.toString());
		 String post = HttpUtil.post(AuthorizationURL, map);
		 System.out.println("返回参数："+post);
		 String unWrap = StrUtil.unWrap(post, "<a href='", "'>点此付费</a>");
		 try {
			 JSONObject parseObj = JSONUtil.parseObj(unWrap);
			 Object object = parseObj.get("status");
			 if(ObjectUtil.isNotNull(object)) {
				 System.out.println("支付失败");
				 System.out.println(parseObj.get("msg"));
			 }
		} catch (Exception e) {
				 System.out.println("转换后的字符为："+unWrap);
		}
		
	
	}
	public static String createParam(Map<String, Object> map) {
		try {
			if (map == null || map.isEmpty())
				return null;
			Object[] key = map.keySet().toArray();
			Arrays.sort(key);
			StringBuffer res = new StringBuffer(128);
			for (int i = 0; i < key.length; i++) 
				if(ObjectUtil.isNotNull(map.get(key[i])))
					res.append(key[i] + "=" + map.get(key[i]) + "&");
			String rStr = res.substring(0, res.length() - 1);
			return rStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
