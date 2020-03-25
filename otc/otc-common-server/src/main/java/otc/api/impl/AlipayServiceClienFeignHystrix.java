package otc.api.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import otc.api.AlipayServiceClienFeign;
import otc.bean.alipay.Medium;
@Component
public class AlipayServiceClienFeignHystrix implements AlipayServiceClienFeign {

	@Override
	public List<Medium> findIsDealMedium(String mediumType, String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Medium> findIsDealMedium(String mediumAlipay) {
		// TODO Auto-generated method stub
		return null;
	}

}
