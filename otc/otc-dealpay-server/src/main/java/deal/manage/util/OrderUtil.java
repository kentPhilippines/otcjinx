package deal.manage.util;

import org.springframework.stereotype.Component;

import otc.result.Result;

@Component
public class OrderUtil {

	
	/**
	 * <p>卡商代付金额金额扣减及流水生成</p>
	 * @param orderId		订单号
	 * @return
	 */
	public Result cardsubRunning(String orderId) {
		return Result.buildFail();
	}

}
