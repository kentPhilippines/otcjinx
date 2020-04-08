package deal.manage.util;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import deal.manage.bean.DealOrder;
import deal.manage.bean.UserFund;
import deal.manage.bean.UserInfo;
import deal.manage.service.OrderService;
import deal.manage.service.RunOrderService;
import deal.manage.service.UserFundService;
import deal.manage.service.UserInfoService;
import otc.api.alipay.Common;
import otc.exception.user.UserException;
import otc.result.Result;
/**
 * <p>资金处理方法</p>
 * @author K
 */
@Component
public class AmountUtil {
	Logger log = LoggerFactory.getLogger(AmountUtil.class);
	@Autowired UserInfoService userInfoServiceImpl;
	@Autowired UserFundService userFundServiceImpl;
	@Autowired RunOrderService runorderServiceImpl;
	@Autowired OrderService orderServiceImpl;
	@Autowired AmountRunUtil amountRunUtil;
	public static final String ADD_AMOUNT_RECHARGE = "ADD_AMOUNT_RECHARGE";//资金充值
	public static final String ADD_AMOUNT_PROFIT = "ADD_AMOUNT_PROFIT";//代理利润分成
	public static final String ADD_AMOUNT = "ADD_AMOUNT";//人工加钱
	public static final String ADD_AMOUNT_DEAL = "ADD_AMOUNT_DEAL";//交易利润加钱
	public static final String ADD_AMOUNT_DEAL_APP = "ADD_AMOUNT_DEAL_APP";//交易账户加款
	public static final String DELETE_DEAL = "DELETE_DEAL";//交易减充值点数
	public static final String DELETE_AMOUNT = "DELETE_AMOUNT";//人工减钱
	public static final String DELETE_WITHDRAW = "DELETE_WITHDRAW";//提现withdraw
	public static final String DELETE_FREEZE = "DELETE_FREEZE";//冻结  FreezeBalance

