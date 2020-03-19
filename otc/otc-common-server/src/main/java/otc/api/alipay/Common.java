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
		
		
		/**
		 * <p>用户总开关，打开</p>
		 */
		public static final String USER_INFO_ON = "1";//用户总开关打开
		/**
		 * <p>用户总开关，关闭</p>
		 */
		public static final String USER_INFO_OFF = "0";//用户总开关关闭
	}
	
	
	public static class Order{
		/**
		 * <p>订单处理中</p>
		 */
		public static final Integer ORDER_STATUS_DISPOSE = 1;//订单处理中
		/**
		 * <p>订单成功</p>
		 */
		public static final Integer ORDER_STATUS_SU = 2;//订单成功
		/**
		 * <p>订单未收到回调</p>
		 */
		public static final Integer ORDER_STATUS_NO_CALLBACK= 3;//订单未收到回调
		/**
		 * <p>订单失败</p>
		 */
		public static final Integer ORDER_STATUS_ER= 4;//订单失败
		/**
		 * <p>订单超时</p>
		 */
		public static final Integer ORDER_STATUS_OVERTIME = 5;//订单超时
	}
	
	
	

}
