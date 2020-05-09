package alipay.manage.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import alipay.manage.bean.DealOrder;
import alipay.manage.util.BankTypeUtil;
import alipay.manage.util.OrderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import alipay.manage.api.channel.util.zhaunshi.BankEnum;
import alipay.manage.api.config.FactoryForStrategy;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.Product;
import alipay.manage.bean.UserRate;
import alipay.manage.bean.util.DealBean;
import alipay.manage.bean.util.WithdrawalBean;
import alipay.manage.mapper.ProductMapper;
import alipay.manage.service.OrderAppService;
import alipay.manage.service.WithdrawService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import otc.api.alipay.Common;
import otc.bean.dealpay.Withdraw;
import otc.result.Result;
import otc.util.MapUtil;
import otc.util.number.Number;

/**
 * <p>商户请求交易接口</p>
 * @author hx08
 *
 */
@RestController
@RequestMapping("/deal")
public class DealAppApi {
	@Autowired VendorRequestApi vendorRequestApi;
	Logger log = LoggerFactory.getLogger(DealAppApi.class);
	@Autowired FactoryForStrategy factoryForStrategy;
    @Autowired AccountApiService accountApiServiceImpl;
    @Autowired OrderAppService orderAppServiceImpl;
    @Autowired WithdrawService withdrawServiceImpl;
	@Autowired OrderUtil orderUtil;
	@Autowired ProductMapper productDao;
	/**
	 * <p>下游商户交易接口</p>
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/pay")
	public Result dealAppPay(HttpServletRequest request) {
		Result pay = vendorRequestApi.pay(request);
		if(!pay.isSuccess())
			return pay;
		Object result = pay.getResult();
		DealBean mapToBean = MapUtil.mapToBean((Map<String, Object>)result, DealBean.class);
		if(ObjectUtil.isNull(mapToBean))
			return Result.buildFailMessage("加密前格式错误，参数为空");
		String clientIP = HttpUtil.getClientIP(request);
		if(StrUtil.isNotBlank(clientIP))
			mapToBean.setIp(clientIP);
		log.info("【当前请求交易实体类："+mapToBean.toString()+"】");
		String passcode = mapToBean.getPassCode(); //通道支付编码
		if(StrUtil.isBlank(passcode))
			return Result.buildFailMessage("通道编码为空");
		log.info("【当前通道编码："+passcode+"】");
		DealOrderApp dealBean = createDealAppOrder(mapToBean);
		if(ObjectUtil.isNull(dealBean))
			return Result.buildFailMessage("交易预订单生成出错");
		Result deal = null;
		try {
			 deal = factoryForStrategy.getStrategy(passcode).deal(dealBean, passcode);
		} catch (Exception e) {
			log.info("【当前通道编码对于的实体类不存在】");
			return Result.buildFailMessage("当前通道编码不存在");
		}
		if(ObjectUtil.isNull(deal))
			return Result.buildFailMessage("其他错误，请联系客服人员处理");
		deal.setResult(new ResultDeal(true,0,deal.getCode(),deal.getResult()));
		return deal;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/wit")
	public Result witOrder(HttpServletRequest request) {
		String manage = request.getParameter("manage");
		boolean flag = false;
		if(StrUtil.isNotBlank(manage))
			flag = true;
		Result withdrawal = vendorRequestApi.withdrawal(request, flag);
		if(!withdrawal.isSuccess())
			return withdrawal;
		Object result = withdrawal.getResult();
		WithdrawalBean wit = MapUtil.mapToBean((Map<String, Object>)result, WithdrawalBean.class);
		wit.setIp(HttpUtil.getClientIP(request));
		UserRate userRate = accountApiServiceImpl.findUserRateWitByUserId(wit.getAppid());
		String dpaytype = wit.getDpaytype();
		if(StrUtil.isBlank(dpaytype)) {
			log.info("【必穿参数为空，当前商户："+wit.getAppid()+"，当前订单号:"+ wit.getApporderid()+"】");
			return Result.buildFailMessage("必传参数为空");
		}
		if(!isClick(dpaytype, userRate)) {
			log.info("【代付类型与当前费率类型符合，当前商户："+wit.getAppid()+"，当前订单号:"+ wit.getApporderid()+"】");
			return Result.buildFailMessage("代付类型与当前费率类型不符合，请检查费率类型与dpaytype传值是否符合，dpayType传值错误");
		}
		String bank2 = BankTypeUtil.getBank(wit.getBankcode());
		if(StrUtil.isBlank(bank2)) {
			log.info("【当前银行不支持代付，当前商户："+wit.getAppid()+"，当前订单号:"+ wit.getApporderid()+"】");
			return Result.buildFailMessage("bankcode错误，当前银行不支持合， bankcode传值错误");
		}
		Withdraw bean = createWit(wit,userRate,flag);
        Result deal = null;
        if(ObjectUtil.isNull(bean))
        	return Result.buildFailMessage("代付订单生成失败");
        try {
			 deal = factoryForStrategy.getStrategy(userRate.getPayTypr()).withdraw(bean);
		} catch (Exception e) {
			log.info("【当前通道编码对于的实体类不存在】");
			return Result.buildFailMessage("当前通道编码不存在");
		}
        if(ObjectUtil.isNull(deal))
			return Result.buildFailMessage("其他错误，请联系客服人员处理");
		return deal;
	}
	
	
	static final String BANK = "Bankcard",ALIPAY = "Alipay",WECHAR="Wechar";
	boolean isClick(String type ,UserRate rate){
		if(type.equals(BANK)) {
		List<Product> productList =	productDao.findCode(BANK);
		for( Product p  : productList) {
			if(p.getProductId().equals(rate.getPayTypr()))
				return true;
		}
		}else if(type.equals(ALIPAY)) {
			List<Product> productList =	productDao.findCode(ALIPAY);
			for( Product p  : productList) {
				if(p.getProductId().equals(rate.getPayTypr()))
					return true;
			}
		}else if(type.equals(ALIPAY)) {
			List<Product> productList =	productDao.findCode(WECHAR);
			for( Product p  : productList) {
				if(p.getProductId().equals(rate.getPayTypr()))
					return true;
			}
		}
		return false;
	}
	
//	/**
//	 *
//	 * @param request
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	@PostMapping("/check")
//	public Result checkOrder(HttpServletRequest request) {
//		String manage=request.getParameter("manage");
//		boolean flag = false;
//		if(StrUtil.isNotBlank(manage))
//			flag = true;
//		Result checkOrderl = vendorRequestApi.checkOrderl(request);
//		if(!checkOrderl.isSuccess())
//			return checkOrderl;
//		Object result = checkOrderl.getResult();
//		DealOrder order = MapUtil.mapToBean((Map<String, Object>)result,DealOrder.class);
//		if(order.getOrderStatus().equals("2")) {
//			//修改订单状态
//			orderAppServiceImpl.updateOrderSu(order.getOrderId(), order.getOrderStatus());
//		}else
//		    orderAppServiceImpl.updateOrderSu(order.getOrderId(),order.getOrderStatus());
//		return Result.buildSuccessResult();
//	}

	
	
	private Withdraw createWit(WithdrawalBean wit, UserRate userRate,Boolean fla ) {
		String type = "";
		if(fla)type=Common.Order.Wit.WIT_TYPE_API;else type = Common.Order.Wit.WIT_TYPE_MANAGE;
		Withdraw witb = new Withdraw();
		witb.setUserId(wit.getAppid());
		witb.setAmount(new BigDecimal(wit.getAmount()));
		witb.setFee(userRate.getFee());
		witb.setActualAmount(new BigDecimal(wit.getAmount()));
		witb.setMobile(wit.getMobile());
		witb.setBankNo(wit.getAcctno());
		witb.setAccname(wit.getAcctname());
		witb.setWithdrawType(Common.Order.Wit.WIT_ACC);
		witb.setOrderId(Number.getWitOrder());
		witb.setOrderStatus(Common.Order.DealOrderApp.ORDER_STATUS_DISPOSE.toString());
		witb.setNotify(wit.getNotifyurl());
		witb.setRetain2(wit.getIp());//代付ip
		witb.setRetain1(type);
		witb.setWitType(userRate.getPayTypr());//代付类型
		witb.setApply(wit.getApply());
		witb.setBankcode(wit.getBankcode());
		boolean flag = withdrawServiceImpl.addOrder(witb);
		if(flag)
			return witb;
		return null;
	}

	DealOrderApp createDealAppOrder(DealBean dealBean){
		DealOrderApp dealApp = new DealOrderApp ();
		dealApp.setAppOrderId(dealBean.getOrderId());
		dealApp.setOrderId(Number.getAppOreder()); 
		dealApp.setNotify(dealBean.getNotifyUrl());
		BigDecimal money = new BigDecimal(dealBean.getAmount());
		dealApp.setOrderAmount(money);
		String userId = dealBean.getAppId();
		String passcode = dealBean.getPassCode();
		UserRate userRate = accountApiServiceImpl.findUserRateByUserId(userId, passcode);
		dealApp.setFeeId(userRate.getId());
		dealApp.setOrderAccount(userId);
		if(StrUtil.isNotBlank(dealBean.getIp()))
			dealApp.setOrderIp(dealBean.getIp());
		dealApp.setBack(dealBean.getPageUrl());
		dealApp.setOrderStatus(Common.Order.DealOrder.ORDER_STATUS_DISPOSE.toString());
		dealApp.setOrderType(Common.Order.ORDER_TYPE_DEAL);
		dealApp.setDealDescribe("下游商户发起充值交易");
		dealApp.setRetain1(userRate.getPayTypr());
		BigDecimal fee = userRate.getFee();
		BigDecimal multiply = money.multiply(fee);
		dealApp.setRetain3(multiply.toString());
		boolean add = orderAppServiceImpl.add(dealApp);
		if(add)
			return dealApp;
		return null;
	}
}
class ResultDeal{
	private boolean sussess;	//是否成功	        True 成功  false  失败 
	private Integer cod;	//订单状态码【“0”为成功】	详情请查看响应状态码
	private Integer openType;//	打开方式	【1】为url打开方式【2】为html浏览器打开方式
	private String  returnUrl;	//支付内容
	
	public ResultDeal(boolean sussess, Integer cod, Integer openType, Object returnUrl) {
		super();
		this.sussess = sussess;
		this.cod = cod;
		this.openType = openType;
		this.returnUrl = returnUrl.toString();
	}
	public boolean isSussess() {
		return sussess;
	}
	public void setSussess(boolean sussess) {
		this.sussess = sussess;
	}
	public Integer getCod() {
		return cod;
	}
	public void setCod(Integer cod) {
		this.cod = cod;
	}
	public Integer getOpenType() {
		return openType;
	}
	public void setOpenType(Integer openType) {
		this.openType = openType;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
}
