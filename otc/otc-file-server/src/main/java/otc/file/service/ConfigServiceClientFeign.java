package otc.file.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import otc.common.PayApiConstant;
import otc.file.service.impl.ConfigServiceClientFeignHystrix;
import otc.result.Result;
/**
 * <p>配置文件获取</p>
 * @author kent
 */
@FeignClient(value=PayApiConstant.Server.CONFIG_TASK_SERVER, fallback = ConfigServiceClientFeignHystrix.class)
public interface ConfigServiceClientFeign {
	/**
	 * <p>配置文件数据获取接口</p>
	 * @param system					配置所属服务
	 * @param key						配置唯一键
	 * @return
	 */
	@PostMapping(PayApiConstant.Config.CONFIG_API+PayApiConstant.Config.CONFIG_API_GET_CONFIG_SYSTEM)
	public Result getConfig(@RequestParam("system") String system ,@RequestParam("key")  String key);
}
