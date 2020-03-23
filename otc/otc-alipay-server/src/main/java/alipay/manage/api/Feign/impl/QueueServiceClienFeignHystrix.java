package alipay.manage.api.Feign.impl;

import org.springframework.stereotype.Component;

import alipay.manage.api.Feign.QueueServiceClienFeign;
import otc.result.Result;
@Component
public class QueueServiceClienFeignHystrix implements QueueServiceClienFeign{
	@Override
	public Result getQueue(String[] code) {
		return Result.buildFail();
	}
}
