package otc.apk.api;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.util.StrUtil;
import otc.common.PayApiConstant;
import otc.result.Result;

@RestController
@RequestMapping(PayApiConstant.Queue.QUEUE_API)
public class QueueApi {
	@Autowired Queue queueList;
	/**
	 * <p>获取队列</p>
	 * @param code				某个订单的队列标识
	 * @return
	 */
	@PostMapping(PayApiConstant.Queue.FIND_QR)
	public Result findQr(String[] code) {
		return Result.buildSuccessResult(queueList.getList(code));
	}
}
