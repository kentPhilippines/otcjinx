package alipay.manage.api.feign.impl;

import org.springframework.stereotype.Component;

import alipay.manage.api.feign.ConfigServiceClient;
import otc.api.impl.ConfigServiceClientFeignHystrix;
@Component
public class ConfigServiceClientHystrix extends ConfigServiceClientFeignHystrix implements ConfigServiceClient{

}
