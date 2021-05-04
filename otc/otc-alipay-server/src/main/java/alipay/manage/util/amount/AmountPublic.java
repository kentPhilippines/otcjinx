package alipay.manage.util.amount;

import alipay.config.redis.RedisLockUtil;
import alipay.manage.bean.UserFund;
import alipay.manage.service.RunOrderService;
import alipay.manage.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>资金处理方法</p>
 *
 * @author K
 */
@Component
public class AmountPublic extends Util {
	Logger log = LoggerFactory.getLogger(AmountPublic.class);
	@Autowired
	UserInfoService userInfoServiceImpl;
	@Autowired
	RunOrderService runorderServiceImpl;
	@Autowired
	AmountConfig amountConfig;
	@Autowired
	private RedisLockUtil redisLockUtil;
	static Lock lock = new ReentrantLock();

	/**
	 * 增加账户授权额度
	 *
	 * @param userFund 账户信息  userId 不为null 即可
	 * @param balance  金额
	 * @return
	 */
	public Result addQuotaAmount(UserFund userFund, BigDecimal balance, String orderId) {
		return amountConfig.addAmountBalance(userFund, balance, ADD_QUOTA, new BigDecimal(0), orderId);
	}

	/**
	 * 减少账户授权额度
	 *
	 * @param userFund 账户信息  userId 不为null 即可
	 * @param balance  金额
	 * @return
	 */
	public Result deleteQuotaAmount(UserFund userFund, BigDecimal balance, String orderId) {
		return amountConfig.deleteAmountBalance(userFund, balance, DELETE_QUOTA,orderId);
	}


	/**
     * <p><strong>增加交易点数</strong></p>
     *
     * @param userFund 当前账户实体【必传字段为userId】
     * @param balance  当前操作金额
     * @return
     */
    public Result addAmounRecharge(UserFund userFund, BigDecimal balance, String orderId) {
        return amountConfig.addAmountBalance(userFund, balance, ADD_AMOUNT_RECHARGE, new BigDecimal(0), orderId);
    }


    /**
     * 卡商出款结算
     *
     * @param userFund 卡商账户
     * @param balance  出款金额
     * @param orderId  订单号
     * @return
     */
    public Result addAmounRechargeWit(UserFund userFund, BigDecimal balance, String orderId) {
        return amountConfig.addAmountBalance(userFund, balance, Util.ADD_AMOUNT_RECHARGE_WIT, new BigDecimal(0), orderId);
    }

    /**
     * <p><strong>增加代理商利润</strong></p>
     *
     * @param userFund 当前账户实体【必传字段为userId】
     * @param balance  当前操作金额
     * @return
     */
    public Result addAmounProfit(UserFund userFund, BigDecimal balance, String orderId) {
        return amountConfig.addAmountBalance(userFund, balance, ADD_AMOUNT_PROFIT, new BigDecimal(0), orderId);
	}

	/**
	 * <p><strong>人工加钱</strong></p>
	 *
	 * @param userFund 当前账户实体【必传字段为userId】
	 * @param balance  当前操作金额
	 * @return
	 */
	public Result addAmountAdd(UserFund userFund, BigDecimal balance, String orderId) {
		return amountConfig.addAmountBalance(userFund, balance, ADD_AMOUNT, new BigDecimal(0), orderId);
    }

    /**
     * <p><strong>增加交易分润</strong></p>
     *
     * @param userFund   资金账户
     * @param balance    记录资金【分润金额】
     * @param dealAmount 交易金额【订单金额】
     * @return
     */
    public Result addDeal(UserFund userFund, BigDecimal balance, BigDecimal dealAmount, String orderId) {
        return amountConfig.addAmountBalance(userFund, balance, ADD_AMOUNT_DEAL, dealAmount, orderId);
    }

    /**
     * 卡商出款费率结算
     *
     * @param userFund
     * @param fee
     * @param orderId
     * @return
     */
    public Result addDealWit(UserFund userFund, BigDecimal fee, String orderId) {
        return amountConfig.addAmountBalance(userFund, fee, ADD_AMOUNT_DEAL_WIT, new BigDecimal(0), orderId);
    }

    /**
     * 资金账户余额加钱，解冻账户余额
     *
     * @param userFund
     * @param freezeAmont
     * @return
     */
    public Result addFreeze(UserFund userFund, BigDecimal freezeAmont, String orderId) {
        return amountConfig.addAmountBalance(userFund, freezeAmont, ADD_FREEZE, new BigDecimal(0), orderId);
	}

	/**
	 * <p>下游商户交易时，增加下游商户商户余额</p>
	 *
	 * @param userFund
	 * @param balance
	 * @return
	 */
	public Result addDealApp(UserFund userFund, BigDecimal balance, String orderId) {
		return amountConfig.addAmountBalance(userFund, balance, ADD_AMOUNT_DEAL_APP, new BigDecimal("0"), orderId);
	}

	/**
	 * <p><strong>减少交易点数【交易订单置为成功调用这个方法】</strong></p>
	 *
	 * @param userFund 当前账户实体【必传字段为userId】
	 * @param balance  当前操作金额
	 * @return
	 */
	public Result deleteDeal(UserFund userFund, BigDecimal balance, String orderId) {
		return amountConfig.deleteAmountBalance(userFund, balance, DELETE_DEAL, orderId);
	}

	/**
	 * <p><strong>码商取现减款</strong></p>
	 *
	 * @param userFund 当前账户实体【必传字段为userId】
	 * @param balance  当前操作金额
	 * @return
	 */
	public Result deleteWithdraw(UserFund userFund, BigDecimal balance, String orderId) {
		return amountConfig.deleteAmountBalance(userFund, balance, DELETE_WITHDRAW, orderId);
	}

	/**
	 * <p>人工扣款接口</p>
	 *
	 * @param userFund
	 * @param balance
	 * @return
	 */
	public Result deleteAmount(UserFund userFund, BigDecimal balance, String orderId) {
        return amountConfig.deleteAmountBalance(userFund, balance, DELETE_AMOUNT, orderId);
    }

    /**
     * <p><strong>人工冻结</strong></p>
     *
     * @param userFund 当前账户实体【必传字段为userId】
     * @param balance  当前操作金额
     * @return
     */
    public Result deleteFreeze(UserFund userFund, BigDecimal balance, String orderId) {
        return amountConfig.deleteAmountBalance(userFund, balance, DELETE_FREEZE, orderId);
    }


    public Result witStatis(UserFund userFund, BigDecimal balance, String orderId) {
        return amountConfig.addAmountBalance(userFund, balance, WIT_SUCCESSS_STS, new BigDecimal(0), orderId);
    }


    public Result addAmounProfitBank(UserFund userFund, BigDecimal balance, String orderId) {
        return amountConfig.addAmountBalance(userFund, balance, Util.ADD_AMOUNT_PROFIT_BANK, new BigDecimal(0), orderId);
    }
}
