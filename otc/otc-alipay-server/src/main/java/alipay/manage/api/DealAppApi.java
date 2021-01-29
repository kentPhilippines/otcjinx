package alipay.manage.api;

import alipay.config.redis.RedisLockUtil;
import alipay.manage.api.config.FactoryForStrategy;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.*;
import alipay.manage.bean.util.DealBean;
import alipay.manage.bean.util.Fund;
import alipay.manage.bean.util.FundBean;
import alipay.manage.bean.util.WithdrawalBean;
import alipay.manage.mapper.ChannelFeeMapper;
import alipay.manage.service.ExceptionOrderService;
import alipay.manage.service.OrderAppService;
import alipay.manage.service.UserInfoService;
import alipay.manage.service.WithdrawService;
import alipay.manage.util.BankTypeUtil;
import alipay.manage.util.CheckUtils;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.api.alipay.Common;
import otc.bean.dealpay.Withdraw;
import otc.result.Result;
import otc.util.MapUtil;
import otc.util.number.Number;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>商户请求交易接口</p>
 *
 * @author hx08
 */
@RestController
@RequestMapping("/deal")
public class DealAppApi extends PayOrderService {
    @Autowired VendorRequestApi vendorRequestApi;
    Logger log = LoggerFactory.getLogger(DealAppApi.class);
    @Autowired private FactoryForStrategy factoryForStrategy;
    @Autowired private AccountApiService accountApiServiceImpl;
    @Autowired private OrderAppService orderAppServiceImpl;
    @Autowired private WithdrawService withdrawServiceImpl;
    @Autowired private UserInfoService userInfoServiceImpl;
    @Resource private ChannelFeeMapper channelFeeDao;
	@Autowired private ExceptionOrderService exceptionOrderServiceImpl;
    @RequestMapping("/findFund")
    public Result findFund(HttpServletRequest request) {
		String appId = request.getParameter("appId");
		String sign = request.getParameter("sign");
		if (StrUtil.isBlank(appId) || StrUtil.isBlank(sign)) {
			return Result.buildFailMessage("必传参数为空");
		}
		UserInfo userInfo = userInfoServiceImpl.findPassword(appId);
		if (ObjectUtil.isNull(userInfo)) {
			return Result.buildFailMessage("商户不存在");
		}
		Map<String, Object> map = new ConcurrentHashMap<String, Object>(5);
		map.put("appId", appId);
		map.put("sign", sign);
		boolean verifySign = CheckUtils.verifySign(map, userInfo.getPayPasword());
		map = null;
		if (!verifySign) {
			return Result.buildFailMessage("签名错误");
		}
		UserFund fund = userInfoServiceImpl.findBalace(appId);
		if (ObjectUtil.isNull(fund)) {
			log.info("【当前查询的商户号不存在，请核实，商户号为：" + appId + "】");
			return Result.buildFailMessage("当前查询的订单不存在，请核实");
		}
		Map<String, Object> mapr = new ConcurrentHashMap<String, Object>();
		mapr.put("userId", fund.getUserId());
		mapr.put("userName", fund.getUserName());
		mapr.put("balance", fund.getAccountBalance());
		String sign2 = CheckUtils.getSign(mapr, userInfo.getPayPasword());
		userInfo = null;
		mapr = null;
		Fund fundInfo = new Fund();
		fundInfo.setBalance(fund.getAccountBalance());
		fundInfo.setSign(sign2);
		fundInfo.setUserId(fund.getUserId());
		fundInfo.setUserName(fund.getUserName());
		return Result.buildSuccessResult(fundInfo);
	}
	@RequestMapping("/findOrder")
	public Result findOrder(HttpServletRequest request) {
		String appId = request.getParameter("appId");
		String appOrderId = request.getParameter("appOrderId");
		String type = request.getParameter("type");
		String sign = request.getParameter("sign");
		if(StrUtil.isBlank(type) ) {
			if (StrUtil.isBlank(appId) || StrUtil.isBlank(appOrderId) || StrUtil.isBlank(sign)) {
				return Result.buildFailMessage("必传参数为空");
			}
			UserInfo userInfo = userInfoServiceImpl.findPassword(appId);
			if (ObjectUtil.isNull(userInfo)) {
				return Result.buildFailMessage("商户不存在");
			}
			Map<String, Object> map = new ConcurrentHashMap<String, Object>();
			if (StrUtil.isBlank(type)) {
				type = null;
			}
			map.put("appId", appId);
			map.put("appOrderId", appOrderId);
			map.put("sign", sign);
			boolean verifySign = CheckUtils.verifySign(map, userInfo.getPayPasword());
			map = null;
			if (!verifySign) {
				return Result.buildFailMessage("签名错误");
			}
			DealOrderApp orderApp = orderAppServiceImpl.findOrderByApp(appId, appOrderId);
			Map<String, Object> mapr = new ConcurrentHashMap<String, Object>();
			mapr.put("appId", appId);
			mapr.put("appOrderId", orderApp.getAppOrderId());
			mapr.put("amount", orderApp.getOrderAmount());
			mapr.put("orderStatus", orderApp.getOrderStatus());
			String sign2 = CheckUtils.getSign(mapr, userInfo.getPayPasword());
			userInfo = null;
			mapr = null;
			FundBean fund = new FundBean();
			fund.setAmount(orderApp.getOrderAmount().toString());
			fund.setOrderId(orderApp.getAppOrderId());
			fund.setOrderStatus(orderApp.getOrderStatus());
			fund.setSign(sign2);
			return Result.buildSuccessResult(fund);
		}
		if("pay".equals(type) ) {
			if (StrUtil.isBlank(appId) || StrUtil.isBlank(appOrderId) || StrUtil.isBlank(sign)) {
				return Result.buildFailMessage("必传参数为空");
			}
			UserInfo userInfo = userInfoServiceImpl.findPassword(appId);
			if (ObjectUtil.isNull(userInfo)) {
				return Result.buildFailMessage("商户不存在");
			}
			Map<String, Object> map = new ConcurrentHashMap<String, Object>();
			if (StrUtil.isBlank(type)) {
				type = null;
			}
			map.put("appId", appId);
			map.put("appOrderId", appOrderId);
			map.put("type", type);
			map.put("sign", sign);
			boolean verifySign = CheckUtils.verifySign(map, userInfo.getPayPasword());
			map = null;
			if (!verifySign) {
				return Result.buildFailMessage("签名错误");
			}
			DealOrderApp orderApp = orderAppServiceImpl.findOrderByApp(appId, appOrderId);
			if (ObjectUtil.isNull(orderApp)) {
				log.info("【当前查询的订单不存在，请核实，订单号为：" + appOrderId + "】");
				return Result.buildFailMessage("当前查询的订单不存在，请核实");
			}
			Map<String, Object> mapr = new ConcurrentHashMap<String, Object>();
			mapr.put("appId", appId);
			mapr.put("appOrderId", orderApp.getAppOrderId());
			mapr.put("amount", orderApp.getOrderAmount());
			mapr.put("orderStatus", orderApp.getOrderStatus());
			String sign2 = CheckUtils.getSign(mapr, userInfo.getPayPasword());
			userInfo = null;
			mapr = null;
			FundBean fund = new FundBean();
			fund.setAmount(orderApp.getOrderAmount().toString());
			fund.setOrderId(orderApp.getAppOrderId());
			fund.setOrderStatus(orderApp.getOrderStatus());
			fund.setSign(sign2);
			return Result.buildSuccessResult(fund);
		}
		if("wit".equals(type)) {
			if (StrUtil.isBlank(appId) || StrUtil.isBlank(appOrderId) || StrUtil.isBlank(sign)) {
				return Result.buildFailMessage("必传参数为空");
			}
			UserInfo userInfo = userInfoServiceImpl.findPassword(appId);
			if (ObjectUtil.isNull(userInfo)) {
				return Result.buildFailMessage("商户不存在");
			}
			Map<String, Object> map = new ConcurrentHashMap<String, Object>();
			if (StrUtil.isBlank(type)) {
				type = null;
			}
			map.put("appId", appId);
			map.put("appOrderId", appOrderId);
			map.put("type", type);
			map.put("sign", sign);
			boolean verifySign = CheckUtils.verifySign(map, userInfo.getPayPasword());
			map = null;
			if (!verifySign) {
				return Result.buildFailMessage("签名错误");
			}
			log.info("【当前进入代付订单查询，订单号为：" + appOrderId + "】");
			Withdraw witb = withdrawServiceImpl.findOrderByApp(appId, appOrderId);
			if (ObjectUtil.isNull(witb)) {
				log.info("【当前查询的订单不存在，请核实，订单号为：" + appOrderId + "】");
				return Result.buildFailMessage("当前查询的订单不存在，请核实");
			}
			String clientIP = HttpUtil.getClientIP(request);
			if (StrUtil.isBlank(clientIP)) {
				return Result.buildFailMessage("未获取到代付查询ip");
			}
			Map<String, Object> mapr = new ConcurrentHashMap<String, Object>();
			String commect = StrUtil.isBlank(witb.getComment()) ? "代付订单" : witb.getComment();
			mapr.put("appId", appId);
			mapr.put("appOrderId", witb.getAppOrderId());
			mapr.put("amount", witb.getAmount());
			mapr.put("orderStatus", witb.getOrderStatus());
			mapr.put("msg", commect);
			String sign2 = CheckUtils.getSign(mapr, userInfo.getPayPasword());
			userInfo = null;
			mapr = null;
			FundBean fund = new FundBean();
			fund.setAmount(witb.getAmount().toString());
			fund.setOrderId(witb.getAppOrderId());
			fund.setOrderStatus(witb.getOrderStatus());
			//fund.setMsg(commect);
			fund.setSign(sign2);
			return Result.buildSuccessResult(fund);
		}
		return Result.buildFailMessage("查询失败");
	}
	/**
	 * <p>下游商户交易接口</p>
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/pay")
	public Result dealAppPay(HttpServletRequest request) {
		if (ObjectUtil.isNull(request.getParameter("userId"))) {
			log.info("当前传参，参数格式错误");
			return Result.buildFailMessage("当前传参，参数格式错误，请使用[application/x-www-form-urlencoded]表单格式传参");
		}
		Result pay = vendorRequestApi.pay(request);
		if (!pay.isSuccess()) {
			return pay;
		}
		Object result = pay.getResult();
		DealBean mapToBean = MapUtil.mapToBean((Map<String, Object>) result, DealBean.class);
		if (ObjectUtil.isNull(mapToBean)) {
			return Result.buildFailMessage("加密前格式错误，参数为空");
		}
		String clientIP = HttpUtil.getClientIP(request);
		if (StrUtil.isNotBlank(clientIP)) {
			mapToBean.setIp(clientIP);
		}
		log.info("【当前请求交易实体类：" + mapToBean.toString() + "】");
		String passcode = mapToBean.getPassCode(); //通道支付编码
		if (StrUtil.isBlank(passcode)) {
			return Result.buildFailMessage("通道编码为空");
		}
		log.info("【当前通道编码：" + passcode + "】");
		UserRate userRate = null;
		ChannelFee channelFee = null;
		try {
			userRate = accountApiServiceImpl.findUserRateByUserId(mapToBean.getAppId(), passcode, mapToBean.getAmount());
			channelFee = channelFeeDao.findImpl(userRate.getChannelId(), userRate.getPayTypr());
		} catch (Exception e) {
			exceptionOrderServiceImpl.addDealOrder(mapToBean, "用户报错：当前通道编码有误，产品类型设置重复；处理方法：当前配置用户产品的时候配置用户产品重复", clientIP);
			log.info("【当前通道编码设置有误，产品类型设置重复：" + e.getMessage() + "】");
			return Result.buildFailMessage("当前通道编码设置有误，产品类型设置重复");
        }
        if (ObjectUtil.isNull(channelFee)) {
            log.info("【通道实体不存在，当前商户订单号：" + mapToBean.getOrderId() + "】");
			log.info("【通道实体不存在，费率配置错误】");
			exceptionOrderServiceImpl.addDealOrder(mapToBean, "用户报错：通道实体不存在；处理方法：渠道费率未设置", clientIP);
			return Result.buildFailMessage("通道实体不存在，费率配置错误");
		}
		DealOrderApp orderApp = orderAppServiceImpl.findOrderByApp(mapToBean.getAppId(), mapToBean.getOrderId());
		if (ObjectUtil.isNotNull(orderApp)) {
			log.info("【当前商户订单号重复：" + mapToBean.getOrderId() + "】");
			exceptionOrderServiceImpl.addDealOrder(mapToBean, "用户报错：商户订单号重复；处理方法：提醒用户换一个订单号提交支付请求", clientIP);
			return Result.buildFailMessage("商户订单号重复");
		}
		DealOrderApp dealBean = createDealAppOrder(mapToBean, userRate);
		if (ObjectUtil.isNull(dealBean)) {
			exceptionOrderServiceImpl.addDealOrder(mapToBean, "用户报错：交易预订单生成出错；处理方法：让商户重新发起支付提交请求，或联系技术人员处理", clientIP);
			return Result.buildFailMessage("交易预订单生成出错");
		}
		Result deal = null;
		try {
			deal = factoryForStrategy.getStrategy(channelFee.getImpl()).deal(dealBean, channelFee.getChannelId());
		} catch (Exception e) {
			log.info("【当前通道编码对于的实体类不存在：" + e.getMessage() + "】", e);
			exceptionOrderServiceImpl.addDealOrder(mapToBean, "用户报错：当前通道编码不存在；处理方法：生成交易订单时候出现错误，或者请求三方渠道支付请求的时候出现异常返回，或联系技术人员处理," +
					"三方渠道报错信息：" + e.getMessage(), clientIP);
			return Result.buildFailMessage("当前通道编码不存在");
		}
		/*if (deal.isSuccess())
			deal.setResult(new ResultDeal(true, 0, deal.getCode(), deal.getResult()));*/
		return deal;
	}

	@Autowired
	private RedisLockUtil redisLockUtil;

	@SuppressWarnings("unchecked")
	@PostMapping("/wit")
	public Result witOrder(HttpServletRequest request) {
		if (ObjectUtil.isNull(request.getParameter("userId"))) {
			log.info("当前传参，参数格式错误");
			return Result.buildFailMessage("当前传参，参数格式错误，请使用[application/x-www-form-urlencoded]表单格式传参");
		}
		String lock = this.getClass().getName() + "witOrder" + request.getParameter("userId");
		redisLockUtil.redisLock(lock);
		String manage = request.getParameter("manage");
		boolean flag = false;
		if (StrUtil.isNotBlank(manage)) {
			flag = true;
		}
		Result withdrawal = vendorRequestApi.withdrawal(request, flag);
		if (!withdrawal.isSuccess()) {
			return withdrawal;
		}
		Object result = withdrawal.getResult();
		WithdrawalBean wit = MapUtil.mapToBean((Map<String, Object>) result, WithdrawalBean.class);
		wit.setIp(HttpUtil.getClientIP(request));
		UserRate userRate = accountApiServiceImpl.findUserRateWitByUserId(wit.getAppid());
		UserInfo userInfo = accountApiServiceImpl.findUserInfo(wit.getAppid());
		String dpaytype = wit.getDpaytype();
		ChannelFee channelFee = channelFeeDao.findImpl(userRate.getChannelId(), userRate.getPayTypr());
		if (ObjectUtil.isNull(channelFee)) {
			log.info("【通道实体不存在，费率配置错误】");
			exceptionOrderServiceImpl.addWitOrder(wit, "用户报错：通道实体不存在，费率配置错误；处理方法：请检查商户提交的通道编码，反复确认", HttpUtil.getClientIP(request));
			Result.buildFailMessage("通道实体不存在，费率配置错误");
		}
		String bankcode = BankTypeUtil.getBank(wit.getBankcode());
		if (StrUtil.isBlank(bankcode)) {
			log.info("【当前银行卡类型不支持】");
			log.info("【当前银行不支持代付，当前商户：" + wit.getAppid() + "，当前订单号:" + wit.getApporderid() + "】");
			exceptionOrderServiceImpl.addWitOrder(wit, "用户报错：当前银行不支持合， 银行code值错误；处理方法：请商户检查提交的银行卡code是否正确", HttpUtil.getClientIP(request));
			return Result.buildFailMessage("当前银行不支持合， 银行code值错误");
		}
		Withdraw witb = withdrawServiceImpl.findOrderByApp(wit.getAppid(), wit.getApporderid());
		if (ObjectUtil.isNotNull(witb)) {
			log.info("【当前商户订单号重复：" + wit.getApporderid() + "】");
			exceptionOrderServiceImpl.addWitOrder(wit, "用户报错：商户订单号重复；处理方法：提醒用户换一个订单号提交代付请求请求", HttpUtil.getClientIP(request));
			return Result.buildFailMessage("商户订单号重复");
		}
		Withdraw bean = createWit(wit, userRate, flag, channelFee);
		Result deal = null;
		if (ObjectUtil.isNull(bean)) {
			return Result.buildFailMessage("代付订单生成失败");
		}
		try {
			Integer autoWit = userInfo.getAutoWit();
			if (1 == autoWit) {
				//自动推送
				Result withdraw = super.withdraw(bean);
				if (withdraw.isSuccess()) {
					deal = factoryForStrategy.getStrategy(channelFee.getImpl()).withdraw(bean);
				} else {
					withdrawServiceImpl.updateWitError(bean.getOrderId());
					return Result.buildFailMessage("代付失败，当前排队爆满，请再次发起代付");
				}
			} else {
                //手动处理
				deal = super.withdraw(bean);
			}
		} catch (Exception e) {
			log.error("[代码执行时候出现错误]", e);
			//		super.withdrawEr(bean, "系统异常，请联系技术人员处理", HttpUtil.getClientIP(request));
			exceptionOrderServiceImpl.addWitOrder(wit, "用户报错：当前通道编码不存在；处理方法：提交技术人员处理，报错信息：" + e.getMessage(), HttpUtil.getClientIP(request));
			log.info("【当前通道编码对于的实体类不存在】");
			withdrawServiceImpl.updateWitError(bean.getOrderId());
			redisLockUtil.unLock(lock);
			return Result.buildFailMessage("当前通道编码不存在");
		}
		redisLockUtil.unLock(lock);
		return deal;
	}
	private Withdraw createWit(WithdrawalBean wit, UserRate userRate,Boolean fla, ChannelFee channelFee ) {
		log.info("【当前转换参数 代付实体类为：" + wit.toString() + "】");
		String type = "";
		String bankName = "";
		if (fla) {
			type = Common.Order.Wit.WIT_TYPE_API;
		} else {
			type = Common.Order.Wit.WIT_TYPE_MANAGE;
		}
		Withdraw witb = new Withdraw();
		witb.setUserId(wit.getAppid());
		witb.setAmount(new BigDecimal(wit.getAmount()));
		witb.setFee(userRate.getFee());
		witb.setActualAmount(new BigDecimal(wit.getAmount()));
		witb.setMobile(wit.getMobile());
		witb.setBankNo(wit.getAcctno());
		witb.setAccname(wit.getAcctname());
		bankName = wit.getBankName();
		if (StrUtil.isBlank(bankName)) {
			bankName = BankTypeUtil.getBankName(wit.getBankcode());
		}
		witb.setBankName(bankName);
		witb.setWithdrawType(Common.Order.Wit.WIT_ACC);
		witb.setOrderId(Number.getWitOrder());
		witb.setOrderStatus(Common.Order.DealOrderApp.ORDER_STATUS_DISPOSE.toString());
		witb.setNotify(wit.getNotifyurl());
		witb.setRetain2(wit.getIp());//代付ip
		witb.setAppOrderId(wit.getApporderid());
		witb.setRetain1(type);
		witb.setWitType(userRate.getPayTypr());//代付类型
		witb.setApply(wit.getApply());
		witb.setBankcode(wit.getBankcode());
		witb.setWitChannel(channelFee.getChannelId());
		boolean flag = withdrawServiceImpl.addOrder(witb);
		if (flag) {
			return witb;
		}
		return null;
	}

	DealOrderApp createDealAppOrder(DealBean dealBean, UserRate userRate) {
		DealOrderApp dealApp = new DealOrderApp();
		dealApp.setAppOrderId(dealBean.getOrderId());
		dealApp.setOrderId(Number.getAppOreder());
		dealApp.setNotify(dealBean.getNotifyUrl());
		dealApp.setOrderAmount(new BigDecimal(dealBean.getAmount()));
		String userId = dealBean.getAppId();
		String passcode = dealBean.getPassCode();
		dealApp.setFeeId(userRate.getId());
		dealApp.setOrderAccount(userId);
		if (StrUtil.isNotBlank(dealBean.getIp())) {
			dealApp.setOrderIp(dealBean.getIp());
		}
		dealApp.setBack(dealBean.getPageUrl());
		dealApp.setOrderStatus(Common.Order.DealOrder.ORDER_STATUS_DISPOSE.toString());
		dealApp.setOrderType(Common.Order.ORDER_TYPE_DEAL);
		dealApp.setDealDescribe("下游商户发起充值交易");
		dealApp.setRetain1(userRate.getPayTypr());
		dealApp.setRetain3(userRate.getFee().multiply(new BigDecimal(dealBean.getAmount())).toString());
		boolean add = orderAppServiceImpl.add(dealApp);
		if (add) {
			return dealApp;
		}
		return null;
	}
}
