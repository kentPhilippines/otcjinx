package alipay.manage.api.config;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.discovery.converters.Auto;

import alipay.manage.bean.ChannelFee;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserInfo;
import alipay.manage.mapper.ChannelFeeMapper;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import alipay.manage.service.WithdrawService;
import alipay.manage.util.AmountRunUtil;
import alipay.manage.util.AmountUtil;
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
	public static final Log log = LogFactory.get();
	@Autowired OrderUtil orderUtilImpl;
	@Autowired WithdrawService withdrawServiceImpl;
	@Autowired CheckUtils checkUtils;
    @Autowired UserInfoService userInfoServiceImpl;
    @Autowired OrderService orderServiceImpl;
    @Autowired NotifyUtil notifyUtilImpl;
    @Autowired ChannelFeeMapper channelFeeDao;
    @Autowired AmountUtil amountUtil;
    @Autowired AmountRunUtil amountRunUtil;
    static Lock lock = new  ReentrantLock();
    public Result witNotfy(String orderId, String ip) {
        log.info("【进入代付回调抽象类，当前代付成功订单号："+orderId+"】");
        Withdraw wit = withdrawServiceImpl.findOrderId(orderId);
        wit.setComment("代付成功");
        Result withrawOrderSu = orderUtilImpl.withrawOrderSu(wit);
        if(withrawOrderSu.isSuccess()) {
          ThreadUtil.execute(()->{
            wit(orderId);
          });
        }
        UserFund userFund = new UserFund();
        userFund.setUserId(wit.getWitChannel());
        ThreadUtil.execute(()->{
          log.info("【当前代付订单成功，代付订单号为："+orderId+"，对代付渠道进行加款操作】");
        Result addAmountAdd = amountUtil.addAmountAdd(userFund, wit.getAmount());
        if(addAmountAdd.isSuccess())
          log.info("【当前代付渠道账户加款成功，代付订单号为："+orderId+"，生成渠道加款流水】");
        else
          log.info("【当前代付渠道账户加款【失败】，代付订单号为："+orderId+"，加款渠道为："+wit.getOrderId()+"】");
        Result addChannelWit = amountRunUtil.addChannelWit(wit, ip );
        if(addChannelWit.isSuccess())
          log.info("【当前代付渠道账户加款流水成功，代付订单号为："+orderId+"，生成渠道加款流水】");
        else
          log.info("【当前代付渠道账户加款流水【失败】，代付订单号为："+orderId+"，加款渠道为："+wit.getOrderId()+"】");
        ChannelFee findChannelFee = channelFeeDao.findChannelFee(wit.getWitChannel(),wit.getWitType());
        String channelDFee = findChannelFee.getChannelDFee();
        log.info("【当前渠道代付手续费为："+channelDFee+" 】");
        Result add = amountUtil.addAmountAdd(userFund, new BigDecimal(channelDFee));
        log.info("【渠道账户记录代付手续费为："+channelDFee+" 】");
        if(add.isSuccess())
          log.info("【当前代付渠道账户取款手续费加款成功，代付订单号为："+orderId+"，生成渠道加款手续费流水】");
        else
          log.info("【当前代付渠道账户取款手续费加款【失败】，代付订单号为："+orderId+"，加款渠道为："+wit.getOrderId()+"】");
        Result addChannelWitFee = amountRunUtil.addChannelWitFee(wit, ip, new BigDecimal(channelDFee) );
        if(addChannelWitFee.isSuccess())
          log.info("【当前代付渠道账户取款手续费加款流水成功，代付订单号为："+orderId+"，生成渠道加款流水】");
        else
          log.info("【当前代付渠道账户取款手续费加款流水【失败】，代付订单号为："+orderId+"，加款渠道为："+wit.getOrderId()+"】");
        });
        return withrawOrderSu;
      }
    public Result witNotfyEr(String orderId, String ip,String msg) {
        log.info("【进入代付失败回调 抽象类："+orderId+"】");
        Withdraw wit = withdrawServiceImpl.findOrderId(orderId);
        Result result = orderUtilImpl.withrawOrderErBySystem(orderId, ip,msg);
        if(result.isSuccess()) {
          ThreadUtil.execute(()->{
            wit(orderId);
          });
        }
        return Result.buildFail();
      }
	public Result dealpayNotfiy(String orderId,String ip,String msg) {
		log.info("【进入支付成功回调处理类："+orderId+"】");
		DealOrder order = orderServiceImpl.findOrderByOrderId(orderId);
		if(ObjectUtil.isNull(order)) {
			log.info("【当前回调订单不存在，当前回调订单号："+orderId+"】");
			return Result.buildFailMessage("当前回调订单不存在");
		}
		lock.lock();
		try {
			Result dealAmount = orderUtilImpl.updataDealOrderSu(order.getOrderId(), msg, ip, false );
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
	public Result dealpayNotfiy(String orderId,String ip) {
	 return dealpayNotfiy(orderId,ip,"三方系统回调成功");
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
