package alipay.manage.api.channel.deal;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alipay.manage.api.config.PayOrderService;
import alipay.manage.api.feign.ConfigServiceClient;
import alipay.manage.bean.DealOrderApp;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import otc.bean.config.ConfigFile;
import otc.common.PayApiConstant;
import otc.result.Result;
@Component("HaoFuAlipayScan")
public class HaoFuAlipayScan  extends PayOrderService{
	private static final Log log = LogFactory.get();
	@Autowired ConfigServiceClient configServiceClientImpl;
	@Override
	public Result deal(DealOrderApp dealOrderApp, String payType) {
		log.info("【进入豪富支付宝个码支付】");
		String channelId = payType;//配置的渠道账号
		String create = create(dealOrderApp, channelId);
		if(StrUtil.isNotBlank(create)) {
			log.info("【本地订单创建成功，开始请求远程三方支付】");
			Result config = configServiceClientImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.SERVER_IP);
			log.info("【回调地址ip为："+config.toString()+"】" );
			String url = createOrder(config.getResult()+PayApiConstant.Notfiy.NOTFIY_API_WAI+"/haofu-notfiy", dealOrderApp.getOrderAmount(),create);
			if(StrUtil.isBlank(url)) {
				boolean orderEr = orderEr(dealOrderApp);
				if(orderEr)
					return Result.buildFailMessage("支付失败");
			}else {
				return Result.buildSuccessResultCode("支付处理中", url,1);
			}
		}
		return  Result.buildFailMessage("支付错误");
	}
	private String createOrder(String notfiy, BigDecimal orderAmount, String orderId) {
		Map<String,Object> map = new HashMap<String,Object>();
		String key  = "afdfasdf16541asdf51asd6f621sd";
		map.put("partner", "1014634188");
		map.put("amount", orderAmount.intValue());
		map.put("request_time", System.currentTimeMillis()/1000);
		map.put("trade_no", orderId);
		map.put("pay_type", "sm");
		map.put("notify_url",notfiy);
		String createParam = createParam(map);
		String md5 = md5(createParam+"&"+key);
		map.put("sign", md5);
		log.info("【当前豪富请求参数为："+map.toString()+"】");
		String post = HttpUtil.post("https://mmtnpkedtb.6785151.com/payCenter/aliPay2", map);
		log.info("【豪富响应参数为："+post+"】");
		JSONObject parseObj = JSONUtil.parseObj(post);
		Object object = parseObj.get("is_success");
		if(ObjectUtil.isNotNull(object)) {
			log.info("当前豪富的订单为："+object+"");
			if(object.equals("T")) {
				Object object2 = parseObj.get("result");
				if(ObjectUtil.isNotNull(object2)) {
					log.info("【支付链接为："+object2+"】");
					return object2.toString();
				}
			}
		}
		return "";
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
