package test.number.channal;

import java.math.BigDecimal;

public class amount {
    public static void main(String[] args) {

        BigDecimal higMoney = new BigDecimal("50000");
        if (!(higMoney.compareTo(new BigDecimal("50000")) > -1)) {
            System.out.println("【当前代付最高金额为50000】");
        }
    }
}
