package otc.file.feign;

import org.springframework.cloud.openfeign.FeignClient;

import otc.api.AlipayServiceClienFeign;
import otc.common.PayApiConstant;
import otc.file.feign.impl.AlipayServiceClienHystrix;
@FeignClient(value=PayApiConstant.Server.ALIPAY_SERVER, fallback = AlipayServiceClienHystrix.class)
public interface AlipayServiceClien extends AlipayServiceClienFeign {

}
