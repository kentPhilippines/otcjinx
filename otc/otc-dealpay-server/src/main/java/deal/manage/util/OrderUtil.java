package deal.manage.util;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import deal.manage.bean.DealOrder;
import deal.manage.bean.UserFund;
import deal.manage.bean.Withdraw;
import deal.manage.mapper.WithdrawMapper;
import deal.manage.service.OrderService;
import deal.manage.service.WithdrawService;
import otc.api.dealpay.Common;
import otc.exception.order.OrderException;
import otc.result.Result;

@Component
public class OrderUtil {
	@Autowired OrderService orderServiceImpl;
	@Autowired AmountUtil amountUtil;
	@Autowired WithdrawMapper withdrawDao;
	@Autowired WithdrawService withdrawServiceImpl;
	@Autowired AmountRunUtil amountRunUtil;
	
	/**
	 * <p>卡商代付金额金额扣减及流水生成</p>
	 * @param orderId		订单号
	 * @return
	 */
	public Result cardsubRunning(String orderId,String ip) {
		Result withrawOrder = withrawOrder(orderId, ip, false);
		return withrawOrder;
	}

	
	/**
	 * <p>卡商【入款】订单置为成功</P>
	 * @param orderId	 交易订单号
	 * @param flag		true 人工操作0       false    自动操作
	 * @param operator	操作人
	 * @return
	 */
	@Transactional
	public Result orderDeal(String orderId,boolean flag,String operator ,String ip) {
		if(flag && StrUtil.isBlank(operator)) return Result.buildFailMessage("请填写操作人");
		/**
		 * #########################步骤##########################
		 * 0,订单修改为成功			
		 * 1,扣减卡商押金-并生成流水
		 * 2,增加卡商分分润-并生成流水
		 */
		if(StrUtil.isBlank(orderId))
			return Result.buildFailMessage("订单号为空");
		DealOrder order = orderServiceImpl.findOrderByOrderId(orderId);
		if(ObjectUtil.isNull(order))
			return Result.buildFailMessage("当前订单不存在");
		String orderType = order.getOrderType();
		boolean updateOrderStatus = false;
		if(flag) //人工操作
			updateOrderStatus = orderServiceImpl.updateOrderStatus(orderId, Common.Order.DealOrder.ORDER_STATUS_SU, operator+"，手动操作为成功");
		else 
			updateOrderStatus = orderServiceImpl.updateOrderStatus(orderId, Common.Order.DealOrder.ORDER_STATUS_SU);
		if(!updateOrderStatus)
			return Result.buildFailMessage("订单状态变更失败");
		if(orderType.equals(Common.Order.DealOrder.DEAL_ORDER_R)) {//入款账户变更
			Result orderAamount = amountUtil.orderAmountR(orderId,ip,flag);
			if(!orderAamount.isSuccess())
				return orderAamount;
			return Result.buildSuccessMessage("操作成功");
		}
		if(orderType.equals(Common.Order.DealOrder.DEAL_ORDER_C)) {//出款账户变更
			Result orderAamount = amountUtil.orderAmountC(orderId,ip,flag);
			if(!orderAamount.isSuccess())
				throw new OrderException("订单结算失败", null);
			return Result.buildSuccessMessage("操作成功");
		}
//		Result amountR = amountUtil.orderAmountR(order.getOrderId(),ip,flag);
		return Result.buildFailMessage("结算失败");
	}
	
	
	/**
	 * <p>新建代付订单时候账户扣款</p>
	 * @param orderId				代付订单号
	 * @return
	 */
	private Result withrawOrder(String orderId,String ip,Boolean flag) {
		if(StrUtil.isBlank(orderId))
			return Result.buildFailMessage("必传参数为空");
		Withdraw wit = withdrawServiceImpl.findOrderId(orderId);
		UserFund userFund = new UserFund();
		userFund.setUserId(wit.getUserId());
		Result withdraw = amountUtil.deleteWithdraw(userFund,wit.getAmount());
		if(!withdraw.isSuccess())
			return withdraw;
		Result deleteAmount = amountRunUtil.deleteAmount(wit, ip, flag);
		if(!deleteAmount.isSuccess())
			return deleteAmount;
		if(wit.getFee().compareTo(new BigDecimal("0"))==0)
			return Result.buildSuccessMessage("代付扣款成功");
		Result withdraws = amountUtil.deleteWithdraw(userFund,wit.getFee());
		if(!withdraws.isSuccess())
			return withdraws;
		Result deleteAmountFee = amountRunUtil.deleteAmountFee(wit, ip, flag);
		if(!deleteAmountFee.isSuccess())
			return deleteAmountFee;
	return Result.buildSuccess();
	}
	
	
	
	
	
	
	
	
	
}
