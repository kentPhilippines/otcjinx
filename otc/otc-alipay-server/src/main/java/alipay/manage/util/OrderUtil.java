package alipay.manage.util;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import alipay.manage.api.OrderApi;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserRate;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import otc.bean.alipay.OrderDealStatus;
import otc.result.Result;
@Component
public class OrderUtil {
	Logger log = LoggerFactory.getLogger(OrderApi.class);
	@Autowired OrderService orderServiceImpl;
	@Autowired AmountUtil amountUtil;
	@Autowired AmountRunUtil amountRunUtil;
	@Autowired UserInfoService userInfoServiceImpl;
	
	
	
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
}
