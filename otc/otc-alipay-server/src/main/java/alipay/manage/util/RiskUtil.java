package alipay.manage.util;

import alipay.config.redis.RedisUtil;
import alipay.manage.api.feign.ConfigServiceClient;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.UserFund;
import alipay.manage.service.CorrelationService;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.api.alipay.Common;
import otc.bean.alipay.FileList;
import otc.bean.config.ConfigFile;
import otc.util.date.DateUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>选码风控工具类</p>
 *
 * @author kent
 * @date 2020-3-31 21:17:35
 */
@Component
public class RiskUtil {
	private static final Log log = LogFactory.get();
	@Resource
	RedisUtil redisUtil;
	@Autowired ConfigServiceClient configServiceClientImpl;
	@Autowired CorrelationService correlationServiceImpl;
	DateFormat formatter = new SimpleDateFormat(Common.Order.DATE_TYPE);
	/**
	 * <p> 更新缓存中的账户余额 </p>
	 * @param user			资金账户
	 * @param flag 			是否发起顶代扣款冻结
	 * @throws ParseException 时间转换异常
	 */
	void updataUserAmountRedis(UserFund user, boolean flag) throws ParseException {
		if(flag) {
			log.info("【顶代账户余额冻结模式】");
			String findAgent = correlationServiceImpl.findAgent(user.getUserId());
			log.info("【当前顶代账号为】");
			Map<Object, Object> hmget = redisUtil.hmget(findAgent);
			Set<Object> keySet = hmget.keySet();
			for (Object obj : keySet) {
				String accountId = user.getUserId();
				int length = accountId.length();
				String subSuf = StrUtil.subSuf(obj.toString(), length);// 时间戳
				Date parse = formatter.parse(subSuf);
				Object object = hmget.get(obj.toString());// 当前金额
				if (!DateUtil.isExpired(parse, DateField.SECOND,
						Integer.valueOf(configServiceClientImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.FREEZE_PLAIN_VIRTUAL).getResult().toString()), new Date())) {
					redisUtil.hdel(user.getUserId(), obj.toString());
				}
			}
			return;
		}
		Map<Object, Object> hmget = redisUtil.hmget(user.getUserId());
		Set<Object> keySet = hmget.keySet();
		for (Object obj : keySet) {
			String accountId = user.getUserId();
			int length = accountId.length();
			String subSuf = StrUtil.subSuf(obj.toString(), length);// 时间戳
			Date parse = formatter.parse(subSuf);
			Object object = hmget.get(obj.toString());// 当前金额
			if (!DateUtil.isExpired(parse, DateField.SECOND,
					Integer.valueOf(configServiceClientImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.FREEZE_PLAIN_VIRTUAL).getResult().toString()), new Date())) {
				redisUtil.hdel(user.getUserId(), obj.toString());
			}
		}
	}

	
	/**
	 *	<p>验证当前用户账户余额是否可以接单</p>
	 * @param user					二维码文件信息
	 * @param amount2				交易金额
	 * @param usercollect			用户集合
	 * @param flag					true 顶代结算模式   false  非顶代结算模式
	 * @return
	 */
	boolean isClickAmount(FileList file, BigDecimal amount2, ConcurrentHashMap<String, UserFund> usercollect,boolean flag) {
		if(flag) {
			//获取顶代账号 
			//发起账户冻结比对
			log.info("【顶代账户余额冻结模式】");
			String findAgent = correlationServiceImpl.findAgent(file.getFileholder());
			log.info("【当前顶代账号为】");
			Map<Object, Object> hmget = redisUtil.hmget(findAgent);
			Set<Object> keySet = hmget.keySet();
			BigDecimal amount = amount2;
			for (Object obj : keySet) {
				Object object = hmget.get(obj);
				if (ObjectUtil.isNull(object)) {
					object = "0";
				}
				BigDecimal bigDecimal = new BigDecimal(object.toString());
				amount = amount.add(bigDecimal);
			}
			UserFund user2 = usercollect.get(file.getFileholder());
			return amount.compareTo(user2.getAccountBalance()) == -1;
		}
		Map<Object, Object> hmget = redisUtil.hmget(file.getFileholder());
		Set<Object> keySet = hmget.keySet();
		BigDecimal amount = amount2;
		for (Object obj : keySet) {
			Object object = hmget.get(obj);
			if (ObjectUtil.isNull(object)) {
				object = "0";
			}
			BigDecimal bigDecimal = new BigDecimal(object.toString());
			amount = amount.add(bigDecimal);
		}
		UserFund user2 = usercollect.get(file.getFileholder());
		return amount.compareTo(user2.getAccountBalance()) == -1;
	}
	
	
	/**
	 * <p>订单成功更新风控规则</p>
	 * @param order
	 */
	public void orderSu(DealOrder order) {
		updataRedisOrDate(order);
	}

	/**
	 * <p>清除缓存值</p>
	 * @param qrcodeDealOrder 
	 * @return
	 */
	boolean updataRedisOrDate(DealOrder qrcodeDealOrder) {
	//	clearAmount(qrcodeDealOrder);
	//	updataQr(qrcodeDealOrder);
		updateCorrelation(qrcodeDealOrder);
	//	try {
	//		deleteRedisAmount(qrcodeDealOrder);
	//	} catch (ParseException e) {
	//		log.info("解锁订单当前码商订单金额发生异常，当前码商改订单金额解锁失败，解锁时间误差时间为20秒");
	//	}
		return true;
	};
	/**
	 * <p>更新数据统计</p>
	 */
	private void updateCorrelation(DealOrder qrcodeDealOrder) {
		log.info("更新数据统计服务");
		correlationServiceImpl.updateCorrelationDate(qrcodeDealOrder.getOrderId());
	}
	/**
	 * <p>当前订单成功</p>
	 * @param qrcodeDealOrder
	 */
	private void clearAmount(DealOrder qrcodeDealOrder) {
		redisUtil.del(qrcodeDealOrder.getOrderQr()+qrcodeDealOrder.getDealAmount());
	}
	private void updataQr(DealOrder qrcodeDealOrder){
		Map<Object, Object> hmget = redisUtil.hmget(qrcodeDealOrder.getOrderQr());
		if(hmget.size() > 0) {//两次锁定二维码   成功解锁
			Set<Object> keySet = hmget.keySet();
			for (Object obj : keySet) {
				redisUtil.hdel(qrcodeDealOrder.getOrderQr(), obj.toString());//二维码三次未收到回调锁定一小时
			}
		}
	}
	private void deleteRedisAmount(DealOrder qrcodeDealOrder) throws ParseException{
		Map<Object, Object> hmget2 = redisUtil.hmget(qrcodeDealOrder.getOrderQrUser());
		if (hmget2.size() <= 0) //成功订单提前解锁金额
		{
			return;
		}
			Set<Object> keySet = hmget2.keySet();
			for(Object obj : keySet) {
				String accountId = qrcodeDealOrder.getOrderQrUser();
    			int length = accountId.length();
				String subSuf = StrUtil.subSuf(obj.toString(), length);//时间戳
    			Date parse = formatter.parse(subSuf);
				if(qrcodeDealOrder.getDealAmount().compareTo(new BigDecimal(hmget2.get(obj.toString()).toString())) == 0  && DateUtils.isTimeScope(20, qrcodeDealOrder.getCreateTime(), parse)) {
					redisUtil.hdel(qrcodeDealOrder.getOrderQrUser(), obj.toString());
					break;
				}
			}
	}
	private  void addDealSu(DealOrder qrcodeDealOrder) {
		Map<Object, Object> hmget = redisUtil.hmget(qrcodeDealOrder.getOrderQr());
		if (hmget.size() > 0) {
			redisUtil.hset(qrcodeDealOrder.getOrderQr() + qrcodeDealOrder.getOrderQrUser(), qrcodeDealOrder.getOrderQr() + qrcodeDealOrder.getOrderId(), qrcodeDealOrder.getOrderId());
		} else {
			redisUtil.hset(qrcodeDealOrder.getOrderQr() + qrcodeDealOrder.getOrderQrUser(), qrcodeDealOrder.getOrderQr() + qrcodeDealOrder.getOrderId(), qrcodeDealOrder.getOrderId(), 1800);
		}
	}
}
