package alipay.manage.api.config;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.bean.*;
import alipay.manage.mapper.ChannelFeeMapper;
import alipay.manage.mapper.WithdrawMapper;
import alipay.manage.service.*;
import alipay.manage.util.NotifyUtil;
import alipay.manage.util.OrderUtil;
import alipay.manage.util.amount.AmountPublic;
import alipay.manage.util.amount.AmountRunUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;
import otc.api.alipay.Common;
import otc.bean.dealpay.Withdraw;
import otc.common.SystemConstants;
import otc.result.Result;
import otc.util.RSAUtils;
import otc.util.number.GenerateOrderNo;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>请求交易抽象【交易】【代付】</p>
 *
 * @author kent
 */
public abstract class PayOrderService implements PayService {
	public static final Log log = LogFactory.get();
	private static final String ORDER = "orderid";
	@Autowired
	private AmountPublic amountPublic;
	@Autowired
	private AmountRunUtil amountRunUtil;
	@Autowired
	private UserInfoService userInfoServiceImpl;
	@Autowired
	NotifyUtil notifyUtil;
	@Autowired
	private OrderService orderServiceImpl;
	@Autowired
	private OrderAppService OrderAppServiceImpl;
	@Autowired
	private CorrelationService correlationServiceImpl;
	@Autowired
	private UserRateService userRateServiceImpl;
	@Resource
	private ChannelFeeMapper channelFeeDao;
    @Autowired
    private OrderUtil orderUtilImpl;
  @Autowired
    private WithdrawService withdrawServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
		if (Common.Deal.PRODUCT_ALIPAY_SCAN.equals(channel)) {
			return dealAlipayScan(dealOrderApp);
		} else if (Common.Deal.PRODUCT_ALIPAY_H5.equals(channel)) {
			return dealAlipayH5(dealOrderApp);
		}
		return null;
	}

	public boolean orderEr(DealOrderApp orderApp, String msg) {
		log.info("【将当前订单置为失败，当前交易订单号：" + orderApp.getOrderId() + "】");
		DealOrder dealOrder = orderServiceImpl.findAssOrder(orderApp.getOrderId());
		if (ObjectUtil.isNotNull(dealOrder)) {
			boolean updateOrderStatus = orderServiceImpl.updateOrderStatus(dealOrder.getOrderId(), Common.Order.DealOrder.ORDER_STATUS_ER, msg);
			if (updateOrderStatus) {
				OrderAppServiceImpl.updateOrderEr(orderApp.getOrderId(), msg);
				return true;
			}
		}
		return false;
	}

	public boolean orderAppEr(DealOrderApp orderApp, String msg) {
		log.info("【将当前订单置为失败，当前交易订单号：" + orderApp.getOrderId() + "】");
		OrderAppServiceImpl.updateOrderEr(orderApp.getOrderId(), msg);
		return true;
	}

	public boolean orderEr(DealOrderApp orderApp) {
		return orderEr(orderApp, "暂无支付渠道");
	}

	public String create(DealOrderApp orderApp, String channeId) {
		log.info("【开始创建本地订单，当前创建订单的商户订单为：" + orderApp.toString() + "】");
		log.info("【当前交易的渠道账号为：" + channeId + "】");
		DealOrder order = new DealOrder();
		UserInfo userinfo = userInfoServiceImpl.findDealUrl(channeId);//查询渠道账户
		UserRate rate = userRateServiceImpl.findRateFeeType(orderApp.getFeeId());//长久缓存
		ChannelFee channelFee = channelFeeDao.findChannelFee(rate.getChannelId(), rate.getPayTypr());
		log.info("【当前交易的产品类型为：" + userinfo.getUserNode() + "】");
		order.setAssociatedId(orderApp.getOrderId());
		order.setDealDescribe("正常交易订单");
		order.setActualAmount(orderApp.getOrderAmount().subtract(new BigDecimal(orderApp.getRetain3())));
		order.setDealAmount(orderApp.getOrderAmount());
		order.setDealFee(new BigDecimal(orderApp.getRetain3()));
		order.setExternalOrderId(orderApp.getAppOrderId());
		order.setOrderAccount(orderApp.getOrderAccount());
		order.setNotify(orderApp.getNotify());
		String orderQrCh = GenerateOrderNo.Generate("J");
        order.setOrderId(orderQrCh);
        order.setOrderQrUser(userinfo.getUserId());
        order.setOrderStatus(Common.Order.DealOrder.ORDER_STATUS_DISPOSE.toString());
        order.setOrderType(Common.Order.ORDER_TYPE_DEAL.toString());
        order.setRetain1(rate.getPayTypr());
        order.setBack(orderApp.getBack());
        order.setCurrency(orderApp.getCurrency());
        String channelRFee = channelFee.getChannelRFee();
        BigDecimal orderAmount = orderApp.getOrderAmount();
        BigDecimal fee = new BigDecimal(channelRFee);
        log.info("【当前渠道费率：" + fee + "】");
        BigDecimal multiply = orderAmount.multiply(fee);
        log.info("【当前渠道收取手续费：" + multiply + "】");
        log.info("【当前收取商户手续费：" + orderApp.getRetain3() + "】");
        BigDecimal subtract = new BigDecimal(orderApp.getRetain3()).subtract(multiply);
        log.info("【当前订单系统盈利：" + subtract + "】");
		order.setRetain3(subtract.toString());
		orderServiceImpl.addOrder(order);
		return orderQrCh;
	};


	/**
	 * <p>支付宝扫码支付实体</p>
	 */
	public Result dealAlipayScan(DealOrderApp dealOrderApp) {
		/**
		 * #############################
		 * 生成预订单病返回支付连接
		 */
		Map<String, Object> param = MapUtil.newHashMap();
		param.put(ORDER, dealOrderApp.getOrderId());
		String encryptPublicKey = RSAUtils.getEncryptPublicKey(param, SystemConstants.INNER_PLATFORM_PUBLIC_KEY);
		String URL = "";//configServiceClientImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.SERVER_IP).getResult().toString();补充链接即可成为三方支付服务
		return Result.buildSuccessResult(URL + "/pay/alipayScan/" + encryptPublicKey);
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
	@Autowired
	private TransactionTemplate transactionTemplate;
	/**
	 * <p>代付</p>
	 */
	@Override
	public Result withdraw(Withdraw wit) {
		/**
		 * #####################################
		 * 代付扣款操作
		 */
		//	lock.lock();
		try {
			UserFund userFund = new UserFund();// userInfoServiceImpl.findUserFundByAccount(wit.getUserId());
			userFund.setUserId(wit.getUserId());
			Result deleteTransaction = transactionTemplate.execute((Result) -> {
				Result deleteWithdraw = amountPublic.deleteWithdraw(userFund, wit.getActualAmount(), wit.getOrderId());
				if (!deleteWithdraw.isSuccess()) {
					return deleteWithdraw;
				}
				Result deleteAmount = amountRunUtil.deleteAmount(wit, wit.getRetain2(), false);
				if (!deleteAmount.isSuccess()) {
					return deleteAmount;
				}
				return deleteAmount;
			});
			if (!deleteTransaction.isSuccess()) {
				return Result.buildFailMessage("账户扣减失败,请联系技术人员处理");
			}
			Result deleteFeeTransaction = transactionTemplate.execute((Result) -> {
			Result deleteWithdraw2 = amountPublic.deleteWithdraw(userFund, wit.getFee(), wit.getOrderId());
			if (!deleteWithdraw2.isSuccess()) {
				return deleteWithdraw2;
			}
			Result deleteAmountFee = amountRunUtil.deleteAmountFee(wit, wit.getRetain2(), false);
			if (!deleteAmountFee.isSuccess()) {
				return deleteAmountFee;
			}
			return deleteAmountFee;
			});
			if (!deleteFeeTransaction.isSuccess()) {
				return Result.buildFailMessage("账户扣减失败,请联系技术人员处理");
			}
		}  finally {
			//	lock.unlock();
		}
		return Result.buildSuccess();

	}
	/**
	 * <p>代付失败</p>
	 * @param wit
	 * @param msg
	 * @param ip
	 * @return
	 */
	public Result withdrawEr(Withdraw wit, String msg, String ip) {
		Result withrawOrderErBySystem = orderUtilImpl.withrawOrderErBySystem(wit.getOrderId(), ip, msg);
		if (withrawOrderErBySystem.isSuccess()) {
				notifyUtil.wit(wit.getOrderId());
		}
		return withrawOrderErBySystem;
	}
	public Result withdrawErMsg(Withdraw wit, String msg, String ip) {


		withdrawServiceImpl.updateMsg(wit.getOrderId(),msg);
			return  Result.buildSuccess();
	}

	@Resource
	private WithdrawMapper withdrawDao;

	public void witComment(String orderId) {
		withdrawDao.updateComment(orderId, "已提交三方处理");
	}


	/**
	 * 请求渠道时,获取渠道详情
	 *
	 * @param channelId
	 * @param payType
	 * @return
	 */
	protected ChannelInfo getChannelInfo(String channelId, String payType) {
		ChannelInfo channelInfo = new ChannelInfo();
		UserInfo userInfo = userInfoServiceImpl.findNotifyChannel(channelId);
		channelInfo.setChannelAppId(userInfo.getUserNode());
		channelInfo.setChannelPassword(userInfo.getPayPasword());
		channelInfo.setDealurl(userInfo.getDealUrl());
		ChannelFee channelFee = channelFeeDao.findChannelFee(channelId, payType);
		channelInfo.setChannelType(channelFee.getChannelNo());
		if (StrUtil.isNotBlank(userInfo.getWitip())) {
			channelInfo.setWitUrl(userInfo.getWitip());
		}
		return channelInfo;
	}

	/**
	 * 修改代付订单为失败，只是单纯的失败
	 *
	 * @param orderId
	 * @return
	 */
	protected Result withdrawErByAmount(String orderId, String msg) {
		int a = withdrawDao.updataOrderStatusEr(orderId,
				Common.Order.Wit.ORDER_STATUS_ER, msg);
		if (a > 0) {
				notifyUtil.wit(orderId);
			return Result.buildSuccessMessage("修改成功");
		} else {
			return Result.buildFailMessage("修改失败");
		}
	}
}
