package alipay.manage.api.feign;

import alipay.manage.api.feign.impl.QueueServiceClienHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import otc.api.QueueServiceClienFeign;
import otc.common.PayApiConstant;
@FeignClient(value=PayApiConstant.Server.QUEUE_APK_SERVER, fallback = QueueServiceClienHystrix.class)
public interface QueueServiceClien   extends  QueueServiceClienFeign{
}
