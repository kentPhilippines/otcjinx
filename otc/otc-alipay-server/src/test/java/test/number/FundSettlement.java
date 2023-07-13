package test.number;

/**
 * 资金结算接口
 */
public interface FundSettlement {
    /**
     * 执行结算操作
     *
     * @param amount  结算金额
     * @param account 结算账户
     * @return 结算结果
     */
    public boolean settle(double amount, String account);
}
