package alipay.manage.api.Feign.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import alipay.manage.api.Feign.QueueServiceClienFeign;
import otc.bean.alipay.FileList;
import otc.bean.alipay.Medium;
import otc.result.Result;
@Component
public class QueueServiceClienFeignHystrix implements QueueServiceClienFeign{
	Logger log = LoggerFactory.getLogger(QueueServiceClienFeignHystrix.class);
	@Override
	public List<String> getQueue(String[] code) {
		log.info("【远程获取列元素失败】");
		return new ArrayList<String>();
	}
	@Override
	public void updataNode(String mediumNumber, FileList file) {
	}
	@Override
	public Result addNode(Medium medium) {
		log.info("【远程调用添加队列元素失败】");
		return Result.buildFail();
	}
}
