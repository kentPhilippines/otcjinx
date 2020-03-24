package otc.apk.api;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	Logger log = LoggerFactory.getLogger(QueueApi.class);
	@Autowired Queue queueList;
	/**
	 * <p>获取队列</p>
	 * @param code				某个订单的队列标识
	 * @return
	 */
	@PostMapping(PayApiConstant.Queue.FIND_QR)
	public Object[] findQr(String[] code) {
		log.info("【远程调用队列处理方法】");
		Set<Object> list = queueList.getList(code);
		Object[] array = list.toArray();
		for(Object obj  : array)
			log.info("【队列值为："+obj+"】");
		return array;
	}
}
