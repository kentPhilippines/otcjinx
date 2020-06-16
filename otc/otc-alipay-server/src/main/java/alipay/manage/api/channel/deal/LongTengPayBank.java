package alipay.manage.api.channel.deal;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alipay.manage.api.channel.util.longteng.LongTentUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.api.feign.ConfigServiceClient;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.service.OrderService;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import otc.api.alipay.Common;
import otc.bean.config.ConfigFile;
import otc.common.PayApiConstant;
import otc.result.Result;
@Component("LongTengPayBank")
public class LongTengPayBank  extends PayOrderService{
	@Autowired ConfigServiceClient configServiceClientImpl;
	@Autowired OrderService orderServiceImpl;
	@Override
	public Result deal(DealOrderApp dealOrderApp, String channel) {
		log.info("【进入龙腾支付支付】");
		String create = create(dealOrderApp, channel);
		if(StrUtil.isNotBlank(create)) {
			Result config = configServiceClientImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.SERVER_IP);
			log.info("【回调地址ip为："+config.toString()+"】" );
		 /*	DealOrder order = orderServiceImpl.findOrderByOrderId(create);
			log.info("【本地订单创建成功，开始请求远程三方支付】");
			if(StrUtil.isBlank(order.getBack())) {
				log.info("【当前渠道须传递同步回调地址，当前订单号："+create+"】" );
				orderEr(dealOrderApp,"当前渠道产品须传递同步回调地址");
				return Result.buildFailMessage("支付失败");
			}
			*/
			String url = createOrder(config.getResult()+PayApiConstant.Notfiy.NOTFIY_API_WAI+"/longteng-notfiy", dealOrderApp.getOrderAmount(),create,  "");
			if(StrUtil.isBlank(url)) {
				log.info("【请求失败】");
				orderEr(dealOrderApp,"暂无支付渠道");
				boolean orderEr = orderEr(dealOrderApp);
				if(orderEr)
					return Result.buildFailMessage("支付失败");
			} else {
				return Result.buildSuccessResultCode("支付处理中", url,1);
			}
		}
		return  Result.buildFailMessage("支付错误");
	}
	private String createOrder(String notify, BigDecimal orderAmount, String orderId,String back) {
		Map<String, Object> headMap = new HashMap();
		String ltUrl = LongTentUtil.URL;
		String key = LongTentUtil.KEY;
		String sid = LongTentUtil.APPID;
		String timestamp = System.currentTimeMillis()+"";
		String nonce = StrUtil.uuid();
		String url = "/pay/qrorder";
		headMap.put("sid", sid);
		headMap.put("timestamp", timestamp);
		headMap.put("nonce", nonce);
		headMap.put("url", url);
		String head = LongTentUtil.createParam(headMap);
		Map<String, Object> bodyMap = new HashMap<String, Object>();
		String out_trade_no =  orderId;
		String channel = "8";
		String amount = orderAmount.intValue()+".00";
		String currency = "CNY";
		String notify_url = notify;
	    InetAddress address1 = null;
		try {
			address1 = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			log.info("IP 获取异常");
		}
        String ip = address1.getHostAddress();//获得本机IP
		bodyMap.put("out_trade_no", out_trade_no);
		bodyMap.put("channel", channel);
		bodyMap.put("amount", amount);
		bodyMap.put("currency", currency);
		bodyMap.put("notify_url", notify_url);
		bodyMap.put("return_url", notify_url);
		bodyMap.put("attach", notify_url);
		bodyMap.put("send_ip", ip);
		String body = LongTentUtil.createParam(bodyMap);
		String sign = head + body + key;
		log.info("【签名前加密参数："+sign+"】");
		String upperCase = LongTentUtil.md5(sign).toUpperCase();
		log.info(upperCase);
		String createParam = LongTentUtil.createParam1(bodyMap);
		log.info("【body参数数据："+createParam+"】");
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
		//{"code":"1000","msg":"success","pay_url":"https://qldk.womiya.com/index/pay?amount=100.00&chan_code=8&out_trade_no=w7m4692f6k&sign=1AF49E7860AC959597D52A0E66A5FCB5","trade_no":"B20200614140225614306","out_trade_no":"w7m4692f6k","sign":"C363AC02EC7D84BE4E65A400D48ECC9C"}
		JSONObject parseObj = JSONUtil.parseObj(result2);
		if(parseObj.get("code").equals("1000")) 
			return parseObj.get("pay_url").toString();
 		return null;
	}

	
}
