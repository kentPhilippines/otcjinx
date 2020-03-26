package alipay.manage.api.feign.impl;

import org.springframework.stereotype.Component;

import alipay.manage.api.feign.QueueServiceClien;
import otc.api.impl.QueueServiceClienFeignHystrix;
@Component
public class QueueServiceClienHystrix extends QueueServiceClienFeignHystrix implements QueueServiceClien {

}
