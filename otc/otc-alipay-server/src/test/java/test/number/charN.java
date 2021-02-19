package test.number;

import cn.hutool.core.thread.ThreadUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class charN {
	static int o = 1;
	static Lock lock = new ReentrantLock();

	public static void main(String[] args) {

		BigInteger bigInteger = new BigDecimal("180.00").toBigInteger();
		BigInteger bigInteger1 = new BigDecimal("1000000").toBigInteger();
		System.out.println(bigInteger);
		System.out.println(bigInteger1);

		BigInteger multiply = bigInteger.multiply(bigInteger1);
		System.out.println(multiply);
	}

	static boolean test() {
		lock.lock();
		try {
			o++;
			System.out.println("执行次数：" + o);
			int a = 1;
			if (o == 2) {
				System.out.println("执行次数2---------------------------：" + o);

			}
			int b = 5;


			int c = b - a;
			ThreadUtil.sleep(200);
			if (c == 4) {
				a = 2;

				return true;
			} else {
				System.out.println("错误，a == " + a);
				System.out.println("错误，c == " + c);
				return false;
			}
		} finally {
			lock.unlock();
		}


	}
}
