package otc.api.impl;

import org.springframework.stereotype.Component;

import otc.api.DealpayServiceClienFeign;
import otc.bean.dealpay.Recharge;
import otc.result.Result;
@Component
public class DealpayServiceClienFeignHystrix implements DealpayServiceClienFeign {

	@Override
	public Result recharge(Recharge recharge) {
		return null;
	}

}
