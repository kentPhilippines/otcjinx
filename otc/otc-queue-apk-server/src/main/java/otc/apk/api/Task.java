package otc.apk.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import otc.apk.util.HeartUtil;
import otc.common.PayApiConstant;

@RestController
public class Task {
    private static final Log log = LogFactory.get();
	@Autowired HeartUtil heartUtil;
	@RequestMapping(PayApiConstant.Queue.QUEUE_API+PayApiConstant.Queue.TASK_MEDIUM)
	public void task() {
		log.info("【执行更新队列操作】");
		heartUtil.clickHeart();
	}
	

}
