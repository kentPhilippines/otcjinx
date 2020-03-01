package otc.otc.util;

import cn.hutool.setting.Setting;

public class SettingFile {
	public static final String REQUEST_SECOND_ORDER = "requset_second_order";//交易订单过期时间 单位:秒
	public static final String LOCALSTORAGEPATH = "localStoragePath";//二维码上传地址  该地址需要有文件夹,否则会报错,上传时若该文件夹
	public static final String LOCALSTORAGEPATH_BAK = "LOCALSTORAGEPATH_BAK";//图片剪裁地址
	public static final String QR_OUT_TIME = "qrOutTime";//二维码交易订单超时时间
	public static final String OPEN_ACCOUNT_CODE = "open_account_code";//二维码交易订单超时时间
	public static final String YUECHAUNG_NOTIFY_URL = "yuechuang_notify_url";//越创回调地址
	public static final String TIBLE_LINK = "table_link";//网站标题
	public static final String REGISTER_ENABLED = "RegisterEnabled";//是否开放注册功能
	public static final String IS_DEAL_IP = "is_deal_ip";//当前ip是否可以交易次数
	public static final String IS_DEAL_IP_TIME = "is_deal_ip_time";//当前ip是否可以交易次数
	public static final String APPEAL_TIME = "APPEAL_TIME";//订单申述时间    小时为单位
	public static final String ASYNC_URL = "async_url";
	public static final String DPAYASYNC_URL = "dpayasync_url";
	public static final String SYNC_URL = "sync_url";
	public static final String FTP_ADDRESS = "FTP_ADDRESS";
	public static final String FTP_PORT = "FTP_PORT";
	public static final String FTP_USERNAME = "FTP_USERNAME";
	public static final String FTP_PASSWORD = "FTP_PASSWORD";
	public static final String FREEZE_AMOUNT_VIRTUAL = "FREEZE_AMOUNT_VIRTUAL";//虚拟金额分界线,该分界线为  大额金额分界线
	public static final String FREEZE_SECOND_VIRTUAL = "FREEZE_SECOND_VIRTUAL";//虚拟冻结大额金额时间 单位：秒
	public static final String FREEZE_PLAIN_VIRTUAL = "FREEZE_PLAIN_VIRTUAL";//虚拟冻结普通金额时间 单位：秒
	/**
	 * 码商充值代付api
	 */
	public static final String BANKCARD_PAYURL = "BANKCARD_PAYURL";
	public static final String BANKCARD_DPAYURL = "BANKCARD_DPAYURL";
	/**
	 * 码商充值回调api
	 */
	public static final String BANKCARD_PAYNOTFIYURL = "BANKCARD_PAYNOTFIYURL";
	public static final String BANKCARD_DPAYNOTFIYURL = "BANKCARD_DPAYNOTFIYURL";
	public static final String ENTER_ORDER_SU = "ENTER_ORDER_SU_W";
	public static final String LOCALSTORAGEPATH_GOOGLE_IMG = "GOOGLE_IMG_PATH";
	public static final String UAER_MAIL_J = "UAER_MAIL_J";//修改密码 的安全邮箱    1261047188@qq.com
	public static final String QR_IS_CLICK = "QR_IS_CLICK";//检查二维码超过三次锁定时间   单位秒
	public static final String QR_IS_CLICK_COUNT = "QR_IS_CLICK_COUNT";//二维码未收到回调 次数
	public static final String WITHDRAW_ORDER_TIME = "WITHDRAW_ORDER_TIME";//码商提现出款时间    48小时     配置时间单位为秒
	public static final String LINUX_PYTHON_READ_IMG = "LINUX_PYTHON_READ_IMG";
	public static final String APPID_FIXATION_MONEY = "APPID_FIXATION_MONEY";//非固码账户
	public static final String NEW_QRCODE_PRIORITY = "NEW_QRCODE_PRIORITY";//二维码优先天数
	public static final String IP_BIZ_LIMIT_TIMES = "IP_BIZ_LIMIT_TIMES";//异常接单次数
	public static final String BIZ_AMOUNT_CLASS = "BIZ_AMOUNT_CLASS";//接单金额档次
	public static final String BIZ_LIMIT_DATE = "BIZ_LIMIT_DATE";//支付宝上传时间

	private Setting setting = new Setting();
	public Setting getSetting() {
		return setting;
	}
	public void setSetting(Setting setting) {
		this.setting = setting;
	}
	
	/**
	 * <p>根据key值获取value</p>
	 * @param key
	 * @return
	 */
	public String getName(String key) {
		String string = setting.get(key);
		return string;
	}
	/**
	 * <p>从新加载配置文件</p>
	 * @return
	 */
	public boolean load() {
		boolean load = setting.load();
		return load;
	}

}
