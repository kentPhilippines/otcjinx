package test.number;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.Console;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.http.HttpUtil;

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
        String s = HttpUtil.get("https://www.douyin.com/aweme/v1/web/comment/list/?device_platform=webapp&aid=6383&channel=channel_pc_web&aweme_id=7025220358479318283&cursor=0&count=20&version_code=170400&version_name=17.4.0&cookie_enabled=true&screen_width=2048&screen_height=1280&browser_language=zh-CN&browser_platform=MacIntel&browser_name=Mozilla&browser_version=5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit%2F537.36+(KHTML,+like+Gecko)+Chrome%2F94.0.4606.81+Safari%2F537.36&browser_online=true&msToken=gbLXQR_dnKrn-RFmB_fVD4_bQTA1jPEudDj6smJ08oea8lRJhJkM6QBH0oKR_H4t4QZEm6wl0YQtVB_03IbsuRo6qvGerzT6JHs72MnTeU-vCUd9kG8QhB95mQ==&X-Bogus=DFSzsdVLB-sANnZPSuzg/37TlqCp&_signature=_02B4Z6wo00001A-S4xwAAIDDm0S4Ej1fYRgPlueAAGKhV3UzBMCIeoISau-hswePScGqeQP08.9Ey.xReipzX6MARk0C5XuzahFvfHKs3MTkH95uWOmMuN1VUcczne9T6.orB--.L7haJAmL9c");
        System.out.println("【】"+s);
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

