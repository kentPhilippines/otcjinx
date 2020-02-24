package admin.contorller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import admin.api.payserver.TestSeviceClientFeign;
import otc.result.Result;

@RequestMapping("/test")
@RestController
public class TestContorller {
	Logger log = LoggerFactory.getLogger(TestContorller.class);
	@Autowired
	TestSeviceClientFeign testSeviceClientFeignImpl;
	@GetMapping("/findTest")
	public Result findResult() {
		log.info("收到测试请求");
		return testSeviceClientFeignImpl.findTest();
	}

}
