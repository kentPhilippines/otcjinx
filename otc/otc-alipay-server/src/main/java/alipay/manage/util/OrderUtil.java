package alipay.manage.util;

import alipay.manage.bean.*;
import alipay.manage.mapper.*;
import alipay.manage.service.CorrelationService;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import alipay.manage.service.WithdrawService;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import otc.api.alipay.Common;
import otc.bean.alipay.OrderDealStatus;
import otc.bean.dealpay.Recharge;
import otc.bean.dealpay.Withdraw;
import otc.exception.order.OrderException;
import otc.result.Result;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class OrderUtil {
	Logger log = LoggerFactory.getLogger(OrderUtil.class);
	@Autowired private OrderService orderServiceImpl;
	@Autowired private AmountUtil amountUtil;
	@Autowired private AmountRunUtil amountRunUtil;
	@Autowired private UserInfoService userInfoServiceImpl;
	@Resource private RechargeMapper rechargeDao;
	@Resource private WithdrawMapper withdrawDao;
	@Resource private DealOrderAppMapper dealOrderAppDao;
	@Resource private UserRateMapper userRateDao;
	@Autowired private RiskUtil riskUtil;
	@Autowired private CorrelationService correlationServiceImpl;
	/**
	 * <p>充值订单置为成功</p>
	 * @param orderId			订单号
	 * @return
	 */
	public Result rechargeOrderEr(String orderId) {
		if(StrUtil.isBlank(orderId))
			return Result.buildFailMessage("必传参数为空");
		Recharge order = rechargeDao.findRechargeOrder(orderId);
		return rechargeOrderEr(order);
	}

	/**
	 * <p>充值充值订单成功【自动回调】</p>
	 * @param orderIds			订单号
	 * @return
	 */
	public Result rechargeOrderSu(String orderIds) {
		if(StrUtil.isBlank(orderIds))
			return Result.buildFailMessage("必传参数为空");
		return rechargeOrderSu(orderIds,false);
	}

	/**
	 * <p>充值订单人工置为成功</p>
	 *
	 * @param orderIds
	 * @return
	 */
	public Result rechargeSu(String orderIds) {
		if (StrUtil.isBlank(orderIds))
			return Result.buildFailMessage("必传参数为空");
		return rechargeOrderSu(orderIds, true);
	}

	@Resource ChannelFeeMapper channelFeeDao;


	/**
	 * <p>代付订单置为失败【这里只能是人工操作】</p>
	 *
	 * @param orderId 这里只能是人工操作
	 * @param ip      操作 ip
	 * @return
	 */
	@Transactional
	public Result withrawOrderEr(String orderId, String approval, String comment, String ip) {
		Withdraw order = withdrawDao.findWitOrder(orderId);
		if (order == null) {
			return Result.buildFailMessage("平台订单号不存在");
		}
		/*if (!Common.Order.Wit.ORDER_STATUS_YU.equals(order.getOrderStatus())) {
			return Result.buildFailMessage("订单已被处理，不允许操作");
		 	2020-10-04 增加任意状态下都可将代付订单处理为失败，故注释该代码
		}*/
		order.setApproval(approval);
		order.setComment(comment);
		return withrawOrderEr(order,ip);
	}


	/**
	 * <p>码商充值订单置为成功</p>
	 * @param orderId
	 * @param flag
	 * @return
	 */
	public Result rechargeOrderSu(String orderId ,boolean flag) {
		if(StrUtil.isBlank(orderId))
			return Result.buildFailMessage("必传参数为空");
		Recharge order = rechargeDao.findRechargeOrder(orderId);
		return rechargeOrderSu(order,flag);
	}


	/**
	 * <p>【码商交易订单】系统成功调用方法</p>
	 * @param orderId				订单号
	 * @param ip					ip
	 * @return
	 */
	public Result orderDealSu(String orderId ,String ip) {
		Result orderDealSu = orderDealSu(orderId, ip,false ,null);
		return orderDealSu;
	}
	/**
	 * <p>【码商交易订单】人工成功调用方法</p>
	 * @param orderId				交易订单号
	 * @param ip					ip
	 * @param userId				操作人
	 * @return
	 */
	public Result orderDealSu(String orderId ,String ip ,String userId) {
		if(StrUtil.isBlank(userId))
			return Result.buildFailMessage("当前必传参数为空，请传递操作人ID");
		return orderDealSu(orderId, ip,true ,userId);
	}
	/**
	 * <p>【码商交易订单】人工调用失败的方法</p>
	 * @param orderId
	 * @param mag
	 * @param ip
	 * @return
	 */
	public Result orderDealEr(String orderId,String mag ,String ip) {
		log.info("调用订单失败的方法");
		if(StrUtil.isBlank(mag))
			return Result.buildFailMessage("请填写失败理由");
		Result updateDealOrderEr = updateDealOrderEr(orderId, mag, ip);
		return updateDealOrderEr;
	}


	/**
	 * ###############################################################
	 * 【以下方法 不对外开放】
	 * ###############################################################
	 */
	/**
	 * <p>交易订单成功</p>
	 * @param orderId				交易订单
	 * @param ip					交易ip
	 * @param flag					true 自然流水     false  人工流水
	 * @return
	 */
	public Result orderDealSu(String orderId ,String ip ,boolean flag ,String userId) {
		log.info("【调用码商交易订单为成功的方法】");
		Result updataDealOrderSu = updataDealOrderSu(orderId, flag?"人工置交易订单为成功，操作人为："+userId+"":"系统置交易订单为成功", ip, flag);
		log.info("【返回结果："+updataDealOrderSu.toString()+"】");
		return updataDealOrderSu;
	}
	/**
	 * <p>订单置为成功的方法</p>
	 * ##############################
	 * 1，查询当前订单是否是成功状态<p>若为成功则禁止修改</p>
	 * 2，修改当前订单为成功
	 * 3，根据订单类型处理不同订单类型的资金处理
	 * 4，根据不同的类型处理不同资金的流水类型
	 * 5，订单修改完毕
	 */
	static Lock lock = new  ReentrantLock();
	@Transactional
	public  Result updataDealOrderSu(String orderId,String msg , String ip ,Boolean flag) {
		if (StrUtil.isBlank(orderId) || StrUtil.isBlank(ip))
			return Result.buildFailMessage("必传参数为空");
		lock.lock();
		try {
			DealOrder order = orderServiceImpl.findOrderByOrderId(orderId);
			if (order.getOrderStatus().toString().equals(OrderDealStatus.成功.getIndex().toString()))
				return Result.buildFailMessage("当前订单不支持修改");
			if (order.getOrderStatus().toString().equals(OrderDealStatus.失败.getIndex().toString()))
				return Result.buildFailMessage("当前订单不支持修改");
			Result dealAmount = dealAmount(order, ip, flag, msg);
			if (!dealAmount.isSuccess())
  			throw new OrderException("订单修改异常", null);
  		Result dealAmount1 = enterOrderApp(order.getAssociatedId(), ip, flag);
  		if(!dealAmount1.isSuccess())
  			throw new OrderException("订单修改异常", null);
        ThreadUtil.execute( ()->{//更新风控数据 统计数据等
        	DealOrder orderSu = orderServiceImpl.findOrderByOrderId(orderId);
        	if(orderSu.getOrderStatus().toString().equals(OrderDealStatus.成功.getIndex().toString()))
        		riskUtil.orderSu(order);
        });
	    }catch (Exception e) {
	    	throw new OrderException("订单修改异常", null);
		}  finally {
	        lock.unlock();
	    }
		return Result.buildSuccess();
	}

	@SuppressWarnings("unused")
	@Transactional
	public Result updateDealOrderEr(String orderId,String mag ,String ip) {
		if (StrUtil.isBlank(orderId) || StrUtil.isBlank(ip))
			return Result.buildFailMessage("必传参数为空");
		DealOrder order = orderServiceImpl.findOrderByOrderId(orderId);
		if (order.getOrderStatus().toString().equals(OrderDealStatus.成功.getIndex().toString()))
			return Result.buildFailMessage("当前订单不支持修改");
		if (order.getOrderStatus().toString().equals(OrderDealStatus.失败.getIndex().toString()))
			return Result.buildFailMessage("当前订单不支持修改");
		boolean updateOrderStatus = orderServiceImpl.updateOrderStatus(orderId, OrderDealStatus.失败.getIndex().toString(), mag);
		if (!updateOrderStatus)
			return Result.buildFailMessage("订单修改失败，请重新发起成功");
		else
			return Result.buildSuccess();
	}
	/**
	 * <p>码商交易订单【渠道交易订单】置为成功的时候的资金和流水处理</p>
	 * @param order					交易订单
	 * @param ip					交易成功回调ip
	 * @param flag					true 自然流水     false  人工流水
	 * @return
	 */
	public Result dealAmount(DealOrder order,String ip ,Boolean flag,String msg) {
		if (!orderServiceImpl.updateOrderStatus(order.getOrderId(), OrderDealStatus.成功.getIndex().toString(), msg))
			return Result.buildFailMessage("订单修改失败，请重新发起成功");
		//TODO 这里结算模式可选设置为  是否为顶代模式，如果为订单模式 则  只扣减顶代的   账号金额     给当前码商增加利润
		//TODO 如果不为顶代模式 则直接按照当前现有模式  结算
		UserInfo user = userInfoServiceImpl.findUserInfoByUserId(order.getOrderQrUser());
		UserFund userFund = new UserFund();
		userFund.setUserId(order.getOrderQrUser());
		if ("3".equals(user.getUserType().toString())) {//渠道账户结算
			log.info("【进入渠道账户结算，当前渠道：" + user.getUserId() + "】");
			userFund.setUserId(user.getUserId());
			Result deleteDeal = amountUtil.deleteDeal(userFund, order.getDealAmount());//扣除交易点数账户变动
			if (!deleteDeal.isSuccess())
				return deleteDeal;
			Result deleteRechangerNumber = amountRunUtil.deleteRechangerNumber(order, ip, flag);//扣除交易点数 流水生成
			if(!deleteRechangerNumber.isSuccess())
				return deleteRechangerNumber;
			log.info("【金额修改完毕，流水生成成功】");
			return Result.buildSuccessResult();
		}
		if(true) {//顶代结算模式
			String findAgent = correlationServiceImpl.findAgent(order.getOrderQrUser());
			UserInfo userId = userInfoServiceImpl.findUserInfoByUserId(findAgent);
			/**
			 * 1,获取顶代账号
			 * 2,扣减顶代账户余额
			 * 3,单笔交易分润汇入普通码商账号
			 */
			userFund.setUserId(userId.getUserId());
			Result deleteDeal = amountUtil.deleteDeal(userFund, order.getDealAmount());//扣除交易点数账户变动
			if (!deleteDeal.isSuccess())
				return deleteDeal;
			Result deleteRechangerNumber = amountRunUtil.deleteRechangerNumber(order, ip, flag);//扣除交易点数 流水生成
			if (!deleteRechangerNumber.isSuccess())
				return deleteRechangerNumber;
			//顶代账户已扣减
			//下面是对订单交易用户进行账户分润
			//	UserRate userRate = userInfoServiceImpl.findUserRateById(order.getFeeId());
			BigDecimal dealAmount = order.getDealAmount();
			//		log.info("【当前交易金额："+dealAmount+"】");
			//		UserInfo userOrder = userInfoServiceImpl.findUserInfoByUserId(order.getOrderQrUser());//当前接单用户
			//		log.info("【当前订单交易码商："+userOrder.getUserId()+"】");
			//		userFund.setUserId(userOrder.getUserId());

			BigDecimal multiply = new BigDecimal("0");
			Result addDeal = amountUtil.addDeal(userFund, multiply, dealAmount);
			if (!addDeal.isSuccess())
				return addDeal;
			Result addDealAmount = amountRunUtil.addDealAmount(order, ip, flag);
			if (!addDealAmount.isSuccess())
				return addDealAmount;
			log.info("【金额修改完毕，流水生成成功】");
			return Result.buildSuccessResult();


		} else {//正常结算模式
			Result deleteDeal = amountUtil.deleteDeal(userFund, order.getDealAmount());//扣除交易点数账户变动
			if(!deleteDeal.isSuccess())
				return deleteDeal;
			Result deleteRechangerNumber = amountRunUtil.deleteRechangerNumber(order, ip, flag);//扣除交易点数 流水生成
			if(!deleteRechangerNumber.isSuccess())
				return deleteRechangerNumber;
			UserRate findUserRateById = userInfoServiceImpl.findUserRateById(order.getFeeId());
			BigDecimal dealAmount = order.getDealAmount();
			log.info("【当前交易金额："+dealAmount+"】");
			BigDecimal fee = findUserRateById.getFee();
			BigDecimal multiply = dealAmount.multiply(fee);
			log.info("【当前分润费率："+fee+"】");
			log.info("【当前分润金额："+multiply+"】");
			Result addDeal = amountUtil.addDeal(userFund, multiply, dealAmount);
			if(!addDeal.isSuccess())
				return addDeal;
			Result addDealAmount = amountRunUtil.addDealAmount(order, ip, flag);
			if(!addDealAmount.isSuccess())
				return addDealAmount;
			log.info("【金额修改完毕，流水生成成功】");
			return Result.buildSuccessResult();
		}

	}
	/**
	 * <p>充值成功</p>
	 * @return
	 */
	public Result rechargeOrderSu( Recharge rechaege,boolean flag  ){
		/**
		 * ###########################
		 * 充值成功给该账户加钱
		 */
		int a = rechargeDao.updateOrderStatus(rechaege.getOrderId(),Common.Order.Recharge.ORDER_STATUS_SU);
		if(a == 0  || a > 2)
			return Result.buildFailMessage("订单状态修改失败");
		UserFund userFund = new UserFund();
		userFund.setUserId(rechaege.getUserId());
		Result addAmounRecharge = amountUtil.addAmounRecharge(userFund, rechaege.getAmount());
		if(!addAmounRecharge.isSuccess())
			return addAmounRecharge;
		Result addAmount = amountRunUtil.addAmount(rechaege, rechaege.getRetain1(), flag);
		if(!addAmount.isSuccess())
			return addAmount;
		return Result.buildSuccessMessage("充值成功");
	}
	/**
	 * <p>充值失败</p>
	 * @param rechaege
	 * @return
	 */
	public Result rechargeOrderEr(Recharge rechaege) {
		/**
		 * ######################
		 * 充值失败修改订单状态什么都不管
		 */
		int a = rechargeDao.updateOrderStatus(rechaege.getOrderId(), Common.Order.Recharge.ORDER_STATUS_ER);
		if (a > 0 && a < 2)
			return Result.buildSuccessMessage("充值失败，可能原因，暂无充值渠道");
		return Result.buildFail();
	}

	@Autowired
	CheckUtils checkUtils;
	@Autowired
	WithdrawService withdrawServiceImpl;

	/**
	 * <p>代付订单置为成功</p>
	 *
	 * @param orderId
	 * @return
	 */
	@Transactional
	public Result withrawOrderSu(String orderId, String approval,
								 String comment,
								 String channelId, String witType) {
		Withdraw order = withdrawDao.findWitOrder(orderId);
		if (order == null) {
			return Result.buildFailMessage("平台订单号不存在");
		}
		if (!Common.Order.Wit.ORDER_STATUS_YU.equals(order.getOrderStatus())) {
			return Result.buildFailMessage("订单已被处理，不允许操作");
		}
		order.setApproval(approval);
		order.setComment(comment);
		order.setChennelId(channelId);
		order.setWitType(witType);
		return withrawOrderSu(order);
	}

	/**
	 * <p>API下游代付通知</p>
	 */
	void wit(String orderId) {
		ThreadUtil.sleep(2000);
		log.info("【代付订单修改成功，现在开始通知下游，代付订单号：" + orderId + "】");
		Map<String, Object> map = new HashMap<String, Object>();
		Withdraw wit = withdrawServiceImpl.findOrderId(orderId);
		UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(wit.getUserId());
		map.put("apporderid", wit.getAppOrderId());
		map.put("tradesno", wit.getOrderId());
		map.put("status", wit.getOrderStatus());//0 预下单    1处理中  2 成功  3失败
		map.put("amount", wit.getAmount());
		map.put("appid", wit.getUserId());
		String sign = checkUtils.getSign(map, userInfo.getPayPasword());
		map.put("sign", sign);
		send(wit.getNotify(), orderId, map);
	}

	/**
	 * <p>发送通知</p>
	 *
	 * @param url     发送通知的地址
	 * @param orderId 发送订单ID
	 * @param msg     发送通知的内容
	 */
	private void send(String url, String orderId, Map<String, Object> msg) {
		String result = HttpUtil.post(url, msg, -1);
		log.info("服务器返回结果为: " + result.toString());
		log.info("【下游商户返回信息为成功,成功收到回调信息】");
		//更新订单是否通知成功状态
	}

	/**
	 * <p>代付成功</p>
	 * 手动渠道结算
	 *
	 * @return
	 */
	@Transactional
	public Result withrawOrderSu(Withdraw wit) {
		/**
		 * #########################
		 * 代付成功修改订单状态
		 */
		int a = withdrawDao.updataOrderStatus(wit.getOrderId(), wit.getApproval(), wit.getComment(), Common.Order.Wit.ORDER_STATUS_SU, wit.getChennelId());
		if (a == 0 || a > 2)
			return Result.buildFailMessage("订单状态修改失败");
		ThreadUtil.execute(() -> {
			wit(wit.getOrderId());//通知
		});
		wit.setWitChannel(wit.getChennelId());
		//结算实际出款渠道
		UserFund channel = new UserFund();
		channel.setUserId(wit.getChennelId());
		channelWitSu(wit.getOrderId(), wit, wit.getRetain2(), channel);
		agentDpayChannel(wit, wit.getRetain2(), wit.getWitType(), false);//新加代付代理商结算
		return Result.buildSuccessMessage("代付成功");
	}

	/**
	 * <p>三方系统回调成功</p>
	 *
	 * @param wit
	 * @return
	 */
	@Transactional
	public Result withrawOrderSu1(Withdraw wit) {
		int a = withdrawDao.updataOrderStatus(wit.getOrderId(), wit.getApproval(), wit.getComment(), Common.Order.Wit.ORDER_STATUS_SU, wit.getChennelId());
		if (a == 0 || a > 2)
			return Result.buildFailMessage("订单状态修改失败");
		return Result.buildSuccessMessage("代付成功");
	}


	/**
	 * <p>新建代付订单时候账户扣款</p>
	 *
	 * @param orderId 代付订单号
	 * @return
	 */
	public Result withrawOrder(String orderId, String ip, Boolean flag) {
		if (StrUtil.isBlank(orderId))
			return Result.buildFailMessage("必传参数为空");
		Withdraw wit = withdrawDao.findWitOrder(orderId);
		UserFund userFund = new UserFund();
		userFund.setUserId(wit.getUserId());
		Result withdraw = amountUtil.deleteWithdraw(userFund,wit.getAmount());
		if(!withdraw.isSuccess())
			return withdraw;
		Result deleteAmount = amountRunUtil.deleteAmount(wit, ip, flag);
		if(!deleteAmount.isSuccess())
			return deleteAmount;
		Result withdraws = amountUtil.deleteWithdraw(userFund,wit.getFee());
		if(!withdraws.isSuccess())
			return withdraws;
		Result deleteAmountFee = amountRunUtil.deleteAmountFee(wit, ip, flag);
		if(!deleteAmountFee.isSuccess())
			return deleteAmountFee;
	return Result.buildSuccess();
	}

	/**
	 * <p>商户订单结算</p>
	 * @param orderId
	 * @return
	 */
	public Result enterOrderApp(String orderId,String ip , Boolean flag ) {
		log.info("【进入商户订单结算方法】");
		if(StrUtil.isBlank(orderId))
			return Result.buildFailMessage("必传参数为空");
		DealOrderApp orderApp = dealOrderAppDao.findOrderByOrderId(orderId);
		if (ObjectUtil.isNull(orderApp))
			return Result.buildFailMessage("当前订单号不存在");
		if (!orderApp.getOrderStatus().toString().equals(Common.Order.DealOrderApp.ORDER_STATUS_DISPOSE))
			return Result.buildFailMessage("当前订单状态不允许操作");
		boolean status = dealOrderAppDao.updateOrderSu(orderId, Common.Order.DealOrderApp.ORDER_STATUS_SU);
		if (!status)
			return Result.buildFail();
		Integer feeId = orderApp.getFeeId();
		String appId = orderApp.getOrderAccount();
		UserFund userFund = userInfoServiceImpl.findUserFundByAccount(appId);
		Result addDealApp = amountUtil.addDealApp(userFund, orderApp.getOrderAmount());
		if (!addDealApp.isSuccess())
			return Result.buildFail();
		Result addDealAmountApp = amountRunUtil.addDealAmountApp(orderApp, ip, flag);
		if (!addDealAmountApp.isSuccess())
			return Result.buildFail();
		UserRate findFeeById = userRateDao.findFeeById(feeId);
		BigDecimal fee = findFeeById.getFee();
		BigDecimal multiply = orderApp.getOrderAmount().multiply(fee);
		log.info("【当前商户结算费率：" + fee + "，当前商户交易金额：" + orderApp.getOrderAmount() + "，当前商户收取交易手续费：" + multiply + "】");
		Result deleteDeal = amountUtil.deleteDeal(userFund, multiply);
		if (!deleteDeal.isSuccess())
			 throw new OrderException("订单修改异常", null);
		Result feeApp = amountRunUtil.deleteDealAmountFeeApp(orderApp, ip, flag, multiply);
		ThreadUtil.execute(()->{
			log.info("【对当前商户订单的代理商进行结算】");
			agentDealPay(orderApp,flag,ip);
		});
		if(feeApp.isSuccess())
			return feeApp;
		return Result.buildFail();
	}

	/**
	 * <p>商户代理商结算</p>
	 * @param orderApp					商户订单
	 * @return
	 */
	boolean agentDealPay(DealOrderApp orderApp,boolean flag,String ip){
		String appId = orderApp.getOrderAccount();
		UserFund userFund = userInfoServiceImpl.findUserFundByAccount(appId);
		UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(appId);
		if(StrUtil.isBlank(userInfo.getAgent())) {
			log.info("【当前账户无代理商，不进行结算】");
			boolean flag1 = dealOrderAppDao.updateOrderIsAgent(orderApp.getOrderId(),"YES");
			return true;
		}
		Integer feeId = orderApp.getFeeId();
		UserRate findFeeById = userRateDao.findFeeById(feeId);
		Result findUserRateList = findUserRateList(userInfo.getAgent(),findFeeById.getPayTypr(),findFeeById.getChannelId(),findFeeById,orderApp,flag,ip);
		if(findUserRateList.isSuccess()) {
			log.info("【当前订单代理商结算成功】");
			boolean flag1 = dealOrderAppDao.updateOrderIsAgent(orderApp.getOrderId(),"YES");
			return true;
		  }

		return false;
	}

	private Result findUserRateList(String agent, String product, String channelId, UserRate rate, DealOrderApp orderApp,boolean flag, String ip) {
		UserFund userFund = userInfoServiceImpl.findUserFundByAccount(agent);
		UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(agent);
		UserRate userRate = userRateDao.findProductFeeBy(userFund.getUserId(), product, channelId);
		log.info("【当前代理商为：" + userRate.getUserId() + "】");
		log.info("【当前代理商结算费率：" + userRate.getFee() + "】");
		log.info("【当前当前我方：" + rate.getUserId() + "】");
		log.info("【当前我方结算费率：" + rate.getFee() + "】");
		BigDecimal fee = userRate.getFee();
		BigDecimal fee2 = rate.getFee();
		BigDecimal subtract = fee2.subtract(fee);
		log.info("【当前结算费率差为：" + subtract + "】");
		BigDecimal amount = orderApp.getOrderAmount();
		BigDecimal multiply = amount.multiply(subtract);
		 log.info("【当前结算订单金额为："+amount+"，当前结算代理分润为："+multiply+"】");
		 log.info("【开始结算】");
		 Result addAmounProfit = amountUtil.addAmounProfit(userFund, multiply);
		 if(addAmounProfit.isSuccess()) {
			 Result addAppProfit = amountRunUtil.addAppProfit(orderApp , userFund.getUserId(), multiply, ip, flag);
			 if(addAppProfit.isSuccess()) {
				 log.info("【流水成功】");
				 if (StrUtil.isNotBlank(userInfo.getAgent()))
					 return findUserRateList(userInfo.getAgent(), product, channelId, userRate, orderApp, flag, ip);
			 }
		 }
		return Result.buildSuccessMessage("结算成功");
	}

	@Transactional
  public Result withrawOrderErBySystem(String orderId , String ip, String msg) {
    Withdraw order = withdrawDao.findWitOrder(orderId);
    if (order == null) {
      return Result.buildFailMessage("平台订单号不存在");
    }
    if (!Common.Order.Wit.ORDER_STATUS_YU.equals(order.getOrderStatus())) {
      return Result.buildFailMessage("订单已被处理，不允许操作");
    }
    return withrawOrderErBySystem(order,ip,msg);
  }

	/*
	 *
	 * @param wit
	 * @param ip
	 * @return
	 */
	  @Transactional
	  public Result withrawOrderErBySystem(Withdraw wit,String ip,String msg) {
		  /*
		   * ###########################
		   * 代付失败给该用户退钱
		   */
		  int a = withdrawDao.updataOrderStatusEr(wit.getOrderId(),msg, Common.Order.Wit.ORDER_STATUS_ER);
		  if(a == 0  || a > 2)
			  return Result.buildFailMessage("订单状态修改失败");
		  UserFund userFund = new UserFund();
		  userFund.setUserId(wit.getUserId());
		  Result addAmountAdd = amountUtil.addAmountAdd(userFund, wit.getAmount());
		  if(!addAmountAdd.isSuccess())
			  return addAmountAdd;
		  Result addAmountW = amountRunUtil.addAmountW(wit, ip);
		  if (!addAmountW.isSuccess())
			  return addAmountW;
		  Result addFee = amountUtil.addAmountAdd(userFund, wit.getFee());
		  if (!addFee.isSuccess())
			  return addFee;
		  Result addAmountWFee = amountRunUtil.addAmountWFee(wit, ip);
		  if (!addAmountWFee.isSuccess())
			  return addAmountWFee;
		  return Result.buildSuccessMessage("代付金额解冻成功");
	  }

	/**
	 * <p>代付失败</p>
	 *
	 * @return
	 */
	@Transactional
	public Result withrawOrderEr(Withdraw wit, String ip) {
		/**
		 * ###########################
		 * 代付失败给该用户退钱
		 */
		Withdraw witd = wit;
		String userId = wit.getUserId();
		if (wit.getOrderStatus().equals(Common.Order.Wit.ORDER_STATUS_SU)) {
			//1，订单成功时候的时候除了退换商户资金还会对渠道账户进行扣款操作
			//2，对实际出款订单和配置出款订单加加款进行区分
			if (StrUtil.isNotBlank(witd.getChennelId())) {//配置出款
				//按照配置的出款费率给渠道退款
				channelWitEr(witd, witd.getChennelId());
			} else {//手动推送出款
				channelWitEr(witd, witd.getWitChannel());
			}
		}

		int a = withdrawDao.updataOrderStatus1(wit.getOrderId(), wit.getApproval(), wit.getComment(), Common.Order.Wit.ORDER_STATUS_ER);
		if (a == 0 || a > 2)
			return Result.buildFailMessage("订单状态修改失败");
		witd.setUserId(userId);
		UserFund userFund = new UserFund();
		userFund.setUserId(wit.getUserId());
		Result addAmountAdd = amountUtil.addAmountAdd(userFund, wit.getAmount());
		if (!addAmountAdd.isSuccess())
			return addAmountAdd;
		Result addAmountW = amountRunUtil.addAmountW(wit, ip);
		if (!addAmountW.isSuccess())
			return addAmountW;
		Result result = amountUtil.addAmountAdd(userFund, wit.getFee());
		if (!result.isSuccess())
			return result;
		Result result1 = amountRunUtil.addAmountWFee(wit, ip);
		if (!result1.isSuccess())
			return result1;
		return Result.buildSuccessMessage("代付金额解冻成功");
	}

	/**
	 * <p>代付成功渠道结算</p>
	 *
	 * @param orderId 代付订单
	 * @param wit     代付订单实体类
	 * @param ip      代付订单ip
	 * @param channel 结算代付渠道账户
	 * @return
	 */
	public Result channelWitSu(String orderId, Withdraw wit, String ip, UserFund channel) {
		log.info("【当前代付订单成功，代付订单号为：" + orderId + "，对代付渠道进行加款操作】");
		Result addAmountAdd = amountUtil.addAmountAdd(channel, wit.getAmount());
		if (addAmountAdd.isSuccess())
			log.info("【当前代付渠道账户加款成功，代付订单号为：" + orderId + "，生成渠道加款流水】");
		else
			log.info("【当前代付渠道账户加款【失败】，代付订单号为：" + orderId + "，加款渠道为：" + wit.getOrderId() + "】");
		Result addChannelWit = amountRunUtil.addChannelWit(wit, ip);
		if (addChannelWit.isSuccess())
			log.info("【当前代付渠道账户加款流水成功，代付订单号为：" + orderId + "，生成渠道加款流水】");
		else
			log.info("【当前代付渠道账户加款流水【失败】，代付订单号为：" + orderId + "，加款渠道为：" + wit.getOrderId() + "】");
		ChannelFee findChannelFee = channelFeeDao.findChannelFee(wit.getWitChannel(), wit.getWitType());
		String channelDFee = findChannelFee.getChannelDFee();
		log.info("【当前渠道代付手续费为：" + channelDFee + " 】");
		Result add = amountUtil.addAmountAdd(channel, new BigDecimal(channelDFee));
		log.info("【渠道账户记录代付手续费为：" + channelDFee + " 】");
		if (add.isSuccess())
			log.info("【当前代付渠道账户取款手续费加款成功，代付订单号为：" + orderId + "，生成渠道加款手续费流水】");
		else
			log.info("【当前代付渠道账户取款手续费加款【失败】，代付订单号为：" + orderId + "，加款渠道为：" + wit.getOrderId() + "】");
		Result addChannelWitFee = amountRunUtil.addChannelWitFee(wit, ip, new BigDecimal(channelDFee));
		if (addChannelWitFee.isSuccess())
			log.info("【当前代付渠道账户取款手续费加款流水成功，代付订单号为：" + orderId + "，生成渠道加款流水】");
		else
			log.info("【当前代付渠道账户取款手续费加款流水【失败】，代付订单号为：" + orderId + "，加款渠道为：" + wit.getOrderId() + "】");
		if (addAmountAdd.isSuccess() && addChannelWit.isSuccess() && add.isSuccess() && addChannelWitFee.isSuccess())
			return Result.buildSuccess();
		else
			return  Result.buildFail();
	}


	/**
	 * 自动结算
	 * @param wit
	 * @param ip
	 * @param flag1
	 * @return
	 */
	public  Result agentDpayChannel(Withdraw wit,String ip, boolean flag1){
		return agentDpayChannel(wit,true,null,ip,flag1);
	}

	/**
	 * 手动结算
	 * @param wit
	 * @param ip
	 * @param product
	 * @param flag1
	 * @return
	 */
	public  Result agentDpayChannel(Withdraw wit,String ip,String product, boolean flag1){
		return agentDpayChannel(wit,false,product,ip,flag1);
	}

	/**
	 * 【当前为代付代理商结算     配置渠道结算的时候  出款渠道费率为配置出款渠道   实际出款渠道时候，当前出款渠道为实际出款】
	 * 代理商代付利润结算
	 * @param wit			代付订单
	 * @param flag			是否手动推送出款  true 配置出款渠道结算          false    实际出款渠道
	 * @param trueProduct   实际出款渠道
	 * @param ip			代父ip
	 * @param flag1			是否手动
	 * @return
	 */
	private Result agentDpayChannel(Withdraw wit,Boolean flag,String trueProduct,String ip, boolean flag1){
		String channelId = "";
		String product = "";
		if(flag){
			channelId = wit.getWitChannel();
			product  = wit.getWitType();
		}else{
			channelId = wit.getChennelId();//实际出款渠道
			product = trueProduct;//实际出款产品
		}

		UserRate userRate = userRateDao.findProductFeeByAll(wit.getUserId(), product,channelId);//查询费率情况
		final String finalChannelId = channelId;
		final String finalProduct = product;
		UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(wit.getUserId());
		if(StrUtil.isNotBlank(userInfo.getAgent())){
			ThreadUtil.execute(()->{
				witAgent(wit,userInfo.getAgent(), finalProduct, finalChannelId,userRate,ip,flag1);
			});
		}
		return  Result.buildSuccessMessage("代付代理商结算");
	}


	private Result witAgent(Withdraw wit,String username, String product, String channelId, UserRate rate,  String ip, boolean flag) {
		UserFund userFund = userInfoServiceImpl.findUserFundByAccount (username);//查询当前资金情况
		UserRate userRate = userRateDao.findProductFeeByAll(userFund.getUserId(), product,channelId);//查询当前代理费率情况
		log.info("【当前代理商为："+userRate.getUserId()+"】");
		log.info("【当前代理商结算费率："+userRate.getFee()+"】");
		log.info("【当前当前我方："+rate.getUserId()+"】");
		log.info("【当前我方结算费率："+rate.getFee()+"】");
		BigDecimal fee = userRate.getFee();//代理商的费率
		BigDecimal fee2 = rate.getFee();//商户的费率
		BigDecimal subtract = fee2.subtract(fee);//
		log.info("【当前结算费率差为："+subtract+"】");
		log.info("【当前结算费率差为代理商分润："+subtract+"】");//这个钱要加给代理商
		log.info("【开始结算】");
		Result addAmounProfit = amountUtil.addAmounProfit(userFund, subtract);
		if(addAmounProfit.isSuccess())
		 	amountRunUtil.addWitFee(userFund,subtract,wit,ip,flag);
		if(StrUtil.isNotBlank(userFund.getAgent()))
			witAgent(wit,userFund.getAgent(),product,channelId,userRate,ip,flag);
		return Result.buildSuccessMessage("结算成功");
	}


	 Result channelWitEr(Withdraw wit,String userId){
		 UserFund userFund = new UserFund();
		 userFund.setUserId(userId);
		 Result result1 = amountUtil.addAmountAdd(userFund, wit.getActualAmount());
		 if(!result1.isSuccess()){
		 	log.info("【代付订单置为失败渠道账户加减款异常，请详细查看原因，当前代付订单号："+wit.getOrderId()+"】");
			 return result1;
		 }
		 wit.setUserId(userId);
		 Result addAmountW = amountRunUtil.addAmountW(wit, wit.getRetain2());
		 if(!addAmountW.isSuccess()){
			 log.info("【代付订单置为失败渠道账户加减款流水生成异常，请详细查看原因，当前代付订单号："+wit.getOrderId()+"】");
			 return addAmountW;
		 }
		 ChannelFee findChannelFee = channelFeeDao.findChannelFee(userId, wit.getWitType());

		 Result result = amountUtil.addAmountAdd(userFund, new BigDecimal(findChannelFee.getChannelDFee()));
		 if(!result.isSuccess()){
			 log.info("【代付订单置为失败渠道账户手续费加减款生成异常，请详细查看原因，当前代付订单号："+wit.getOrderId()+"】");
			return result;
		 }
		 Result result2 = amountRunUtil.addAmountChannelWitEr(wit, wit.getRetain2(), new BigDecimal(findChannelFee.getChannelDFee()));
		 if(!result2.isSuccess()){
			 log.info("【代付订单置为失败渠道账户手续费加减款流水生成异常，请详细查看原因，当前代付订单号："+wit.getOrderId()+"】");
			 return result2;
		 }

		 return Result.buildSuccessMessage("渠道退款成功");
	}
}
