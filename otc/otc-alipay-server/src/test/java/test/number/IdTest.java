package test.number;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.RandomUtil;
import org.apache.commons.lang.Validate;
import otc.util.number.Number;

import java.lang.management.ManagementFactory;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class IdTest {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {







      /*  ThreadUtil.execute(()->{
            for (int a = 0 ; a <= 10;  a++){
                ThreadUtil.execute(()->{
                    int processPiece = getProcessPiece();
                    processPiece =    processPiece << 2;
                    System.out.println(processPiece  + "-----------a" );
                });
            }
        });

        ThreadUtil.execute(()->{
            for (int a = 0 ; a <= 10;  a++){
                ThreadUtil.execute(()->{
                    int processPiece = getProcessPiece();
                    processPiece =    processPiece << 1;
                    System.out.println( processPiece + "-----------b" );
                });
            }
        });*/
        Map map = new ConcurrentHashMap();
        ThreadUtil.execute(() -> {
            for (int a = 0; a <= 10; a++) {
                String runOrderId = Number.getRunOrderId();
                System.out.println(runOrderId);
                map.put(runOrderId, runOrderId);
            }
            System.out.println(map.size());
        });
        ThreadUtil.execute(() -> {
            for (int a = 0; a <= 10; a++) {
                String runOrderId = Number.getRunOrderId();
                System.out.println(runOrderId);
                map.put(runOrderId, runOrderId);
            }
            System.out.println(map.size());
        });
        ThreadUtil.execute(() -> {
            for (int a = 0; a <= 10; a++) {
                String runOrderId = Number.getRunOrderId();
                System.out.println(runOrderId);
                map.put(runOrderId, runOrderId);
            }
            System.out.println(map.size());
        });
        ThreadUtil.execute(() -> {
            for (int a = 0; a <= 10; a++) {
                String runOrderId = Number.getRunOrderId();
                System.out.println(runOrderId);
                map.put(runOrderId, runOrderId);
            }
            System.out.println(map.size());
        });
        ThreadUtil.execute(() -> {
            for (int a = 0; a <= 10; a++) {
                String runOrderId = Number.getRunOrderId();
                System.out.println(runOrderId);
                map.put(runOrderId, runOrderId);
            }
            System.out.println(map.size());
        });

      /*  String generate = GenerateOrderNo.Generate(Common.Deals.YUCHUANG_FLOW);
        int processPiece = getProcessPiece();
        processPiece =    processPiece << 2;
        String witOrderQr = Number.getWitOrderQr()+processPiece;
        System.out.println(witOrderQr);


        System.out.println(
                generate
        );*/


    }

    public static int nextInt(final int startInclusive, final int endExclusive) {
        Validate.isTrue(endExclusive >= startInclusive,
                "Start value must be smaller or equal to end value.");
        Validate.isTrue(startInclusive >= 0, "Both range values must be non-negative.");

        if (startInclusive == endExclusive) {
            return startInclusive;
        }
        return startInclusive + RANDOM.nextInt(endExclusive - startInclusive);
    }

    private static int getProcessPiece() {
        // 进程码
        // 因为静态变量类加载可能相同,所以要获取进程ID + 加载对象的ID值
        final int processPiece;
        // 进程ID初始化
        int processId;
        try {
            // 获取进程ID
            final String processName = ManagementFactory.getRuntimeMXBean().getName();
            final int atIndex = processName.indexOf('@');
            if (atIndex > 0) {
                processId = Integer.parseInt(processName.substring(0, atIndex));
            } else {
                processId = processName.hashCode();
            }
        } catch (Throwable t) {
            processId = RandomUtil.randomInt();
        }

        final ClassLoader loader = ClassLoaderUtil.getClassLoader();
        // 返回对象哈希码,无论是否重写hashCode方法
        int loaderId = (loader != null) ? System.identityHashCode(loader) : 0;

        // 进程ID + 对象加载ID
        StringBuilder processSb = new StringBuilder();
        processSb.append(Integer.toHexString(processId));
        processSb.append(Integer.toHexString(loaderId));
        // 保留前2位
        processPiece = processSb.toString().hashCode() & 0xFFFF;

        return processPiece;
    }


    private static int getMachinePiece() {
        // 机器码
        int machinePiece;
        try {
            StringBuilder netSb = new StringBuilder();
            // 返回机器所有的网络接口
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            // 遍历网络接口
            while (e.hasMoreElements()) {
                NetworkInterface ni = e.nextElement();
                // 网络接口信息
                netSb.append(ni.toString());
            }
            // 保留后两位
            machinePiece = netSb.toString().hashCode() << 16;
        } catch (Throwable e) {
            // 出问题随机生成,保留后两位
            machinePiece = (RandomUtil.randomInt()) << 16;
        }
        return machinePiece;
    }
}
