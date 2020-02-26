package admin.api.payserver;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import admin.api.payserver.impl.TestSeviceClientFeignHystrix;
import otc.api.pay.TestSeviceClientFeignCommon;
import otc.common.PayApiConstant;
import otc.result.Result;
/**
 * <p>远程调用服务接口</p>
 * @author K
 */
@FeignClient(value=PayApiConstant.Server.PAY_SERVER, fallback = TestSeviceClientFeignHystrix.class)
public interface TestSeviceClientFeign extends TestSeviceClientFeignCommon{
}
