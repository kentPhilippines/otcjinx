package test.number;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;

public class IdTest {
	public static void main(String[] args) {
		for(int a = 1 ; a < 1000 ; a++) {
			ThreadUtil.execute(()->{
				/*
				 * String objectId = IdUtil.objectId(); System.out.println(objectId);
				 */
				Snowflake snowflake = IdUtil.createSnowflake(1, 1);
				long id = snowflake.nextId();
				System.out.println(id);
			});
		}
	}

}
