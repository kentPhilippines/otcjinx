package alipay.manage.api.channel.util.zhaunshi;

import com.squareup.okhttp.MediaType;

public class Config {
    public static final String DOMAIN = "https://best-dpay.com";//请求接口
    public static final String KEY = "hSiupW3xkezvfUHy";//MD5key
    public static final String PLATFORM_ID = "PF0083";//商户号
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String PAYOUT_SERVICE_ID = "SVC0004"; //服务Id请填入SVC0004(银行卡代付)
    /**
     * 		service_id 枚举列表 			枚举值			类型			说明
										SVC0001			String			银行卡代收
										SVC0002			String			銀行卡充值
										SVC0004			String			银行卡代付
										SVC0005			String			支付宝扫码转卡
										SVC0006			String			云闪付扫码转卡
										SVC0008			String			微信扫码
										SVC0009			String			微信话费
										SVC0010			String			支付宝扫码
     */
}
