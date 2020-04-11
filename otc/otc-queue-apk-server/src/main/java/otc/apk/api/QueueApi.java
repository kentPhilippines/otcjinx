package otc.apk.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import otc.apk.redis.RedisUtil;
import otc.apk.util.Queue;
import otc.bean.alipay.FileList;
import otc.bean.alipay.Medium;
import otc.common.PayApiConstant;
import otc.common.RedisConstant;
import otc.result.Result;
import otc.util.RSAUtils;

@RestController
@RequestMapping(PayApiConstant.Queue.QUEUE_API)
public class QueueApi {
	private static final Log log = LogFactory.get();
	private static final String DATA_QUEUE_HASH = RedisConstant.Queue.MEDIUM_HASH;
	@Autowired Queue queueList;
	@Autowired RedisUtil	redisUtil;
	/**
	 * <p>获取队列</p>
	 * @param code				某个订单的队列标识
	 * @return
	 */
	@PostMapping(PayApiConstant.Queue.FIND_QR)
	public List findQr(@RequestBody String[] code) {
		log.info("【远程调用队列处理方法，参数为："+code+"】");
		Set<Object> list = queueList.getList(code);
		Object[] array = list.toArray();
		List<String> lists = new ArrayList<String>();
		for(Object obj  : array) {
			log.info("【队列值为："+obj+"】");
			lists.add(obj.toString());
		}
		return lists;
	}
	
	@PostMapping(PayApiConstant.Queue.UPDATA_QR)
	public void updata(@RequestBody String mediumNumber , @RequestBody FileList file) {
		log.info("【远程调用队列处理方法-更改队列顺序,当前队列标志："+file.getAttr()+"】");
		queueList.updataNode(mediumNumber, file, file.getAttr());
	}
	
	@PostMapping(PayApiConstant.Queue.ADD_QR)
	public Result addQueue(@RequestBody Medium medium) {
		log.info("【远程调用参数："+medium.toString()+"】");
		boolean addNode = queueList.addNode(medium.getMediumNumber(), medium.getAttr());
		if(addNode) {
			String md5 = RSAUtils.md5(DATA_QUEUE_HASH+medium.getMediumNumber() );
			redisUtil.hset(DATA_QUEUE_HASH, md5, medium, 259200);//本地收款媒介缓存数据会缓存三天			
			return Result.buildSuccess();
		}
		return Result.buildFailMessage("打开失败");
	} 
	
	@PostMapping(PayApiConstant.Queue.DELETE_QR)
	public Result deleteNode(@RequestBody Medium medium) {
		log.info("【远程调用参数："+medium.toString()+"】");
		log.info("【执行出列操作，出列对象："+medium.getMediumNumber()+"】");
		boolean deleteNode = queueList.deleteNode(medium.getMediumNumber(),medium.getAttr());
		return Result.buildSuccess();
	}
	
}
