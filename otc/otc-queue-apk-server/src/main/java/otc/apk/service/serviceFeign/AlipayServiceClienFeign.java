package otc.apk.service.serviceFeign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import otc.apk.service.serviceFeign.impl.AlipayServiceClienFeignHystrix;
import otc.bean.alipay.Medium;
import otc.common.PayApiConstant;
@FeignClient(value=PayApiConstant.Server.CONFIG_TASK_SERVER, fallback = AlipayServiceClienFeignHystrix.class)
public interface AlipayServiceClienFeign {
	
	
	@PostMapping(PayApiConstant.Alipay.MEDIUM_API+PayApiConstant.Alipay.FIND_MEDIUM_IS_DEAL)
	List<Medium> findIsDealMedium(@RequestParam("mediumType")String mediumType, @RequestParam("code")String code);
	
	
	@PostMapping(PayApiConstant.Alipay.MEDIUM_API+PayApiConstant.Alipay.FIND_MEDIUM_IS_DEAL)
	List<Medium> findIsDealMedium(@RequestParam("mediumType")String mediumAlipay);
}
