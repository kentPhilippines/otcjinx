package test.number;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpUtil;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class charN {
	static int o = 1;
	static Lock lock = new ReentrantLock();

	public static void main(String[] args) {

		String s = HttpUtil.get("Http://starpay168.com:5055/api-alipay/deal/wit");

		System.out.println(s);
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
