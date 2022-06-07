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
		
		public static final String ALIPAY_FEE ="1";//入款费率
		
		public static final String ALIPAY_PRODUCTID ="ALIPAY_SCAN";//入款费率
	
		
	}


	public static class Order {
		public static class Recharge {
			public static final String ORDER_STATUS_SU = "2";//订单成功
			public static final String ORDER_STATUS_ER = "3";//订单失败
			public static final String ORDER_STATUS_YU = "1";//订单处理中
		}

		public static class Wit {
			public static final String ORDER_STATUS_SU = "2";//订单成功
			public static final String ORDER_STATUS_ER = "3";//订单失败
			public static final String ORDER_STATUS_PUSH = "4";//订单已推送
			public static final String ORDER_STATUS_YU = "1";//订单处理中

			public static final String WIT_QR = "2";                //码商提现
			public static final String WIT_ACC = "1";                //商户提现


			public static final String WIT_TYPE_API = "1";                //API代付
			public static final String WIT_TYPE_MANAGE = "2";            //商户提现
			public static final String WIT_TYPE_CLI = "3";                //码商客户端提现

		}

		public static final Integer ORDER_TYPE_DEAL = 1;//交易订单
		public static final Integer ORDER_TYPE_WIT = 5;//代付充值
		public static final Integer ORDER_TYPE_BANKCARD_R = 3;// 卡商入款订单
		public static final Integer ORDER_TYPE_BANKCARD_W = 4;// 卡商出款订单
		public static final Integer DAPY_OPEN = 1;//代付开启
		public static final Integer DAPY_OFF = 2;//代付关闭
		public static final Integer OFF = 0;//总开关 关闭
		public static final Integer OPEN = 1;//总开关开启
		public static final Integer DEAL_OPEN = 1;//交易开启
		public static final Integer DEAL_OFF = 2;//交易关闭

		public static class DealOrderApp {
			public static final String ORDER_STATUS_DISPOSE = "1";//订单处理中
			public static final String COMMENT_WIT = "等待推送中";//订单处理中
			public static final String ORDER_STATUS_SU = "2";//订单成功
			public static final String ORDER_STATUS_ER = "4";//订单失败

		}

		public static class DealOrder {
			/**
			 * <p>订单处理中</p>
			 */
			public static final String ORDER_STATUS_DISPOSE = "1";//订单处理中
			/**
			 * <p>订单成功</p>
			 */
			public static final String ORDER_STATUS_SU = "2";//订单成功
			/**
			 * <p>订单未收到回调</p>
			 */
			public static final String ORDER_STATUS_NO_CALLBACK= "3";//订单未收到回调
			/**
			 * <p>订单失败</p>
			 */
			public static final String ORDER_STATUS_ER= "4";//订单失败
			/**
			 * <p>订单超时</p>
			 */
			public static final String ORDER_STATUS_OVERTIME = "5";//订单超时
		}
		


		public static final String DATE_TYPE = "yyyyMMddHHmmss";
	}

	public static class Deals{
		public static final String ORDERDEAL = "DE";//交易订单
		public static final String ORDERDEAL_CHANNEL = "CH";//渠道交易订单
		public static final String WITDBC = "BDC";//卡商交易出款订单
		public static final String ORDERRUN = "RUN";//流水订单
		public static final String ORDERWIT = "WIT";//代付订单
		public static final String ORDERWIT_APP = "AW";//代付订单【商户】
		public static final String ORDERWIT_QR = "QRW";//代付订单【码商】
		public static final String ORDERWIT_CA = "CBW";//代付订单【卡商】
		public static final String ORDEREXCE = "EXCE";//异常订单
		public static final String ORDERRECORD = "REX";//所有订单
		public static final String IMG = "IMG";//所有订单
		public static final String MEDIUM = "MM";//所有订单
		public static final String BANK = "BAN";//所有订单
		public static final String ADD_MOUNT = "ADD";//系统加钱订单
		public static final String DEL_MOUNT = "DEL";//系统减钱订单
		public static final String YUCHUANG_FLOW = "ANO";//越创流水
		public static final String YUCHUANG_FLOW_A = "ALI";//越创流水
		public static final String WITDBR = "BDC";
		public static final String RECHARGE = "RE-Q";//码商充值订单
		public static final String RECHARGE_CARD = "RE-C";//码商充值订单
	}

	public static final Integer STATUS_IS_OK = 1;//数据有效
	public static final Integer STATUS_IS_NOT_OK = 0;//数据无效
	public static final String notOk = "1";//数据逻辑删除
	public static final String isOk = "2";//数据逻辑可用
	public static final String MEDIUM_ALIPAY = "alipay";//支付宝收款媒介

	
	
	
	public static class  Bank {
		public static final Integer BANK_QR = 2;		//码商的卡
		public static final Integer BANK_SYS = 1;		//系统的卡
		public static final Integer BANK_APP = 3;		//商户的卡
	};

	
	public static class Deal {
		public static final String PRODUCT_ALIPAY_SCAN = "ALIPAY_SCAN";//支付宝扫码
        public static final String PDD_303 = "PDD_303";//金星拼多多
		public static final String PRODUCT_ALIPAY_H5 = "ALIPAY_H5";//支付宝H5
		public static final String WITHDRAW_MY = "SYSTEM_DP";//系统出款
		public static final String WITHDRAW_QAINKUI_ALIPAY = "WITHDRAW_QAINKUI_ALIPAY";//钱柜alipay代付
		public static final String WITHDRAW_ZAHUNSHI_ALIPAY = "WITHDRAW_ZAHUNSHI_ALIPAY";//钻石alipay代付
		public static final String WITHDRAW_DEAL = "WITHDRAW_DEAL";//三方代付
		public static final String AMOUNT_ORDER_ADD = "1";//资金订单  加款类型
		public static final String AMOUNT_ORDER_DELETE = "2";//资金订单  扣款款类型
		public static final String AMOUNT_ORDER_DELETE_FREEZE = "3";//资金冻结
		public static final String AMOUNT_ORDER_ADD_FREEZE = "4";//资金解冻
		public static final String AMOUNT_ORDER_ADD_QUOTA = "5";//增加授权额度
		public static final String AMOUNT_ORDER_DELETE_QUOTA = "6";//减少授权额度
		public static final String AMOUNT_ORDER_FRANSFER = "7";//商户转账申请


		public static final String AMOUNT_ORDER_SU = "3";//订单成功【加减款订单】
		public static final String AMOUNT_ORDER_HE = "2";//订单处理中【加减款订单】
		public static final String AMOUNT_ORDER_ER = "4";//订单失败 【加减款订单】
	}


	public static class Medium {
		public static final String QR_IS_DEAL_OFF = "1";//二维码不可用
		public static final String QR_IS_DEAL_ON = "2";//二维码可用
		public static final String MEDIUM_ALIPAY = "alipay";
		public static final String MEDIUM_BANK = "card";
		public static final String IMG_NUMBER = "IMG";//图片标签
		public static final String MM_NUMBER = "MM";//图片标签
		public static final String BANK_NUMBER = "BK";//媒介标签
	}
	
	public static class Notfiy{
		public static final String ORDER_AMOUNT = "amount";//回调金额
		public static final String ORDER_PHONE = "phone";//回调设备号
		public static final String ORDER_ENTER_IP = "orderip";//回调设备号
	}
}
