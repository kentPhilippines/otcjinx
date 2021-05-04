package test.number;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class thread {
	static Lock lock = new ReentrantLock();

	public static void main(String[] args) {
        for (int a = 0; a <= 100; a++) {
            Random ram = new Random();
            int i = ram.nextInt(5);
            System.out.println(i);

        }


    }


}

