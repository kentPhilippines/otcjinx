package test.number;

import cn.hutool.core.util.RandomUtil;
import otc.api.alipay.Common;

public class random {
	public static void main(String[] args) {
    	String randomString = RandomUtil.randomString(10);
    	System.out.println(randomString);
    	String randomString2 = RandomUtil.randomNumbers(15);
    	String orderId = "APP"+randomString2 ; 
    	System.out.println(orderId);
	}

}
