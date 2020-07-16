package alipay.manage.api.channel.notfiy;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import alipay.manage.api.channel.util.uzpay.UzPayUtil;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import otc.common.PayApiConstant;
import otc.result.Result;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class UzPayNotify extends NotfiyChannel{
	private static final Log log = LogFactory.get();
    @RequestMapping("/UZPAY-notfiy")
	 public String notify( 
			 String orderid,
			 @RequestBody String json,
			 HttpServletRequest request
			    )  {
    	log.info("【收到UzPay回调】");
    	String clientIP = HttpUtil.getClientIP(request);
		log.info("【当前回调ip为："+clientIP+"】");
		if(!clientIP.equals("52.193.97.128")) {
			log.info("【当前回调ip为："+clientIP+"，固定IP登记为："+"52.193.97.128"+"】");
			log.info("【当前回调ip不匹配】");
			return "ip errer";
		}
		JSONObject rtJson = JSONUtil.parseObj(json);
		if(null == rtJson) return "ERROR";
		orderid = rtJson.getStr("orderid");
		if(StrUtil.isBlank(orderid)) {
			log.info("【回调参数异常，订单号为获取到】");
			return "ERROR";
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("uid", UzPayUtil.UID);
		map.put("orderid", orderid);
		String createParam = UzPayUtil.createParam(map);
		log.info("签名串加密前参数为："+createParam);
		String md5 = UzPayUtil.md5(createParam+"&key="+UzPayUtil.KEY);
		map.put("sign", md5);
		String createParam2 = UzPayUtil.createParam(map);
		String post = HttpUtil.post(UzPayUtil.QUERY_URL, map);
		log.info("【UzPay订单查询结果为："+post+"  】");
		JSONObject parseObj = JSONUtil.parseObj(post) ;
		Object object = parseObj.get("order");
		log.info( ""+object);
		JSONObject parseObj2 = JSONUtil.parseObj(object) ;
		Object object2 = parseObj2.get("status");
		if(ObjectUtil.isNotNull(object2)) {
			 if("verified".equals(object2.toString())) {
				 Result dealpayNotfiy = dealpayNotfiy(orderid, clientIP, "UzPay回调订单成功");
				 if(dealpayNotfiy.isSuccess()) {
					 log.info("【订单回调修改成功，订单号为 ："+orderid+" 】");
				 }
			 }
		 }
		return clientIP;
    }
}
