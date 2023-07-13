package test.number;

/**
 * 资金结算处理器
 */
public class FundSettlementProcessor {
    /**
     * 处理资金结算请求
     *
     * @param type    结算服务类型
     * @param amount  结算金额
     * @param account 结算账户
     * @return 结算结果
     */
    public boolean processFundSettlement(String type, double amount, String account) {
        FundSettlement fundSettlement = FundSettlementFactory.getFundSettlement(type);
        return fundSettlement.settle(amount, account);
    }
}
