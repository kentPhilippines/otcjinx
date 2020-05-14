package otc.otc.feign.impl;

import org.springframework.stereotype.Component;

import otc.api.impl.AlipayServiceClienFeignHystrix;
import otc.otc.feign.AlipayServiceClien;
@Component
public class AlipayServiceClienHystrix extends AlipayServiceClienFeignHystrix  implements AlipayServiceClien {

}
