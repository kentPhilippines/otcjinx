package alipay.manage.api;

import alipay.manage.bean.Amount;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.UserFund;
import alipay.manage.mapper.AmountMapper;
import alipay.manage.mapper.DealOrderMapper;
import alipay.manage.service.MediumService;
import alipay.manage.service.UserInfoService;
import alipay.manage.util.AmountRunUtil;
import alipay.manage.util.AmountUtil;
import alipay.manage.util.LogUtil;
import alipay.manage.util.NotifyUtil;
import alipay.manage.util.OrderUtil;
import alipay.manage.util.QrUtil;
import alipay.manage.util.QueueUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import otc.api.alipay.Common;
import otc.bean.alipay.Medium;
import otc.common.PayApiConstant;
import otc.common.SystemConstants;
import otc.exception.BusinessException;
import otc.result.Result;
import otc.util.RSAUtils;
import otc.util.enums.DeductStatusEnum;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
public class Api {
	@Autowired AmountMapper amountDao;
	@Autowired MediumService mediumServiceImpl;
	@Autowired LogUtil logUtil;
	@Autowired AmountUtil amountUtil;
	@Autowired UserInfoService userInfoServiceImpl;
	@Autowired AmountRunUtil amountRunUtil;
	private static final Log log = LogFactory.get();
	@Autowired OrderUtil orderUtil;
	@Autowired DealOrderMapper dealOrderDao;
	@Autowired QrUtil qrUtil;
	@Autowired NotifyUtil notifyUtil;
	@Autowired QueueUtil queueUtil;
	@PostMapping(PayApiConstant.Alipay.MEDIUM_API+PayApiConstant.Alipay.OFF_MEDIUM_QR)
	Result offMediumQueue(@RequestParam("mediumNumber")String mediumNumber) {
	if(StrUtil.isBlank(mediumNumber))
		return Result.buildFailMessage("必传参数为空");
		Medium medium = mediumServiceImpl.findMediumByMediumNumber(mediumNumber);
		Result pop = queueUtil.pop(medium);
		return pop;
	};
	/**
	 * <p>系统回调订单成功资金处理</p>
	 * @param param
	 * @param request
	 * @return
	 */
	@PostMapping(PayApiConstant.Alipay.ORDER_API+PayApiConstant.Alipay.ORDER_ENTER_ORDER_SYSTEM+"/{param:.+}")
	public Result enterOrderSystem(@PathVariable("param") String param, HttpServletRequest request) {
		log.info("【接收到系统回调的方法，参数为："+param+"】");
		Map<String, Object> stringObjectMap = RSAUtils.retMapDecode(param, SystemConstants.INNER_PLATFORM_PRIVATE_KEY);
		log.info("【接受系统回调参数为："+stringObjectMap.toString()+"】");
		if(MapUtil.isEmpty(stringObjectMap)) {
			log.info("【当前系统订单成功回调参数为空】");
			return Result.buildFailMessage("必传参数为空");
		}
		Object obja = stringObjectMap.get(Common.Notfiy.ORDER_AMOUNT);
		Object objp = stringObjectMap.get(Common.Notfiy.ORDER_PHONE);
		Object obji = stringObjectMap.get(Common.Notfiy.ORDER_ENTER_IP);
		if(ObjectUtil.isNull(objp)||ObjectUtil.isNull(obja))
			return  Result.buildFailMessage("回调设备号或者金额 为空");
		String amount = obja.toString();
		String phone = objp.toString();
		String ip = HttpUtil.getClientIP(request);
		if(ObjectUtil.isNotNull(obji))
			ip = obji.toString();
		/**
		 * ###################################
		 * 1,通过回调参数拿到商户预订单号
		 * 2,通过预订单参数修改码商交易订单并生成流水
		 * 3,通过码商交易订单拿到商户订单并生成流水
		 * 4,发送回调数据
		 */
		 /**
		  * 如果金额是100.1 或者是  100.20  等
		  * 就会被转换为      100    10       100     20
		  */
		String[] split = amount.split("\\.");
		String startAmount = split[0];
		String endAmount = split[1];
		int length = endAmount.length();
		if(length == 1) //当交易金额为整小数的时候        补充0
			endAmount += "0";
		amount = startAmount + "." + endAmount;//得到正确的金额
		log.info("=============【当前回调金额："+amount+"】============");
		String associatedId = qrUtil.findOrderBy(new BigDecimal(amount), phone);
		if(StrUtil.isBlank(associatedId)) {
			log.info("【商户交易订单失效，或订单匹配不正确】");	
			return Result.buildFailMessage("商户交易订单失效，或订单匹配不正确");
		}
		DealOrder order = dealOrderDao.findOrderByAssociatedId(associatedId);
		if(ObjectUtil.isNull(order)) {
			log.info("【通过商户订单号无法查询到码商交易订单号，当前交易订单号："+associatedId+"】");	
			return Result.buildFailMessage("通过商户订单号无法查询到码商交易订单号，当前交易订单号："+associatedId+"");
		}
		Result orderDealSu = orderUtil.orderDealSu(order.getOrderId(), ip);
		ThreadUtil.execute(()->{
			if(orderDealSu.isSuccess()) {
				notifyUtil.sendMsg(order.getOrderId());
			}
		});
		if(!orderDealSu.isSuccess()) 
			Result.buildFailMessage("回调失败,订单修改失败");
		return Result.buildSuccessResult("回调成功", order.getOrderId());
	}
	@PostMapping(PayApiConstant.Alipay.MEDIUM_API+PayApiConstant.Alipay.FIND_MEDIUM_IS_DEAL)
	public List<Medium> findIsDealMedium(String mediumType, String code) {
		/**
		 * ##############################################
		 * 1，根据code值获取对应的所有支付媒介  code值即为自己的顶级代理账号
		 * 2，如果code值为空 则获取所有的支付媒介
		 */
		log.info("【远程调用获取初始化数据，传递参数为："+mediumType+"，code  = "+code+"】");
		if(StrUtil.isBlank(code)) {
			//获取所有的支付媒介
			List<Medium> mediumList = mediumServiceImpl.findMediumByType(mediumType);
			return mediumList;
		}else {
			List<Medium> mediumList = mediumServiceImpl.findMediumByType(mediumType,code);
			return mediumList;
		}
	}
	
