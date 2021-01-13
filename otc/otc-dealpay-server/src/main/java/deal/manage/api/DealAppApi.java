package deal.manage.api;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import deal.manage.bean.UserRate;
import deal.manage.bean.Withdraw;
import deal.manage.bean.util.DealBean;
import deal.manage.bean.util.WithdrawalBean;
import deal.manage.service.WithdrawService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.api.alipay.Common;
import otc.api.alipay.Common.Order.DealOrderApp;
import otc.result.Result;
import otc.util.MapUtil;
import otc.util.number.Number;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>商户请求交易接口</p>
 *
 * @author hx08
 */
@RestController
@RequestMapping("/deal")
public class DealAppApi {
	@Autowired
	VendorRequestApi vendorRequestApi;
	Logger log = LoggerFactory.getLogger(DealAppApi.class);
    @Autowired AccountApiService accountApiServiceImpl;
    @Autowired WithdrawService withdrawServiceImpl;
	/**
	 * <p>下游商户交易接口</p>
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/pay")
	public Result dealAppPay(HttpServletRequest request) {
		Result pay = vendorRequestApi.pay(request);
		if (!pay.isSuccess()) {
			return pay;
		}
		Object result = pay.getResult();
		DealBean mapToBean = MapUtil.mapToBean((Map<String, Object>) result, DealBean.class);
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
		DealOrderApp dealBean = createDealAppOrder(mapToBean);
		if (ObjectUtil.isNull(dealBean)) {
			return Result.buildFailMessage("交易预订单生成出错");
		}
		Result deal = null;
		try {
			//		 deal = factoryForStrategy.getStrategy(passcode).deal(dealBean, passcode);
		} catch (Exception e) {
			log.info("【当前通道编码对于的实体类不存在】");
			return Result.buildFailMessage("当前通道编码不存在");
		}
		if (ObjectUtil.isNull(deal)) {
			return Result.buildFailMessage("其他错误，请联系客服人员处理");
		}
		return deal;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/wit")
	public Result witOrder(HttpServletRequest request) {
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
		Withdraw bean = createWit(wit, userRate, flag);
		Result deal = null;
		if (ObjectUtil.isNull(bean)) {
			return Result.buildFailMessage("代付订单生成失败");
		}
		try {
			//		 deal = factoryForStrategy.getStrategy(userRate.getPayTypr()).withdraw(bean);
		} catch (Exception e) {
			log.info("【当前通道编码对于的实体类不存在】");
			return Result.buildFailMessage("当前通道编码不存在");
		}
		if (ObjectUtil.isNull(deal)) {
			return Result.buildFailMessage("其他错误，请联系客服人员处理");
		}
		return deal;
	}
	/**
	 * <p>生成代付订单</p>
	 * @param wit
	 * @param userRate
	 * @param fla
	 * @return
	 */
	private Withdraw createWit(WithdrawalBean wit, UserRate userRate,Boolean fla ) {
		String type = "";
		if (fla) {
			type = Common.Order.Wit.WIT_TYPE_API;
		} else {
			type = Common.Order.Wit.WIT_TYPE_MANAGE;
		}
		Withdraw witb = new Withdraw();
		witb.setAmount(new BigDecimal(wit.getAmount()));
		witb.setFee(userRate.getFee());
		witb.setActualAmount(new BigDecimal(wit.getAmount()));
		witb.setMobile(wit.getMobile());
		witb.setBankNo(wit.getAcctno());
		witb.setBankName(wit.getAcctname());
		//	witb.setWithdrawType(Common.Order.Wit.WIT_ACC);
		witb.setOrderId(Number.getWitOrder());
		witb.setOrderStatus(Common.Order.DealOrderApp.ORDER_STATUS_DISPOSE.toString());
		witb.setNotify(wit.getNotifyurl());
		witb.setRetain2(wit.getIp());//代付ip
		witb.setRetain1(type);
		//	witb.setWitType(userRate.getPayTypr());//代付类型
		boolean flag = withdrawServiceImpl.addOrder(witb);
		if (flag) {
			return witb;
		}
		return null;
	}

	
	/**
	 * <p>生成充值订单</p>
	 * @param dealBean
	 * @return
	 */
	DealOrderApp createDealAppOrder(DealBean dealBean){
		 
		return null;
	}
}
