package test.number;

/**
 * 第三方支付资金结算服务
 */
public class ThirdPartyFundSettlement implements FundSettlement {
    /**
     * 执行结算操作
     *
     * @param amount  结算金额
     * @param account 结算账户
     * @return 结算结果
     */
    @Override
    public boolean settle(double amount, String account) {
        // 实现第三方支付资金结算逻辑
        return true;
    }
}
