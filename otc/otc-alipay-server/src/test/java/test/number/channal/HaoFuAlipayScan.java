package test.number.channal;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import alipay.manage.api.channel.util.haofu.HaoFuUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class HaoFuAlipayScan {
	public static void main(String[] args) {
		alipaytobank();
	}
	static void alipaytobank(){
		Map<String,Object> map = new HashMap<String,Object>();
		String key  = HaoFuUtil.KEY;
		map.put("partner", HaoFuUtil.APPID);
		map.put("amount", "500");
		map.put("request_time", System.currentTimeMillis()/1000);
		map.put("trade_no", StrUtil.uuid());
		map.put("type",  "ali");
		map.put("result_type",  "json");
		map.put("notify_url","https://www.uz-pay.com/Api/collection/query");
		String createParam = createParam(map);
		String md5 = md5(createParam+"&"+key);
		map.put("sign", md5);
		System.out.println("【当前豪富请求参数为："+map.toString()+"】");
		String post = HttpUtil.post(HaoFuUtil.URL+"/payCenter/gatewaypay", map);
		System.out.println("【豪富响应参数为："+post+"】");
		JSONObject parseObj = JSONUtil.parseObj(post);
		Object object = parseObj.get("is_success");
	}


	static  void alipayScan(){
		Map<String,Object> map = new HashMap<String,Object>();
		/**
		 * 	partner			32		是		是		商户合作号，由平台注册提供
		 amount			32		是		是		金额(单位:元,支持两位小数)
		 request_time	10		是		是		时间戳,精确到秒
		 sign			256		是		否		签名字符串
		 trade_no		32		是		是		订单号
		 pay_type		2		是		是		唤醒参数:h5 		扫码参数:sm
		 notify_url		64		是		是		异步通知地址
		 */
		String key  = "afdfasdf16541asdf51asd6f621sd";
		map.put("partner", "1014634188");
		map.put("amount", "500");
		map.put("request_time", System.currentTimeMillis()/1000);
		map.put("trade_no", UUID.randomUUID().toString());
		map.put("pay_type", "ali");
		map.put("notify_url", "www.baidu.com");
		String createParam = createParam(map);
		String md5 = md5(createParam+"&"+key);
		map.put("sign", md5);
		System.out.println("【当前豪富请求参数为："+map.toString()+"】");
		String post = HttpUtil.post("https://mmtnpkedtb.6785151.com/payCenter/gatewaypay", map);
		System.out.println("【豪富响应参数为："+post+"】");
		JSONObject parseObj = JSONUtil.parseObj(post);
		Object object = parseObj.get("is_success");
		if(ObjectUtil.isNotNull(object)) {
			System.out.println("当前豪富的订单为："+object+"");
			if(object.equals("T")) {
				Object object2 = parseObj.get("result");
				if(ObjectUtil.isNotNull(object2)) {
					System.out.println("【支付链接为："+object2+"】");
				}
			}
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
