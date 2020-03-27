package test.number;

import java.math.BigDecimal;

public class AmountTest {
	public static void main(String[] args) {
		BigDecimal a = new BigDecimal("200");
		BigDecimal b= new BigDecimal("300");
		BigDecimal c = new BigDecimal("500");
		if(a.add(b).compareTo(c)==0) {
			System.out.println("asdsad");
		}
	}

}
