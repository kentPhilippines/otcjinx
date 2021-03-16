package test.number;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class thread {
	static Lock lock = new ReentrantLock();

	public static void main(String[] args) {
		try {


			lock.lock();


		} finally {
			lock.unlock();
		}
	}


}

