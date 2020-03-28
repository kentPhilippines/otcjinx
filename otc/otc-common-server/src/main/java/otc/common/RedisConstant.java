package otc.common;
/**
 * <p>redis公共键</p>
 * @author K
 *
 */
public class RedisConstant {
	public class RedisOne {
		public static final String Test = "test";
	}

	public class RedisTwo {
	}

	/**
	 * <p>关于队列操作Redis缓存的键【缓存分区】</p>
	 */
	public class Queue {
		public final static String QUEUE_REDIS = "QUEUE:REDIS:";
		public static final String HEARTBEAT="HEARTBEAT_";
		
		
	}

	/**
	 * <p>关于用户操作的redis缓存的键【缓存区分】</p>
	 *
	 * @author K
	 */
	public class User {
		public static final String QUEUEQRNODE = "QUEUEQRNODE:ACCOUNT:"; // 支付宝账户  +  此id  决定是否该账户添加到了队列中
		public static final String LOGIN_PARENT = "LOGIN:PARENT:STATUS:";// 用户登陆标识Key--只做统计人数用
		public static final String BIZ_QUEUE = "BIZ:QUEUE:STATUS:";// 用户接单标识Key--只做统计人数用
		public static final String USERPARENT = "USERPARENT:";
		public static final String USER = "USER:";
	}
	
	
	
	

}
