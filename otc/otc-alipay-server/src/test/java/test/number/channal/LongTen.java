package test.number.channal;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math.analysis.integration.RombergIntegrator;

import com.netflix.ribbon.proxy.annotation.Http;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import cn.hutool.*;

public class LongTen {
	public static void main(String[] args) throws IOException {
		test();
	}
	private static void test() throws IOException {
		Map<String, Object> headMap = new HashMap();
		String ltUrl = "https://ltezp.xyz/pay/qrorder";
		String key = "zGWotwd81pQhZ9JP";
		String sid = "Jm4f58b1";
		String timestamp = System.currentTimeMillis()+"";
		String nonce = StrUtil.uuid();
		String url = "/pay/qrorder";
		headMap.put("sid", sid);
		headMap.put("timestamp", timestamp);
		headMap.put("nonce", nonce);
		headMap.put("url", url);
		String head = createParam(headMap);
		
		
	
		Map<String, Object> bodyMap = new HashMap<String, Object>();
		String out_trade_no = RandomUtil.randomString(10);
		String channel = "8";
		String amount = "100";
		String currency = "CNY";
		String notify_url = "http://starpay77.com/index";
	    InetAddress address1 = InetAddress.getLocalHost();
        String ip = address1.getHostAddress();//获得本机IP
		String send_ip = "175.176.32.255";
		bodyMap.put("out_trade_no", out_trade_no);
		bodyMap.put("channel", channel);
		bodyMap.put("amount", amount);
		bodyMap.put("currency", currency);
		bodyMap.put("notify_url", notify_url);
	//	bodyMap.put("return_url", notify_url);
		bodyMap.put("attach", notify_url);
		bodyMap.put("send_ip", send_ip);
		String body = createParam(bodyMap);
		String sign = head + body + key;
		System.out.println("签名前加密参数："+sign);
		String upperCase = md5(sign).toUpperCase();
		System.out.println(upperCase);
		String createParam = createParam1(bodyMap);
		System.out.println("body参数数据："+createParam);
		String result2 = HttpRequest.post(ltUrl)
			    .header("sid" , sid)//头信息，多个头信息多次调用此方法即可
			    .header("timestamp" , timestamp)//头信息，多个头信息多次调用此方法即可
			    .header("nonce" , nonce)//头信息，多个头信息多次调用此方法即可
			    .header("url" , url)//头信息，多个头信息多次调用此方法即可
			    .header("sign" , upperCase)
			    //头信息，多个头信息多次调用此方法即可Content-Type
		 	    .body(createParam,"application/x-www-form-urlencoded")
		 	    .timeout(20000)//超时，毫秒
		 	    .execute().body();
			Console.log("应答报文"+result2);
	  }
	public static String createParam1(Map<String, Object> map) {
		try {
			if (map == null || map.isEmpty())
				return null;
			Object[] key = map.keySet().toArray();
			Arrays.sort(key);
			StringBuffer res = new StringBuffer(128);
			for (int i = 0; i < key.length; i++) 
				if(ObjectUtil.isNotNull(map.get(key[i])))
					res.append(key[i] + "=" + map.get(key[i])+"&" );
			String rStr = res.substring(0, res.length() - 1);
			return rStr;
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
			for (int i = 0; i < key.length; i++) {
				if(ObjectUtil.isNotNull(map.get(key[i]))) {
					res.append(key[i] + "" + map.get(key[i]));
					System.out.println(key[i] + "" + map.get(key[i]) );
				}
			}
			return res.toString();
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
