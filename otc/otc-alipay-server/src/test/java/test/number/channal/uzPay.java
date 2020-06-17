package test.number.channal;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class uzPay {
	public static void main(String[] args) {
		String key = "83e63494c7abb22b87d397155c04d413";
		String url ="https://www.uz-pay.com/Api/collection";
		String query ="https://www.uz-pay.com/Api/collection/query";
		String uid =  "55589";
		String userid = "JX888";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("uid", uid);
		map.put("orderid", "asdasdas5654dss1231as55");
		String createParam = createParam(map);
		System.out.println("签名串加密前参数为："+createParam);
		String md5 = md5(createParam+"&key="+key);
		map.put("sign", md5);
		String createParam2 = createParam(map);
		String post = HttpUtil.post(query, map);
		System.out.println(post);
		JSONObject parseObj = JSONUtil.parseObj(post) ;
		Object object = parseObj.get("order");
		System.out.println(object);
		JSONObject parseObj2 = JSONUtil.parseObj(object) ;
		Object object2 = parseObj2.get("status");
		if(ObjectUtil.isNotNull(object2)) {
			 if("verified".equals(object2.toString())) {
			 }
		 }
		 
		
		
		//订单查询{"success":true,"code":200,"order":{"oid":"I-asdasdas5654dss1231as55","orderid":"asdasdas5654dss1231as55","userid":"JX888","amount":"200.00","status":"processing","created_time":"1591435703","verified_time":null,"type":"remit"}}

	/*	Map<String,Object> map = new HashMap<String,Object>();
		map.put("amount", "200");
		map.put("orderid", "asdassadsdas5654dss1231as55");
		map.put("userip", "127.0.0.1");
		map.put("notify", "www.baidu.com");
		String createParam = createParam(map);
		System.out.println("签名串加密前参数为："+createParam);
		String md5 = md5(createParam+"&key="+key);
		map.put("sign", md5);
		String createParam2 = createParam(map);
		String a  = url + "?"+createParam2;
		System.out.println(a );
		*/		
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
