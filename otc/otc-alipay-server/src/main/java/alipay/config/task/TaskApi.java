package alipay.config.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import otc.common.PayApiConstant;
import otc.result.Result;

@RestController
public class TaskApi {
	private static final Log log = LogFactory.get();
	@Autowired UserTask userTaskImpl;
	
	@GetMapping(PayApiConstant.Alipay.TASK_API+PayApiConstant.Alipay.TASK_API_USER)
	public Result userTask() {
		log.info("【开始进行每日账户清算】");
		userTaskImpl.userAddTask();
		userTaskImpl.userTask();
		return Result.buildSuccessMessage("账户清算完成");
	}

}
