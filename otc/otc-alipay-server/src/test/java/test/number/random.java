package test.number;

import cn.hutool.core.date.DatePattern;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class random {
	public static void main(String[] args) {
		time();
	}

	static void time() {


		long a = 1609780976;
		SimpleDateFormat sdf = new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
		java.util.Date date = new Date(a * 1000);
		String str = sdf.format(date);
		System.out.println(str);


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
