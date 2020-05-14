package alipay.manage.api.channel.notfiy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import otc.common.PayApiConstant;
import otc.result.Result;
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@Controller
public class XianYuNotfiyPay extends NotfiyChannel{
	private static final Log log = LogFactory.get();
	@PostMapping("/xianyu-notfiy")
	 public void notify(HttpServletRequest req, HttpServletResponse res) throws IOException {
		log.info("进入咸鱼支付宝H5 回调处理");
		String clientIP = HttpUtil.getClientIP(req);
		log.info("【当前回调ip为："+clientIP+"】");
		if(!clientIP.equals("103.84.90.39")) {
			log.info("【当前回调ip为："+clientIP+"，固定IP登记为："+"103.84.90.39"+"】");
			log.info("【当前回调ip不匹配】");
			res.getWriter().write("ip错误");
			return;
		}
		
		ServletInputStream inputStream = req.getInputStream();
		 String body;
		 StringBuilder stringBuilder = new StringBuilder();
		 BufferedReader bufferedReader = null;
	try {
		 if (inputStream != null) {
	         bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	         char[] charBuffer = new char[128];
	         int bytesRead = -1;
	         while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	             stringBuilder.append(charBuffer, 0, bytesRead);
	         }
		 } else {
	            stringBuilder.append("");
	     }
	  } catch (IOException ex) {
	        throw ex;
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	                throw ex;
	            }
	        }
	    }
	    body = stringBuilder.toString();
	    JSON parse = JSONUtil.parse(body);
	    JSONObject parseObj = JSONUtil.parseObj(parse);
	    
	    Set<String> keySet = parseObj.keySet();
		log.info("【收到咸鱼支付宝H5支付成功请求，当前请求参数为："+parseObj+"】");
	    Map<String,Object> decodeParamMap = new ConcurrentHashMap();
	    for(String key : keySet) 
	    	decodeParamMap.put(key, parseObj.getObj(key));
		String queryString = req.getQueryString();
		log.info("【当前咸鱼请求参数为："+queryString+"】");
		String getid = decodeParamMap.get("fxid").toString();
		String getddh = decodeParamMap.get("fxddh").toString();
		String getorder = decodeParamMap.get("fxorder").toString();
		String getattch = decodeParamMap.get("fxattch").toString();
		String getdesc = decodeParamMap.get("fxdesc").toString();
		String getfee = decodeParamMap.get("fxfee").toString();
		String getstatus = decodeParamMap.get("fxstatus").toString();
		String gettime = decodeParamMap.get("fxtime").toString();
		String getsign = decodeParamMap.get("fxsign").toString();
		String successstatus = "1";
		if (getstatus.equals(successstatus)!=true) {
		    res.getWriter().write("状态错误");
		    return;
		}
		//订单签名 【md5(订单状态+商务号+商户订单号+支付金额+商户秘钥)】
		String mysign = md5(getstatus + getid + getddh + getfee + "AHFuoYCUgZcOdpectBxYiPElWMVGljbc");
		mysign = mysign.toLowerCase();
		if (mysign.equals(getsign)!=true) {
		    log.info("签名错误，我方签名："+mysign+"，对方签名："+getsign+"");
		    res.getWriter().write("签名错误");
		    return;
		}
		 Result dealpayNotfiy = dealpayNotfiy(getddh, clientIP);
		 if(dealpayNotfiy.isSuccess()) {
			 res.getWriter().write("success");
			 log.info("【咸鱼H5 交易成功】 ");
			 return;
		 }
		  return;
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
