package deal.manage.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import deal.manage.bean.DealOrder;
import deal.manage.service.OrderService;
import otc.api.dealpay.Common;
import otc.result.Result;

@Component
public class OrderUtil {
	@Autowired OrderService orderServiceImpl;
	@Autowired AmountUtil amountUtil;
	
	/**
	 * <p>卡商代付金额金额扣减及流水生成</p>
	 * @param orderId		订单号
	 * @return
	 */
	public Result cardsubRunning(String orderId) {
		return Result.buildFail();
	}

	
	/**
	 * <p>卡商【入款】订单置为成功</P>
	 * @param orderId	 交易订单号
	 * @param flag		true 人工操作0       false    自动操作
	 * @param operator	操作人
	 * @return
	 */
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
		if(ObjectUtil.isNotNull(order))
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
				return orderAamount;
			return Result.buildSuccessMessage("操作成功");
		}
//		Result amountR = amountUtil.orderAmountR(order.getOrderId(),ip,flag);
		return Result.buildFailMessage("结算失败");
	}
	
	
	
	
	
	
	
	
	
	
	
}
