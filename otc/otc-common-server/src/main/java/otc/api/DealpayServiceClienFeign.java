package otc.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import otc.api.impl.DealpayServiceClienFeignHystrix;
import otc.bean.dealpay.Recharge;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

@FeignClient(value=PayApiConstant.Server.DEALPAY_SERVER, fallback = DealpayServiceClienFeignHystrix.class)
public interface DealpayServiceClienFeign {

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,value = PayApiConstant.Dealpay.DEAL_API+PayApiConstant.Dealpay.RECHARGE_URL)
	public Result recharge(Recharge recharge);
	
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,value = PayApiConstant.Dealpay.DEAL_API+PayApiConstant.Dealpay.WITH_PAY)
	public Result wit(Withdraw  wit) ;
}
