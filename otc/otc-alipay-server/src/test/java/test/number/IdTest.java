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
	public static void main(String[] args) {
		Snowflake snowflake = IdUtil.createSnowflake(1, 1);
		String id = snowflake.nextId()+"";
		System.out.println(id);
		System.out.println("长度："+id.length());
	}
}
