package test.number;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class syLock {

    final static Lock lock = new ReentrantLock();

    public static void test(Long userId) throws InterruptedException {
        System.out.println("------线程开始进入-----" + new Date());
        lock.lock();
        try {
            System.err.println("打印..." + new Date());
            Thread.sleep(1);
        } catch (Exception e) {
        }finally {
            lock.unlock();    //必须在finally中解锁
        }
    }

    public static void test2(Long userId) throws InterruptedException {
        System.out.println("------线程开始进入-----" + new Date());
        synchronized (String.valueOf(userId).intern()) {
            System.err.println("打印..." + new Date());
            Thread.sleep(1);
        }
    }

    public static void main(String[] srrt) {
        for(int i = 0;i < 3;i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        test(1234L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
