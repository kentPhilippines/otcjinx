package deal.manage.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import deal.config.feign.ConfigServiceClient;
import deal.manage.bean.Amount;
import deal.manage.bean.DealOrder;
import deal.manage.bean.UserFund;
import deal.manage.mapper.AmountMapper;
import deal.manage.service.OrderService;
import deal.manage.service.RechargeService;
import deal.manage.service.UserInfoService;
import deal.manage.service.WithdrawService;
import deal.manage.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import otc.api.dealpay.Common;
import otc.bean.config.ConfigFile;
import otc.bean.dealpay.Recharge;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.common.SystemConstants;
import otc.exception.BusinessException;
import otc.result.DealBean;
import otc.result.Result;
import otc.util.RSAUtils;
import otc.util.enums.DeductStatusEnum;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

@RestController
public class Api {
	private static final Log log = LogFactory.get();
	@Autowired
	CardBankOrderUtil cardBankOrderUtil;
	@Autowired
	RechargeService rechargeServiceImpl;
	@Autowired
	WithdrawService withdrawServiceImpl;
	@Autowired
	ConfigServiceClient configServiceClientImpl;
	@Autowired
	OrderService orderServiceImpl;
	@Autowired
	UserInfoService userInfoServiceImpl;
	@Resource
	AmountMapper amountDao;
	@Autowired
	AmountUtil amountUtil;
	@Autowired
	AmountRunUtil amountRunUtil;
	@Autowired
	LogUtil logUtil;
	@Autowired
	OrderUtil orderUtil;
	private static String Url;

