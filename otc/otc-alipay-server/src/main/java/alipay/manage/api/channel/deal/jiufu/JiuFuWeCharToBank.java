package alipay.manage.api.channel.deal.jiufu;

import alipay.manage.api.channel.util.jiufu.JiUFuUtil;
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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component("JiuFuWeCharToBank")
public class JiuFuWeCharToBank extends PayOrderService {
	private static final Log log = LogFactory.get();
	@Autowired
	private UserInfoService userInfoServiceImpl;

	@Override
	public Result deal(DealOrderApp dealOrderApp, String channel) {
		log.info("【进入玖富支付宝转卡支付】");
		String create = create(dealOrderApp, channel);
		if (StrUtil.isNotBlank(create)) {
			log.info("【本地订单创建成功，开始请求远程三方支付】");
			UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
			if (StrUtil.isBlank(userInfo.getDealUrl())) {
				orderEr(dealOrderApp, "当前商户交易url未设置");
				return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
			}
			log.info("【回调地址ip为：" + userInfo.getDealUrl() + "】");
			String url = createOrder(userInfo.getDealUrl() + PayApiConstant.Notfiy.NOTFIY_API_WAI + "/jiufu-notfiy", dealOrderApp.getOrderAmount(), create);
			if (StrUtil.isBlank(url)) {
				log.info("【请求失败】");
				orderEr(dealOrderApp, "暂无支付渠道");
				boolean orderEr = orderEr(dealOrderApp);
				if (orderEr)
					return Result.buildFailMessage("支付失败");
			} else {
				return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(url));
			}
		}
		return  Result.buildFailMessage("支付错误");
	}
	private String createOrder(String notify, BigDecimal orderAmount, String orderId) {
		String partner = JiUFuUtil.APPID;
		String service = JiUFuUtil.SERVER_ALIAPY_BANK_H5;//"10108" 宝转卡    10101 网关支付    10114
		String tradeNo = orderId;
		String amount = orderAmount.intValue()+"";
		String notifyUrl = notify;
		String extra = "abc";
		String resultType = "json";
		String sign = "";
		String key = JiUFuUtil.KEY;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("partner", partner);
		map.put("service", service);
		map.put("tradeNo", tradeNo);
		map.put("amount", amount);
		map.put("notifyUrl", notifyUrl);
		map.put("resultType", resultType);
		map.put("extra", extra);
		String createParam = JiUFuUtil.createParam(map);
		map.put("sign", JiUFuUtil.md5(createParam+"&"+key));
		log.info("【玖富请求前参数："+map.toString()+"】");
		String post = HttpUtil.post(JiUFuUtil.URL, map);
		log.info("【玖富返回参数："+post.toString()+"】");
		JSONObject parseObj = JSONUtil.parseObj(post);
		Object object = parseObj.get("isSuccess");
		if(ObjectUtil.isNotNull(object)) {
			log.info("【当前玖富的订单为："+object+"】");
			if(object.equals("T")) {
				Object object2 = parseObj.get("url");
				if(ObjectUtil.isNotNull(object2)) {
					log.info("【支付链接为："+object2+"】");
					return object2.toString();
				}
			}
		}
		return null;
	}
}
