package alipay.manage.api.feign;

import org.springframework.cloud.openfeign.FeignClient;

import alipay.manage.api.feign.impl.ConfigServiceClientHystrix;
import otc.api.ConfigServiceClientFeign;
import otc.api.impl.ConfigServiceClientFeignHystrix;
import otc.common.PayApiConstant;
@FeignClient(value=PayApiConstant.Server.CONFIG_TASK_SERVER, fallback = ConfigServiceClientHystrix.class)
public interface ConfigServiceClient extends ConfigServiceClientFeign{

}
