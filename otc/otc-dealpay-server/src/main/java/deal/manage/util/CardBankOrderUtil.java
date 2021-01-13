package deal.manage.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import deal.config.redis.RedisUtil;
import deal.manage.bean.*;
import deal.manage.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.api.dealpay.Common;
import otc.bean.dealpay.Recharge;
import otc.result.Result;
import otc.util.number.Number;

import java.math.BigDecimal;
import java.util.*;
@Component
public class CardBankOrderUtil {
	private static final Log log = LogFactory.get();
	private static final String SU = "SU";//是订单成功
	private static final String ER = "ER";//订单失败
	@Autowired OrderService orderServiceImpl;
	@Autowired AmountUtil amountUtil;
	@Autowired WithdrawService withdrawServiceImpl;
	@Autowired UserInfoService userInfoServiceImpl;
	@Autowired UserRateService userRateServiceImpl;
	@Autowired RechargeService rechargeServiceImpl;
	@Autowired BankUtil bankUtil;
	@Autowired RedisUtil redisUtil;
	@Autowired BankListService bankListServiceImpl;
	@Autowired OrderUtil orderUtil;
	
	
	
	/**
	 * <p>系统成功</p>
	 * @param orderId			订单号
	 * @param ip				操作ip
	 * @return
	 */
	public Result updateOrderSu(String orderId, String ip) {
		return updataOrderStatusSu(orderId, false, "", ip);
	}
	
	
	/**
	 * <p>人工置订单为成功</p>
	 * @param orderId						订单号
	 * @param ip							订单ip
	 * @param operation						操作人
	 * @return
	 */
	public Result updateOrderSu(String orderId, String ip,String operation) {
		return updataOrderStatusSu(orderId, true,operation, ip);
	}
	
	/**
	 * <p>手动失败</p>
	 * @param orderId				订单号
	 * @param operation				操作人
	 * @param ip					操作ip
	 * @return
	 */
	public Result updataOrderErOperation(String orderId,String operation, String ip) { 
		return updataOrderStatusEr(orderId, true,operation, ip);
	}
	/**
	 * <p>系统失败</p>
	 * @param orderId			订单号
	 * @param ip				ip
	 * @return
	 */
	public Result updataOrderEr(String orderId, String ip) {//系统失败
		return updataOrderStatusEr(orderId, false,"", ip);
	}
	
	/**
	 * <p>后台手动置为失败</p>
	 * @param orderId				订单号
	 * @param flag					
	 * @param operation
	 * @param ip
	 * @return
	 */
	private  Result updataOrderStatusEr(String orderId,boolean flag, String operation, String ip) {
		return updateOrder(orderId, flag, operation, ip, ER);
	}
	private  Result updataOrderStatusSu(String orderId,boolean flag, String operation, String ip) {
		return updateOrder(orderId, flag, operation, ip, SU);
	}
	/**
	 * <p>订单操作核心调用类</p>
	 * @param orderId						订单号	
	 * @param flag							true  人工操作    false   系统自动操作
	 * @param operation						如果是人工操作   传参为操作人
	 * @param ip							操作ip
	 * @param status						操作 类型  置为成功  置为失败
	 * @return
	 */
	
