package alipay.manage.api.Feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import alipay.manage.api.Feign.impl.QueueServiceClienFeignHystrix;
import otc.bean.alipay.FileList;
import otc.bean.alipay.Medium;
import otc.common.PayApiConstant;
import otc.result.Result;

@FeignClient(value=PayApiConstant.Server.QUEUE_APK_SERVER, fallback = QueueServiceClienFeignHystrix.class)
public interface QueueServiceClienFeign {
	@PostMapping(PayApiConstant.Queue.QUEUE_API+PayApiConstant.Queue.FIND_QR)
	public List<String> getQueue(@RequestParam("code")String[] code);

	@PostMapping(PayApiConstant.Queue.QUEUE_API+PayApiConstant.Queue.UPDATA_QR)
	public void updataNode(@RequestParam("mediumNumber")String mediumNumber, @RequestParam("file")FileList file);
	
	@PostMapping(PayApiConstant.Queue.QUEUE_API+PayApiConstant.Queue.ADD_QR)
	public Result addNode(@RequestParam("medium")Medium medium);
	
}
