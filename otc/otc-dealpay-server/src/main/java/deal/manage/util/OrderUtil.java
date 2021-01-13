package deal.manage.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import deal.manage.bean.DealOrder;
import deal.manage.bean.UserFund;
import deal.manage.bean.Withdraw;
import deal.manage.mapper.WithdrawMapper;
import deal.manage.service.OrderService;
import deal.manage.service.RechargeService;
import deal.manage.service.UserFundService;
import deal.manage.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import otc.api.dealpay.Common;
import otc.bean.dealpay.Recharge;
import otc.exception.order.OrderException;
import otc.result.Result;

import java.math.BigDecimal;

@Component
public class OrderUtil {
	private static final Log log = LogFactory.get();
	@Autowired
	OrderService orderServiceImpl;
	@Autowired
	AmountUtil amountUtil;
	@Autowired
	WithdrawMapper withdrawDao;
	@Autowired
	WithdrawService withdrawServiceImpl;
	@Autowired
	AmountRunUtil amountRunUtil;
	@Autowired
	RechargeService rechargeServiceImpl;
	@Autowired UserFundService userFundServiceImpl;
	
	/**
	 * <p>卡商代付金额金额扣减及流水生成</p>
	 * @param orderId		订单号
	 * @return
	 */
	public Result cardsubRunning(String orderId,String ip) {
		Result withrawOrder = withrawOrder(orderId, ip, false);
		return withrawOrder;
	}
	
	public Result witSu(String orderId ,  String msg) {
		if (StrUtil.isBlank(orderId)) {
			return Result.buildFailMessage("必传参数为空");
		}
		Withdraw wit = withdrawServiceImpl.findOrderId(orderId);
		if (!wit.getOrderStatus().toString().equals(Common.Order.Wit.ORDER_STATUS_YU.toString())) {
			return Result.buildFailMessage("当前订单状态不允许修改");
		}
		boolean a = withdrawServiceImpl.updateStatusSu(orderId, msg);
		if (!a) {
			return Result.buildFailMessage("订单状态修改失败");
		}
		return Result.buildSuccessMessage("订单状态修改成功");
	}
	
	
	/**
	 * <p>代付失败，资金退回</p>
	 * @param orderId				代付订单号
	 * @param ip					操作ip
	 * @return
	 */
	public Result witEr(String orderId , String ip , String msg) {
		if (StrUtil.isBlank(orderId)) {
			return Result.buildFailMessage("必传参数为空");
		}
		Withdraw wit = withdrawServiceImpl.findOrderId(orderId);
		if (!wit.getOrderStatus().toString().equals(Common.Order.Wit.ORDER_STATUS_YU.toString())) {
			return Result.buildFailMessage("当前订单状态不允许修改");
		}
		/**
		 * ###########################
		 * 代付失败给该用户退钱
		 */
		boolean statusEr = withdrawServiceImpl.updateStatusEr(wit.getOrderId(), msg);
		if (!statusEr) {
			return Result.buildFailMessage("订单状态修改失败");
		}
		UserFund userFund = new UserFund();
		userFund.setUserId(wit.getUserId());
		Result addAmountAdd = amountUtil.addAmountAdd(userFund, wit.getAmount());
		if (!addAmountAdd.isSuccess()) {
			return addAmountAdd;
		}
		Result addAmountW = amountRunUtil.addAmountW(wit, ip);
		if (!addAmountW.isSuccess()) {
			return addAmountW;
		}
		return Result.buildSuccessMessage("代付金额解冻成功");
	}
	
