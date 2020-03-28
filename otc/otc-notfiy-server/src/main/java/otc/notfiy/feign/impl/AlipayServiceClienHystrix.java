package otc.notfiy.feign.impl;

import org.springframework.stereotype.Component;

import otc.api.impl.AlipayServiceClienFeignHystrix;
import otc.notfiy.feign.AlipayServiceClien;
@Component
public class AlipayServiceClienHystrix extends AlipayServiceClienFeignHystrix  implements AlipayServiceClien {

}
