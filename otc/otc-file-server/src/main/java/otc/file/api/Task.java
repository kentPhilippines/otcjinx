package otc.file.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import otc.common.PayApiConstant;
import otc.file.util.CutUtil;
@RestController
public class Task {
	private static final Log log = LogFactory.get();
	@Autowired CutUtil cutUtil;
	@PostMapping(PayApiConstant.File.FILE_API+PayApiConstant.File.FILE_TASK)
	public void task() {
		log.info("文件服务器开始执行");
		cutUtil.cut();
	}
	
}
