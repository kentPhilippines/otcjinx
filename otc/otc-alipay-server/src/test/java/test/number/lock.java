package test.number;

import cn.hutool.core.thread.ThreadUtil;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class lock {

      static Lock lock = new ReentrantLock();










    public static void main(String[] args) {
        for (int a = 1; a <= 5; a++) {
            int finalA = a;
            ThreadUtil.execute(() -> {
                ThreadUtil.execute(() -> {
                    execute(finalA);
                });
                ThreadUtil.execute(() -> {
                    try {
                        execute2(finalA);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            });
        }
    }

    public static void execute(int a) {
        try {
            int i = 0;
            do {
                i++;
                systenprint(i);
            } while (i < 10);
        }finally {

        }
    }

    public static void execute2(int a ) throws InterruptedException {

        try {
            int i = 0;
            do {
                i++;
                systenprint( i);
            } while (i < 10);
        }   finally {
        }
    }



/*

    public static void systenprint(int i) throws InterruptedException {
    //    System.out.println(Thread.currentThread().getName());

        String print = "";
     //   if("pool-1-thread-3".equals(Thread.currentThread().getName())) {
            print = "" + System.currentTimeMillis();
    //    }
        synchronized (Thread.currentThread().getName()+i) {
            System.out.println(Thread.currentThread().getName() + " " + i + "--" +print   );
        }
    }
*/


    static void systenprint(int i) {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " " + i + "--" + System.currentTimeMillis());
        } finally {
            lock.unlock();
        }

    }








}

class MyThread extends Thread {

    private lock myTask;

    public MyThread(lock myTask) {
        this.myTask = myTask;
    }

    @Override
    public void run() {
        int a =  1 ;
        myTask.execute(a);
    }
}