	/**
	 * <p><strong>增加交易点数</strong></p>
	 * @param userFund					当前账户实体【必传字段为userId】
	 * @param balance					当前操作金额
	 * @return
	 */
	public Result addAmounRecharge(UserFund userFund , BigDecimal balance) {
		return addAmountBalance(userFund, balance, ADD_AMOUNT_RECHARGE,new BigDecimal(0),false);
	}
	/**
	 * <p><strong>增加代理商利润</strong></p>
	 * @param userFund					当前账户实体【必传字段为userId】
	 * @param balance					当前操作金额
	 * @return
	 */
	public Result addAmounProfit(UserFund userFund , BigDecimal balance) {
		return addAmountBalance(userFund, balance, ADD_AMOUNT_PROFIT,new BigDecimal(0),false);
	}
	/**
	 * <p><strong>人工加钱</strong></p>
	 * @param userFund					当前账户实体【必传字段为userId】
	 * @param balance					当前操作金额
	 * @return
	 */
	public Result addAmountAdd(UserFund userFund , BigDecimal balance ) {
		return addAmountBalance(userFund, balance, ADD_AMOUNT,new BigDecimal(0),false);
	}
	/**
	 * <p><strong>增加交易分润</strong></p>
	 * @param userFund							资金账户
	 * @param balance							记录资金【分润金额】
	 * @param dealAmount						交易金额【订单金额】
	 * @param flag						 		true   入款      false  出款
	 * @return
	 */
	public Result addDeal(UserFund userFund , BigDecimal balance, BigDecimal dealAmount ,boolean flag) {
		return addAmountBalance(userFund, balance, ADD_AMOUNT_DEAL,dealAmount , flag);
	}
	/**
	 * <p><strong>减少交易点数【交易订单置为成功调用这个方法】</strong></p>
	 * @param userFund					当前账户实体【必传字段为userId】
	 * @param balance					当前操作金额
	 * @return
	 */
	public Result deleteDeal(UserFund userFund , BigDecimal balance) {
		return deleteAmountBalance(userFund, balance, DELETE_DEAL);
	}
	/**
	 * <p><strong>码商取现减款</strong></p>
	 * @param userFund					当前账户实体【必传字段为userId】
	 * @param balance					当前操作金额
	 * @return
	 */
	public Result deleteWithdraw(UserFund userFund , BigDecimal balance) {
		return deleteAmountBalance(userFund, balance, DELETE_WITHDRAW);
	}
	/**
	 * <p>人工扣款接口</p>
	 * @param userFund
	 * @param balance
	 * @return
	 */
	public Result deleteAmount(UserFund userFund , BigDecimal balance) {
		return deleteAmountBalance(userFund, balance, DELETE_AMOUNT);
	}
	/**
	 * <p><strong>人工冻结</strong></p>
	 * @param userFund					当前账户实体【必传字段为userId】
	 * @param balance					当前操作金额
	 * @return
	 */
	public Result deleteFreeze(UserFund userFund , BigDecimal balance) {
		return deleteAmountBalance(userFund, balance, DELETE_FREEZE);
	}
	/**
	 * #####################################################################
	 * 	以下方法暂时不对外界开放
	 * #####################################################################
	 */
	/**
	 * <p>增加余额</p>
	 * @return
	 */
	public Result addAmountBalance(UserFund userFund1 , BigDecimal balance , String addType  , BigDecimal dealAmount,boolean flag) {
		UserFund userFund = userInfoServiceImpl.findUserFundByAccount(userFund1.getUserId());
			if(!clickUserFund(userFund).isSuccess())
				return Result.buildFailMessage("【资金账户存在问题】");
			if(ADD_AMOUNT_RECHARGE.equals(addType)) {//资金充值【加充值点数】
				Result addAmountRecharge = addAmountRecharge(userFund,  balance);
				if(addAmountRecharge.isSuccess()) 
					return addAmountRecharge;
				log.info("【账户余额添加失败，请查询当前时间范围内的异常情况】");
				return 	Result.buildFailMessage("【账户余额添加失败，请联系技术人员查看当前服务异常】");
			}else if(ADD_AMOUNT_PROFIT.equals(addType)) {//代理利润分成
				Result profit = addAmountAgentProfit(userFund,balance);
				if(profit.isSuccess()) 
					return profit;
				log.info("【账户代理商分润增加失败，请查询当前时间范围内的异常情况】");
				return 	Result.buildFailMessage("【账户代理商分润增加失败，请查询当前时间范围内的异常情况，请联系技术人员查询情况】");
			}else if (ADD_AMOUNT.equals(addType)) {//手动加钱
				Result addAmountRecharge = addAmountRecharge(userFund,  balance);
				if(addAmountRecharge.isSuccess()) 
					return addAmountRecharge;
				log.info("【手动加钱失败，请联系技术人员处理");
				return 	Result.buildFailMessage("【手动加钱失败，请联系技术人员处理】");
			}else if(ADD_AMOUNT_DEAL.equals(addType)) {//交易利润分成 ,统计交易笔数
				Result addAmountDeal = addAmountDeal(userFund,  balance,dealAmount,flag);
				if(addAmountDeal.isSuccess()) 
					return addAmountDeal;
				log.info("【账户余额添加失败，请查询当前时间范围内的异常情况】");
				return 	Result.buildFailMessage("【当前账户分润分润增加和利润统计失败，请联系技术人员查询情况】");
			}else if(ADD_AMOUNT_DEAL_APP.equals(addType)) {
				Result addAmountDeal = addAmountDealApp(userFund,  balance);
				if(addAmountDeal.isSuccess()) 
					return addAmountDeal;
				log.info("【账户余额添加失败，请查询当前时间范围内的异常情况】");
				return 	Result.buildFailMessage("【当前账户更新失败，请联系技术人员查询情况】");
			}
			return Result.buildFailMessage("传参异常");
	}
	public Result addAmountDealApp(UserFund userFund, BigDecimal balance) {
		BigDecimal accountBalance = userFund.getAccountBalance();//当前账户比较金额
		BigDecimal cashBalance = userFund.getCashBalance();//当前利润账户
		BigDecimal freezeBalance = userFund.getFreezeBalance();//当前冻结账户
		BigDecimal rechargeNumber = userFund.getRechargeNumber();//当前充值点数
		
		
		
		
		
		
		
	//	BigDecimal sumAgentProfit = userFund.getSumAgentProfit();//当前代理商分润  【当前订单为自己接单，不需要该字段】
	//	BigDecimal sumProfit = userFund.getSumProfit();//当前当前总的利润
	//	BigDecimal todayAgentProfit = userFund.getTodayAgentProfit();//今日代理分润  【当前订单为自己接单，无需统计该字段】
	//	BigDecimal todayProfit = userFund.getTodayProfit();//今日总利润  =  今日代理分润 + 今日接单分润
		log.info("【金额修改后账户情况：当前账户总比较金额："+accountBalance+"，当前账户充值点数："+rechargeNumber+"，当前账户利润金额："+cashBalance+"，当前账户冻结金额："+freezeBalance+"，当前账户："+userFund.getUserId()+"】");
	//以上为当前账户情况	
		rechargeNumber = rechargeNumber.add(balance);
		accountBalance = rechargeNumber.add(cashBalance).subtract(freezeBalance);
		userFund.setAccountBalance(accountBalance);
		userFund.setCashBalance(cashBalance);
		userFund.setFreezeBalance(freezeBalance);
		userFund.setRechargeNumber(rechargeNumber);
		Boolean updataAmount = userInfoServiceImpl.updataAmount(userFund);
		if(updataAmount) 
			log.info("【金额修改后账户情况：当前账户总比较金额："+accountBalance+"，当前账户充值点数："+rechargeNumber+"，当前账户利润金额："+cashBalance+"，当前账户冻结金额："+freezeBalance+"，当前账户："+userFund.getUserId()+"】");
		else {
			log.info("【账户修改失败】");
			Boolean updataStatusEr = userInfoServiceImpl.updataStatusEr(userFund.getUserId());
			if(updataStatusEr)
				log.info("【账户已修改为不可使用，当前账号为："+userFund.getUserId()+"】");
			else
				throw new UserException("账户修改异常", null);
			return Result.buildFail();
		}
		return Result.buildSuccessResult();
	}
	/**
	 * <p>账户减少</p>
	 * @param userFund
	 * @param balance
	 * @param addType
	 * @return
	 */
	public Result deleteAmountBalance(UserFund userFund , BigDecimal balance , String addType ) {
		userFund = userInfoServiceImpl.findUserFundByAccount(userFund.getUserId());
		if(!clickUserFund(userFund).isSuccess())
			return Result.buildFailMessage("【资金账户存在问题】");
		if(DELETE_DEAL.equals(addType)) {//交易减点数
			Result deductRecharge = deductRecharge(userFund, balance);
			if(deductRecharge.isSuccess()) 
				return deductRecharge;
			log.info("【账户金额扣除失败，请查询当前时间范围内的异常情况，并修改当前账户交易和资金状态为 不可用】");
			if(userInfoServiceImpl.updataStatusEr(userFund.getUserId()))
				log.info("【账户已修改为不可使用，当前账号为："+userFund.getUserId()+"】");
			else
				throw new UserException("账户修改异常", null);
			return 	Result.buildFailMessage("【当前账户余额扣除失败，请联系技术人员查询情况】");
			
		}else if(DELETE_AMOUNT.equals(addType)) {//人工减钱
			Result deductBalance = deductBalance(userFund, balance);
			if(deductBalance.isSuccess()) 
				return deductBalance;
			log.info("【账户金额扣除失败，请查询当前时间范围内的异常情况，并修改当前账户交易和资金状态为 不可用】");
			if(userInfoServiceImpl.updataStatusEr(userFund.getUserId()))
				log.info("【账户已修改为不可使用，当前账号为："+userFund.getUserId()+"】");
			else
				throw new UserException("账户修改异常", null);
			return 	Result.buildFailMessage("【当前账户余额扣除失败，请联系技术人员查询情况】");
		}else if(DELETE_WITHDRAW.equals(addType)) {
			Result withdrawBalance = withdrawBalance(userFund, balance);
			if(withdrawBalance.isSuccess()) 
				return withdrawBalance;
			log.info("【账户金额扣除失败，请查询当前时间范围内的异常情况，并修改当前账户交易和资金状态为 不可用】");
			if(userInfoServiceImpl.updataStatusEr(userFund.getUserId()))
				log.info("【账户已修改为不可使用，当前账号为："+userFund.getUserId()+"】");
			else
				throw new UserException("账户修改异常", null);
			return 	Result.buildFailMessage("【当前账户余额扣除失败，请联系技术人员查询情况】");
		}else if (DELETE_FREEZE.equals(addType)) {
			Result freezeBalance = freezeBalance(userFund, balance);
			if(freezeBalance.isSuccess()) 
				return freezeBalance;
			log.info("【账户金额冻结失败，请查询当前时间范围内的异常情况，并修改当前账户交易和资金状态为 不可用】");
			if(userInfoServiceImpl.updataStatusEr(userFund.getUserId()))
				log.info("【账户已修改为不可使用，当前账号为："+userFund.getUserId()+"】");
			else
				throw new UserException("账户修改异常", null);
			return 	Result.buildFailMessage("【当前账户余额冻结失败，请联系技术人员查询情况】");
			
		}
		return Result.buildFailMessage("传参异常");
	}
	/**
	 * <p>账户冻结</p>
	 * @param userFund
	 * @param amount
	 * @return
	 */
	public Result freezeBalance(UserFund userFund, BigDecimal amount) {
		log.info("【当前方法为 【码商或者商户资金冻结】 ，当前操作金额为："+amount+"】");
		BigDecimal accountBalance = userFund.getAccountBalance();
		BigDecimal rechargeNumber = userFund.getRechargeNumber();
		BigDecimal cashBalance = userFund.getCashBalance();
		BigDecimal freezeBalance = userFund.getFreezeBalance();
		freezeBalance = freezeBalance.add(amount);
		accountBalance =  rechargeNumber.add(cashBalance).subtract(freezeBalance);
		userFund.setAccountBalance(accountBalance);
		userFund.setFreezeBalance(freezeBalance);
		userFund.setCashBalance(cashBalance);
		userFund.setRechargeNumber(rechargeNumber);
		Boolean updataAmount = userInfoServiceImpl.updataAmount(userFund);
		if(updataAmount) {
			log.info("【当前冻结资金执行成功，操作的金额为："+amount+"】");
			log.info("【冻结资金后的账户金额为：账户总余额："+accountBalance+"，当前利润账户【现金账户】："+cashBalance+"，当前冻结账户："+freezeBalance+"当前操作金额为："+amount+"】");
			return Result.buildSuccessMessage("当前金额取款方法执行成功");
		} else 
			return Result.buildFailMessage("取款扣除金额方法执行失败");
	}
	/**
	 * <p>取款</p>
	 * @return
	 */
	public Result withdrawBalance(UserFund userFund, BigDecimal amount) {
		log.info("【当前方法为 【码商或者商户发起提现】 ，当前操作金额为："+amount+"】");
		BigDecimal accountBalance = userFund.getAccountBalance();
		BigDecimal rechargeNumber = userFund.getRechargeNumber();
		BigDecimal cashBalance = userFund.getCashBalance();
		BigDecimal freezeBalance = userFund.getFreezeBalance();
		accountBalance = accountBalance.subtract(amount);
		rechargeNumber = rechargeNumber.subtract(amount);
		if(rechargeNumber.compareTo(new BigDecimal("0")) == -1) {//交易点数小于0 的时候
			cashBalance = cashBalance.add(rechargeNumber);
			rechargeNumber = new BigDecimal("0");
		}
		userFund.setAccountBalance(accountBalance);
		userFund.setCashBalance(cashBalance);
		userFund.setFreezeBalance(freezeBalance);
		userFund.setRechargeNumber(rechargeNumber);
		Boolean updataAmount = userInfoServiceImpl.updataAmount(userFund);
		if(updataAmount) {
			log.info("【当前金额扣除执行成功，操作的金额为："+amount+"】");
			log.info("【金额修改后账户情况：当前账户总比较金额："+accountBalance+"，当前账户充值点数："+rechargeNumber+"，当前账户利润金额："+cashBalance+"，当前账户冻结金额："+freezeBalance+"，当前账户："+userFund.getUserId()+"】");
			return Result.buildSuccessMessage("当前金额取款方法执行成功");
		} else 
			return Result.buildFailMessage("取款扣除金额方法执行失败");
	}
	/**
	 * <p>扣款【或者扣点数】</p>
	 * @param amount
	 * @return
	 * 当前扣款为系统或者后台人员操作扣款，所以针对账户详情的资金开关，针对后台和系统管理人员无效
	 */
	public Result deductBalance(UserFund userFund , BigDecimal amount) {
		log.info("【当前方法为 【人工扣除账户金额方法】 ，当前操作金额为："+amount+"】");
		BigDecimal accountBalance = userFund.getAccountBalance();
		BigDecimal rechargeNumber = userFund.getRechargeNumber();
		BigDecimal cashBalance = userFund.getCashBalance();
		BigDecimal freezeBalance = userFund.getFreezeBalance();
		accountBalance = accountBalance.subtract(amount);
		rechargeNumber = rechargeNumber.subtract(amount);
		if(rechargeNumber.compareTo(new BigDecimal("0")) == -1) {//交易点数小于0 的时候
			cashBalance = cashBalance.add(rechargeNumber);
			rechargeNumber = new BigDecimal("0");
		}
		userFund.setAccountBalance(accountBalance);
		userFund.setCashBalance(cashBalance);
		userFund.setFreezeBalance(freezeBalance);
		userFund.setRechargeNumber(rechargeNumber);
		Boolean updataAmount = userInfoServiceImpl.updataAmount(userFund);
		if(updataAmount) {
			log.info("【当前金额扣除执行成功，操作的金额为："+amount+"】");
			log.info("【金额修改后账户情况：当前账户总比较金额："+accountBalance+"，当前账户充值点数："+rechargeNumber+"，当前账户利润金额："+cashBalance+"，当前账户冻结金额："+freezeBalance+"，当前账户："+userFund.getUserId()+"】");
			return Result.buildSuccessMessage("充值点数扣除方法执行成功");
		} else 
			return Result.buildFailMessage("金额扣除方法执行失败");
	}
	/**
	 * <p>交易减充值点数</p>
	 * @param amount
	 * @return
	 */
	public Result deductRecharge(UserFund userFund , BigDecimal amount) {
		log.info("【当前方法为 【交易扣除充值点数】 ，当前操作金额为："+amount+"】");
		BigDecimal accountBalance = userFund.getAccountBalance();
		BigDecimal rechargeNumber = userFund.getRechargeNumber();
		BigDecimal cashBalance = userFund.getCashBalance();
		BigDecimal freezeBalance = userFund.getFreezeBalance();
		accountBalance = accountBalance.subtract(amount);
		rechargeNumber = rechargeNumber.subtract(amount);
		if(rechargeNumber.compareTo(new BigDecimal("0")) == -1) {//交易点数小于0 的时候
			cashBalance = cashBalance.add(rechargeNumber);
			rechargeNumber = new BigDecimal("0");
		}
		userFund.setAccountBalance(accountBalance);
		userFund.setCashBalance(cashBalance);
		userFund.setFreezeBalance(freezeBalance);
		userFund.setRechargeNumber(rechargeNumber);
		Boolean updataAmount = userInfoServiceImpl.updataAmount(userFund);
		if(updataAmount) {
			log.info("【当前金额扣除执行成功，操作的金额为："+amount+"】");
			log.info("【金额修改后账户情况：当前账户总比较金额："+accountBalance+"，当前账户充值点数："+rechargeNumber+"，当前账户利润金额："+cashBalance+"，当前账户冻结金额："+freezeBalance+"，当前账户："+userFund.getUserId()+"】");
			return Result.buildSuccessMessage("充值点数扣除方法执行成功");
		} else 
			return Result.buildFailMessage("金额扣除方法执行失败");
	}
	/**
	 * <p>当前账户交易利润加钱</p>
	 * @param userFund			资金账户表			
	 * @param balance			金额					该笔交易较小【分润】
	 * @param dealAmount 
	 * @param dealType      	true 入款 统计			false  出款统计
	 * @return
	 * 
	 * <p>交易利润加钱需要怎加交易利润字段</p>
	 */
	@SuppressWarnings("unlikely-arg-type")
	public Result addAmountDeal(UserFund userFund, BigDecimal balance, BigDecimal dealAmount,boolean dealType) {
		log.info("【当前方法为 【交易利润加钱】，当前交易金额为："+dealAmount+"，当前操作金额为："+balance+"】");
		UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(userFund.getUserId());
		if(Common.User.USER_INFO_OFF.equals(userInfo.getSwitchs())) {
			log.info("【===========【当前账户被标记为禁止使用资金账户功能，请检查该账户存在的交易异常，当前账户为："+userInfo.getUserId()+"】===========】");
			//TODO 当前位置除了做运行日志记录和数据日志记录，还应将该处存在的问题推送到系统醒目的地方
			return Result.buildFailMessage("当前账户被禁止使用资金流水功能，请检查账户是否存在异常");
		}
		BigDecimal accountBalance = userFund.getAccountBalance();//当前账户比较金额
		BigDecimal cashBalance = userFund.getCashBalance();//当前利润账户
		BigDecimal freezeBalance = userFund.getFreezeBalance();//当前冻结账户
		BigDecimal rechargeNumber = userFund.getRechargeNumber();//当前充值点数
		BigDecimal sumProfit = userFund.getSumProfit();//当前当前总的利润
		BigDecimal todayProfit = userFund.getTodayProfit();//今日总利润  =  今日代理分润 + 今日接单分润
		BigDecimal todayDealAmountC = userFund.getTodayDealAmountC();
		BigDecimal todayDealAmountR = userFund.getTodayDealAmountR();
		Integer todayOrderCountC = userFund.getTodayOrderCountC();
		Integer todayOrderCountR = userFund.getTodayOrderCountR();
		
		BigDecimal sumDealAmountC = userFund.getSumDealAmountC();
		BigDecimal sumDealAmountR = userFund.getSumDealAmountR();
		Integer sumOrderCountC = userFund.getSumOrderCountC();
		Integer sumOrderCountR = userFund.getSumOrderCountR();
		log.info("【金额修改后账户情况：当前账户总比较金额："+accountBalance+"，当前账户充值点数："+rechargeNumber+"，当前账户利润金额："+cashBalance+"，当前账户冻结金额："+freezeBalance+"，当前账户："+userFund.getUserId()+"】");
		//以上为当前账户情况	
		cashBalance = cashBalance.add(balance);
		accountBalance = cashBalance.add(rechargeNumber).subtract(freezeBalance);
		sumProfit = sumProfit.add(balance);
		todayProfit = todayProfit.add(balance);
		if(dealType) {//入款统计
			todayDealAmountR = todayDealAmountR.add(dealAmount);
			todayOrderCountR += 1;
			sumDealAmountR = sumDealAmountR.add(dealAmount);
			sumOrderCountR += 1;
		} else {
			todayDealAmountC = todayDealAmountC.add(dealAmount);
			todayOrderCountC += 1;
			sumDealAmountC = sumDealAmountC.add(dealAmount);
			sumOrderCountC += 1;
		}
		//当日统计
		userFund.setTodayDealAmountC(todayDealAmountC);
		userFund.setTodayDealAmountR(todayDealAmountR);
		userFund.setTodayOrderCountC(todayOrderCountC);
		userFund.setTodayOrderCountR(todayOrderCountR);
		userFund.setTodayProfit(todayProfit);
		
		
		//累计统计
		userFund.setSumDealAmountC(sumDealAmountC);
		userFund.setSumDealAmountR(sumDealAmountR);
		userFund.setSumOrderCountC(sumOrderCountC);
		userFund.setSumOrderCountR(sumOrderCountR);
		userFund.setSumProfit(sumProfit);
		
		
		//金额账户
		userFund.setAccountBalance(accountBalance);
		userFund.setCashBalance(cashBalance);
		userFund.setFreezeBalance(freezeBalance);
		userFund.setRechargeNumber(rechargeNumber);
		Boolean updataAmount = userInfoServiceImpl.updataAmount(userFund);
		if(updataAmount) 
			log.info("【金额修改后账户情况：当前账户总比较金额："+accountBalance+"，当前账户充值点数："+rechargeNumber+"，当前账户利润金额："+cashBalance+"，当前账户冻结金额："+freezeBalance+"，当前账户："+userFund.getUserId()+"】");
		else {
			log.info("【账户修改失败】");
			ThreadUtil.execute( ()->{
				//TODO 新建线程提交，该线程不受主线程事务控制
				Boolean updataStatusEr = userInfoServiceImpl.updataStatusEr(userFund.getUserId());
				if(updataStatusEr)
				log.info("【账户已修改为不可使用，当前账号为："+userFund.getUserId()+"】");
			});
			throw new UserException("账户修改异常", null);
		}
		return Result.buildSuccessResult();
	}
	public Result addAmountAgentProfit(UserFund userFund, BigDecimal balance) {
		log.info("【当前方法为 【增加代理商点数】 ，当前操作金额为："+balance+"】");
		UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(userFund.getUserId());
		if(Common.User.USER_INFO_OFF.equals(userInfo.getSwitchs())) {
			log.info("【===========【当前账户被标记为禁止使用资金账户功能，请检查该账户存在的交易异常，当前账户为："+userInfo.getUserId()+"】===========】");
			//TODO 当前位置除了做运行日志记录和数据日志记录，还应将该处存在的问题推送到系统醒目的地方
			return Result.buildFailMessage("当前账户被禁止使用资金流水功能，请检查账户是否存在异常");
		}
		BigDecimal accountBalance = userFund.getAccountBalance();//当前账户比较金额
		BigDecimal cashBalance = userFund.getCashBalance();//当前利润账户
		BigDecimal freezeBalance = userFund.getFreezeBalance();//当前冻结账户
		BigDecimal rechargeNumber = userFund.getRechargeNumber();//当前充值点数
		BigDecimal sumAgentProfit = userFund.getSumAgentProfit();//当前代理商分润  
		BigDecimal sumProfit = userFund.getSumProfit();//当前总的利润  =  交易利润 + 代理利润
		BigDecimal todayAgentProfit = userFund.getTodayAgentProfit();//今日代理分润  
		BigDecimal todayProfit = userFund.getTodayProfit();//今日总利润  =  今日代理分润 + 今日接单分润
		log.info("【金额修改后账户情况：当前账户总比较金额："+accountBalance+"，当前账户充值点数："+rechargeNumber+"，当前账户利润金额："+cashBalance+"，当前账户冻结金额："+freezeBalance+"，当前账户："+userFund.getUserId()+"】");
	//以上为当前账户情况	
		cashBalance = cashBalance.add(balance);//增加 分润账户
		accountBalance = cashBalance.add(rechargeNumber).subtract(freezeBalance);
		sumAgentProfit = sumAgentProfit.add(balance);
		todayAgentProfit = todayAgentProfit.add(balance);
		todayProfit = todayProfit.add(balance);
		sumProfit = sumProfit.add(balance);
		userFund.setCashBalance(cashBalance);
		userFund.setAccountBalance(accountBalance);
		userFund.setTodayAgentProfit(todayAgentProfit);
		userFund.setTodayProfit(todayProfit);
		userFund.setSumAgentProfit(sumAgentProfit);
		userFund.setSumProfit(sumProfit);
		Boolean updataAmount = userInfoServiceImpl.updataAmount(userFund);
		if(updataAmount) 
			log.info("【金额修改后账户情况：当前账户总比较金额："+accountBalance+"，当前账户充值点数："+rechargeNumber+"，当前账户利润金额："+cashBalance+"，当前账户冻结金额："+freezeBalance+"，当前账户："+userFund.getUserId()+"】");
		else {
			log.info("【账户修改失败】");
			ThreadUtil.execute( ()->{
				//TODO 新建线程提交，该线程不受主线程事务控制
				Boolean updataStatusEr = userInfoServiceImpl.updataStatusEr(userFund.getUserId());
				if(updataStatusEr)
				log.info("【账户已修改为不可使用，当前账号为："+userFund.getUserId()+"】");
			});
			throw new UserException("账户修改异常", null);
		}
		return Result.buildSuccessResult();
	}
	
