package otc.apk.feign;

import org.springframework.cloud.openfeign.FeignClient;

import otc.api.AlipayServiceClienFeign;
import otc.api.impl.AlipayServiceClienFeignHystrix;
import otc.apk.feign.impl.AlipayServiceClienHystrix;
import otc.common.PayApiConstant;
@FeignClient(value=PayApiConstant.Server.ALIPAY_SERVER, fallback = AlipayServiceClienHystrix.class)
public interface AlipayServiceClien extends AlipayServiceClienFeign {

}
