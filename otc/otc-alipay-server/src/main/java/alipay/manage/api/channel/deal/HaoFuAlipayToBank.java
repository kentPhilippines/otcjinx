package alipay.manage.api.channel.deal;

import alipay.manage.api.channel.util.haofu.HaoFuUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component("HaoFuAlipayToBank")
public class HaoFuAlipayToBank extends PayOrderService {
	private static final Log log = LogFactory.get();
	@Autowired
	private UserInfoService userInfoServiceImpl;

	@Override
	public Result deal(DealOrderApp dealOrderApp, String payType) {
		log.info("【进入豪富支付宝转银行卡】");
		String channelId = payType;//配置的渠道账号
		String create = create(dealOrderApp, channelId);
		if (StrUtil.isNotBlank(create)) {
			log.info("【本地订单创建成功，开始请求远程三方支付】");
			UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
			if (StrUtil.isBlank(userInfo.getDealUrl())) {
				orderEr(dealOrderApp, "当前商户交易url未设置");
				return Result.buildFailMessage("请联系运营为您的商户号设置交易url");
			}
			log.info("【回调地址ip为：" + userInfo.getDealUrl() + "】");
			String url = createOrder(dealOrderApp, userInfo.getDealUrl() + PayApiConstant.Notfiy.NOTFIY_API_WAI + "/haofu-notfiy", dealOrderApp.getOrderAmount(), create);
			if (StrUtil.isBlank(url)) {
				log.info("【豪富支付宝转卡支付失败，订单号为：" + create + "】");
			} else {
				return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(url));
			}
		}
		return  Result.buildFailMessage("支付错误");
	}
	private String createOrder(DealOrderApp dealOrderApp,String notfiy, BigDecimal orderAmount, String orderId) {
		Map<String,Object> map = new HashMap<String,Object>();
		String key  = HaoFuUtil.KEY;
		map.put("partner", HaoFuUtil.APPID);
		map.put("amount", orderAmount.toString());
		map.put("request_time", System.currentTimeMillis()/1000);
		map.put("trade_no", orderId);
		map.put("type",  "ali");
		map.put("result_type",  "json");
		map.put("notify_url",notfiy);
		String createParam = createParam(map);
		String md5 = md5(createParam+"&"+key);
		map.put("sign", md5);
		System.out.println("【当前豪富请求参数为："+map.toString()+"】");
		String post = HttpUtil.post(HaoFuUtil.URL+"/payCenter/gatewaypay", map);
		System.out.println("【豪富响应参数为："+post+"】");
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
			}else{
				orderEr(dealOrderApp,  parseObj.get("fail_msg").toString());
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
