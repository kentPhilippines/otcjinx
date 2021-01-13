package deal.manage.util;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import deal.manage.bean.DealOrder;
import deal.manage.bean.OrderStatus;
import deal.manage.bean.UserInfo;
import deal.manage.service.OrderService;
import deal.manage.service.OrderStatusService;
import deal.manage.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import otc.api.dealpay.Common;
import otc.result.Result;
/**
 * <p>交易订单确认工具类</p>
 * @author K
 */
@Component
public class EnterOrderUtil {
	private static final int CODE_SU = 1;//双方不存在争议  内部返回状态    
	private static final int CODE_ER = 2;//双方存在争议  内部返回状态
	private static final int CODE_SU_F = 3;//或第一次确认   不存在争议
	private static final int CARD_ORDER_SU= 4;//或第一次确认   不存在争议
	@Autowired OrderService orderSerciceImpl;
	@Autowired OrderStatusService orderStatusServiceImpl;
	@Autowired UserInfoService  userInfoServiceImpl;
	@Autowired CardBankOrderUtil cardBankOrderUtil;
	
	/**
	 * <p>卡商手动置订单为失败,该接口只能确认失败时调用</p>
	 * @param orderId				订单号
	 * @param userId				用户id
	 * @param ip					操作ip
	 * @return
	 */
	public Result CardAppEnterOrderEr(String orderId ,String userId,String ip) {
		if (StrUtil.isBlank(orderId) || StrUtil.isBlank(userId)) {
			return Result.buildFailResult("必传参数为空");
		}
		UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(userId);
		if (ObjectUtil.isNull(userInfo)) {
			return Result.buildFailResult("操作用户不存在");
		}
		if (ObjectUtil.isNull(userInfo.getUserType()) && !Common.User.USER_TYPE_CARD.toString().equals(userInfo.getUserType())) {
			return Result.buildFailResult("当前用户不存在");
		}
		String orderStatus = Common.Order.DealOrder.ORDER_STATUS_ER.toString();
		return EnterOrder(orderId, orderStatus, userId, ip);
	}
	/**
	 * <p>点击订单成功的时候,调用这个方法</p>
	 * @param orderId			订单号
	 * @param userId			点击当前用户
	 * @param ip				点击ip
	 * @return					success   true   操作成功     false   操作失败
	 */
	@Transactional
	public Result EnterOrderSu (String orderId ,String userId,String ip) {
		if (StrUtil.isBlank(orderId) || StrUtil.isBlank(userId)) {
			return Result.buildFailResult("必传参数为空");
		}
		String orderStatus = Common.Order.DealOrder.ORDER_STATUS_SU.toString();
		return EnterOrder(orderId, orderStatus, userId, ip);
	}
	/***
	 * <p>确认订单对外统一封装类</p>		
	 * @param orderId			订单号
	 * @param orderStatus		待确认订单状态
	 * @param userId			操作用户
	 * @return
	 */
	private  Result EnterOrder(String orderId ,String orderStatus,String userId,String ip) {
		if (StrUtil.isBlank(orderId) || StrUtil.isBlank(orderStatus) || StrUtil.isBlank(userId)) {
			return Result.buildFailResult("必传参数为空");
		}
		UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(userId);
		String userType = null;
		if (ObjectUtil.isNull(userInfo)) {
			userType = Common.User.USER_TYPE_APP.toString();
		} else {
			userType = userInfo.getUserType().toString();
		}
		if (StrUtil.isBlank(userType))  //兼容之前的真实数据
		{
			userType = Common.User.USER_TYPE_CARD.toString();
		}
		Result enterOrderStatus = EnterOrderStatus(orderId, orderStatus, userType, userId);
		if (enterOrderStatus.isSuccess()) {
			boolean operation = true;
			int code = enterOrderStatus.getCode();
			if (code != 0 && code == CARD_ORDER_SU) { //卡商确认出款
				Result updataOrderStatusSu = cardBankOrderUtil.updateOrderSu(orderId, ip);
				if (updataOrderStatusSu.isSuccess()) {
					return Result.buildSuccessResult();
				}
			}
			if(code != 0 && code == CODE_SU) {//双方都确认过订单,改变订单状态为成功   或者订单状态为失败
				OrderStatus queryByOrderId = orderStatusServiceImpl.queryByOrderId(orderId);
				Result updataOrderStatusSu = Result.buildFail();
				if (queryByOrderId.getOrderStatusCard().equals(queryByOrderId.getOrderStatusApp()) && StrUtil.isNotBlank(queryByOrderId.getOrderStatusApp()) && queryByOrderId.getOrderStatusApp().equals(Common.Order.DealOrder.ORDER_STATUS_SU.toString())) {
					updataOrderStatusSu = cardBankOrderUtil.updateOrderSu(orderId, ip);
				} else {
					updataOrderStatusSu = cardBankOrderUtil.updataOrderEr(orderId, ip);
				}
				if (updataOrderStatusSu.isSuccess()) {
					return Result.buildSuccessResult();
				}
			} else if(code != 0 && code == CODE_ER) {//订单存在争议  ,修改订单状态为人工操作  
				boolean updataOrderStatusByOrderId = orderSerciceImpl.updataOrderStatusByOrderId(orderId, Common.Order.DealOrder.ORDER_STATUS_OVERTIME_PER.toString(), true);
				if (updataOrderStatusByOrderId) {
					return Result.buildSuccessMessage("订单状态已修改为人工处理");
				}
			} else if (code != 0 && code == CODE_SU_F) //第一次操作,   直插入状态记录表
			{
				return Result.buildSuccessMessage("订单待确认状态已插入待确认表,等待双方确认订单");
			} else {
				return Result.buildFail();
			}
		}
		return Result.buildFail();
	}
	/**
	 * <p>订单确认内部调用封装</p>
	 * @param orderId				确认订单号
	 * @param orderStatus			待确认状态
	 * @param userType				用户类型
	 * @param userId				用户id
	 * @return						success  true 操作成功   false  操作失败       code   1  待双方确认         2  双方存在争议   
	 */
	private Result EnterOrderStatus(String orderId ,String orderStatus,String userType,String userId) {
		if (StrUtil.isBlank(orderId) || StrUtil.isBlank(orderStatus) || StrUtil.isBlank(userType) || StrUtil.isBlank(userId)) {
			return Result.buildFailMessage("必传参数为空");
		}
		DealOrder order = orderSerciceImpl.findOrderByOrderId(orderId);
	/*	if(order.getDealType().equals(Common.BANK_DEAL_ORDER_W)) //卡商出款订单确认
			return JsonResult.buildSuccessMessageCode("卡商确认出款成功", CARD_ORDER_SU);*/
		OrderStatus orderS = orderStatusServiceImpl.queryByOrderId(orderId);
		if (Common.User.USER_TYPE_APP.toString().equals(userType)) {//码商确认
			if (ObjectUtil.isNull(orderS)) {//状态未记录,该订单首次确认
				orderS = new OrderStatus();
				orderS.setOrderStatusApp(orderStatus);
				orderS.setOrderId(orderId);
				boolean flag = orderStatusServiceImpl.insertOrderstatus(orderS);
				if (flag) {
					return Result.buildSuccessMessageCode("订单状态确认成功,请等待双方确认", CODE_SU_F, CODE_SU_F);
				}
				return Result.buildFailResult("状态确认失败");
			}else {//订单已被确认过
				String orderStatusCard = orderS.getOrderStatusCard();
				String orderStatusQr = orderS.getOrderStatusApp();
				if(StrUtil.isNotBlank(orderStatusCard)) {
					if(orderStatus.equals(orderStatusCard) ) {
						orderS.setOrderStatusApp(orderStatus);
						boolean flag = orderStatusServiceImpl.updateOrderStatus(orderS);
						if (flag) {
							return Result.buildSuccessMessageCode("订单状态确认成功,请等待双方确认", CODE_SU, CODE_SU);
						}
						return Result.buildFailResult("状态确认失败");
					} else {
						orderS.setOrderStatusApp(orderStatus);
						boolean flag = orderStatusServiceImpl.updateOrderStatus(orderS);
						if (flag) //当前订单存在争议
						{
							return Result.buildSuccessMessageCode("双方存在争议", CODE_ER, CODE_ER);
						}
						return Result.buildFailResult("状态确认失败");	
					}
				} else {//码商之前一定确认过
					if (orderStatus.equals(orderStatusQr)) {
						return Result.buildSuccessMessageCode("订单状态确认成功,请等待双方确认", CODE_SU_F, CODE_SU_F);
					} else {
						orderS.setOrderStatusApp(orderStatus);
						boolean flag = orderStatusServiceImpl.updateOrderStatus(orderS);
						if (flag) {
							return Result.buildSuccessMessageCode("订单状态确认成功,请等待双方确认", CODE_SU_F, CODE_SU_F);
						}
						return Result.buildFailResult("状态确认失败");
					}
				}
			}
		} else if(Common.User.USER_TYPE_CARD.toString().equals(userType)){//卡商确认
			if(ObjectUtil.isNull(orderS)) {//状态未记录,该订单首次确认
				orderS = new OrderStatus();
				orderS.setOrderStatusCard(orderStatus);
				orderS.setOrderId(orderId);
				boolean flag = orderStatusServiceImpl.insertOrderstatus(orderS);
				if (flag) {
					return Result.buildSuccessMessageCode("订单状态确认成功,请等待双方确认", CODE_SU_F, CODE_SU_F);
				}
				return Result.buildFailResult("状态确认失败");
			} else {
				String orderStatusQr = orderS.getOrderStatusApp();
				String orderStatusCard = orderS.getOrderStatusCard();
				if(StrUtil.isNotBlank(orderStatusQr) ) {//码商之前确认过
					if(orderStatus.equals(orderStatusQr) ) {
						orderS.setOrderStatusCard(orderStatus);
						boolean flag = orderStatusServiceImpl.updateOrderStatus(orderS);
						if (flag) {
							return Result.buildSuccessMessageCode("订单状态确认成功,请等待双方确认", CODE_SU, CODE_SU);
						}
						return Result.buildFailResult("状态确认失败");
					} else {
						orderS.setOrderStatusCard(orderStatus);
						boolean flag = orderStatusServiceImpl.updateOrderStatus(orderS);
						if (flag) //当前订单存在争议
						{
							return Result.buildSuccessMessageCode("双方存在争议", CODE_ER, CODE_ER);
						}
						return Result.buildFailResult("状态确认失败");	
					}
				} else {//卡商之前一定确认过
					if (orderStatus.equals(orderStatusCard)) {
						return Result.buildSuccessMessageCode("订单状态确认成功,请等待双方确认", CODE_SU_F, CODE_SU_F);
					} else {
						orderS.setOrderStatusCard(orderStatus);
						boolean flag = orderStatusServiceImpl.updateOrderStatus(orderS);
						if (flag) {
							return Result.buildSuccessMessageCode("订单状态确认成功,请等待双方确认", CODE_SU_F, CODE_SU_F);
						}
						return Result.buildFailResult("状态确认失败");
					}
				}
			}
		}
		return Result.buildFail();
	}
}
