package pay.api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import otc.common.PayApiConstant;
import otc.result.Result;
import pay.entity.TestBean;
import pay.service.TestService;

@RestController
@RequestMapping(PayApiConstant.Test.TEST)
public class TestApi {
	Logger log = LoggerFactory.getLogger(TestApi.class);
	@Autowired
	TestService testServiceImpl;
	@GetMapping(PayApiConstant.Test.FIND_TEST)
	public Result findTest() {
		log.info("远程调用方法执行");
		TestBean test = testServiceImpl.findTest();
		return Result.buildFailResult(test);
	}
	
}
