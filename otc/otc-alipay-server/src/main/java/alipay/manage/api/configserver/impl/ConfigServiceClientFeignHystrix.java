package alipay.manage.api.configserver.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import alipay.manage.api.configserver.ConfigServiceClientFeign;
import otc.result.Result;
@Component
public class ConfigServiceClientFeignHystrix implements ConfigServiceClientFeign{
	Logger log = LoggerFactory.getLogger(ConfigServiceClientFeignHystrix.class);
	@Override
	public Result getConfig(String system, String key) {
		 log.info("获取参数system ：： " + system);
		 log.info("获取参数key ：： " + key);
		 log.info("当前远程程序未调用，请检查服务运行状况");
		 log.info("当前采取默认配置情况");
		return Result.buildFail( );
	}

}
