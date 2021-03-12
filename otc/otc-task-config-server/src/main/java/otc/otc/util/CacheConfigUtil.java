package otc.otc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import otc.bean.config.ConfigFile;
import otc.result.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>配置类区分</p>
 *
 * @author K
 */
@Component
public class CacheConfigUtil {
	Logger log = LoggerFactory.getLogger(CacheConfigUtil.class);
	private static final String SYSTEM_PAY = ConfigFile.PAY;
	private static final String SYSTEM_DEAL = ConfigFile.DEAL;
	private static final String SYSTEM_ALIPAY = ConfigFile.ALIPAY;  
	private static Map<String, String> mapAlipay;
	private static Map<String, String> mapDeal;
	private static Map<String, String> mapPay;
	static {
		alipayMap();
		dealMap();
		payMap();
	}
	static void alipayMap() {
		mapAlipay = new HashMap<String, String>();
		mapAlipay.put(ConfigFile.Alipay.LOCAL_STORAGE_PATH, "/img");
		mapAlipay.put(ConfigFile.Alipay.QR_OUT_TIME, "300");
		mapAlipay.put(ConfigFile.Alipay.QR_IS_CLICK, "3000");
		mapAlipay.put(ConfigFile.Alipay.FREEZE_PLAIN_VIRTUAL, "1800");
		mapAlipay.put(ConfigFile.Alipay.TIBLE_LINK, "金星");
		mapAlipay.put(ConfigFile.Alipay.NEW_QRCODE_PRIORITY, "3");
		mapAlipay.put(ConfigFile.Alipay.LOCAL_STORAGE_PATH_BAK, "/img/bak");
		mapAlipay.put(ConfigFile.Alipay.SERVER_IP, "http://starpay168.com:5055/api-alipay");
	}
	static void dealMap(){
		mapDeal  = new HashMap<String, String>();
		mapDeal.put(ConfigFile.Deal.TIBLE_LINK, "快马卡商");
		mapDeal.put(ConfigFile.Deal.URL, "http://112.121.162.162:7010");
		mapDeal.put(ConfigFile.Deal.RECHARGE_URL, "http://112.121.162.162:7010/otc365?");
	}
	static void payMap(){
		mapPay  = new HashMap<String, String>();
		mapPay.put(ConfigFile.Alipay.LOCAL_STORAGE_PATH, "/img");
	}
	public Result getconfig(String system , String key) {
		Object obj  ; 
		switch (system) {
		case SYSTEM_ALIPAY:
			obj = mapAlipay.get(key);
			log.info("【当前配置数据:"+mapAlipay.toString()+"】");
			return Result.buildSuccessResult(obj);
		case SYSTEM_DEAL:
			obj = mapDeal.get(key);
			return Result.buildSuccessResult(obj);
		case SYSTEM_PAY:
			obj = mapPay.get(key);
			return Result.buildSuccessResult(obj);
		}
		
		
		
		
		
		
		
		return Result.buildSuccessMessage("获取配置数据");
	}
}
