package alipay.manage.api.feign;

import org.springframework.cloud.openfeign.FeignClient;

import alipay.manage.api.feign.impl.DealpayServiceClienHystrix;
import otc.api.DealpayServiceClienFeign;
import otc.api.impl.DealpayServiceClienFeignHystrix;
import otc.common.PayApiConstant;

@FeignClient(value=PayApiConstant.Server.DEALPAY_SERVER, fallback = DealpayServiceClienHystrix.class)
public interface DealpayServiceClien extends DealpayServiceClienFeign{

}
