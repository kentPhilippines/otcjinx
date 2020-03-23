package alipay.manage.api.Feign;

import org.springframework.cloud.openfeign.FeignClient;

import alipay.manage.api.Feign.impl.FileServiceClienFeignHystrix;
import otc.common.PayApiConstant;

@FeignClient(value=PayApiConstant.Server.QUEUE_APK_SERVER, fallback = FileServiceClienFeignHystrix.class)
public interface QueueServiceClienFeign {

}
