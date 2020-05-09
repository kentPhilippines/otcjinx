package alipay.manage.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import alipay.config.thread.ThreadConnection;
import alipay.config.thread.TransactionBusiness;
import alipay.config.thread.TransactionBusinessManager;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.UserRate;
import alipay.manage.mapper.DealOrderAppMapper;
import alipay.manage.mapper.RechargeMapper;
import alipay.manage.mapper.UserRateMapper;
import alipay.manage.mapper.WithdrawMapper;
import alipay.manage.service.CorrelationService;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import otc.api.alipay.Common;
import otc.bean.alipay.OrderDealStatus;
import otc.bean.dealpay.Recharge;
import otc.bean.dealpay.Withdraw;
import otc.exception.order.OrderException;
import otc.result.Result;

@Component
public class OrderUtil {
	Logger log = LoggerFactory.getLogger(OrderUtil.class);
	@Autowired OrderService orderServiceImpl;
	@Autowired AmountUtil amountUtil;
	@Autowired AmountRunUtil amountRunUtil;
	@Autowired UserInfoService userInfoServiceImpl;
	@Autowired RechargeMapper rechargeDao;
	@Autowired WithdrawMapper withdrawDao;
	@Autowired DealOrderAppMapper dealOrderAppDao;
	@Autowired UserRateMapper userRateDao;
	@Autowired RiskUtil riskUtil;
	@Autowired CorrelationService correlationServiceImpl;
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
	@Transactional
	public Result withrawOrderSu(String orderId, String approval, String comment) {
		Withdraw order = withdrawDao.findWitOrder(orderId);
		if (order == null) {
			return Result.buildFailMessage("平台订单号不存在");
		}
		if (!Common.Order.Wit.ORDER_STATUS_YU.equals(order.getOrderStatus())) {
			return Result.buildFailMessage("订单已被处理，不允许操作");
		}
		order.setApproval(approval);
		order.setComment(comment);
		return withrawOrderSu(order);
	}
	
	
	/**
	 * <p>代付订单置为失败【这里只能是人工操作】</p>
	 * @param orderId			这里只能是人工操作
	 * @param ip				操作 ip
	 * @return
	 */
	@Transactional
	public Result withrawOrderEr(String orderId, String approval, String comment, String ip) {
		Withdraw order = withdrawDao.findWitOrder(orderId);
		if (order == null) {
			return Result.buildFailMessage("平台订单号不存在");
		}
		if (!Common.Order.Wit.ORDER_STATUS_YU.equals(order.getOrderStatus())) {
			return Result.buildFailMessage("订单已被处理，不允许操作");
		}
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
	
    @Resource(name = "platformTransactionManager")
    private PlatformTransactionManager platformTransactionManager;
	@SuppressWarnings("unused")
	public  Result updataDealOrderSu(String orderId,String msg , String ip ,Boolean flag) {
		if(StrUtil.isBlank(orderId) || StrUtil.isBlank(ip)) 
			return Result.buildFailMessage("必传参数为空");
		DealOrder order = orderServiceImpl.findOrderByOrderId(orderId);
		if(order.getOrderStatus().toString().equals(OrderDealStatus.成功.getIndex().toString()))
			return Result.buildFailMessage("当前订单不支持修改");
		if(order.getOrderStatus().toString().equals(OrderDealStatus.失败.getIndex().toString()))
			return Result.buildFailMessage("当前订单不支持修改");
		 /**管理线程连接*/
        ThreadConnection threadConnection = new ThreadConnection();
        /**事务业务管理*/
        TransactionBusinessManager tbm = new TransactionBusinessManager();
        ExecutorService es = Executors.newFixedThreadPool(2); //这里的线程池需要优化
        es.execute(()->{
        	try{
        	Result rs = tbm.execute(new TransactionBusiness<Result>() {
  				@Override
  				public PlatformTransactionManager getPlatformTransactionManager() { return platformTransactionManager; }
  				@Override
  				public ThreadConnection getThreadConnection() { return threadConnection; }
  				/**
  				 * 业务执行
  				 * @return
  				 */
  				@Override
  				public Result doInTransaction() {
  					 Result dealAmount = dealAmount(order, ip, flag,msg);
  					 if(!dealAmount.isSuccess())
   						throw new OrderException("订单修改异常", null);
  					 return dealAmount;
  				}
  				});
        	   } catch (Exception e) {
        		   threadConnection.rollback();
        	   }
        	});
        es.execute(()->{
        	  	try{
        	Result rs = tbm.execute(new TransactionBusiness<Result>() {
  				@Override
  				public PlatformTransactionManager getPlatformTransactionManager() { return platformTransactionManager; }
  				@Override
  				public ThreadConnection getThreadConnection() { return threadConnection; }
  				/**
  				 * 业务执行
  				 * @return
  				 */
  				@Override
  				public Result doInTransaction() {
  					 Result dealAmount = enterOrderApp(order.getAssociatedId(), ip, flag);
  					 if(!dealAmount.isSuccess())
  						throw new OrderException("订单修改异常", null);
  					return dealAmount;
  				}
  				});
        	    } catch (Exception e) {
         		   threadConnection.rollback();
          }
        });
        es.shutdown();
        /**等待所有线程池执行完毕*/
        for(;;) {
            if(es.isTerminated()) {
                break;
            }
        }
        /**事务提交  注意：事务执行回滚操作就不会执行事务提交操作，反之执行事务提交*/
        threadConnection.commit();
        ThreadUtil.execute( ()->{//更新风控数据 统计数据等
        	DealOrder orderSu = orderServiceImpl.findOrderByOrderId(orderId);
        	if(orderSu.getOrderStatus().toString().equals(OrderDealStatus.成功.getIndex().toString()))
        		riskUtil.orderSu(order);
        });
		return Result.buildSuccess();
	}
	
	@SuppressWarnings("unused")
	@Transactional
	public Result updateDealOrderEr(String orderId,String mag ,String ip) {
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
	 * <p>码商交易订单【渠道交易订单】置为成功的时候的资金和流水处理</p>
	 * @param order					交易订单
	 * @param ip					交易成功回调ip
	 * @param flag					true 自然流水     false  人工流水
	 * @return
	 */
	public Result dealAmount(DealOrder order,String ip ,Boolean flag,String msg){
		if(!orderServiceImpl.updateOrderStatus(order.getOrderId(),OrderDealStatus.成功.getIndex().toString(),msg))
			return Result.buildFailMessage("订单修改失败，请重新发起成功");
		//TODO 这里结算模式可选设置为  是否为顶代模式，如果为订单模式 则  只扣减顶代的   账号金额     给当前码商增加利润     
		//TODO 如果不为顶代模式 则直接按照当前现有模式  结算 
		UserInfo user = userInfoServiceImpl.findUserInfoByUserId(order.getOrderQrUser());
		UserFund userFund = new UserFund();
		userFund.setUserId(order.getOrderQrUser());
		if("3".equals(user.getUserType().toString())) {//渠道账户结算
			log.info("【进入渠道账户结算，当前渠道："+user.getUserId()+"】");
			userFund.setUserId(user.getUserId());
			Result deleteDeal = amountUtil.deleteDeal(userFund, order.getDealAmount());//扣除交易点数账户变动
			if(!deleteDeal.isSuccess())
				return deleteDeal;
			Result deleteRechangerNumber = amountRunUtil.deleteRechangerNumber(order, ip, flag);//扣除交易点数 流水生成
			if(!deleteRechangerNumber.isSuccess())
				return deleteRechangerNumber;
			log.info("【金额修改完毕，流水生成成功】");
			return Result.buildSuccessResult();
		}
		String findAgent = correlationServiceImpl.findAgent(order.getOrderQrUser());
		UserInfo userId = userInfoServiceImpl.findUserInfoByUserId(findAgent);
		if(true) {//非正常结算模式
			userFund.setUserId(userId.getUserId());
			Result deleteDeal = amountUtil.deleteDeal(userFund, order.getDealAmount());//扣除交易点数账户变动
			if(!deleteDeal.isSuccess())
				return deleteDeal;
			Result deleteRechangerNumber = amountRunUtil.deleteRechangerNumber(order, ip, flag);//扣除交易点数 流水生成
			if(!deleteRechangerNumber.isSuccess())
				return deleteRechangerNumber;
			UserRate findUserRateById = userInfoServiceImpl.findUserRateById(order.getFeeId());
			BigDecimal dealAmount = order.getDealAmount();
			log.info("【当前交易金额："+dealAmount+"】");
			BigDecimal multiply = new BigDecimal("0"); 
			Result addDeal = amountUtil.addDeal(userFund, multiply, dealAmount);
			if(!addDeal.isSuccess())
				return addDeal;
			Result addDealAmount = amountRunUtil.addDealAmount(order, ip, flag);
			if(!addDealAmount.isSuccess())
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
	public Result rechargeOrderEr( Recharge rechaege ){
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
	@Transactional
	public Result withrawOrderSu(Withdraw wit) {
		/**
		 * #########################
		 * 代付成功修改订单状态
		 */
		int a = withdrawDao.updataOrderStatus(wit.getOrderId(),wit.getApproval(),wit.getComment(),Common.Order.Wit.ORDER_STATUS_SU);
		if(a == 0  || a > 2)
			return Result.buildFailMessage("订单状态修改失败");
		return Result.buildSuccessMessage("代付成功");
	}
	/**
	 * <p>代付失败</p>
	 * @return
	 */
	@Transactional
	public Result withrawOrderEr(Withdraw wit,String ip) {
		/**
		 * ###########################
		 * 代付失败给该用户退钱
		 */
		int a = withdrawDao.updataOrderStatus(wit.getOrderId(), wit.getApproval(), wit.getComment(), Common.Order.Wit.ORDER_STATUS_ER);
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
	
	/**
	 * <p>新建代付订单时候账户扣款</p>
	 * @param orderId				代付订单号
	 * @return
	 */
	public Result withrawOrder(String orderId,String ip,Boolean flag) {
		if(StrUtil.isBlank(orderId))
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
		if(ObjectUtil.isNull(orderApp))
			return Result.buildFailMessage("当前订单号不存在");	
		if(!orderApp.getOrderStatus().toString().equals(Common.Order.DealOrderApp.ORDER_STATUS_DISPOSE))
			return Result.buildFailMessage("当前订单状态不允许操作");
		boolean status = dealOrderAppDao.updateOrderSu(orderId,Common.Order.DealOrderApp.ORDER_STATUS_SU);
		if(!status)
			return Result.buildFail();
		Integer feeId = orderApp.getFeeId();
		String appId = orderApp.getOrderAccount();
		UserFund userFund = userInfoServiceImpl.findUserFundByAccount(appId);
		Result addDealApp = amountUtil.addDealApp(userFund, orderApp.getOrderAmount());
		if(!addDealApp.isSuccess()) 
			return Result.buildFail();
		Result addDealAmountApp = amountRunUtil.addDealAmountApp(orderApp, ip, flag);
		if(!addDealAmountApp.isSuccess())
			return Result.buildFail();
		UserRate findFeeById = userRateDao.findFeeById(feeId);
		BigDecimal fee = findFeeById.getFee();
		BigDecimal multiply = orderApp.getOrderAmount().multiply(fee);
		log.info("【当前商户结算费率："+fee+"，当前商户交易金额："+orderApp.getOrderAmount()+"，当前商户收取交易手续费："+multiply+"】");
		Result deleteDeal = amountUtil.deleteDeal(userFund, multiply);
		if(!deleteDeal.isSuccess())
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
	 * @param userRateList				需要结算的费率
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
		Result findUserRateList = findUserRateList(userInfo.getAgent(),findFeeById.getPayTypr(),findFeeById,orderApp,flag,ip);
		if(findUserRateList.isSuccess()) {
			log.info("【当前订单代理商结算成功】");
			boolean flag1 = dealOrderAppDao.updateOrderIsAgent(orderApp.getOrderId(),"YES");
			return true;
		  }
		
		return false;
	}

	private Result findUserRateList(String agent, String product, UserRate rate, DealOrderApp orderApp,boolean flag, String ip) {
		 UserFund userFund = userInfoServiceImpl.findUserFundByAccount (agent);
		 UserRate userRate = userRateDao.findProductFeeBy(userFund.getUserId(), product);
		 log.info("【当前代理商为："+userRate.getUserId()+"】");
		 log.info("【当前代理商结算费率："+userRate.getFee()+"】");
		 log.info("【当前当前我方："+rate.getUserId()+"】");
		 log.info("【当前我方结算费率："+rate.getFee()+"】");
		 BigDecimal fee = userRate.getFee();
		 BigDecimal fee2 = rate.getFee();
		 BigDecimal subtract = fee2.subtract(fee);
		 log.info("【当前结算费率差为："+subtract+"】");
		 BigDecimal amount = orderApp.getOrderAmount();
		 BigDecimal multiply = amount.multiply(subtract);
		 log.info("【当前结算订单金额为："+amount+"，当前结算代理分润为："+multiply+"】");
		 log.info("【开始结算】");
		 Result addAmounProfit = amountUtil.addAmounProfit(userFund, multiply);
		 if(addAmounProfit.isSuccess()) {
			 Result addAppProfit = amountRunUtil.addAppProfit(orderApp.getOrderIp(), userFund.getUserId(), amount, ip, flag);
			 if(addAppProfit.isSuccess()) {
				 log.info("【流水成功】");
				 if(StrUtil.isNotBlank(userFund.getAgent()))
					 return findUserRateList(userFund.getAgent(), product, userRate, orderApp, flag, ip);
			 }
		 }
		return Result.buildSuccessMessage("结算成功");
	}
	
	
	
}
