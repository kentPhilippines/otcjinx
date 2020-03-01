package otc.api.alipay;

/**
 * <p>公共常量</p>
 * @author K
 */
public class Common {
	/**
	 * <p>用户类型</p>
	 * @author K
	 */
	public class User{
		//用户类型：商户1 码商2
		/**
		 * <p>码商</p>
		 */
		public static final String USER_TYPE_QR = "2";
		/**
		 * <p>商户</p>
		 */
		public static final String USER_TYPE_APP = "1";
		//是否为代理商:1为代理商 2不为代理商
		/**
		 * <p>代理商</p>
		 */
		public static final String USER_IS_AGENT = "1";
		/**
		 * <p>不为代理商</p>
		 */
		public static final String USER_IS_MEMBER = "2";
		
		/**
		 * <p>人工操作流水</p>
		 */
		public static final String RUN_TYPE_ARTIFICIAL = "2";//流水操作类型 人工操作
		/**
		 * <p>自动操作流水</p>
		 */
		public static final String RUN_TYPE_AUTOMATION = "1";//流水操作类型 自动操作
		
		
		
		
	}
	
	
	
	

}
