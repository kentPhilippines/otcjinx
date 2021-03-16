package alipay.manage.util.amount;

import alipay.manage.bean.UserFund;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.thread.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import otc.exception.user.UserException;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 资金核心处理类
 */
@Component
public class AmountConfig extends Util {
    @Autowired
    static Lock lock = new ReentrantLock();
    Logger log = LoggerFactory.getLogger(AmountPrivate.class);
    @Autowired
    UserInfoService userInfoServiceImpl;
    @Autowired
    AmountPrivate amountPrivate;

    /**
     * <p>增加余额</p>
     *
     * @return
     */
    protected Result addAmountBalance(UserFund userFund1, final BigDecimal balance,
                                      final String addType, final BigDecimal dealAmount, String orderId) {
        lock.lock();
        try {
            boolean flag = true;
            int lockMsg = 1;
            do {
                if (lockMsg != 1) {
                    log.info("【当前账户乐观锁发生作用，再次执行，当前账户：" + userFund1.getUserId() + "，金额："
                            + dealAmount + "，方法：addAmountBalance，类型：" + addType + "】");
                    final UserFund finalUserFund = userFund1;
                    ThreadUtil.execute(() -> {
                        amountPrivate.addExcption(finalUserFund, addType, balance, orderId);
                    });
                }
                UserFund userFund = userInfoServiceImpl.findUserFundByAccount(userFund1.getUserId());
                if (!amountPrivate.clickUserFund(userFund).isSuccess()) {
                    return Result.buildFailMessage("【资金账户存在问题】");
                }
                if (ADD_AMOUNT_RECHARGE.equals(addType)) {//资金充值【加充值点数】
                    Result addAmountRecharge = amountPrivate.addAmountRecharge(userFund, balance);
                    if (addAmountRecharge.isSuccess()) {
                        flag = false;
                        return addAmountRecharge;
                    }
                    lockMsg++;
                    log.info("【账户余额添加失败，请查询当前时间范围内的异常情况】");
                } else if (ADD_AMOUNT_PROFIT.equals(addType)) {//代理利润分成
                    Result profit = amountPrivate.addAmountAgentProfit(userFund, balance);
                    if (profit.isSuccess()) {
                        flag = false;
                        return profit;
                    }
                    lockMsg++;
                    log.info("【账户代理商分润增加失败，请查询当前时间范围内的异常情况】");
                } else if (ADD_AMOUNT.equals(addType)) {//手动加钱
                    Result addAmountRecharge = amountPrivate.addAmountRecharge(userFund, balance);
                    if (addAmountRecharge.isSuccess()) {
                        flag = false;
                        return addAmountRecharge;
                    }
                    lockMsg++;
                    log.info("【手动加钱失败，请联系技术人员处理,请查询当前时间范围内的异常情况");
                } else if (ADD_AMOUNT_DEAL.equals(addType)) {//交易利润分成 ,统计交易笔数
                    Result addAmountDeal = amountPrivate.addAmountDeal(userFund, balance, dealAmount);
                    if (addAmountDeal.isSuccess()) {
                        flag = false;
                        return addAmountDeal;
                    }
                    lockMsg++;
                    log.info("【账户余额添加失败，请查询当前时间范围内的异常情况】");
                } else if (ADD_AMOUNT_DEAL_APP.equals(addType)) {
                    Result addAmountDeal = amountPrivate.addAmountDealApp(userFund, balance);
                    if (addAmountDeal.isSuccess()) {
                        flag = false;
                        return addAmountDeal;
                    }
                    lockMsg++;
                    log.info("【账户余额添加失败，请查询当前时间范围内的异常情况】");
                } else if (ADD_FREEZE.equals(addType)) {
                    Result addAmountDeal = amountPrivate.addFreezeAmount(userFund, balance);
                    if (addAmountDeal.isSuccess()) {
                        flag = false;
                        return addAmountDeal;
                    }
                    lockMsg++;
                    log.info("【账户余额添加失败，请查询当前时间范围内的异常情况】");
                } else if (ADD_QUOTA.equals(addType)) {
                    Result addAmountDeal = amountPrivate.addQuota(userFund, balance);
                    if (addAmountDeal.isSuccess()) {
                        flag = false;
                        return addAmountDeal;
                    }
                    lockMsg++;
                    log.info("【账户余额添加失败，请查询当前时间范围内的异常情况】");
                }
                if (lockMsg > 20) {
                    log.info("【账户余额添加失败，请查询当前时间范围内的异常情况，当前账户：" + userFund1.getUserId() + "，金额：" +
                            dealAmount + "，方法：addAmountBalance，类型：" + addType + "】");
                    return Result.buildFailMessage("【账户余额添加失败，请联系技术人员查看当前服务异常】");
                }
            } while (flag);
        } finally {
            lock.unlock();
        }
        return Result.buildFailMessage("传参异常");
    }


