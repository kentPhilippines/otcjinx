package deal.config.feign.impl;

import org.springframework.stereotype.Component;

import deal.config.feign.QueueServiceClien;
import otc.api.impl.QueueServiceClienFeignHystrix;
@Component
public class QueueServiceClienHystrix extends QueueServiceClienFeignHystrix implements QueueServiceClien {

}
