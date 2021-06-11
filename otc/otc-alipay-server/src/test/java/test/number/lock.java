package test.number;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class lock {

    private static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        for (int a = 1; a <= 5; a++) {
            ThreadUtil.execute(() -> {
                ThreadUtil.execute(() -> {
                    execute();
                });
                ThreadUtil.execute(() -> {
                    execute2();
                });
            });
        }
    }

    public static void execute() {
        try {
            int i = 0;
            do {
                i++;
                systenprint(i);
            } while (i < 10);
        } finally {
        }
    }

    public static void execute2() {

        try {
            int i = 0;
            do {
                i++;
                systenprint(i);
            } while (i < 10);
        } finally {
        }
    }


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
        myTask.execute();
    }
}
