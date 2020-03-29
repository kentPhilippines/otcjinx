package otc.notfiy.util;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import otc.api.alipay.Common;
import otc.notfiy.bean.Mms;
import otc.notfiy.service.MmsService;
@Component
public class AndroidUtil {
	final SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");
	Logger log = LoggerFactory.getLogger(AndroidUtil.class);
	private static final String KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKWqdZtT9N9WDeIhyG0dtH6adRNoyNlJj9gb5GquejqYPKg/dDNhEnB7dSGuAz2w3doCoeIRg1f7BYiPw5yoZqfDZGpr216gifd4cWauTCvBqREujJMjvJc2k+FkDJa9wY6oO3GQirZ3ijfFl8lpmSNK/wG7XYfS3XqCasxexaCrAgMBAAECgYA+LzXmECWij5K2hyfMjZHq09+OYY7CwTIVVKLwyH1o8SwTm33qq01Ym37kHYVp6rHb25EYYqqCo9732775VtzxEVo37UtSI/PItDnrQbvf9/N/3tYs8rh1G10CI60YTU1drSdApQFHlTENAcXqgnFPLuF3ew+50BoenF34r/JAAQJBANWJMjeZUHxZKHj5tRLMuBuS05dS+Bw7FIx2hI5Wp2lugstODE+ZZJN5kPKliDk5Ns/aE9Ixh1guZIGYKyqUzAECQQDGnESHzyxekmot+mekP5RpBuyP608taZ5uUVaAIrQyDIA5HomB2/Wp3YMUWhvOGPnh9Ckjn4FFnIi9gefoEVyrAkAbZ7c9MX0F6H9sP0gA+KssRsTHKAvVu7Ngb5mFlxN3UYqRwxuLX7lrv+9dZOc9yN0DAg8HK/od1B5sD3aCyYQBAkEAlJa+8rg9ord5ttJbjdd/aiAjBf1vLDOTs0cpJw5PsA4INDOzfrMYlTBDbAuKN+QZt0GbMaqY5YKaDuXMoaOzpwJATqwHi7gNleA2QBpyCGDuEeHIftw0rAoRFSPYsUpJsDBBwAPAFJ6uebg32VdFBx5gNMqS8TzM6ds7FWXzTzBJDA==";                          
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired MmsService mmsServiceImpl;
	public void http2(  Map<Object, Object> postjson,HttpServletRequest request, HttpServletResponse response) throws IOException {
	String data = postjson.get("data").toString();
	log.info("接受回调信息data："+data);
	String decryptByPrivateKey = null;
	try {
		decryptByPrivateKey = UseRSAUtil.decryptByPrivateKey(data,KEY);
		decryptByPrivateKey = URLDecoder.decode(decryptByPrivateKey, "UTF-8");
		Thread.sleep(2000);
	} catch (Exception e) {
		e.printStackTrace();
	}
	log.info("接受回调信息："+decryptByPrivateKey+"，接收时间"+ 	d.format(new Date()));
	Map<String, String> datajson = toMap(decryptByPrivateKey);
	String type = datajson.get("type").toString(); //种类  例:alipayTocard
	String deviceid = datajson.get("deviceid").toString(); //物理介质唯一标识  例:宝转卡到账
	String money = datajson.get("money").toString(); //金额  例:1500.01
	String content = decryptByPrivateKey;
	if(StrUtil.isBlank(money)) {
		log.info("回调信息为空");
		return ;
	}
	if(!(money.indexOf("通过扫码") > -1)) {//过滤回调消息
		log.info("过滤回调信息"+money);
		Mms msg = new Mms();
		msg.setContent(money);
		msg.setDeviceid(deviceid);
		msg.setEncrypt("0");
		msg.setType(type);
		msg.setMoney("000000");
		msg.setTitle("过滤信息");
		msg.setTime(df.format(new Date()));
		msg.setRetain2("N");	
		mmsServiceImpl.addMms(msg);
		return ;
	}
	if(type.equals("alipay")) {
		log.info("接收到支付宝监听消息："+money);
		money = money.replace("元", "");
		money = money.replace("块", "");
		money = money.replace("圆", "");
	boolean flag = false;
	Mms msg = new Mms();
	String number = AmountUtil.getNumber(money);
	msg.setContent("支付宝到账："+number +" ，元。");
	msg.setDeviceid(deviceid);
	msg.setEncrypt("0");
	msg.setType(type);
	msg.setMoney("000000");
	msg.setTitle("回调通知");
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	msg.setTime(df.format(new Date()));
	msg.setRetain2("N");	
	msg.setRetain4(content);
	boolean addMms = mmsServiceImpl.addMms(msg);
	if(addMms) 
		log.info("【短信回调数据记录成功】");	
	else
		log.info("【短信回调数据记录失败】");	
	HashMap<String, Object> paramMap = new HashMap<>();
	paramMap.put(Common.Notfiy.ORDER_AMOUNT, number);
	paramMap.put(Common.Notfiy.ORDER_PHONE, deviceid);
	paramMap.put(Common.Notfiy.ORDER_ENTER_IP, HttpUtil.getClientIP(request));
	
	/*
	log.info("【正在向后台发起请求，请求参数为："+paramMap.toString()+"，请求URL为："+config.getGatewayUrl()+"】");
	String result = HttpUtil.post(config.getAlipayToAccount(), paramMap);
	//当返回的数据有以下两个字段的时候
	JSONObject parseObj = JSONUtil.parseObj(result);
	JsonResult bean = JSONUtil.toBean(parseObj, JsonResult.class);
	log.info("【主服务器响应结果为："+bean.toString()+"】");
	if(0 == bean.getCode()) {
		msg.setResult(bean.getMessage());
		flag = mmsServiceImpl.updataMms(msg);
	}
	if(bean.isSuccess()) {//成功
		msg.setRetain2("Y");
		msg.setRetain1(bean.getResult().toString());
		msg.setResult(bean.getMessage());
		flag = mmsServiceImpl.updataMms(msg);
//	}
		*/
	}
}
/**
 * <p>接受回调信息</p>
 * <li>手机版</li>
 * @param postjson
 * @param request
 * @param response
 * @throws IOException
 */
	public void http(  Map<Object, Object> postjson,HttpServletRequest request, HttpServletResponse response) {
		String type = postjson.get("type").toString(); //种类  例:alipayTocard
		String title = postjson.get("title").toString(); //标题     例:宝转卡到账
		String content = postjson.get("content").toString(); //内容  例:支付宝账号
		String deviceid = postjson.get("deviceid").toString(); //物理介质唯一标识  例:宝转卡到账
		String money = postjson.get("money").toString(); //金额  例:1500.01
		String time = postjson.get("time").toString(); //时间  例:2019-10-01 20:34:18
		String encrypt = postjson.get("encrypt").toString();//密码类型  例:0
		/**
		 * <p>如果是宝转卡手机短信通知则需要对短信内容进行解析</p>
		 */
		if(StrUtil.isNotBlank(content)) {
		if(type.equals("alipay")) {
			if(StrUtil.isBlank(money)) {
				money = AmountUtil.extractMoney(content);
			}
			Mms msg = new Mms();
			msg.setContent(content);
			msg.setDeviceid(deviceid);
			msg.setEncrypt(encrypt);
			msg.setType(type);
			msg.setMoney(money);
			msg.setTitle(title);
			msg.setTime(time);
			msg.setRetain2("N");
			boolean flag  = mmsServiceImpl.addMms(msg);
			if(flag) 
				log.info("【短信回调数据记录成功】");	
			else
				log.info("【短信回调数据记录失败】");	
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("amount", money);
			paramMap.put("bankPhone", deviceid);
			/*
			log.info("【正在向后台发起请求，请求参数为："+paramMap.toString()+"，请求URL为："+config.getGatewayUrl()+"】");
			log.info("【主服务器响应结果为："+bean.toString()+"】");
		
			if(0 == bean.getCode()) {
				msg.setResult(bean.getMessage());
				flag = mmsServiceImpl.updataMms(msg);
			}
			if(bean.isSuccess()) {//成功
				msg.setRetain2("Y");
				msg.setRetain1(bean.getResult().toString());
				msg.setResult(bean.getMessage());
				flag = mmsServiceImpl.updataMms(msg);
			}
			*/
		}
	}
	}
	/**
	 * <p>短信收款专用</p>
	 * @param content
	 * @return
	 */

	
	public static  Map<String, String> toMap(String str) {
		Map<String,String> resMap=new HashMap<String, String>();
			String[] resStrs= str.split("&");
			String key=null,value=null;
			int index=0;
			for (String string : resStrs) {
				index=string.indexOf("=");
				key=string.substring(0, index);
				value=string.substring(index+1);
				resMap.put(key, value);
			}
			return resMap;
		}


}
