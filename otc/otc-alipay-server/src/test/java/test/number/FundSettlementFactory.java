package test.number;

/**
 * 资金结算工厂类
 */
public class FundSettlementFactory {
    /**
     * 获取资金结算服务
     *
     * @param type 结算服务类型
     * @return 资金结算服务对象
     */
    public static FundSettlement getFundSettlement(String type) {
        // 根据结算服务类型获取对应的资金结算服务对象
        if ("bank".equals(type)) {
            return new BankFundSettlement();
        } else if ("thirdparty".equals(type)) {
            return new ThirdPartyFundSettlement();
        } else {
            throw new IllegalArgumentException("Invalid fund settlement type: " + type);
        }
    }
}
