package test.number;

import java.math.BigDecimal;

public class BeanTest {
	public static void main(String[] args) {
        String amount = "1200.00";
        int i = new BigDecimal(amount).intValue();
        Integer integer = Integer.valueOf(i);

        System.out.println(i);


    }

}