    @Transactional
    protected Result deleteAmountBalance(UserFund userFund, final BigDecimal balance, final String addType, String orderId) {
        synchronized (this.getClass()) {
            try {
                boolean flag = true;
                Integer lockMsg = 1;
                do {
                    if (lockMsg != 1) {
                        log.info("【当前账户乐观锁发生作用，再次执行，当前账户：" + userFund.getUserId() + "，金额："
                                + balance + "，方法：addAmountBalance，类型：" + addType + "】");
                        final UserFund finalUserFund = userFund;
                        ThreadUtil.execute(() -> {
                            amountPrivate.addExcption(finalUserFund, addType, balance, orderId);
                        });
                    }
                    userFund = userInfoServiceImpl.findUserFundByAccount(userFund.getUserId());
                    if (!amountPrivate.clickUserFund(userFund).isSuccess()) {
                        return Result.buildFailMessage("【资金账户存在问题】");
                    }
                    if (DELETE_DEAL.equals(addType)) {//交易减点数
                        Result deductRecharge = amountPrivate.deductRecharge(userFund, balance);
                        if (deductRecharge.isSuccess()) {
                            flag = false;
                            return deductRecharge;
                        }
                        lockMsg++;
                        log.info("【账户余额添加失败，请查询当前时间范围内的异常情况】");
                    } else if (DELETE_AMOUNT.equals(addType)) {//人工减钱
                        Result deductBalance = amountPrivate.deductBalance(userFund, balance);
                        if (deductBalance.isSuccess()) {
                            flag = false;
                            return deductBalance;
                        }
                        log.info("【账户余额添加失败，请查询当前时间范围内的异常情况】");
                    } else if (DELETE_WITHDRAW.equals(addType)) {
                        Result withdrawBalance = amountPrivate.withdrawBalance(userFund, balance);
                        if (withdrawBalance.isSuccess()) {
                            flag = false;
                            return withdrawBalance;
                        }
                        lockMsg++;
                        log.info("【账户余额添加失败，请查询当前时间范围内的异常情况】");
                    } else if (DELETE_FREEZE.equals(addType)) {
                        Result freezeBalance = amountPrivate.freezeBalance(userFund, balance);
                        if (freezeBalance.isSuccess()) {
                            flag = false;
                            return freezeBalance;
                        }
                        lockMsg++;
                        log.info("【账户余额添加失败，请查询当前时间范围内的异常情况】");
                    } else if (DELETE_QUOTA.equals(addType)) {
                        Result freezeBalance = amountPrivate.deleteQuota(userFund, balance);
                        if (freezeBalance.isSuccess()) {
                            flag = false;
                            return freezeBalance;
                        }
                        lockMsg++;
                        log.info("【账户授权额度失败，请查询当前时间范围内的异常情况】");
                    }
                    if (lockMsg > 20) {
                        log.info("【当前账户余额扣除失败，请查询当前时间范围内的异常情况，当前账户：" + userFund.getUserId() + "，金额：" +
                                balance + "，方法：addAmountBalance，类型：" + addType + "】");
                        log.info("【账户金额冻结失败，请查询当前时间范围内的异常情况，并修改当前账户交易和资金状态为 不可用】");
                        if (userInfoServiceImpl.updataStatusEr(userFund.getUserId())) {
                            log.info("【账户已修改为不可使用，当前账号为：" + userFund.getUserId() + "】");
                        } else {
                            throw new UserException("账户修改异常", null);
                        }
                        log.info("【当前账户余额扣除失败，请联系技术人员查询情况，当前账户：" + userFund.getUserId() + "】");
                        return Result.buildFailMessage("【当前账户余额扣除失败，请联系技术人员查看当前服务异常】");
                    }
                } while (flag);
                lockMsg = null;
            } finally {
            }
        }
        return Result.buildFailMessage("传参异常");
    }
}
