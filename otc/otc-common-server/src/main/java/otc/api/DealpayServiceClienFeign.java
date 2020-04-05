package otc.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import otc.api.impl.DealpayServiceClienFeignHystrix;
import otc.bean.dealpay.Recharge;
import otc.common.PayApiConstant;
import otc.result.Result;

@FeignClient(value=PayApiConstant.Server.DEALPAY_SERVER, fallback = DealpayServiceClienFeignHystrix.class)
public interface DealpayServiceClienFeign {

	@PostMapping(PayApiConstant.Dealpay.DEAL_API+PayApiConstant.Dealpay.RECHARGE_URL)
	public Result recharge(Recharge recharge);
	
	
	
}
