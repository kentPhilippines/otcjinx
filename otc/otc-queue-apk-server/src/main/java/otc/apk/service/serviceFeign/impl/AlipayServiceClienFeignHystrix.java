package otc.apk.service.serviceFeign.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import otc.apk.service.serviceFeign.AlipayServiceClienFeign;
import otc.bean.alipay.Medium;
@Component
public class AlipayServiceClienFeignHystrix implements AlipayServiceClienFeign{
	Logger log = LoggerFactory.getLogger(ConfigServiceClientFeignHystrix.class);
	@Override
	public List<Medium> findIsDealMedium(String mediumType, String code) {
		 log.info(" 当前服务不可用" + code);
		return new ArrayList();
	}
	@Override
	public List<Medium> findIsDealMedium(String mediumAlipay) {
		 log.info(" 当前服务不可用" + mediumAlipay);
		return new ArrayList();
	}
}
