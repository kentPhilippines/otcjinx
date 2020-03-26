package otc.apk.feign;

import org.springframework.cloud.openfeign.FeignClient;

import otc.api.ConfigServiceClientFeign;
import otc.api.impl.ConfigServiceClientFeignHystrix;
import otc.apk.feign.impl.ConfigServiceClientHystrix;
import otc.common.PayApiConstant;
@FeignClient(value=PayApiConstant.Server.CONFIG_TASK_SERVER, fallback = ConfigServiceClientHystrix.class)
public interface ConfigServiceClient extends ConfigServiceClientFeign{

}
