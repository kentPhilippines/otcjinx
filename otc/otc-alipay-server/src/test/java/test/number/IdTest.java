package test.number;

import java.lang.management.ManagementFactory;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;

public class IdTest {
	   
		private static int getProcessPiece() {
			// 进程码
			// 因为静态变量类加载可能相同,所以要获取进程ID + 加载对象的ID值
			final int processPiece;
			// 进程ID初始化
			int processId;
			try {
				// 获取进程ID
				final String processName =ManagementFactory.getRuntimeMXBean().getName();
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
		private static final AtomicInteger nextInc = new AtomicInteger(RandomUtil.randomInt());
		public static byte[] nextBytes() {
			final ByteBuffer bb = ByteBuffer.wrap(new byte[12]);
			bb.putInt((int) DateUtil.currentSeconds());// 4位
			bb.putInt(getProcessPiece() );// 4位
			
			bb.putInt(nextInc.getAndIncrement());// 4位
			return bb.array();
		}
		   private static final int THREADS_COUNT = 2;

		    public static int count = 0;
		    public static volatile int countVolatile = 0;
		    public static AtomicInteger atomicInteger = new AtomicInteger(10000);
		    public static CountDownLatch countDownLatch = new CountDownLatch(2);
		    private final static ReentrantLock lock=new ReentrantLock();
		    public static void increase() {
		    	lock.lock();//加锁
		    	try {
		        atomicInteger.incrementAndGet();
		        if(atomicInteger.get()>10010)
					 atomicInteger = new AtomicInteger(10001);
		    	}finally {
		    		lock.unlock();
				}
		    }

		public static void main(String[] args) {
			long currentTimeMillis = System.currentTimeMillis();
			 for(int a = 0 ; a < 20 ; a++) {
				 ThreadUtil.execAsync(()->{
					 increase();
					 int i = atomicInteger.get();
					 System.out.println(i);
				 });
			 }
			 long currentTimeMillis2 = System.currentTimeMillis();
				long A= currentTimeMillis2-currentTimeMillis;
			 
				System.out.println("用时："+ A );
		}

}