	private Result updateOrder(String orderId,boolean flag, String operation, String ip,String status ) {
		DealOrder order = orderServiceImpl.findOrderByOrderId(orderId);
		if (ObjectUtil.isNull(order)) {
			return Result.buildFailMessage("当前订单不存在");
		}
		switch (status) {
			case SU://订单置为成功的方法
				/**
				 * ##########################
				 * 1,修改订单状态
				 * 2,修改账户金额
				 * 3,生成对应流水
				 * 4,更新对应缓存规则
				 */
				//1>当订单为成功或者失败的时候不可以修改订单状态也无法做订单变更
				if (order.equals(Common.Order.DealOrder.ORDER_STATUS_ER) || order.equals(Common.Order.DealOrder.ORDER_STATUS_SU)) {
					return Result.buildFailMessage("当前订单状态不允许改变");
				}
				Result orderDeal = orderUtil.orderDeal(orderId, flag, operation, ip);
				if (orderDeal.isSuccess()) {
					return orderDeal;
				}
				return Result.buildFail();
			case ER://订单置为失败的方法
				/**
				 * ##########################
				 * 1,修改订单状态
				 * 4,更新对应缓存规则
				 */
				boolean updateOrderStatus = false;
				if (flag && StrUtil.isBlank(operation)) {
					return Result.buildFailMessage("请填写操作人");
				}
				if (flag) //人工操作
				{
					updateOrderStatus = orderServiceImpl.updateOrderStatus(orderId, Common.Order.DealOrder.ORDER_STATUS_ER, operation + "，手动操作为成功");
				} else {
					updateOrderStatus = orderServiceImpl.updateOrderStatus(orderId, Common.Order.DealOrder.ORDER_STATUS_ER);
				}
				if (!updateOrderStatus) {
					return Result.buildFailMessage("订单变更失败");
				}
				return Result.buildFailMessage("操作失败");
		}
		return Result.buildFail();
	}
	/**
	 * <p>卡商入款订单生成</p>
	 * @param orderId					
	 * @param flag
	 * @return
	 */
	public Result createBankOrderR( String orderId)   {
		Recharge recharge = rechargeServiceImpl.findOrderId(orderId);
		log.info("【进入卡商交易订单生成类：当前交易金额为 = "+recharge.getAmount()+",当前订单类型为:卡商入款】");
		BankList clickBnak = null ; 
		DealOrder order = new DealOrder();
		order.setOrderId(Number.getBDR());
		clickBnak = clickBank(recharge.getAmount(),order.getOrderId(),recharge.getWeight());
		if (ObjectUtil.isNull(clickBnak))// 无银行卡 订单创建失败
		{
			return Result.buildFailMessage("暂无入款卡商");
		}
		order.setAssociatedId(recharge.getOrderId());
		order.setDealAmount(recharge.getAmount());
		order.setOrderType(Common.Order.DealOrder.DEAL_ORDER_R);
		order.setExternalOrderId(recharge.getOrderId());
		order.setNotify(recharge.getNotfiy());
		order.setIsNotify("NO");
		order.setActualAmount(recharge.getAmount());
		order.setOrderStatus(Common.Order.DealOrder.ORDER_STATUS_DISPOSE);
		order.setOrderAccount(recharge.getUserId());
		order.setOrderQr(clickBnak.getBankcardId());
		order.setOrderQrUser(clickBnak.getAccount());
		order.setBack(recharge.getBackUrl());
		/**
		 * 卡商入款结算费率
		 */
		UserRate rate = userRateServiceImpl.findUserRateR(clickBnak.getAccount());
		order.setDealFee(rate.getFee().multiply(recharge.getAmount()));
		order.setFeeId(rate.getId());
		boolean addOrder = orderServiceImpl.addOrder(order);
		if (addOrder) {
			return Result.buildSuccessResult("订单生成成功", clickBnak);
		}
		return Result.buildFailMessage("操作失败");
	}
	/**
	 * <p>根据充值订单和充值金额，选定入款的卡商</p>
	 * @param bigDecimal
	 * @param string
	 * @param orderId
	 * @return
	 */
	private BankList clickBank(BigDecimal amount, String string, String orderId) {
		List<BankList> bankList = bankUtil.findBank(amount);
		if (bankList.size() <= 0) {
			return null;
		}
		if (CollUtil.isEmpty(bankList)) {
			return null;
		}
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (BankList bean : bankList) {
			Map<Object, Object> hmget = redisUtil.hmget(bean.getBankcardId());
			map.put(bean.getBankcardId(), hmget.size());
		}
		Integer count = 0;
		int i = 0;
		String cardbank = "";
		Set<String> keySet = map.keySet();
		for (String cardId : keySet) {
			i++;
			if(i==1) {
				if (count <= map.get(cardId)) {
					count = map.get(cardId);
				}
			} else {
				if (count >= map.get(cardId)) {
					count = map.get(cardId);
				}
			}
		}
		log.info("当前银行卡对应次数:" + map.toString());
		List<String> list = new ArrayList<String>();
		for (String cardId : keySet) {
			if (count.equals(map.get(cardId))) {
				list.add(cardId);
			}
		}
		Map<String, BigDecimal> amounMap = new HashMap<String, BigDecimal>();
		if (0 == list.size()) {
			return null;
		} else if (list.size() == 1) {
			cardbank = CollUtil.getFirst(list);//存在次数最小的银行卡
		} else if (list.size() > 1) {
			for (String bankCard : list) {
				amounMap.put(bankCard, getBankAmount(bankCard));
			}
			int j = 0;
			log.info("当前金额银行卡对应金额:" + amounMap.toString());
			Set<String> keySet2 = amounMap.keySet();
			BigDecimal money = new BigDecimal(0);
			for (String bankcard : keySet2) {
				j++;
				if (j == 1) {
					if (money.compareTo(amounMap.get(bankcard)) == -1) {
						money = amounMap.get(bankcard);
					} else if (money.compareTo(amounMap.get(bankcard)) == 1) {
						money = amounMap.get(bankcard);
					}
				}
			}
			for (String bankcard : keySet2) {
				if (money.compareTo(amounMap.get(bankcard)) == 0) {
					cardbank = bankcard;
				}
			}
		}
		if(StrUtil.isNotBlank(cardbank)) {
			BankList bankInfo = bankListServiceImpl.findBankInfoNo(cardbank);
			redisUtil.hset(bankInfo.getAccount(), orderId   ,amount.toString(),1800);
			redisUtil.hset(bankInfo.getBankcardId(), orderId   ,amount.toString(),1800);
			return bankInfo;
		}
		return null;
	}
	/**
	 * <p>获取银行卡交易金额【1800秒内】</p>
	 * @param bankCard
	 * @return
	 */
	public BigDecimal getBankAmount(String bankCard) {
		BigDecimal amount = new BigDecimal(0);
		Map<Object, Object> hmget = redisUtil.hmget(bankCard);//用户的虚拟hash金额缓存  key = 用户  +  时间     value  =  金额
		Set<Object> keySet = hmget.keySet();
		for(Object obj : keySet) {
			Object object = hmget.get(obj);
			BigDecimal money = new BigDecimal(object.toString());
			amount = amount.add(money);
		}
		return amount;
	}
	/**
	 * <p>生成卡商的交易订单</p>
	 * <p><strong>出款</strong></p>
	 * @param orderId			代付订单号
	 * ####################################################{@link #clickBank(List, BigDecimal)}
	 *	这里的卡商出款有如下几种情况
	 * <li>1,码商代付</li>
	 * <li>2,其他商户代付</li>
	 * @return
	 */
	public Result createBankOrderW(String orderId) {
		Withdraw orderWit = withdrawServiceImpl.findOrderId(orderId);
		log.info("【进入卡商交易订单生成类：当前交易金额为 = "+orderWit.getAmount()+",当前订单类型为:卡商出款】");
		DealOrder order = new DealOrder();
		order.setAssociatedId(orderWit.getOrderId());
		order.setOrderId(Number.getBDC());
		order.setDealAmount(orderWit.getAmount());
		order.setOrderType(Common.Order.DealOrder.DEAL_ORDER_C);
		order.setDealDescribe("卡商交易订单【出款】订单");
		order.setOrderAccount(orderWit.getUserId());
		order.setActualAmount(orderWit.getAmount());
		UserFund userFund = null;
		if (StrUtil.isBlank(orderWit.getWeight())) //供应标识为空    所有卡商任意选择
		{
			userFund = findUserInfo(null, orderWit.getAmount());
		} else //根据权重选择卡商出款
		{
			userFund = findUserInfo(orderWit.getWeight(), orderWit.getAmount());
		}
		if (ObjectUtil.isNull(userFund)) {
			return Result.buildFailMessage("无法找到符合出款条件卡商");
		}
		if (StrUtil.isBlank(orderWit.getAppOrderId())) {
			order.setExternalOrderId(orderWit.getOrderId());
		} else {
			order.setExternalOrderId(orderWit.getAppOrderId());
		}
		order.setNotify(orderWit.getNotify());
		order.setIsNotify("NO");
		order.setOrderStatus(Common.Order.DealOrder.ORDER_STATUS_DISPOSE);
		order.setOrderAccount(orderWit.getUserId());
		String userId = userFund.getUserId();
		log.info("【当前出款用户：" + userId + "，查询当前卡商的出款费率】");
		UserRate rate = userRateServiceImpl.findUserRateC(userId);
		BigDecimal fee = rate.getFee();
		log.info("【当前出款用户：" + userId + "，当前卡商的出款结算费率为：" + fee + "】");
		//计算当前订单卡商的收益
		order.setDealFee(fee.multiply(orderWit.getAmount()));
		order.setOrderQrUser(userId);
		order.setOrderQr(orderWit.getBankNo());//出款银行卡
		order.setFeeId(rate.getId());
		boolean addOrder = orderServiceImpl.addOrder(order);
		if (addOrder) {
			return Result.buildSuccessMessage("订单生成成功");
		}
		return Result.buildFail();
	}
	/**
	 *	<p>获取出款卡商</p>
	 * @param weight			关于选择出款卡商的权重【卡商订代账号】
	 * @param amount 
	 * @return
	 */
	private UserFund findUserInfo(String weight, BigDecimal amount) {
		/**
		 * 1,权重为空 不做权重选择【权重及为卡商顶代账号】
		 * 2,用户状态打开
		 * 3,用户入款金额大于 当前交易金额
		 */
		if (StrUtil.isBlank(weight)) {
			return findUserInfo(amount);
		}
		String[] split = weight.split(",");
		List<UserFund> userFundList = userInfoServiceImpl.findUserByWeight(split);
		Collections.sort(userFundList, new Comparator<UserFund>() {
			@Override
			public int compare(UserFund o1, UserFund o2) {
				return o1.getTodayDealAmountR().subtract(o2.getTodayDealAmountC()).compareTo(amount);
			}
		});
		return CollUtil.getFirst(userFundList);
	}
	
	private UserFund findUserInfo( BigDecimal amount) {
		List<UserFund> userFundList = userInfoServiceImpl.findUserFund();
		Collections.sort(userFundList,new Comparator<UserFund>() {
			@Override
			public int compare(UserFund o1, UserFund o2) {
				return o1.getTodayDealAmountR().subtract(o2.getTodayDealAmountC()).compareTo(amount);
			}
		});
		return  CollUtil.getFirst(userFundList);
	}
}