	/**
	 * <p>充值订单置为成功</p>
	 * @param orderId				充值订单	号
	 * @param ip					操作ip
	 * @return
	 */
	public Result rechargeOrderSu(String orderId,String ip,String msg) {
		Result recharge = rechargeOrderEnter(orderId, ip, false, msg);
		return recharge;
	}
	/**
	 * <p>充值订单失败</p>
	 * @param orderId			订单号
	 * @param msg				描述消息
	 * @return
	 */
	public Result rechargeOrderEr(String orderId,  String msg) {
		if (StrUtil.isBlankIfStr(orderId)) {
			return Result.buildFailMessage("必传参数为空");
		}
		Recharge recharge = rechargeServiceImpl.findOrderId(orderId);
		if (!Common.Order.Recharge.ORDER_STATUS_YU.toString().equals(recharge.getOrderStatus().toString())) {
			return Result.buildFailMessage("当前订单状态不允许操作");
		}
		boolean a = rechargeServiceImpl.updateStatusEr(orderId, msg);
		if (!a) {
			return Result.buildFailMessage("订单修改失败");
		}
		return Result.buildSuccessMessage("账户修改成功");
	}
	private Result rechargeOrderEnter(String orderId, String ip, boolean b,String msg) {
		/**
		 * 1,修改订单状态
		 * 2,修改账户状态
		 * 3,生成流水
		 */
		if (StrUtil.isBlankIfStr(orderId) || StrUtil.isBlank(ip)) {
			return Result.buildFailMessage("必传参数为空");
		}
		Recharge recharge = rechargeServiceImpl.findOrderId(orderId);
		if (!Common.Order.Recharge.ORDER_STATUS_YU.toString().equals(recharge.getOrderStatus().toString())) {
			return Result.buildFailMessage("当前订单状态不允许操作");
		}
		boolean a = rechargeServiceImpl.updateStatusSu(orderId, msg);
		if (!a) {
			return Result.buildFailMessage("订单状态修改错误");
		}
		UserFund userFund = userFundServiceImpl.findUserFund(recharge.getUserId());
		log.info("【当前充值成功操作卡商账户：" + recharge.getUserId() + "】");
		log.info("【当前充值成功操作卡商账户实体：" + userFund.toString() + "】");
		Result result = amountUtil.addAmountRecharge(userFund, recharge.getActualAmount());
		log.info("【当前修改账户结果集：" + result.toString() + "】");
		if (!result.isSuccess()) {
			return Result.buildFailMessage("账户修改出错");
		}
		Result amount = amountRunUtil.addAmount(recharge, ip, b);
		log.info("【当前增加流水结果集：" + amount.toString() + "】");
		if (!amount.isSuccess()) {
			return Result.buildFailMessage("资金流水出错");
		}
		return Result.buildSuccessMessage("账户修改成功");
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
		if (flag && StrUtil.isBlank(operator)) {
			return Result.buildFailMessage("请填写操作人");
		}
		/**
		 * #########################步骤##########################
		 * 0,订单修改为成功			
		 * 1,扣减卡商押金-并生成流水
		 * 2,增加卡商分分润-并生成流水
		 */
		if (StrUtil.isBlank(orderId)) {
			return Result.buildFailMessage("订单号为空");
		}
		DealOrder order = orderServiceImpl.findOrderByOrderId(orderId);
		if (ObjectUtil.isNull(order)) {
			return Result.buildFailMessage("当前订单不存在");
		}
		String orderType = order.getOrderType();
		boolean updateOrderStatus = false;
		if (flag) //人工操作
		{
			updateOrderStatus = orderServiceImpl.updateOrderStatus(orderId, Common.Order.DealOrder.ORDER_STATUS_SU, operator + "，手动操作为成功");
		} else {
			updateOrderStatus = orderServiceImpl.updateOrderStatus(orderId, Common.Order.DealOrder.ORDER_STATUS_SU);
		}
		if (!updateOrderStatus) {
			return Result.buildFailMessage("订单状态变更失败");
		}
		if (orderType.equals(Common.Order.DealOrder.DEAL_ORDER_R)) {//入款账户变更
			Result orderAamount = amountUtil.orderAmountR(orderId, ip, flag);
			if (!orderAamount.isSuccess()) {
				return orderAamount;
			}
			return Result.buildSuccessMessage("操作成功");
		}
		if (orderType.equals(Common.Order.DealOrder.DEAL_ORDER_C)) {//出款账户变更
			Result orderAamount = amountUtil.orderAmountC(orderId, ip, flag);
			if (!orderAamount.isSuccess()) {
				throw new OrderException("订单结算失败", null);
			}
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
		if (StrUtil.isBlank(orderId)) {
			return Result.buildFailMessage("必传参数为空");
		}
		Withdraw wit = withdrawServiceImpl.findOrderId(orderId);
		UserFund userFund = new UserFund();
		userFund.setUserId(wit.getUserId());
		Result withdraw = amountUtil.deleteWithdraw(userFund, wit.getAmount());
		if (!withdraw.isSuccess()) {
			return withdraw;
		}
		Result deleteAmount = amountRunUtil.deleteAmount(wit, ip, flag);
		if (!deleteAmount.isSuccess()) {
			return deleteAmount;
		}
		if (wit.getFee().compareTo(new BigDecimal("0")) == 0) {
			return Result.buildSuccessMessage("代付扣款成功");
		}
		Result withdraws = amountUtil.deleteWithdraw(userFund, wit.getFee());
		if (!withdraws.isSuccess()) {
			return withdraws;
		}
		Result deleteAmountFee = amountRunUtil.deleteAmountFee(wit, ip, flag);
		if (!deleteAmountFee.isSuccess()) {
			return deleteAmountFee;
		}
		return Result.buildSuccess();
	}
	
	
	
	
	
	
	
	
	
}
