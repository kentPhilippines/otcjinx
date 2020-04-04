package deal.config.feign;

import org.springframework.cloud.openfeign.FeignClient;

import deal.config.feign.impl.QueueServiceClienHystrix;
import otc.api.QueueServiceClienFeign;
import otc.common.PayApiConstant;
@FeignClient(value=PayApiConstant.Server.QUEUE_APK_SERVER, fallback = QueueServiceClienHystrix.class)
public interface QueueServiceClien   extends  QueueServiceClienFeign{

}
