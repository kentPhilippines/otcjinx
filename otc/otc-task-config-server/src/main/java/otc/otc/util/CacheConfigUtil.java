package otc.otc.util;

import org.springframework.stereotype.Component;

import otc.result.Result;

/**
 * <p>配置类区分</p>
 * @author K
 */
@Component
public class CacheConfigUtil {
	private static final String SYSTEM_PAY = "system-pay";
	private static final String SYSTEM_DEAL = "system-deal";
	private static final String SYSTEM_ALIPAY = "system-alipay";
	public Result getconfig(String system , String key) {
		return Result.buildSuccessMessage("获取配置数据");
	}
}
