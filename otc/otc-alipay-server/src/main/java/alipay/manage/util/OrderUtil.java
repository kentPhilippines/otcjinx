package alipay.manage.util;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import alipay.manage.api.OrderApi;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.Recharge;
import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserRate;
import alipay.manage.bean.Withdraw;
import alipay.manage.mapper.RechargeMapper;
import alipay.manage.mapper.WithdrawMapper;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import otc.api.alipay.Common;
import otc.bean.alipay.OrderDealStatus;
import otc.result.Result;
@Component
public class OrderUtil {
	Logger log = LoggerFactory.getLogger(OrderApi.class);
	@Autowired OrderService orderServiceImpl;
	@Autowired AmountUtil amountUtil;
	@Autowired AmountRunUtil amountRunUtil;
	@Autowired UserInfoService userInfoServiceImpl;
	@Autowired RechargeMapper rechargeDao;
	@Autowired WithdrawMapper withdrawDao;
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
	 * @param orderIds
	 * @return
	 */
	public Result rechargeSu(String orderIds) {
		if(StrUtil.isBlank(orderIds))
			return Result.buildFailMessage("必传参数为空");
		return rechargeOrderSu(orderIds,true);
	}
	/**
	 * <p>代付订单置为成功</p>
	 * @param orderId
	 * @return
	 */
	public Result withrawOrderSu(String orderId) {
		if(StrUtil.isBlank(orderId))
			return Result.buildFailMessage("订单号为空");
		Withdraw order = withdrawDao.findWitOrder(orderId);
		return withrawOrderSu(order);
	}
	
	
	/**
	 * <p>代付订单置为失败【这里只能是人工操作】</p>
	 * @param orderId			这里只能是人工操作
	 * @param ip				操作 ip
	 * @return
	 */
	public Result withrawOrderEr(String orderId , String ip) {
		if(StrUtil.isBlank(orderId) || StrUtil.isBlank(ip))
			return Result.buildFailMessage("必传参数为空");
		Withdraw order = withdrawDao.findWitOrder(orderId);
		return withrawOrderEr(order,ip);
	}
	
	
	
	
	
	
	
	
	/**
	 * <p>码商充值订单置为成功</p>
	 * @param orderId
	 * @param flag
	 * @return
	 */
	private Result rechargeOrderSu(String orderId ,boolean flag) {
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
		return orderDealSu(orderId, ip,false ,null);
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
	private Result orderDealSu(String orderId ,String ip ,boolean flag ,String userId) {
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
	@SuppressWarnings("unused")
	@Transactional
	private  Result updataDealOrderSu(String orderId,String mag , String ip ,Boolean flag) {
		if(StrUtil.isBlank(orderId) || StrUtil.isBlank(ip)) 
			return Result.buildFailMessage("必传参数为空");
		DealOrder order = orderServiceImpl.findOrderByOrderId(orderId);
		if(order.getOrderStatus().toString().equals(OrderDealStatus.成功.getIndex().toString()))
			return Result.buildFailMessage("当前订单不支持修改");
		if(order.getOrderStatus().toString().equals(OrderDealStatus.失败.getIndex().toString()))
			return Result.buildFailMessage("当前订单不支持修改");
		if(!orderServiceImpl.updateOrderStatus(orderId,OrderDealStatus.成功.getIndex().toString(),mag))
			return Result.buildFailMessage("订单修改失败，请重新发起成功");
		Result dealAmount = dealAmount(order, ip, flag);
		return dealAmount;
	}
	
	@SuppressWarnings("unused")
	@Transactional
	private Result updateDealOrderEr(String orderId,String mag ,String ip) {
		if(StrUtil.isBlank(orderId) || StrUtil.isBlank(ip)) 
			return Result.buildFailMessage("必传参数为空");
		DealOrder order = orderServiceImpl.findOrderByOrderId(orderId);
		if(order.getOrderStatus().toString().equals(OrderDealStatus.成功.getIndex().toString()))
			return Result.buildFailMessage("当前订单不支持修改");
		if(order.getOrderStatus().toString().equals(OrderDealStatus.失败.getIndex().toString()))
			return Result.buildFailMessage("当前订单不支持修改");
		boolean updateOrderStatus = orderServiceImpl.updateOrderStatus(orderId,OrderDealStatus.失败.getIndex().toString(),mag);
		if(!updateOrderStatus)
			return Result.buildFailMessage("订单修改失败，请重新发起成功");
		else
			return Result.buildSuccess();
	}
	/**
	 * <p>码商交易订单置为成功的时候的资金和流水处理</p>
	 * @param order					交易订单
	 * @param ip					交易成功回调ip
	 * @param flag					true 自然流水     false  人工流水
	 * @return
	 */
	private Result dealAmount(DealOrder order,String ip ,Boolean flag){
		UserFund userFund = new UserFund();
		userFund.setUserId(order.getOrderQrUser());
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
	
	
	
	
	
	
	
	/**
	 * <p>充值成功</p>
	 * @return
	 */
	private Result rechargeOrderSu( Recharge rechaege,boolean flag  ){
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
	private Result rechargeOrderEr( Recharge rechaege ){
		/**
		 * ######################
		 * 充值失败修改订单状态什么都不管
		 */
		int a = rechargeDao.updateOrderStatus(rechaege.getOrderId(),Common.Order.Recharge.ORDER_STATUS_ER);
		if(a > 0  && a < 2)
			return Result.buildSuccessMessage("充值失败，可能原因，暂无充值渠道");
		return Result.buildFail();
	}
	/**
	 * <p>代付成功</p>
	 * @return
	 */
	private Result withrawOrderSu(Withdraw wit) {
		/**
		 * #########################
		 * 代付成功修改订单状态
		 */
		int a = withdrawDao.updataOrderStatus(wit.getOrderId(),Common.Order.Wit.ORDER_STATUS_SU);
		if(a == 0  || a > 2)
			return Result.buildFailMessage("订单状态修改失败");
		return Result.buildSuccessMessage("代付成功");
	}
	/**
	 * <p>代付失败</p>
	 * @return
	 */
	private Result withrawOrderEr(Withdraw wit,String ip) {
		/**
		 * ###########################
		 * 代付失败给该用户退钱
		 */
		int a = withdrawDao.updataOrderStatus(wit.getOrderId(),Common.Order.Wit.ORDER_STATUS_ER);
		if(a == 0  || a > 2)
			return Result.buildFailMessage("订单状态修改失败");
		UserFund userFund = new UserFund();
		userFund.setUserId(wit.getUserId());
		Result addAmountAdd = amountUtil.addAmountAdd(userFund, wit.getAmount());
		if(!addAmountAdd.isSuccess())
			return addAmountAdd;
		Result addAmountW = amountRunUtil.addAmountW(wit, ip);
		if(!addAmountW.isSuccess())
			return addAmountW;
		return Result.buildSuccessMessage("代付金额解冻成功");
	}
}
