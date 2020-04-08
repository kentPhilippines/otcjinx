package deal.manage.api;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import deal.config.feign.ConfigServiceClient;
import deal.config.redis.RedisUtil;
import deal.manage.bean.DealOrder;
import deal.manage.service.OrderService;
import deal.manage.service.RechargeService;
import deal.manage.service.WithdrawService;
import deal.manage.util.CardBankOrderUtil;
import deal.manage.util.LogUtil;
import otc.api.dealpay.Common;
import otc.bean.config.ConfigFile;
import otc.bean.dealpay.Recharge;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.common.SystemConstants;
import otc.result.DealBean;
import otc.result.Result;
import otc.util.RSAUtils;
@RestController
public class Api {
	private static final Log log = LogFactory.get();
	@Autowired CardBankOrderUtil cardBankOrderUtil;
	@Autowired RechargeService rechargeServiceImpl;
	@Autowired WithdrawService withdrawServiceImpl;
	@Autowired ConfigServiceClient configServiceClientImpl;
	@Autowired OrderService orderServiceImpl;
	@Autowired LogUtil logUtil;
	private static String Url;
	
	
	
	/**
	 * <p>后台操作卡商交易订单</p>
	 * @param param
	 * @param request
	 * @return
	 */
	@PostMapping(PayApiConstant.Alipay.ORDER_API+PayApiConstant.Alipay.ORDER_ENTER_ORDER+"/{param:.+}")
	public Result enterOrder(@PathVariable("param") String param, HttpServletRequest request) {
		log.info("【请求交易的终端用户交易请求参数为："+param+"】");
		Map<String, Object> stringObjectMap = RSAUtils.retMapDecode(param, SystemConstants.INNER_PLATFORM_PRIVATE_KEY);
		if(CollUtil.isEmpty(stringObjectMap)) {
			log.info("【参数解密为空】");
			return Result.buildFailMessage("参数为空");
		}
		Object obj = stringObjectMap.get("orderId");
		if(ObjectUtil.isNull(obj)) return Result.buildFailMessage("未识别当前订单号");
		Object sta = stringObjectMap.get("orderStatus");
		if(ObjectUtil.isNull(sta)) return Result.buildFailMessage("未识别当前订单状态");
		Object user = stringObjectMap.get("userName");
		if(ObjectUtil.isNull(user)) return Result.buildFailMessage("未识别当前操作人");
		String orderId = obj.toString();//订单号
		String orderstatus = sta.toString();//将要改变订单状态
		String userop = user.toString();//操作人
		log.info("【当前调用人工处理订单接口，当前订单号："+orderId+"，当前修改订单状态："+orderstatus+"，当前操作人："+userop+"】");
		DealOrder order  =  orderServiceImpl.findOrderByOrderId(orderId);
		String clientIP = HttpUtil.getClientIP(request);
		if(StrUtil.isBlank(clientIP))
			return Result.buildFailMessage("当前使用代理服务器 或是操作ip识别出错，不允许操作");
		if(ObjectUtil.isNull(order))
			return Result.buildFailMessage("当前订单不存在");
		if(order.getOrderStatus().equals(Common.Order.DealOrder.ORDER_STATUS_ER.toString()) || order.getOrderStatus().equals(Common.Order.DealOrder.ORDER_STATUS_SU.toString()))
			return Result.buildFailMessage("当前订单状态不允许操作");
		if(orderstatus.equals(Common.Order.DealOrder.ORDER_STATUS_ER.toString())) {
			ThreadUtil.execAsync(()->{
				logUtil.addLog(request, "后台人员置交易订单失败，操作人："+userop+"", userop);
			});
			Result updataOrderErOperation = cardBankOrderUtil.updataOrderErOperation(orderId, userop, clientIP);
			return updataOrderErOperation;
		} else if (orderstatus.equals(Common.Order.DealOrder.ORDER_STATUS_SU.toString())) {
			ThreadUtil.execAsync(()->{
			 logUtil.addLog(request, "后台人员置交易订单成功，操作人："+userop+"", userop);
			});
			Result updateOrderSu = cardBankOrderUtil.updateOrderSu(orderId, clientIP, userop);
			return updateOrderSu;
		}
		return Result.buildFailMessage("操作失败");
	}
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
