package otc.bean.config;

/**
 * <p>配置文件固定键</p>
 * @author K
 */
public class ConfigFile {
	public static final String ALIPAY = "alipay";
	public static final String DEAL = "deal";
	public static final String PAY = "pay";
	
	
	
	public class Deal{
		
	}
	public class Alipay{
		/**
		 * <p>当前ip被风控限制次数</p>
		 */
		public static final String IS_DEAL_IP_COUNT = "IS_DEAL_IP_COUNT";//当前ip被风控限制次数
		public static final String TIBLE_LINK = "TIBLE_LINK";//网站标题
		public static final String REGISTER_ENABLED = "REGISTER_ENABLED";//是否开放注册功能
		public static final String NEW_QRCODE_PRIORITY = "NEW_QRCODE_PRIORITY";//注册优先天数
		public static final String LOCAL_STORAGE_PATH = "LOCAL_STORAGE_PATH";//二维码图片地址
	}
	public class Pay{
		public static final String TEST = "test";
		
	}
}
