package test.number;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.Console;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>推送算法测试类</p>
 */
public class TIME {
    static final String SC5 = "*/3 * * * * ? ";
    static final String SC1 = "*/1 * * * * ? ";
    static Lock lock = new ReentrantLock();
    static Map<Integer, Integer> map = new ConcurrentHashMap();

    public static void main(String[] args) {
        CronUtil.setMatchSecond(true);
        CronUtil.start();
        for (int a = 1; a < 5; a++) {
            int finalA = a;
            new TIME().test(finalA);
        }
        for (int a = 6; a < 15; a++) {
            int finalA = a;
            new TIME().test(finalA);
        }
    }

    private void test(int a) {
        lock.lock();
        try {
            String schedule = CronUtil.schedule("" + a, SC5, (Task) () -> {
                Console.log(a + "   ：   Task excuted.");
                Console.log(DatePattern.NORM_DATETIME_FORMAT.format(new Date()));
                if (null == map.get(a)) {
                    map.put(a, 1);
                } else {
                    Integer o = map.get(a);
                    o++;
                    if (o > 5) {
                        Console.log(" 删除定时任务id  ：    " + a);
                        CronUtil.remove("" + a);
                    }
                    map.put(a, o);
                }
            });
// 支持秒级别定时任务
        } finally {
            lock.unlock();
        }
    }

}

