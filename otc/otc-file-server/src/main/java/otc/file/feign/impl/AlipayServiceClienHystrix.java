package otc.file.feign.impl;

import org.springframework.stereotype.Component;

import otc.api.impl.AlipayServiceClienFeignHystrix;
import otc.file.feign.AlipayServiceClien;
@Component
public class AlipayServiceClienHystrix extends AlipayServiceClienFeignHystrix  implements AlipayServiceClien {

}
