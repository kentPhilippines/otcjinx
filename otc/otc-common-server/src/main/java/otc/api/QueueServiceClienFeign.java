package otc.api;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import otc.api.impl.QueueServiceClienFeignHystrix;
import otc.bean.alipay.FileList;
import otc.bean.alipay.Medium;
import otc.common.PayApiConstant;
import otc.result.Result;
/**
 * <p>全局队列服务</p>
 * @author K
 */
@FeignClient(value=PayApiConstant.Server.QUEUE_APK_SERVER, fallback = QueueServiceClienFeignHystrix.class)
public interface QueueServiceClienFeign {
	/**
	 * <p>获取队列数据</p>
	 * @param code						队列类别
	 * @return							队列数据
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,value = PayApiConstant.Queue.QUEUE_API+PayApiConstant.Queue.FIND_QR)
	public List<String> getQueue( String[] code);

	/**
	 * <p>往队列里面添加节点</p>
	 * @param mediumNumber					要添加的数据
	 * @param file							添加的数据要求和规则
	 */
	@PostMapping(PayApiConstant.Queue.QUEUE_API+PayApiConstant.Queue.UPDATA_QR)
	public void updataNode(@RequestParam("mediumNumber")String mediumNumber, @RequestParam("file")FileList file);
	
	/**
	 * <p>改变队列规则</p>
	 * @param medium
	 * @return
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, value =PayApiConstant.Queue.QUEUE_API+PayApiConstant.Queue.ADD_QR )
	public Result addNode(Medium medium);
	
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,value = PayApiConstant.Queue.QUEUE_API+PayApiConstant.Queue.DELETE_QR)
	public Result deleteNode(Medium medium);
	
	
	/**
	 * <p>定时任务</p>
	 * @return
	 */
	@PostMapping(PayApiConstant.Queue.QUEUE_API+PayApiConstant.Queue.TASK_MEDIUM)
	public Result task();
	
	
}
