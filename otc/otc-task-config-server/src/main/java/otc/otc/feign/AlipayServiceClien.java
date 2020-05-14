package otc.otc.feign;

import org.springframework.cloud.openfeign.FeignClient;

import otc.api.AlipayServiceClienFeign;
import otc.api.impl.AlipayServiceClienFeignHystrix;
import otc.common.PayApiConstant;
import otc.otc.feign.impl.AlipayServiceClienHystrix;
@FeignClient(value=PayApiConstant.Server.ALIPAY_SERVER, fallback = AlipayServiceClienHystrix.class)
public interface AlipayServiceClien extends AlipayServiceClienFeign {

}
