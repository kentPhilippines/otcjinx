package otc.file.feign.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import otc.file.feign.ConfigServiceClient;
import otc.result.Result;
@Component
public class ConfigServiceClientHystrix implements ConfigServiceClient{
	Logger log = LoggerFactory.getLogger(ConfigServiceClientHystrix.class);
	@Override
	public Result getConfig(String system, String key) {
		 log.info("获取参数system ：： " + system);
		 log.info("获取参数key ：： " + key);
		 log.info("当前远程程序未调用，请检查服务运行状况");
		 log.info("当前采取默认配置情况");
		return Result.buildFail( );
	}

}
