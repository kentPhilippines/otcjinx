package otc.apk.feign.impl;

import org.springframework.stereotype.Component;

import otc.api.impl.ConfigServiceClientFeignHystrix;
import otc.apk.feign.ConfigServiceClient;
@Component
public class ConfigServiceClientHystrix extends ConfigServiceClientFeignHystrix implements ConfigServiceClient{

}
