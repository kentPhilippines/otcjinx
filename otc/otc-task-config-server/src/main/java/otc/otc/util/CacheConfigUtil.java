package otc.otc.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import otc.bean.config.ConfigFile;
import otc.result.Result;

/**
 * <p>配置类区分</p>
 * @author K
 */
@Component
public class CacheConfigUtil {
	private static final String SYSTEM_PAY = ConfigFile.ALIPAY;
	private static final String SYSTEM_DEAL = ConfigFile.DEAL;
	private static final String SYSTEM_ALIPAY = ConfigFile.PAY;
	private static Map mapAlipay;
	private static Map mapDeal;
	private static Map mapPay;
	static {
		alipayMap();
		dealMap();
		payMap();
	}
	static void alipayMap(){
		mapAlipay  = new HashMap();
		mapAlipay.put(ConfigFile.Alipay.LOCAL_STORAGE_PATH, "D:/img");
	}
	static void dealMap(){
		mapDeal  = new HashMap();
		mapDeal.put(ConfigFile.Alipay.LOCAL_STORAGE_PATH, "D:/img");
	}
	static void payMap(){
		mapPay  = new HashMap();
		mapPay.put(ConfigFile.Alipay.LOCAL_STORAGE_PATH, "D:/img");
	}
	public Result getconfig(String system , String key) {
		Object obj  ; 
		switch (system) {
		case SYSTEM_ALIPAY:
			obj = mapAlipay.get(key);
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
