package alipay.manage.api.configserver;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import alipay.manage.api.configserver.impl.ConfigServiceClientFeignHystrix;
import otc.common.PayApiConstant;
import otc.result.Result;

/**
 * 
 * <p>远程调用配置文件获取</p>
 * @author K
 */
@FeignClient(value=PayApiConstant.Server.CONFIG_TASK_SERVER, fallback = ConfigServiceClientFeignHystrix.class)
public interface ConfigServiceClientFeign {
	@PostMapping(PayApiConstant.Config.CONFIG_API+PayApiConstant.Config.CONFIG_API_GET_CONFIG_SYSTEM)
	public Result getConfig(String system , String key);
	

}