	/**
	 * <p>检查当前用户资金状态是否存在问题</p>
	 * @param userFund				用户实时资金数据
	 * @return
	 */
	private Result clickUserFund(UserFund userFund){
		log.info("进入当前账户金额检查方法");
		BigDecimal accountBalance = userFund.getAccountBalance();//当前账户可操作余额【当前现金账户 +当前冻结账户+当前充值点数】
		BigDecimal cashBalance = userFund.getCashBalance();//当前现金账户
		BigDecimal freezeBalance = userFund.getFreezeBalance();//当前冻结账户
		BigDecimal rechargeNumber = userFund.getRechargeNumber();//当前充值点数
		if(cashBalance.add(rechargeNumber).subtract(freezeBalance).compareTo(accountBalance) != 0) { //关闭资金流动的功能  
			log.info("【===========【经过系统核对后，当前用户账户存在问题，请重点关照该用户的账号，检查问题出现原因】===========】");
			ThreadUtil.execute( ()->{
				//TODO 新建线程提交，该线程不受主线程事务控制
				userInfoServiceImpl.updataStatusEr(userFund.getUserId());
			});
			return Result.buildFail();
		}
		log.info("【===========【经过系统核对后，当前用户账户不存在问题，请放心交易】===========】");
		return Result.buildSuccess();
	}
	/**
	 * <p>增加金额【充值】</p>
	 * <p>增加充值金额流水</p>
	 * @param userFund				资金账户实体
	 * @param balance				增加金额
	 * @return
	 */
	@SuppressWarnings("unlikely-arg-type")
	public Result addAmountRecharge(UserFund userFund , BigDecimal balance) {
		UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(userFund.getUserId());
		if(Common.User.USER_INFO_OFF.equals(userInfo.getSwitchs())) {
			log.info("【===========【当前账户被标记为禁止使用资金账户功能，请检查该账户存在的交易异常，当前账户为："+userInfo.getUserId()+"】===========】");
			//TODO 当前位置除了做运行日志记录和数据日志记录，还应将该处存在的问题推送到系统醒目的地方
			return Result.buildFailMessage("当前账户被禁止使用资金流水功能，请检查账户是否存在异常");
		}
		BigDecimal accountBalance = userFund.getAccountBalance();
		BigDecimal cashBalance = userFund.getCashBalance();
		BigDecimal freezeBalance = userFund.getFreezeBalance();
		BigDecimal rechargeNumber = userFund.getRechargeNumber();
		log.info("【当前账户余额："+accountBalance+"】");
		log.info("【当前账户利润余额："+cashBalance+"】");
		log.info("【当前账户冻结余额："+freezeBalance+"】");
		log.info("【当前账户充值点数："+rechargeNumber+"】");
		rechargeNumber = rechargeNumber.add(balance);
		accountBalance = rechargeNumber.add(freezeBalance).add(cashBalance);
		log.info("【当前金额加款操作。。。。。。。。。】");
		userFund.setRechargeNumber(rechargeNumber);
		userFund.setAccountBalance(accountBalance);
		Boolean flag = userInfoServiceImpl.updataAmount(userFund);
		if(flag) {
			log.info("【当前账户添加充值点数成功资金详细情况如下：】");
			log.info("【当前账户余额："+accountBalance+"】");
			log.info("【当前账户利润余额："+cashBalance+"】");
			log.info("【当前账户冻结余额："+freezeBalance+"】");
			log.info("【当前账户充值点数："+rechargeNumber+"】");
			return Result.buildSuccessMessage("当前账户充值金额添加成功");
		} else {
			log.info("【当前账户添加充值点数【失败】，请联系技术检查当前账户存在的异常情况】");
			return	Result.buildFail();
		}
	}
	/**
	 * <p>根据订单的账户变更和流水生成【入款订单】</p>
	 * @param orderId			卡商交易订单号
	 * @param ip				ip
	 * @param flag				true 人工操作    false   自然流水
	 * @return
	 */
	public Result orderAmountR(String orderId,String ip , boolean flag) {
		if(StrUtil.isBlank(orderId))
			return Result.buildFailMessage("订单号为空");
		DealOrder order = orderServiceImpl.findOrderByOrderId(orderId);
		if(!Common.Order.DealOrder.ORDER_STATUS_SU.equals(order.getOrderStatus()))
			return Result.buildFailMessage("订单号状态错误");
		UserFund userFund = new 	UserFund();
		userFund.setUserId(order.getOrderQrUser());
		Result deal = deleteDeal(userFund, order.getDealAmount());
		if(!deal.isSuccess())
			return Result.buildFailMessage("订单结算失败");
		Result result = amountRunUtil.deleteRechangerNumber(order, ip, flag);
		Result addDeal = addDeal(userFund, order.getDealFee(),  order.getDealAmount(),true);
		if(!addDeal.isSuccess())
			return Result.buildFailMessage("订单结算失败");
		Result result2 = amountRunUtil.addDealAmount(order, ip, flag);
		if(result2.isSuccess()&&result.isSuccess()&&addDeal.isSuccess()&&deal.isSuccess())
			return Result.buildSuccessMessage("订单结算成功");
		return Result.buildFailMessage("订单结算失败");
	}
	/**
	 * <p>根据订单的账户变更和流水生成【出款订单】</p>
	 * @param orderId			卡商交易订单号
	 * @param ip				ip
	 * @param flag				true 人工操作    false   自然流水
	 * @return
	 */
	public Result orderAmountC(String orderId , String ip , boolean flag) {
		if(StrUtil.isBlank(orderId))
			return Result.buildFailMessage("订单号为空");
		DealOrder order = orderServiceImpl.findOrderByOrderId(orderId);
		if(!Common.Order.DealOrder.ORDER_STATUS_SU.equals(order.getOrderStatus()))
			return Result.buildFailMessage("订单号状态错误");
		UserFund userFund = new 	UserFund();
		userFund.setUserId(order.getOrderQrUser());
		Result addAmounRecharge = addAmounRecharge(userFund, order.getDealAmount());
		if(!addAmounRecharge.isSuccess())
			return Result.buildFailMessage("订单结算错误");
		Result addCardDealC = amountRunUtil.addCardDealC(order, ip, flag);
		Result addAmountDeal = addAmountDeal(userFund, order.getDealFee(), order.getDealAmount(),false);
		if(!addAmountDeal.isSuccess())
			return Result.buildFailMessage("订单结算错误");
		Result addCardDealFeeC = amountRunUtil.addCardDealFeeC(order, ip, flag);
		if(addCardDealC.isSuccess()&& addCardDealFeeC.isSuccess())
			return Result.buildSuccessMessage("订单结算成功");
		return Result.buildFailMessage("订单结算失败");
	}
}
