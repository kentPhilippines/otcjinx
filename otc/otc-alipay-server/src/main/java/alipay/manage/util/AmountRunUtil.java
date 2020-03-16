package alipay.manage.util;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alipay.manage.bean.RunOrder;
import alipay.manage.service.RunOrderService;
import cn.hutool.core.util.StrUtil;
import otc.result.Result;

/**
 * <p>资金流水处理类</p>
 * @author hx08
 *
 */
@Component
public class AmountRunUtil {
	@Autowired
	RunOrderService runOrderServiceImpl;
	private static final String RUN = "RUN";
	private static final String ADD_AMOUNT = "ADD_AMOUNT";//人工加钱
	private static final Integer ADD_AMOUNT_NUMBER = 17;
	private static final String DETETE_AMOUNT = "DETETE_AMOUNT";//人工扣钱
	private static final Integer DETETE_AMOUNT_NUMBER = 18;//人工扣钱
	private static final String DEAL_AMOUNT_DETETE = "DEAL_AMOUNT_DETETE";//交易时交易点数扣除
	private static final Integer DEAL_AMOUNT_DETETE_NUMBER = 12;//交易时交易点数扣除
	private static final String PROFIT_AMOUNT_DEAL = "PROFIT_AMOUNT_DEAL";//自己账号交易时产生的分润
	private static final String PROFIT_AMOUNT_AGENT = "PROFIT_AMOUNT_AGENT";//自己作为代理商的分润
	private static final Integer PROFIT_AMOUNT_AGENT_NUMBER = 13;//自己作为代理商的分润
	private static final Integer PROFIT_AMOUNT_DEAL_NUMBER = 14;//自己账号交易时产生的分润
	private static final String RECHANGE_AMOUNT = "RECHANGE_AMOUNT";//充值 及 增加交易点数
	private static final Integer RECHANGE_AMOUNT_NUMBER = 11;//充值 及  增加交易点数
	private static final String WITHDRAY_AMOUNT = "WITHDRAY_AMOUNT";//代付冻结
	private static final String WITHDRAY_AMOUNT_FEE = "WITHDRAY_AMOUNT_FEE";//代付手续费冻结
	private static final Integer WITHDRAY_AMOUNT_NUMBER = 10;//代付冻结
	private static final Integer WITHDRAY_AMOUNT_FEE_NUMBER = 9;//代付手续费冻结
	
	
	
	
	
	
	
	
	/**
	 * <p>创建流水总类</p>	
	 * @param associatedId		关联订单号
	 * @param orderAccount		关联账户号
	 * @param runOrderType		订单类型类型					订单类型(1充值交易,2系统加款,3交易手续费,4系统扣款,5代付,6代付手续费,7冻结,8解冻,9代付手手续费冻结,10代付冻结,11增加交易点数,12点数扣除,13代理商分润，14码商分润，15人工加款，16人工减款，17人工加点数，18人工减点数，19 卡商交易加钱)
	 * @param amount			流水金额
	 * @param generationIp		关联ip
	 * @param acountR			入款账户
	 * @param accountW			出款账户
	 * @param runType			人工操作					1.自然状态 2.人工操作
	 * @param amountType		流水类型					1支出0收入
	 * @param dealDescribe		流水说明
	 * @return
	 */
	@SuppressWarnings("unused")
	private Result amountRun(String associatedId,String orderAccount,
			Integer runOrderType,BigDecimal amount,String generationIp,
			String acountR , String accountW,String runType ,String amountType 
			,String dealDescribe,BigDecimal amountNow) {
		if(StrUtil.isBlank(associatedId) || StrUtil.isBlank(orderAccount) 
				|| StrUtil.isBlank(generationIp)
				|| StrUtil.isBlank(runType)
				|| StrUtil.isBlank(amountType)
				|| StrUtil.isBlank(dealDescribe)
				)
			return Result.buildFailMessage("必传参数为空");
		RunOrder run = new RunOrder();
		run.setAssociatedId(associatedId);
		run.setAccountW(accountW);
		run.setAcountR(acountR);
		run.setGenerationIp(generationIp);
		run.setOrderAccount(orderAccount);
		run.setAmountType(amountType);
		run.setDealDescribe(dealDescribe);
		run.setRunOrderType(runOrderType);
		run.setRunType(runType);
		run.setAmountNow(amountNow);
		boolean addOrder = runOrderServiceImpl.addOrder(run);
		if(addOrder)
			return Result.buildSuccessMessage("流水订单生成成功");
		return Result.buildFailMessage("流水订单生成失败");
	}
	
	/**
	 * <p>获取流水订单类型</p>
	 * @param orderType			流水订单标识
	 * @return
	 */
	Integer getRunOrderType(String orderType){
		Integer runOrderType = null;
		switch (orderType) {
		case ADD_AMOUNT:
			runOrderType = ADD_AMOUNT_NUMBER;
			break;
		case DETETE_AMOUNT:
			runOrderType = DETETE_AMOUNT_NUMBER;
			break;
		case DEAL_AMOUNT_DETETE:
			runOrderType = DEAL_AMOUNT_DETETE_NUMBER;
			break;
		case PROFIT_AMOUNT_DEAL:
			runOrderType = PROFIT_AMOUNT_DEAL_NUMBER;
			break;
		case RECHANGE_AMOUNT:
			runOrderType = RECHANGE_AMOUNT_NUMBER;
			break;
		case WITHDRAY_AMOUNT:
			runOrderType = WITHDRAY_AMOUNT_NUMBER;
			break;
		case WITHDRAY_AMOUNT_FEE:
			runOrderType = WITHDRAY_AMOUNT_FEE_NUMBER;
			break;
		case PROFIT_AMOUNT_AGENT:
			runOrderType = PROFIT_AMOUNT_AGENT_NUMBER;
			break;
		default:
			break;
		}
		return runOrderType;
	}
	

	
}
