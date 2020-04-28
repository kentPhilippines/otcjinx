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
		public static final String TIBLE_LINK = "TIBLE_LINK";//网站标题
		public static final String RECHARGE_URL = "RECHARGE_URL";//网站标题
		public static final String URL = "URL";//网站访问地址
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
		public static final String QR_OUT_TIME = "QR_OUT_TIME";//订单超时时间    300  秒
		public static final String QR_IS_CLICK = "QR_IS_CLICK";//检查二维码超过三次锁定时间   单位秒
		public static final String FREEZE_PLAIN_VIRTUAL = "FREEZE_PLAIN_VIRTUAL";//虚拟冻结普通金额时间 单位：秒
		public static final String LOCAL_STORAGE_PATH_BAK = "LOCAL_STORAGE_PATH_BAK";//剪裁后的图片存放地址
	}
	public class Pay{
		public static final String TEST = "test";
		
	}
}