	/****
	 * <p>人工加扣款接口</p>
	 * @param param					加扣款订单
	 * @param request					请求
	 * #############
	 * <li>如果是加款订单，且状态<strong>orderStatus</strong>字段为【成功】,修改订单状态为【成功】调用资金处理方法</li>
	 * <li>如果是加款订单，且状态<strong>orderStatus</strong>字段为【失败】,修改订单状态为失败</li>
	 * <li>如果是减款款订单，且状态<strong>orderStatus</strong>字段为【处理中】,订单状态不做修改，直接调用资金扣款方法</li>
	 * <li>如果是减款款订单，且状态<strong>orderStatus</strong>字段为【成功】,订单状态修改为成功</li>
	 * <li>如果是减款款订单，且状态<strong>orderStatus</strong>字段为【失败】,订单状态修改为失败，调用退还资金修改方法</li>
	 */
	@Transactional
	@PostMapping(PayApiConstant.Alipay.ACCOUNT_API+PayApiConstant.Alipay.AMOUNT+"/{param:.+}")
	public Result addAmount(@PathVariable("param") String param, HttpServletRequest request) {
		log.info("【后台人员请求人工加减款的资金处理的方法参数为："+param+"】");
		Map<String, Object> stringObjectMap = RSAUtils.retMapDecode(param, SystemConstants.INNER_PLATFORM_PRIVATE_KEY);
		if(CollUtil.isEmpty(stringObjectMap)) {
			log.info("【参数解密为空】");
			return Result.buildFailMessage("参数为空");
		}
		Object orderId = stringObjectMap.get("orderId");
		if(ObjectUtil.isNull(orderId))
			return Result.buildFailMessage("订单号为空");
		Object orderStatus = stringObjectMap.get("orderStatus");
		if(ObjectUtil.isNull(orderStatus))
			return Result.buildFailMessage("订单状态为空");
		Object approval = stringObjectMap.get("approval");
		if(ObjectUtil.isNull(approval))
			return Result.buildFailMessage("审核人为空");
		Object comment = stringObjectMap.get("comment");
		if(ObjectUtil.isNull(comment))
			return Result.buildFailMessage("审核意见为空");
		Amount amount =  amountDao.findOrder(orderId.toString());
		if(ObjectUtil.isNull(amount))
			return Result.buildFailMessage("当前订单不存在");
		log.info("【当前调用人工资金处理接口，当前订单号："+orderId+"】");
		String clientIP = HttpUtil.getClientIP(request);
		if(StrUtil.isBlank(clientIP))
			return Result.buildFailMessage("当前使用代理服务器 或是操作ip识别出错，不允许操作");
		String amountType = amount.getAmountType();
		String oldStatus = amount.getOrderStatus();//订单原始状态
		if(!DeductStatusEnum.DEDUCT_STATUS_PROCESS.matches(Integer.parseInt(oldStatus))){//状态不相等，说明订单已经被处理
			return Result.buildFailMessage("订单已被处理，不允许重复操作");
		}
		switch (amountType) {
		case Common.Deal.AMOUNT_ORDER_ADD :
			if(orderStatus.equals(Common.Deal.AMOUNT_ORDER_SU)) {//加款订单成功，
				int a = amountDao.updataOrder( orderId.toString() ,  orderStatus.toString(), approval.toString(), comment.toString());
				if(a > 0 && a< 2) {
					UserFund userFund = userInfoServiceImpl.findUserFundByAccount(amount.getUserId());
					Result addAmountAdd = amountUtil.addAmountAdd(userFund, amount.getAmount());
					if(addAmountAdd.isSuccess()) {
							Result addAmount = amountRunUtil.addAmount(amount, clientIP);
							if(addAmount.isSuccess()) {
							logUtil.addLog(request, "当前发起加钱操作，加款订单号："+amount.getOrderId()+"，加款成功，加款用户："+amount.getUserId()+"，操作人："+amount.getAccname()+"", amount.getAccname());
							return Result.buildSuccessMessage("操作成功");
						}
					}
				}
			}else if(orderStatus.equals(Common.Deal.AMOUNT_ORDER_ER)) {//加款失败
				int a = amountDao.updataOrder( orderId.toString() ,  orderStatus.toString(), approval.toString(), comment.toString());
				if(a > 0 && a< 2) {
					logUtil.addLog(request, "当前发起订单修改操作，加款订单号："+amount.getOrderId()+"，加款订单置为失败，加款用户："+amount.getUserId()+"，操作人："+amount.getAccname()+"", amount.getAccname());
					return Result.buildSuccessMessage("操作成功");
				}
			}
			return Result.buildFailMessage("人工处理加扣款失败");
		case Common.Deal.AMOUNT_ORDER_DELETE :
			if(orderStatus.equals(Common.Deal.AMOUNT_ORDER_SU)) {//减款订单成功，
				int a = amountDao.updataOrder( orderId.toString() ,  orderStatus.toString(), approval.toString(), comment.toString());
				if(a > 0 && a< 2) {
					logUtil.addLog(request, "当前发起订单修改操作，减款订单号："+amount.getOrderId()+"，减款订单置为成功，减款用户："+amount.getUserId()+"，操作人："+amount.getAccname()+"", amount.getAccname());
					return Result.buildSuccessMessage("操作成功");
				} 
			}else if(orderStatus.equals(Common.Deal.AMOUNT_ORDER_ER)) {//减款失败，资金退回
				int a = amountDao.updataOrder( orderId.toString() ,  orderStatus.toString(), approval.toString(), comment.toString());
				if(a > 0 && a< 2) {
					UserFund userFund = userInfoServiceImpl.findUserFundByAccount(amount.getUserId());
					Result addAmountAdd = amountUtil.addAmountAdd(userFund, amount.getAmount());
					if(addAmountAdd.isSuccess()) {
						Result deleteAmount = amountRunUtil.addAmount(amount, clientIP,"扣款失败，资金退回退回");
						if(deleteAmount.isSuccess()) {
							logUtil.addLog(request, "当前扣款订单置为失败，资金原路退回，扣款订单号："+amount.getOrderId()+"，扣款用户："+amount.getUserId()+"，操作人："+amount.getAccname()+"", amount.getAccname());
							return Result.buildSuccessMessage("操作成功");
						}
					}
				}
			}else if(orderStatus.equals(Common.Deal.AMOUNT_ORDER_HE)) {
				int a = amountDao.updataOrder( orderId.toString() ,  orderStatus.toString(), approval.toString(), comment.toString());
				if(a > 0 &&  a < 2) {
					UserFund userFund = userInfoServiceImpl.findUserFundByAccount(amount.getUserId());
					Result deleteAmount2 = amountUtil.deleteAmount(userFund, amount.getAmount());
					if(deleteAmount2.isSuccess()) {
						Result deleteAmount = amountRunUtil.deleteAmount(amount, clientIP);
						if(deleteAmount.isSuccess()) {
							logUtil.addLog(request, "当前发起扣款操作，扣款订单号："+amount.getOrderId()+"，扣款成功，扣款用户："+amount.getUserId()+"，操作人："+amount.getAccname()+"", amount.getAccname());
							return Result.buildSuccessMessage("操作成功");
						}
					}
				}
			}
			return Result.buildFailMessage("人工处理加扣款失败");
		default:
			return Result.buildFailMessage("人工处理加扣款失败");
		}
	}




