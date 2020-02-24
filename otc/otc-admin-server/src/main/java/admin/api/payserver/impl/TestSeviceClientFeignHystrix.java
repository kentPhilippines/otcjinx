package admin.api.payserver.impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import admin.api.payserver.TestSeviceClientFeign;
import otc.result.Result;


@Service
public class TestSeviceClientFeignHystrix implements TestSeviceClientFeign{
	Logger log = LoggerFactory.getLogger(TestSeviceClientFeignHystrix.class);
	@Override
	public Result findTest() {
		log.info("当前未执行远程调用");
		return Result.buildSuccessMessage("当前没有进行远程调用，调用本地默认方法处理");
	}

}
