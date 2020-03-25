package otc.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import otc.api.impl.ConfigServiceClientFeignHystrix;
import otc.common.PayApiConstant;
import otc.result.Result;

/**
 * <p>远程调用配置文件获取</p>
 * @author K
 */
@FeignClient(value=PayApiConstant.Server.CONFIG_TASK_SERVER, fallback = ConfigServiceClientFeignHystrix.class)
public interface ConfigServiceClientFeign {
	/**
	 * <p>全局配置接口获取</p>
	 * @param system					系统类型
	 * @param key						配置键
	 * @return							【返回值】
	 */
	@PostMapping(PayApiConstant.Config.CONFIG_API+PayApiConstant.Config.CONFIG_API_GET_CONFIG_SYSTEM)
	public Result getConfig(@RequestParam("system") String system ,@RequestParam("key")  String key);
}