	@PostMapping(PayApiConstant.Alipay.ORDER_API+PayApiConstant.Alipay.ORDER_ENTER_ORDER+"/{param:.+}")
	public Result enterOrder(@PathVariable("param") String param, HttpServletRequest request) {
		log.info("【请求交易的终端用户交易请求参数为："+param+"】");
		Map<String, Object> stringObjectMap = RSAUtils.retMapDecode(param, SystemConstants.INNER_PLATFORM_PRIVATE_KEY);
		if(CollUtil.isEmpty(stringObjectMap)) {
			log.info("【参数解密为空】");
			return Result.buildFailMessage("参数为空");
		}
		Object obj = stringObjectMap.get("orderId");
		if(ObjectUtil.isNull(obj))
			return Result.buildFailMessage("未识别当前订单号");
		Object sta = stringObjectMap.get("orderStatus");
		if(ObjectUtil.isNull(sta))
			return Result.buildFailMessage("未识别当前订单状态");
		Object user = stringObjectMap.get("userName");
		if(ObjectUtil.isNull(user))
			return Result.buildFailMessage("未识别当前操作人");
		String orderId = obj.toString();//订单号
		String orderstatus = sta.toString();//将要改变订单状态
		String userop = user.toString();//操作人
		log.info("【当前调用人工处理订单接口，当前订单号："+orderId+"，当前修改订单状态："+orderstatus+"，当前操作人："+userop+"】");
		DealOrder order  =  dealOrderDao.findOrderByOrderId(orderId);
		String clientIP = HttpUtil.getClientIP(request);
		if(StrUtil.isBlank(clientIP))
			return Result.buildFailMessage("当前使用代理服务器 或是操作ip识别出错，不允许操作");
		if(ObjectUtil.isNull(order))
			return Result.buildFailMessage("当前订单不存在");
		if(order.getOrderStatus().equals(Common.Order.DealOrder.ORDER_STATUS_ER.toString()) || order.getOrderStatus().equals(Common.Order.DealOrder.ORDER_STATUS_SU.toString()))
			return Result.buildFailMessage("当前订单状态不允许操作");
		if(orderstatus.equals(Common.Order.DealOrder.ORDER_STATUS_ER.toString())) {
			Result orderDealEr = orderUtil.orderDealEr(orderId, "后台人员置交易订单失败，操作人："+userop+"", clientIP);
			if(orderDealEr.isSuccess())
				return Result.buildSuccessMessage("操作成功");
			else
				return orderDealEr;
		} else if (orderstatus.equals(Common.Order.DealOrder.ORDER_STATUS_SU.toString())) {
			Result orderDealSu = orderUtil.orderDealSu(orderId, clientIP, userop);
			if(orderDealSu.isSuccess())
				return Result.buildSuccessMessage("操作成功");
			else
				return orderDealSu;
		}
		return Result.buildFailMessage("操作失败");
	}

