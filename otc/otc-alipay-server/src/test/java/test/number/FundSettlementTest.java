package test.number;

/**
 * 测试类
 */
public class FundSettlementTest {
    public static void main(String[] args) {
        FundSettlementProcessor processor = new FundSettlementProcessor();
        boolean result = processor.processFundSettlement("bank", 1000, "123456789");
        System.out.println("Fund settlement result: " + result);
    }
}
