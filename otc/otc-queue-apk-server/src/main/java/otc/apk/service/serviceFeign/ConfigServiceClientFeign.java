package otc.apk.service.serviceFeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import otc.apk.service.serviceFeign.impl.ConfigServiceClientFeignHystrix;
import otc.common.PayApiConstant;
import otc.result.Result;
@FeignClient(value=PayApiConstant.Server.CONFIG_TASK_SERVER, fallback = ConfigServiceClientFeignHystrix.class)
public interface ConfigServiceClientFeign {
	@PostMapping(PayApiConstant.Config.CONFIG_API+PayApiConstant.Config.CONFIG_API_GET_CONFIG_SYSTEM)
	public Result getConfig(@RequestParam("system") String system ,@RequestParam("key")  String key);
}
