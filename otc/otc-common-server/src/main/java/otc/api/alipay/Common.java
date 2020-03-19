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


		public static final String QR_IS_DEAL_OFF = "1";//二维码不可用

		public static final String QR_IS_DEAL_ON = "2";//二维码可用
	}

	public static class Deals{
		public static final String ORDERDEAL = "DE";//交易订单
		public static final String ORDERRUN = "RUN";//流水订单
		public static final String ORDERWIT = "WIT";//代付订单
		public static final String ORDEREXCE = "EXCE";//异常订单
		public static final String ORDERRECORD = "REX";//所有订单
		public static final String IMG = "IMG";//所有订单
		public static final String MEDIUM = "MM";//所有订单
		public static final String BANK = "BAN";//所有订单
		public static final String ADD_MOUNT = "ADD";//系统加钱订单
		public static final String DEL_MOUNT = "DEL";//系统减钱订单
		public static final String YUCHUANG_FLOW = "ANO";//越创流水
	}

	public static final Integer STATUS_IS_OK = 1;//数据有效
	public static final Integer STATUS_IS_NOT_OK = 0;//数据无效
	public static final String notOk = "1";//数据逻辑删除
	public static final String isOk = "2";//数据逻辑可用
	public static final String MEDIUM_ALIPAY = "alipay";//支付宝收款媒介

}
