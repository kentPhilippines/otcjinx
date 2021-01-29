package test.number;

import java.math.BigDecimal;

public class BeanTest {
	public static void main(String[] args) {
		BigDecimal rate = new BigDecimal("6.43633");
		BigDecimal amount = new BigDecimal("200");

		int i = amount.divide(rate, 6, BigDecimal.ROUND_CEILING).compareTo(new BigDecimal("300"));


		System.out.println(
				i

		);


	}

}