	/**
	 * 后台减款申请生成订单，从用户账户里预扣款，生成流水
	 * @param param
	 * @param request
	 * @return
	 */

	@Transactional
	@PostMapping(PayApiConstant.Alipay.ACCOUNT_API+PayApiConstant.Alipay.GENERATE_ORDER_DEDUCT+"/{param:.+}")
	public Result generateOrderDeduct(@PathVariable("param") String param, HttpServletRequest request){
		log.info("【请求交易的终端用户交易请求参数为："+param+"】");
		Map<String, Object> stringObjectMap = RSAUtils.retMapDecode(param, SystemConstants.INNER_PLATFORM_PRIVATE_KEY);
		Amount alipayAmount = new Amount();
		if(CollUtil.isEmpty(stringObjectMap)) {
			log.info("【参数解密为空】");
			return Result.buildFailMessage("参数为空");
		}
		Object userId = stringObjectMap.get("userId");
		if(ObjectUtil.isNull(userId))
			return Result.buildFailMessage("用户ID不能为空");
		alipayAmount.setUserId(userId.toString());
		Object orderId = stringObjectMap.get("orderId");
		if(ObjectUtil.isNull(orderId))
			return Result.buildFailMessage("订单号不能为空");
		alipayAmount.setOrderId(orderId.toString());
		Object orderStatus = stringObjectMap.get("orderStatus");
		if(ObjectUtil.isNull(orderStatus))
			return Result.buildFailMessage("订单状态为空");
		alipayAmount.setOrderStatus(orderStatus.toString());
		Object amount = stringObjectMap.get("amount");
		if(ObjectUtil.isNull(amount))
			return Result.buildFailMessage("减款金额不能为空");
		alipayAmount.setAmount(new BigDecimal(amount.toString()));
		Object dealDescribe = stringObjectMap.get("dealDescribe");
		if(ObjectUtil.isNull(dealDescribe))
			return Result.buildFailMessage("扣款描述不能为空");
		alipayAmount.setDealDescribe(dealDescribe.toString());
		Object amountType = stringObjectMap.get("amountType");
		if(ObjectUtil.isNull(amountType))
			return Result.buildFailMessage("申请类型不能为空");
		alipayAmount.setAmountType(amountType.toString());
		Object accname = stringObjectMap.get("accname");
		if(ObjectUtil.isNull(accname))
			return Result.buildFailMessage("申请人不能为空");
		alipayAmount.setAccname(accname.toString());
		alipayAmount.setActualAmount(new BigDecimal(amount.toString()));
		String clientIP = HttpUtil.getClientIP(request);
		if(StrUtil.isBlank(clientIP))
			return Result.buildFailMessage("当前使用代理服务器 或是操作ip识别出错，不允许操作");
		UserFund userFund = userInfoServiceImpl.findUserFundByAccount(userId.toString());
		if (userFund == null) {
			throw new BusinessException("此用户不存在");
		}
		BigDecimal balance = userFund.getRechargeNumber();
		BigDecimal deduct = new BigDecimal(amount.toString());
		if (balance.compareTo(deduct) > -1) {//余额充足
//			deduct = deduct.abs().negate();
//			//更新账户余额
//			int i = userInfoServiceImpl.updateBalanceById(userFund.getId(), deduct, userFund.getVersion());
//			if (i == 1){//生成流水
//
//			}
			Result deleteAmount2 = amountUtil.deleteAmount(userFund, deduct);
			if(deleteAmount2.isSuccess()) {
				Result deleteAmount = amountRunUtil.deleteAmount(alipayAmount, clientIP);
				if(deleteAmount.isSuccess()) {
					int i = userInfoServiceImpl.insertAmountEntitys(alipayAmount);
					if (i == 1)
						return Result.buildSuccessMessage("创建订单成功");
					else
						return Result.buildFailMessage("创建订单失败");
				}
			}
		}else{//余额不足
			return Result.buildFailMessage("操作失败，账户余额不足");
		}
		return null;
	}
}
