package otc.otc.feign.impl;

import org.springframework.stereotype.Component;

import otc.api.impl.QueueServiceClienFeignHystrix;
import otc.otc.feign.QueueServiceClien;
@Component
public class QueueServiceClienHystrix extends QueueServiceClienFeignHystrix implements QueueServiceClien {

}
