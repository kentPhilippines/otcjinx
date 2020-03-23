package alipay.manage.api.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import alipay.manage.api.Feign.impl.QueueServiceClienFeignHystrix;
import otc.common.PayApiConstant;
import otc.result.Result;

@FeignClient(value=PayApiConstant.Server.QUEUE_APK_SERVER, fallback = QueueServiceClienFeignHystrix.class)
public interface QueueServiceClienFeign {
	@PostMapping(PayApiConstant.Queue.QUEUE_API+PayApiConstant.Queue.FIND_QR)
	public Result getQueue(String[] code);
	

}
