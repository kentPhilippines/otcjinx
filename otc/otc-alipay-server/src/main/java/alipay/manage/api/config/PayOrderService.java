package alipay.manage.api.config;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;

import alipay.manage.api.feign.ConfigServiceClient;
import alipay.manage.bean.CorrelationData;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.CorrelationService;
import alipay.manage.service.OrderAppService;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import alipay.manage.util.AmountRunUtil;
import alipay.manage.util.AmountUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import otc.api.alipay.Common;
import otc.bean.alipay.FileList;
import otc.bean.alipay.Medium;
import otc.bean.config.ConfigFile;
import otc.bean.dealpay.Withdraw;
import otc.common.SystemConstants;
import otc.result.Result;
import otc.util.RSAUtils;
import otc.util.number.Number;

/**
 * <p>请求交易抽象【交易】【代付】</p>
 * @author kent
 */
public abstract class PayOrderService implements PayService{
	private static final Log log = LogFactory.get();
	private static final String ORDER = "orderid";
	@Autowired AmountUtil amountUtil;
	@Autowired AmountRunUtil amountRunUtil;
    @Autowired UserInfoService userInfoServiceImpl;
    @Autowired ConfigServiceClient configServiceClientImpl;
    @Autowired OrderService orderServiceImpl;
    @Autowired OrderAppService OrderAppServiceImpl;
	@Autowired CorrelationService correlationServiceImpl;
	@Override
	public Result deal(DealOrderApp dealOrderApp,String payType) {
		if(Common.Deal.PRODUCT_ALIPAY_SCAN.equals(payType))
			return dealAlipayScan(dealOrderApp);
		else if(Common.Deal.PRODUCT_ALIPAY_H5.equals(payType)) 
			return dealAlipayH5(dealOrderApp);
		return null;
	}
	public boolean orderEr(DealOrderApp orderApp){
		log.info("【将当前订单置为失败，当前交易订单号："+orderApp.getOrderId()+"】");
		DealOrder dealOrder = orderServiceImpl.findAssOrder(orderApp.getOrderId());
		if(ObjectUtil.isNotNull(dealOrder)) {
			boolean updateOrderStatus = orderServiceImpl.updateOrderStatus(dealOrder.getOrderId(), Common.Order.DealOrder.ORDER_STATUS_ER, "暂无支付渠道");
			if(updateOrderStatus) {
				OrderAppServiceImpl.updateOrderEr(orderApp.getOrderId(), "暂无支付渠道");
				return true;
			}
		}
		return false;
	}
	public String create(DealOrderApp orderApp,String channeId){
		log.info("【开始创建本地订单，当前创建订单的商户订单为："+orderApp.toString()+"】");
		log.info("【当前交易的渠道账号为："+channeId+"】");
		
		DealOrder order = new DealOrder();
		String orderAccount = orderApp.getOrderAccount();//交易商户号
		UserInfo accountInfo = userInfoServiceImpl.findUserInfoByUserId(orderAccount);//这里有为商户配置的 供应队列属性
		UserInfo userinfo = userInfoServiceImpl.findUserInfoByUserId(channeId);//查询渠道账户
		log.info("【当前交易的产品类型为："+userinfo.getUserNode()+"】");
		order.setAssociatedId(orderApp.getOrderId());
		order.setDealDescribe("正常交易订单");
		order.setActualAmount(orderApp.getOrderAmount());
		order.setDealAmount(orderApp.getOrderAmount());
		order.setDealFee(new BigDecimal("0"));
		order.setExternalOrderId(orderApp.getAppOrderId());
		order.setOrderAccount(orderApp.getOrderAccount());
		order.setNotify(orderApp.getNotify());
		String orderQrCh = Number.getOrderQrCh();
		order.setOrderId(orderQrCh);
		order.setOrderQrUser(userinfo.getUserId());
		order.setOrderStatus(Common.Order.DealOrder.ORDER_STATUS_DISPOSE.toString());
		order.setOrderType(Common.Order.ORDER_TYPE_DEAL.toString());
		order.setRetain1(userinfo.getUserNode());
		order.setBack(orderApp.getBack());
		boolean addOrder = orderServiceImpl.addOrder(order);
		if(addOrder) {
			ThreadUtil.execute(()->{
				corr(orderQrCh);
			});
		}
		return orderQrCh;
	};
	
	/**
	 * <p>数据数据统计</p>
	 */
	void corr(String orderId){
		ThreadUtil.execute(()->{
			DealOrder order = orderServiceImpl.findOrderByOrderId(orderId);
			CorrelationData corr = new CorrelationData();
			corr.setAmount(order.getDealAmount());
			corr.setOrderId(order.getOrderId());
			corr.setOrderStatus(Integer.valueOf(order.getOrderStatus()));
			corr.setUserId(order.getOrderQrUser());
			corr.setAppId(order.getOrderAccount());
			boolean addCorrelationDate = correlationServiceImpl.addCorrelationDate(corr);
			if(addCorrelationDate) 
				log.info("【订单号："+order.getOrderId()+"，添加数据统计成功】");
			else
				log.info("【订单号："+order.getOrderId()+"，添加数据统计失败】");
		});
	}
	/**
	 * <p>支付宝扫码支付实体</p>
	 */
	public Result dealAlipayScan(DealOrderApp dealOrderApp) {
		/**
		 * #############################
		 * 生成预订单病返回支付连接
		 */
		Map<String, Object> param = Maps.newHashMap();
		param.put(ORDER, dealOrderApp.getOrderId());
		String encryptPublicKey = RSAUtils.getEncryptPublicKey(param, SystemConstants.INNER_PLATFORM_PUBLIC_KEY);
		String URL = configServiceClientImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.SERVER_IP).getResult().toString();
		return Result.buildSuccessResult(URL+"/pay/alipayScan/"+encryptPublicKey);
	}
	/**
	 * <p>支付宝H5</p>
	 */
	public Result dealAlipayH5(DealOrderApp dealOrderApp) {
		/**
		 * ############################
		 * 生成预订单并返回支付连接
		 */
		return null;
	}
	
	/**
	 * <p>代付</p>
	 */
	@Override
	public Result withdraw(Withdraw wit) {
		/**
		 * #####################################
		 * 代付扣款操作
		 */
		UserFund userFund = userInfoServiceImpl.findUserFundByAccount(wit.getUserId());
		Result deleteWithdraw = amountUtil.deleteWithdraw(userFund, wit.getActualAmount());
		if(!deleteWithdraw.isSuccess())
			return Result.buildFailMessage("账户扣减失败,请联系技术人员处理");
		Result deleteAmount = amountRunUtil.deleteAmount(wit, wit.getRetain2(), true);
		if(!deleteAmount.isSuccess())
			return Result.buildFailMessage("账户扣减失败,请联系技术人员处理");
		Result deleteWithdraw2 = amountUtil.deleteWithdraw(userFund, wit.getFee());
		if(!deleteWithdraw2.isSuccess())
			return Result.buildFailMessage("账户扣减失败,请联系技术人员处理");
		Result deleteAmountFee = amountRunUtil.deleteAmountFee(wit, wit.getRetain2(), true);
		if(!deleteAmountFee.isSuccess())
			return  Result.buildFailMessage("账户扣减失败,请联系技术人员处理");
		return Result.buildSuccess();
	}
}
