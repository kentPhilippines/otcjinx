package alipay.manage.api.channel.deal;

import alipay.manage.api.channel.util.uzpay.UzPayUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component("uzpayBank")
public class UzPay extends PayOrderService {
	private static final Log log = LogFactory.get();
	@Autowired
	private UserInfoService userInfoServiceImpl;

	@Override
	public Result deal(DealOrderApp dealOrderApp, String channnl) {
		log.info("【进入UZPAY银行卡转卡】");
		String orderId = create(dealOrderApp, channnl);
		if (StrUtil.isNotBlank(orderId)) {
			log.info("【本地订单创建成功，开始请求远程三方支付】");
			UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
			if (StrUtil.isBlank(userInfo.getDealUrl())) {
				orderEr(dealOrderApp, "当前商户交易url未设置");
				return Result.buildFailMessage("请联系运营为您的商户号设置交易url");
			}
			log.info("【回调地址ip为：" + userInfo.getDealUrl() + "】");
			String url = createOrder(userInfo.getDealUrl() + PayApiConstant.Notfiy.NOTFIY_API_WAI + "/UZPAY-notfiy", dealOrderApp.getOrderAmount(), orderId);
			if (StrUtil.isNotBlank(url)) {
				log.info("【UZPAY银行卡转卡请求参数：" + url + "】");
				return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(url));

			}
		}
		return Result.buildFailMessage("支付失败");
	}
	private String createOrder(String notify, BigDecimal orderAmount, String orderId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("uid", UzPayUtil.UID);
		map.put("userid", UzPayUtil.USERID);
		map.put("amount", orderAmount);
		map.put("orderid", orderId);
		map.put("userip", "182.16.89.146");
		map.put("notify", notify);
		String createParam = UzPayUtil.createParam(map);
		System.out.println("签名串加密前参数为："+createParam);
		String md5 = UzPayUtil.md5(createParam+"&key="+UzPayUtil.KEY);
		map.put("sign", md5);
		String createParam2 = UzPayUtil.createParam(map);
		String a  = UzPayUtil.URL + "?"+createParam2;
		return a;
	}
}
