package alipay.config.task;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class TaskApi {
	private static final Log log = LogFactory.get();
	@Autowired
	UserTask userTaskImpl;
	@Autowired
	OrderTask orderTask;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@GetMapping(PayApiConstant.Alipay.TASK_API + PayApiConstant.Alipay.TASK_API_USER)
	public Result userTask() {
		log.info("【开始进行每日账户清算】");
		userTaskImpl.userAddTask();
		userTaskImpl.userTask();
		return Result.buildSuccessMessage("账户清算完成");
	}


	@GetMapping(PayApiConstant.Alipay.TASK_API + PayApiConstant.Alipay.TASK_API_ORDER)
	public Result orderTask()  {
		return Result.buildSuccessMessage("订单清算完成");
	}
}
