package alipay.manage.api.config;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.feign.ConfigServiceClient;
import alipay.manage.bean.*;
import alipay.manage.mapper.ChannelFeeMapper;
import alipay.manage.service.*;
import alipay.manage.util.OrderUtil;
import alipay.manage.util.amount.AmountPublic;
import alipay.manage.util.amount.AmountRunUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import otc.api.alipay.Common;
import otc.bean.config.ConfigFile;
import otc.bean.dealpay.Withdraw;
import otc.common.SystemConstants;
import otc.result.Result;
import otc.util.RSAUtils;
import otc.util.number.GenerateOrderNo;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    @Resource
    private ConfigServiceClient configServiceClientImpl;
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

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        if (Common.Deal.PRODUCT_ALIPAY_SCAN.equals(channel))
            return dealAlipayScan(dealOrderApp);
        else if (Common.Deal.PRODUCT_ALIPAY_H5.equals(channel))
            return dealAlipayH5(dealOrderApp);
        return null;
    }

	public boolean orderEr(DealOrderApp orderApp, String msg) {
		log.info("【将当前订单置为失败，当前交易订单号：" + orderApp.getOrderId() + "】");
		DealOrder dealOrder = orderServiceImpl.findAssOrder(orderApp.getOrderId());
		if(ObjectUtil.isNotNull(dealOrder)) {
			boolean updateOrderStatus = orderServiceImpl.updateOrderStatus(dealOrder.getOrderId(), Common.Order.DealOrder.ORDER_STATUS_ER, msg);
			if(updateOrderStatus) {
				OrderAppServiceImpl.updateOrderEr(orderApp.getOrderId(), msg);
				return true;
			}
		}
		return false;
	}
	public boolean orderEr(DealOrderApp orderApp){
		return orderEr(orderApp,"暂无支付渠道");
	}
	public String create(DealOrderApp orderApp,String channeId){
		log.info("【开始创建本地订单，当前创建订单的商户订单为："+orderApp.toString()+"】");
		log.info("【当前交易的渠道账号为："+channeId+"】");
		DealOrder order = new DealOrder();
		String orderAccount = orderApp.getOrderAccount();//交易商户号
		UserInfo accountInfo = userInfoServiceImpl.findUserInfoByUserId(orderAccount);//这里有为商户配置的 供应队列属性
		UserInfo userinfo = userInfoServiceImpl.findUserInfoByUserId(channeId);//查询渠道账户
		UserRate rate = userRateServiceImpl.findRateFee(orderApp.getFeeId());
		ChannelFee channelFee = channelFeeDao.findChannelFee(rate.getChannelId(), rate.getPayTypr());
		log.info("【当前交易的产品类型为："+userinfo.getUserNode()+"】");
		order.setAssociatedId(orderApp.getOrderId());
		order.setDealDescribe("正常交易订单");
		order.setActualAmount(orderApp.getOrderAmount().subtract(new BigDecimal(orderApp.getRetain3())));
		order.setDealAmount(orderApp.getOrderAmount());
		order.setDealFee(new BigDecimal(orderApp.getRetain3()));
		order.setExternalOrderId(orderApp.getAppOrderId());
		order.setOrderAccount(orderApp.getOrderAccount());
		order.setNotify(orderApp.getNotify());
		String orderQrCh = GenerateOrderNo.Generate("C");
		order.setOrderId(orderQrCh);
		order.setOrderQrUser(userinfo.getUserId());
		order.setOrderStatus(Common.Order.DealOrder.ORDER_STATUS_DISPOSE.toString());
		order.setOrderType(Common.Order.ORDER_TYPE_DEAL.toString());
		order.setRetain1(rate.getPayTypr());
		order.setBack(orderApp.getBack());
		String channelRFee = channelFee.getChannelRFee();
		BigDecimal orderAmount = orderApp.getOrderAmount();
		BigDecimal fee = new BigDecimal(channelRFee);
		log.info("【当前渠道费率："+fee+"】");
		BigDecimal multiply = orderAmount.multiply(fee);
		log.info("【当前渠道收取手续费："+multiply+"】");
		log.info("【当前收取商户手续费："+orderApp.getRetain3()+"】");
		BigDecimal subtract = new BigDecimal(orderApp.getRetain3()).subtract(multiply);
		log.info("【当前订单系统盈利："+subtract+"】");
		order.setRetain3(subtract.toString());
		boolean addOrder = orderServiceImpl.addOrder(order);
		if(addOrder) {
			ThreadUtil.execute(()->{
				corr(order,rate,channelFee);
			});
		}
		return orderQrCh;
	};

	/**
	 * <p>数据数据统计</p>
	 */
	void corr(DealOrder order ,UserRate rate ,ChannelFee channelFee){
		ThreadUtil.execute(()->{
			CorrelationData corr = new CorrelationData();
			corr.setAmount(order.getDealAmount());
			corr.setOrderId(order.getOrderId());
			corr.setOrderStatus(Integer.valueOf(order.getOrderStatus()));
			corr.setUserId(order.getOrderQrUser());
			corr.setAppId(order.getOrderAccount());
			corr.setFee(rate.getFee());
			corr.setChannelFee(new BigDecimal(channelFee.getChannelRFee() ));
			corr.setProfit(new BigDecimal(order.getRetain3()));
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
	static Lock lock = new  ReentrantLock();
	/**
	 * <p>代付</p>
	 */
	@Override
	public Result withdraw(Withdraw wit) {
		/**
		 * #####################################
		 * 代付扣款操作
		 */
		lock.lock();
	    try {
			UserFund userFund = new UserFund();// userInfoServiceImpl.findUserFundByAccount(wit.getUserId());
			userFund.setUserId(wit.getUserId());
			Result deleteWithdraw = amountPublic.deleteWithdraw(userFund, wit.getActualAmount(), wit.getOrderId());
			if (!deleteWithdraw.isSuccess())
				return Result.buildFailMessage("账户扣减失败,请联系技术人员处理");
			Result deleteAmount = amountRunUtil.deleteAmount(wit, wit.getRetain2(), false);
			if (!deleteAmount.isSuccess())
				return Result.buildFailMessage("账户扣减失败,请联系技术人员处理");
			Result deleteWithdraw2 = amountPublic.deleteWithdraw(userFund, wit.getFee(), wit.getOrderId());
			if (!deleteWithdraw2.isSuccess())
				return Result.buildFailMessage("账户扣减失败,请联系技术人员处理");
			Result deleteAmountFee = amountRunUtil.deleteAmountFee(wit, wit.getRetain2(), false);
			if (!deleteAmountFee.isSuccess())
				return Result.buildFailMessage("账户扣减失败,请联系技术人员处理");
		}  finally {
	        lock.unlock();
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
        return withrawOrderErBySystem;
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
        UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(channelId);
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
}
