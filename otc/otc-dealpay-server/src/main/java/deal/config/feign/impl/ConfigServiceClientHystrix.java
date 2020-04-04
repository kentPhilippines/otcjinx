package deal.config.feign.impl;

import org.springframework.stereotype.Component;

import deal.config.feign.ConfigServiceClient;
import otc.api.impl.ConfigServiceClientFeignHystrix;
@Component
public class ConfigServiceClientHystrix extends ConfigServiceClientFeignHystrix implements ConfigServiceClient{

}
