package otc.apk.feign.impl;

import org.springframework.stereotype.Component;

import otc.api.impl.AlipayServiceClienFeignHystrix;
import otc.apk.feign.AlipayServiceClien;
@Component
public class AlipayServiceClienHystrix extends AlipayServiceClienFeignHystrix  implements AlipayServiceClien {

}
