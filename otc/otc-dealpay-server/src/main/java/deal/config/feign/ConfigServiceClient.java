package deal.config.feign;

import org.springframework.cloud.openfeign.FeignClient;

import deal.config.feign.impl.ConfigServiceClientHystrix;
import otc.api.ConfigServiceClientFeign;
import otc.common.PayApiConstant;
@FeignClient(value=PayApiConstant.Server.CONFIG_TASK_SERVER, fallback = ConfigServiceClientHystrix.class)
public interface ConfigServiceClient extends ConfigServiceClientFeign{

}