	/**
	 * <p>后台人员置卡商代付订单为成功或者失败</p>
	 * @param param                    加密参数
	 * @param request
	 * @return
	 */
	@Transactional
	@PostMapping(PayApiConstant.Dealpay.ACCOUNT_API+PayApiConstant.Dealpay.WIT_ORDER+"/{param:.+}")
	public Result witOrder(@PathVariable("param") String param, HttpServletRequest request) {
		log.info("【后台人员请求代付订单修改方法：" + param + "】");
		Map<String, Object> stringObjectMap = RSAUtils.retMapDecode(param, SystemConstants.INNER_PLATFORM_PRIVATE_KEY);
		if (CollUtil.isEmpty(stringObjectMap)) {
			log.info("【参数解密为空】");
			return Result.buildFailMessage("参数为空");
		}
		Object orderId = stringObjectMap.get("orderId");
		if (ObjectUtil.isNull(orderId)) {
			return Result.buildFailMessage("订单号为空");
		}
		Object orderStatus = stringObjectMap.get("orderStatus");
		if (ObjectUtil.isNull(orderStatus)) {
			return Result.buildFailMessage("订单状态为空");
		}
		Object userId = stringObjectMap.get("userId");
		if (ObjectUtil.isNull(userId)) {
			return Result.buildFailMessage("操作人为空");
		}
		String clientIP = HttpUtil.getClientIP(request);
		if (StrUtil.isBlank(clientIP)) {
			return Result.buildFailMessage("当前使用代理服务器 或是操作ip识别出错，不允许操作");
		}
		deal.manage.bean.Withdraw wit = withdrawServiceImpl.findOrderId(orderId.toString());
		if (Common.Order.Wit.ORDER_STATUS_SU.toString().equals(orderStatus.toString())) {
			ThreadUtil.execAsync(() -> {
				log.info("【当前调用代付订单置为成功接口，当前订单号：" + orderId + "，当前修改订单状态：" + wit.getOrderStatus() + "，当前操作人：" + userId + "】");
				logUtil.addLog(request, "后台人员置代付订单成功，操作人：" + userId.toString() + "", userId.toString());
			});
			return orderUtil.witSu(wit.getOrderId(), userId + "  置代付订单为成成功，操作ip ：" + clientIP);

		} else if (Common.Order.Wit.ORDER_STATUS_ER.toString().equals(orderStatus.toString())) {
			ThreadUtil.execAsync(() -> {
				log.info("【当前调用充值订单置为成功接口，当前订单号："+orderId+"，当前修改订单状态："+wit.getOrderStatus()+"，当前操作人："+userId+"】");
				logUtil.addLog(request, "后台人员置充值订单成功，操作人："+userId.toString()+"", userId.toString());
			});
			return orderUtil.witEr(wit.getOrderId(), clientIP, userId+"  置代付订单为成失败，操作ip ："+  clientIP);
		}
		return Result.buildFailMessage("订单修改失败");
	}
	
	
	/**
	 * <p>后台人员置卡商充值订单为成功或者失败</p>
	 * @param param					加密参数
	 * @param request
	 * @return
	 */
	@Transactional
	@PostMapping(PayApiConstant.Dealpay.ACCOUNT_API+PayApiConstant.Dealpay.RECHARGE_ORDER+"/{param:.+}")
	public Result recharge(@PathVariable("param") String param, HttpServletRequest request) {
		log.info("【后台人员请求充值订单修改方法：" + param + "】");
		Map<String, Object> stringObjectMap = RSAUtils.retMapDecode(param, SystemConstants.INNER_PLATFORM_PRIVATE_KEY);
		if (CollUtil.isEmpty(stringObjectMap)) {
			log.info("【参数解密为空】");
			return Result.buildFailMessage("参数为空");
		}
		Object orderId = stringObjectMap.get("orderId");
		if (ObjectUtil.isNull(orderId)) {
			return Result.buildFailMessage("订单号为空");
		}
		Object orderStatus = stringObjectMap.get("orderStatus");
		if (ObjectUtil.isNull(orderStatus)) {
			return Result.buildFailMessage("订单状态为空");
		}
		Object userId = stringObjectMap.get("userId");
		if (ObjectUtil.isNull(userId)) {
			return Result.buildFailMessage("操作人为空");
		}
		String clientIP = HttpUtil.getClientIP(request);
		if (StrUtil.isBlank(clientIP)) {
			return Result.buildFailMessage("当前使用代理服务器 或是操作ip识别出错，不允许操作");
		}
		Recharge recharge = rechargeServiceImpl.findOrderId(orderId.toString());
		if (Common.Order.Recharge.ORDER_STATUS_SU.toString().equals(orderStatus.toString())) {
			ThreadUtil.execAsync(() -> {
				log.info("【当前调用充值订单置为成功接口，当前订单号：" + orderId + "，当前修改订单状态：" + recharge.getOrderStatus() + "，当前操作人：" + userId + "】");
				logUtil.addLog(request, "后台人员置充值订单成功，操作人：" + userId.toString() + "", userId.toString());
			});
			Result rechargeOrderSu = orderUtil.rechargeOrderSu(recharge.getOrderId(), clientIP, userId + "  置充值订单为成功，操作ip ：" + clientIP);
			return rechargeOrderSu;
		} else if (Common.Order.Recharge.ORDER_STATUS_ER.toString().equals(orderStatus.toString())) {
			ThreadUtil.execAsync(() -> {
				log.info("【当前调用充值订单置为失败接口，当前订单号："+orderId+"，当前修改订单状态："+recharge.getOrderStatus()+"，当前操作人："+userId+"】");
				logUtil.addLog(request, "后台人员置充值订单失败，操作人："+userId.toString()+"", userId.toString());
			});
			Result orderEr = orderUtil.rechargeOrderEr(recharge.getOrderId(),userId+"  置充值订单为成失败，操作ip ："+  clientIP);
			return orderEr;
		}
		return Result.buildFailMessage("操作失败");
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
		log.info("【后台人员请求人工加减款的资金处理的方法参数为：" + param + "】");
		Map<String, Object> stringObjectMap = RSAUtils.retMapDecode(param, SystemConstants.INNER_PLATFORM_PRIVATE_KEY);
		if (CollUtil.isEmpty(stringObjectMap)) {
			log.info("【参数解密为空】");
			return Result.buildFailMessage("参数为空");
		}
		Object orderId = stringObjectMap.get("orderId");
		if (ObjectUtil.isNull(orderId)) {
			return Result.buildFailMessage("订单号为空");
		}
		Object orderStatus = stringObjectMap.get("orderStatus");
		if (ObjectUtil.isNull(orderStatus)) {
			return Result.buildFailMessage("订单状态为空");
		}
		Object approval = stringObjectMap.get("approval");
		if (ObjectUtil.isNull(approval)) {
			return Result.buildFailMessage("审核人为空");
		}
		Object comment = stringObjectMap.get("comment");
		if (ObjectUtil.isNull(comment)) {
			return Result.buildFailMessage("审核意见为空");
		}
		Amount amount = amountDao.findOrder(orderId.toString());
		if (ObjectUtil.isNull(amount)) {
			return Result.buildFailMessage("当前订单不存在");
		}
		log.info("【当前调用人工资金处理接口，当前订单号：" + orderId + "】");
		String clientIP = HttpUtil.getClientIP(request);
		if (StrUtil.isBlank(clientIP)) {
			return Result.buildFailMessage("当前使用代理服务器 或是操作ip识别出错，不允许操作");
		}
		String amountType = amount.getAmountType();
		String oldStatus = amount.getOrderStatus();//订单原始状态
		if (!DeductStatusEnum.DEDUCT_STATUS_PROCESS.matches(Integer.parseInt(oldStatus))) {//状态不相等，说明订单已经被处理
			return Result.buildFailMessage("订单已被处理，不允许重复操作");
		}
		switch (amountType) {
			case Common.Deal.AMOUNT_ORDER_ADD:
				if (orderStatus.equals(Common.Deal.AMOUNT_ORDER_SU)) {//加款订单成功，
					int a = amountDao.updataOrder(orderId.toString(), orderStatus.toString(), approval.toString(), comment.toString());
					if (a > 0 && a < 2) {
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
	/**
	 * 后台减款申请生成订单，从用户账户里预扣款，生成流水
	 * @param param
	 * @param request
	 * @return
	 */

	@Transactional
	@PostMapping(PayApiConstant.Alipay.ACCOUNT_API+PayApiConstant.Alipay.GENERATE_ORDER_DEDUCT+"/{param:.+}")
	public Result generateOrderDeduct(@PathVariable("param") String param, HttpServletRequest request) {
		log.info("【请求交易的终端用户交易请求参数为：" + param + "】");
		Map<String, Object> stringObjectMap = RSAUtils.retMapDecode(param, SystemConstants.INNER_PLATFORM_PRIVATE_KEY);
		Amount alipayAmount = new Amount();
		if (CollUtil.isEmpty(stringObjectMap)) {
			log.info("【参数解密为空】");
			return Result.buildFailMessage("参数为空");
		}
		Object userId = stringObjectMap.get("userId");
		if (ObjectUtil.isNull(userId)) {
			return Result.buildFailMessage("用户ID不能为空");
		}
		alipayAmount.setUserId(userId.toString());
		Object orderId = stringObjectMap.get("orderId");
		if (ObjectUtil.isNull(orderId)) {
			return Result.buildFailMessage("订单号不能为空");
		}
		alipayAmount.setOrderId(orderId.toString());
		Object orderStatus = stringObjectMap.get("orderStatus");
		if (ObjectUtil.isNull(orderStatus)) {
			return Result.buildFailMessage("订单状态为空");
		}
		alipayAmount.setOrderStatus(orderStatus.toString());
		Object amount = stringObjectMap.get("amount");
		if (ObjectUtil.isNull(amount)) {
			return Result.buildFailMessage("减款金额不能为空");
		}
		alipayAmount.setAmount(new BigDecimal(amount.toString()));
		Object dealDescribe = stringObjectMap.get("dealDescribe");
		if (ObjectUtil.isNull(dealDescribe)) {
			return Result.buildFailMessage("扣款描述不能为空");
		}
		alipayAmount.setDealDescribe(dealDescribe.toString());
		Object amountType = stringObjectMap.get("amountType");
		if (ObjectUtil.isNull(amountType)) {
			return Result.buildFailMessage("申请类型不能为空");
		}
		alipayAmount.setAmountType(amountType.toString());
		Object accname = stringObjectMap.get("accname");
		if (ObjectUtil.isNull(accname)) {
			return Result.buildFailMessage("申请人不能为空");
		}
		alipayAmount.setAccname(accname.toString());
		alipayAmount.setActualAmount(new BigDecimal(amount.toString()));
		String clientIP = HttpUtil.getClientIP(request);
		if (StrUtil.isBlank(clientIP)) {
			return Result.buildFailMessage("当前使用代理服务器 或是操作ip识别出错，不允许操作");
		}
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
					int i = amountDao.insertAmountEntitys(alipayAmount);
					if (i == 1) {
						return Result.buildSuccessMessage("创建订单成功");
					} else {
						return Result.buildFailMessage("创建订单失败");
					}
				}
			}
		}else{//余额不足
			return Result.buildFailMessage("操作失败，账户余额不足");
		}
		return null;
	}
	
	
	/**
	 * <p>后台操作卡商交易订单</p>
	 * @param param
	 * @param request
	 * @return
	 */
	@PostMapping(PayApiConstant.Alipay.ORDER_API+PayApiConstant.Alipay.ORDER_ENTER_ORDER+"/{param:.+}")
	public Result enterOrder(@PathVariable("param") String param, HttpServletRequest request) {
		log.info("【请求交易的终端用户交易请求参数为：" + param + "】");
		Map<String, Object> stringObjectMap = RSAUtils.retMapDecode(param, SystemConstants.INNER_PLATFORM_PRIVATE_KEY);
		if (CollUtil.isEmpty(stringObjectMap)) {
			log.info("【参数解密为空】");
			return Result.buildFailMessage("参数为空");
		}
		Object obj = stringObjectMap.get("orderId");
		if (ObjectUtil.isNull(obj)) {
			return Result.buildFailMessage("未识别当前订单号");
		}
		Object sta = stringObjectMap.get("orderStatus");
		if (ObjectUtil.isNull(sta)) {
			return Result.buildFailMessage("未识别当前订单状态");
		}
		Object user = stringObjectMap.get("userName");
		if (ObjectUtil.isNull(user)) {
			return Result.buildFailMessage("未识别当前操作人");
		}
		String orderId = obj.toString();//订单号
		String orderstatus = sta.toString();//将要改变订单状态
		String userop = user.toString();//操作人
		log.info("【当前调用人工处理订单接口，当前订单号：" + orderId + "，当前修改订单状态：" + orderstatus + "，当前操作人：" + userop + "】");
		DealOrder order = orderServiceImpl.findOrderByOrderId(orderId);
		String clientIP = HttpUtil.getClientIP(request);
		if (StrUtil.isBlank(clientIP)) {
			return Result.buildFailMessage("当前使用代理服务器 或是操作ip识别出错，不允许操作");
		}
		if (ObjectUtil.isNull(order)) {
			return Result.buildFailMessage("当前订单不存在");
		}
		if (order.getOrderStatus().equals(Common.Order.DealOrder.ORDER_STATUS_ER.toString()) || order.getOrderStatus().equals(Common.Order.DealOrder.ORDER_STATUS_SU.toString())) {
			return Result.buildFailMessage("当前订单状态不允许操作");
		}
		if (orderstatus.equals(Common.Order.DealOrder.ORDER_STATUS_ER.toString())) {
			ThreadUtil.execAsync(() -> {
				logUtil.addLog(request, "后台人员置交易订单失败，操作人：" + userop + "", userop);
			});
			Result updataOrderErOperation = cardBankOrderUtil.updataOrderErOperation(orderId, userop, clientIP);
			return updataOrderErOperation;
		} else if (orderstatus.equals(Common.Order.DealOrder.ORDER_STATUS_SU.toString())) {
			ThreadUtil.execAsync(() -> {
				logUtil.addLog(request, "后台人员置交易订单成功，操作人：" + userop + "", userop);
			});
			Result updateOrderSu = cardBankOrderUtil.updateOrderSu(orderId, clientIP, userop);
			return updateOrderSu;
		}
		return Result.buildFailMessage("操作失败");
	}
	/**
	 * <p>接受充值</p>
	 * @param recharge
	 * @return
	 */
	@PostMapping(PayApiConstant.Dealpay.DEAL_API+PayApiConstant.Dealpay.RECHARGE_URL)
	public Result recharge(@RequestBody Recharge recharge) {
		log.info("【接收到下游调用充值渠道】");		
		if(ObjectUtil.isNull(recharge)) {
			log.info("【当前调用参数为空】");		
			return Result.buildFailMessage("充值失败，接口调用参数为空");
		}
		log.info("【接收到下游充值参数："+recharge.toString()+"】");
		Recharge order = new Recharge();
		order.setOrderId(recharge.getOrderId());
		order.setActualAmount(recharge.getActualAmount());
		order.setAmount(recharge.getAmount());
		order.setNotfiy(recharge.getNotfiy());
		order.setChargeBankcard(recharge.getChargeBankcard());
		order.setPhone(recharge.getPhone());
		order.setUserId(recharge.getUserId());
		order.setRechargeType(Common.Order.Recharge.WIT_ACC);
		order.setWeight(recharge.getWeight());
		order.setDepositor(recharge.getDepositor());
		order.setOrderStatus(recharge.getOrderStatus());
		order.setBackUrl(recharge.getBackUrl());
		boolean a = rechargeServiceImpl.addOrder(order);
		if(a) {
			Result createBankOrderR = cardBankOrderUtil.createBankOrderR(recharge.getOrderId());
			if(createBankOrderR.isSuccess()) {
				DealOrder reOrder = orderServiceImpl.findOrderByAssociatedId(order.getOrderId());
				/**
				 * ################################一下不要修改，如要修改请联系开发人员或是你自己完全搞懂系统#######################
				 * type = 1   //下游商户 码商 打开付款界面标识
				 * type = 2   //卡商自己 充值标识
				 * type = 3   //卡商查看出款  标识
				 * 
				 */
				String url = "orderId="+reOrder.getOrderId()+"&type=1";//异常重要
				return	Result.buildSuccessResult(DealBean.DealBeanSu("获取充值渠道成功",
						configServiceClientImpl.getConfig(ConfigFile.DEAL, ConfigFile.Deal.RECHARGE_URL).getResult().toString()+url,
						createBankOrderR.getResult()));
			}
			ThreadUtil.execute(()->{
				boolean b = rechargeServiceImpl.updateStatusEr(order.getOrderId(),createBankOrderR.getMessage());
				log.info("【充值预订单失败的时候，修改订单成功】");		
			});
		}
		return Result.buildFailMessage("暂无充值渠道");
	}
	/**
	 * <p>接受代付</p>
	 * @param wit
	 * @return
	 */
	@PostMapping(PayApiConstant.Dealpay.DEAL_API+PayApiConstant.Dealpay.WITH_PAY)
	public Result wit(@RequestBody Withdraw  wit) {
		log.info("【接收到下游调用代付渠道】");		
		if(ObjectUtil.isNull(wit)) {
			log.info("【当前调用参数为空】");		
			return Result.buildFailMessage("充值失败，接口调用参数为空");
		}
		log.info("【接收到下游代付参数："+wit.toString()+"】");
		deal.manage.bean.Withdraw with = new deal.manage.bean.Withdraw();
		with.setOrderId(wit.getOrderId());
		with.setActualAmount(wit.getActualAmount());
		with.setAmount(wit.getAmount());
		with.setBankName(wit.getBankName());
		with.setBankNo(wit.getBankNo());
		with.setMobile(wit.getMobile());
		with.setNotify(wit.getNotify());
		with.setOrderStatus(wit.getOrderStatus());
		with.setAccname(wit.getAccname());
		with.setUserId(wit.getUserId());
		with.setWithdrawType(Common.Order.Wit.WIT_ACC);
		with.setWeight(wit.getWeight());
		with.setAppOrderId(wit.getAppOrderId());
		boolean order = withdrawServiceImpl.addOrder(with);
		if(order) {
			Result orderW = cardBankOrderUtil.createBankOrderW(with.getOrderId());
			if (orderW.isSuccess()) {
				return orderW;
			}

			ThreadUtil.execute(() -> {
				boolean a = withdrawServiceImpl.updateStatusEr(wit.getOrderId(), orderW.getMessage());
				log.info("【代付预订单失败的时候，修改订单成功】");
			});
		}
		return Result.buildFailMessage("代付失败"); 
	}
}
