package otc.api.pay;

import org.springframework.web.bind.annotation.GetMapping;
import otc.common.PayApiConstant;
import otc.result.Result;

public interface TestSeviceClientFeignCommon {
	@GetMapping(PayApiConstant.Test.TEST+PayApiConstant.Test.FIND_TEST)
	public Result findTest();
}
