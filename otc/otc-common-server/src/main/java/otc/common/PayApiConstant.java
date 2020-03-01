package otc.common;

public class PayApiConstant {
	public class Server{
		public static final String PAY_SERVER = "otc-pay-server";
		public static final String CONFIG_TASK_SERVER = "otc-task-config-server";//定时任务和配置类服务
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
	}
}
