package otc.otc.feign;

import org.springframework.cloud.openfeign.FeignClient;

import otc.api.QueueServiceClienFeign;
import otc.common.PayApiConstant;
import otc.otc.feign.impl.QueueServiceClienHystrix;
@FeignClient(value=PayApiConstant.Server.QUEUE_APK_SERVER, fallback = QueueServiceClienHystrix.class)
public interface QueueServiceClien   extends  QueueServiceClienFeign{

}
