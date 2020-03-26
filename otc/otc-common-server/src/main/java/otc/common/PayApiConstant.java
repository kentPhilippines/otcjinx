package otc.common;

public class PayApiConstant {
	public class Server{
		public static final String PAY_SERVER = "otc-pay-server";//四方支付服务
		public static final String CONFIG_TASK_SERVER = "otc-task-config-server";//定时任务和配置类服务
		public static final String ALIPAY_SERVER = "otc-alipay-server";//支付宝服务
		public static final String FILE_SERVER = "otc-file-server";
		public static final String QUEUE_APK_SERVER = "otc-queue-apk-server";
	}
	public class Test{
		public static final String FIND_TEST = "/findTest";
		public static final String TEST = "/test";
		
	}
	public class Config{
		public static final String CONFIG_API = "/config-api";//后台专用接口
		public static final String CONFIG_API_GET_CONFIG_MANAGE = "/getConfigAdmin";//后台获取所有接口配置的接口
		public static final String CONFIG_API_GET_CONFIG_SYSTEM = "/getConfigSystem";//获取统一配置文件接口
	}
	public class Alipay{
		public static final String ACCOUNT_API = "/account-api";//账户类型接口
		public static final String ADD_ACCOUNT = "/add-account";//用户开户
		public static final String EDIT_ACCOUNT = "/edit-account";//修改用户资料
		public static final String EDIT_ACCOUNT_PASSWORD = "/edit-account-password";//修改登录密码
		public static final String EDIT_ACCOUNT_PAY_PASSWORD = "/edit-account-pay-password";//修改资金密码
		public static final String AMOUNT = "/amount";//账户加钱接口 【充值点数】
		public static final String DELETE_AMOUNT ="/delete_amount";//账户减钱接口【账户金额扣除】
		public static final String FREEZE_AMOUNT = "/freeze_amount";//账户冻结接口【账户资金冻结】
	
		public static final String MEDIUM_API = "/medium";									//媒介处理公共接口
		public static final String FIND_MEDIUM_IS_DEAL = "/find-medium-is-deal";			//查询当前可以交易的媒介
		public static final String AUDIT_MERCHANT_STATUS = "/audit-merchant-status"; //修改商户的状态
	}
	
	public class File{
		public static final String FILE_API = "/file-api";//文件服务api
		public static final String ADD_FILE = "/add-file";//新增文件
		public static final String FIND_FILE = "/find-file";//新增文件
	}
	
	public class Queue{
		public static final String QUEUE_API = "/queue-api";
		public static final String FIND_QR = "/find-queue-qr";
		public static final String UPDATA_QR = "/updata-queue-qr";
		public static final String ADD_QR = "/add-queue-qr";
		
	}
}
