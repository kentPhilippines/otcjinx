package otc.api.impl;

import org.springframework.stereotype.Component;

import otc.api.ConfigServiceClientFeign;
import otc.result.Result;
@Component
public class ConfigServiceClientFeignHystrix implements ConfigServiceClientFeign {
	@Override
	public Result getConfig(String system, String key) {
		return null;
	}

}
