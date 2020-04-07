package deal.manage.api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import deal.config.feign.ConfigServiceClient;
import deal.config.redis.RedisUtil;
import deal.manage.service.RechargeService;
import deal.manage.service.WithdrawService;
import deal.manage.util.CardBankOrderUtil;
import otc.api.dealpay.Common;
import otc.bean.config.ConfigFile;
import otc.bean.dealpay.Recharge;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.DealBean;
import otc.result.Result;
@RestController
public class Api {
	private static final Log log = LogFactory.get();
	@Autowired CardBankOrderUtil cardBankOrderUtil;
	@Autowired RechargeService rechargeServiceImpl;
	@Autowired WithdrawService withdrawServiceImpl;
	@Autowired ConfigServiceClient configServiceClientImpl;
	private static String Url;
	
	
	/**
	 * <p>接受充值</p>
	 * @param recharge
	 * @return
	 */
	@PostMapping(PayApiConstant.Dealpay.DEAL_API+PayApiConstant.Dealpay.RECHARGE_URL)
	public Result recharge(@RequestBody Recharge recharge) {
		log.info("【接收到下游调用充值渠道】");		
		if(ObjectUtil.isNull(recharge)) {
			log.info("【当前调用参数为空】");		
			return Result.buildFailMessage("充值失败，接口调用参数为空");
		}
		log.info("【接收到下游充值参数："+recharge.toString()+"】");
		Recharge order = new Recharge();
		order.setOrderId(recharge.getOrderId());
		order.setActualAmount(recharge.getActualAmount());
		order.setAmount(recharge.getAmount());
		order.setNotfiy(recharge.getNotfiy());
		order.setChargeBankcard(recharge.getChargeBankcard());
		order.setPhone(recharge.getPhone());
		order.setUserId(recharge.getUserId());
		order.setRechargeType(Common.Order.Recharge.WIT_ACC);
		order.setWeight(recharge.getWeight());
		order.setDepositor(recharge.getDepositor());
		order.setOrderStatus(recharge.getOrderStatus());
		boolean a = rechargeServiceImpl.addOrder(order);
		if(a) {
			Result createBankOrderR = cardBankOrderUtil.createBankOrderR(recharge.getOrderId());
			if(createBankOrderR.isSuccess()) 
				return Result.buildSuccessResult(DealBean.DealBeanSu("获取充值渠道成功", configServiceClientImpl.getConfig(ConfigFile.DEAL, ConfigFile.Deal.RECHARGE_URL).getResult().toString(), createBankOrderR.getResult()));
	
			ThreadUtil.execute(()->{
				boolean b = rechargeServiceImpl.updateStatusEr(order.getOrderId(),createBankOrderR.getMessage());
				log.info("【充值预订单失败的时候，修改订单成功】");		
			});
		
		
		}
		return Result.buildFailMessage("暂无充值渠道");
	}
	/**
	 * <p>接受代付</p>
	 * @param wit
	 * @return
	 */
	@PostMapping(PayApiConstant.Dealpay.DEAL_API+PayApiConstant.Dealpay.WITH_PAY)
	public Result wit(@RequestBody Withdraw  wit) {
		log.info("【接收到下游调用代付渠道】");		
		if(ObjectUtil.isNull(wit)) {
			log.info("【当前调用参数为空】");		
			return Result.buildFailMessage("充值失败，接口调用参数为空");
		}
		log.info("【接收到下游代付参数："+wit.toString()+"】");
		deal.manage.bean.Withdraw with = new deal.manage.bean.Withdraw();
		with.setOrderId(wit.getOrderId());
		with.setActualAmount(wit.getActualAmount());
		with.setAmount(wit.getAmount());
		with.setBankName(wit.getBankName());
		with.setBankNo(wit.getBankNo());
		with.setMobile(wit.getMobile());
		with.setNotify(wit.getNotify());
		with.setOrderStatus(wit.getOrderStatus());
		with.setUserId(wit.getUserId());
		with.setWithdrawType(Integer.valueOf(wit.getWithdrawType()));
		with.setWeight(wit.getWeight());
		with.setAppOrderId(wit.getAppOrderId());
		boolean order = withdrawServiceImpl.addOrder(with);
		if(order) {
			Result orderW = cardBankOrderUtil.createBankOrderW(with.getOrderId());
			if(orderW.isSuccess())
				return orderW;
			
			ThreadUtil.execute(()->{
				boolean a = withdrawServiceImpl.updateStatusEr(wit.getOrderId(),orderW.getMessage());
				log.info("【代付预订单失败的时候，修改订单成功】");		
			});
		}
		return Result.buildFailMessage("代付失败"); 
	}
}
