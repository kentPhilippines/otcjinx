package alipay.manage.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alipay.config.redis.RedisUtil;
import alipay.manage.api.feign.QueueServiceClien;
import alipay.manage.service.MediumService;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import otc.bean.alipay.Medium;
import otc.common.RedisConstant;
import otc.result.Result;
/**<p>远程队列操作</p>
 * @author hx070
 *
 */
@Component
public class QueueUtil {
	private static final Log log = LogFactory.get();
	@Autowired QueueServiceClien queueServiceClienImpl;
	@Autowired RedisUtil redisUtil;
	@Autowired MediumService mediumServiceImpl;
	private static final String MEDIUM_QUEUE = RedisConstant.Queue.MEDIUM_QUEUE;
	static Lock lock = new  ReentrantLock();
	/**
	 * <p>收款媒介入列操作</p>
	 * @param medium	必传参数<strong>收款媒介数据id</strong>
	 * @return
	 */
	public Result addNode(Medium medium) {
		log.info("【进入支付媒介入列操作】");
		Future<Result> execAsync = ThreadUtil.execAsync(()->{
			Result addNode  = Result.buildFail();
			try {
				lock.lock();
				boolean flag = mediumServiceImpl.updataMediumStatusSu(medium.getId().toString());// open Medium
				if(!flag)
					return Result.buildFail();
				Medium mediumId = mediumServiceImpl.findMediumId(medium.getId().toString());
				String mediumNo = mediumId.getMediumId();
				log.info("【当前操作的媒介id为："+mediumNo+"】");
				if(redisUtil.hasKey(MEDIUM_QUEUE+mediumNo)) 
					return Result.buildFailMessage("本地标记已存在");
				redisUtil.set(MEDIUM_QUEUE+mediumNo, mediumNo);
				addNode = queueServiceClienImpl.addNode(mediumId);	// addNode - queue
			 }finally {
				 lock.unlock();
			}
			return addNode;
		});
		try {
			return execAsync.get(20,TimeUnit.MILLISECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			return Result.buildFail();
		}
	}
	/**
	 * <p>收款媒介出列操作</p>
	 * @param medium			必传参数<strong>收款媒介数据id</strong>
	 * @return
	 */
	public Result pop(Medium medium) {
		log.info("【进入支付媒介出列操作】");
		Future<Result> execAsync = ThreadUtil.execAsync(()->{
			Result deleteNode  = Result.buildFail();
		try {
		lock.lock();
			boolean flag = mediumServiceImpl.updataMediumStatusEr(medium.getId().toString());   // off medium
			if(!flag)
				return Result.buildFail();
			Medium mediumId = mediumServiceImpl.findMediumId(medium.getId().toString());
			String mediumNo = mediumId.getMediumId();
			log.info("【当前操作的媒介id为："+mediumNo+"】");
			redisUtil.del(MEDIUM_QUEUE+mediumNo);
			deleteNode = queueServiceClienImpl.deleteNode(mediumId);   // delete medium  -  queue
		 }finally {
			 lock.unlock();
		}
		return deleteNode;
		});
		try {
			return execAsync.get(20,TimeUnit.MILLISECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			return Result.buildFail();
		}
	}
}
