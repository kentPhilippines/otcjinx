package alipay.manage.api.feign.impl;

import org.springframework.stereotype.Component;

import alipay.manage.api.feign.DealpayServiceClien;
import otc.api.impl.DealpayServiceClienFeignHystrix;
@Component
public class DealpayServiceClienHystrix extends DealpayServiceClienFeignHystrix implements DealpayServiceClien{

}
