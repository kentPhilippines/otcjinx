package test.number;

import java.math.BigDecimal;

public class random {
	public static void main(String[] args) {
		isNumber("2");
	}

	private static boolean isNumber(String str) {
		BigDecimal a = new BigDecimal("1");
		System.out.println(a);
		double dInput = a.doubleValue();
		long longPart = (long) dInput;
		BigDecimal bigDecimal = new BigDecimal(Double.toString(dInput));
		BigDecimal bigDecimalLongPart = new BigDecimal(Double.toString(longPart));
		double dPoint = bigDecimal.subtract(bigDecimalLongPart).doubleValue();
		System.out.println("整数部分为:" + longPart + "\n" + "小数部分为: " + dPoint);
		return dPoint > 0;
	}
}
