package alipay.manage.api.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.discovery.converters.Auto;

import alipay.manage.bean.DealOrder;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import alipay.manage.service.WithdrawService;
import alipay.manage.util.CheckUtils;
import alipay.manage.util.NotifyUtil;
import alipay.manage.util.OrderUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import otc.bean.dealpay.Withdraw;
import otc.result.Result;

/**
 * <p>代付成功的抽象类</p>
 * @author kent
 */
public abstract class NotfiyChannel {
	private static final Log log = LogFactory.get();
	@Autowired OrderUtil orderUtilImpl;
	@Autowired WithdrawService withdrawServiceImpl;
	@Autowired CheckUtils checkUtils;
    @Autowired UserInfoService userInfoServiceImpl;
    @Autowired OrderService orderServiceImpl;
    @Autowired NotifyUtil notifyUtilImpl;
    static Lock lock = new  ReentrantLock();
	public Result witNotfy(String orderId) {
		log.info("【进入代付回调抽象类，当前代付成功订单号："+orderId+"】");
		Withdraw wit = withdrawServiceImpl.findOrderId(orderId);
		Result withrawOrderSu = orderUtilImpl.withrawOrderSu(wit);
		if(withrawOrderSu.isSuccess()) {
			ThreadUtil.execute(()->{
				wit(orderId);
			});
		}
		return withrawOrderSu;
	}
	
	public Result dealpayNotfiy(String orderId,String ip) {
		log.info("【进入支付成功回调处理类："+orderId+"】");
		DealOrder order = orderServiceImpl.findOrderByOrderId(orderId);
		if(ObjectUtil.isNull(order)) {
			log.info("【当前回调订单不存在，当前回调订单号："+orderId+"】");
			return Result.buildFailMessage("当前回调订单不存在");
		}
		lock.lock();
		try {
			Result dealAmount = orderUtilImpl.updataDealOrderSu(order.getOrderId(), "三方系统回调成功", ip, true );
			if(dealAmount.isSuccess()) {
				log.info("【订单修改成功，向下游发送回调："+orderId+"】");
				notifyUtilImpl.sendMsg(orderId);
				return Result.buildSuccessMessage("订单修改成功");
			}
		} finally {
			lock.unlock();
		}
		return Result.buildFail();
	}
	/**
	 * <p>API下游代付通知</p>
	 */
	  void  wit(String orderId){
		  log.info("【代付订单修改成功，现在开始通知下游，代付订单号："+orderId+"】");
		  Map<String, Object> map = new HashMap<String, Object>();
		  Withdraw wit = withdrawServiceImpl.findOrderId(orderId);
		  UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId( wit.getUserId());
	      map.put("apporderid", wit.getAppOrderId());
	      map.put("tradesno", wit.getOrderId());
	      map.put("status", wit.getOrderStatus());//0 预下单    1处理中  2 成功  3失败
	      map.put("amount", wit.getAmount());
	      map.put("appid", wit.getUserId());
	      String sign = checkUtils.getSign(map, userInfo.getPayPasword());
	      map.put("sign", sign);
	      send(wit.getNotify(), orderId, map);
	}
	  /**
	     * <p>发送通知</p>
	     * @param url   发送通知的地址
	     * @param orderId  发送订单ID
	     * @param msg  发送通知的内容
	     */
	    private void send(String url,String orderId,Map<String,Object> msg){
	        String result = HttpUtil.post(url, msg,-1);
	        log.info("服务器返回结果为: " + result.toString());
	        log.info("【下游商户返回信息为成功,成功收到回调信息】");
	        //更新订单是否通知成功状态
	    }

}
