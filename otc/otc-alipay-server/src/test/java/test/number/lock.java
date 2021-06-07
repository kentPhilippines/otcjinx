package test.number;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class lock {

    private Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        lock myTask = new lock();
        MyThread mt1 = new MyThread(myTask);
        mt1.setName("mt1");
        MyThread mt2 = new MyThread(myTask);
        mt2.setName("mt2");
        mt1.start();
        mt2.start();
    }

    public void execute() {
        lock.lock();
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " " + i + "--" + System.currentTimeMillis());
        }
        lock.unlock();
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
