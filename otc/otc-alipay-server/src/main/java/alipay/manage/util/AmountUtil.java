package alipay.manage.util;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.UserInfoService;
import otc.api.alipay.Common;
import otc.result.Result;
/**
 * <p>资金处理方法</p>
 * @author K
 */
@Component
public class AmountUtil {
	Logger log = LoggerFactory.getLogger(AmountUtil.class);
	@Autowired UserInfoService userInfoServiceImpl;
	private static final String ADD_AMOUNT_RECHARGE = "ADD_AMOUNT_RECHARGE";//资金充值
	private static final String ADD_AMOUNT_PROFIT = "ADD_AMOUNT_PROFIT";//代理利润分成
	private static final String ADD_AMOUNT = "ADD_AMOUNT";//人工加钱
	private static final String ADD_AMOUNT_DEAL = "ADD_AMOUNT_DEAL";//交易利润加钱
	
	private static final String DELETE_DEAL = "DELETE_DEAL";
	private static final String DELETE_AMOUNT = "DELETE_AMOUNT";
	/**
	 * <p>增加余额</p>
	 * @param userId			用户id
	 * @param amount			金额
	 * @return
	 */
	public Result addAmountBalance(UserFund userFund , BigDecimal balance , String addType) {
		if(!clickUserFund(userFund).isSuccess())
			return Result.buildFailMessage("资金账户存在问题");
		if(ADD_AMOUNT_RECHARGE.equals(addType)) {//资金充值【加充值点数】
			Result addAmountRecharge = addAmountRecharge(userFund,  balance);
			if(addAmountRecharge.isSuccess()) 
				return addAmountRecharge;
			return 	Result.buildFailMessage("账户余额添加失败，请联系技术人员查看当前服务异常");
		}else if(ADD_AMOUNT_PROFIT.equals(addType)) {//代理利润分成
			
		}else if (ADD_AMOUNT.equals(addType)) {//手动加钱
			
		}else if(ADD_AMOUNT_DEAL.equals(addType)) {//交易利润分成 ,统计交易笔数
			
		}
		return null;
	}
	/**
	 * <p>增加充值点数</p>
	 * @param userId				用户id
	 * @param amount				金额
	 * @return
	 */
	public Result addRechargeNumberBalance(UserFund userFund, BigDecimal rechargeNumber) {
		if(!clickUserFund(userFund).isSuccess())
			return Result.buildFailMessage("资金账户存在问题");
		return null;
	}
	/**
	 * <p>取款</p>
	 * @param userId					用户id
	 * @param amount					金额
	 * @return
	 */
	public Result withdrawBalance(UserFund userFund, BigDecimal amount) {
		if(!clickUserFund(userFund).isSuccess())
			return Result.buildFailMessage("资金账户存在问题");
		return null;
	}
	/**
	 * <p>扣款【或者扣点数】</p>
	 * @param userId
	 * @param amount
	 * @return
	 */
	public Result deductBalance(UserFund userFund , BigDecimal amount) {
		if(!clickUserFund(userFund).isSuccess())
			return Result.buildFailMessage("资金账户存在问题");
		return null;
	}
	/**
	 * <p>检查当前用户资金状态是否存在问题</p>
	 * @param userFund				用户实时资金数据
	 * @return
	 */
	Result clickUserFund(UserFund userFund){
		BigDecimal accountBalance = userFund.getAccountBalance();//当前账户可操作余额【当前现金账户 +当前冻结账户+当前充值点数】
		BigDecimal cashBalance = userFund.getCashBalance();//当前现金账户
		BigDecimal freezeBalance = userFund.getFreezeBalance();//当前冻结账户
		BigDecimal rechargeNumber = userFund.getRechargeNumber();//当前充值点数
		if(cashBalance.add(rechargeNumber).add(freezeBalance).compareTo(accountBalance) == 0) { //关闭资金流动的功能  
			log.info("【===========【经过系统核对后，当前用户账户存在问题，请重点关照该用户的账号，检查问题出现原因】===========】");
			if(userInfoServiceImpl.updataStatusEr(userFund.getUserId()))
				return Result.buildFail();
		}
		log.info("【===========【经过系统核对后，当前用户账户不存在问题，请放心交易】===========】");
		return Result.buildSuccess();
	}
	/**
	 * <p>增加金额【充值】</p>
	 * @param userFund				
	 * @param balance
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
		}else {
			log.info("【当前账户添加充值点数【失败】，请联系技术检查当前账户存在的异常情况】");
			return	Result.buildFail();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
