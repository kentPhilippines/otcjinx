package alipay.manage.api.config;

import alipay.config.redis.RedisUtil;
import alipay.manage.bean.*;
import alipay.manage.bean.util.WitInfo;
import alipay.manage.mapper.ChannelFeeMapper;
import alipay.manage.mapper.WithdrawMapper;
import alipay.manage.service.*;
import alipay.manage.util.NotifyUtil;
import alipay.manage.util.OrderUtil;
import alipay.manage.util.amount.AmountPublic;
import alipay.manage.util.amount.AmountRunUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;
import otc.api.alipay.Common;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.common.SystemConstants;
import otc.result.Result;
import otc.util.RSAUtils;
import otc.util.number.GenerateOrderNo;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * <p>请求交易抽象【交易】【代付】</p>
 *
 * @author kent
 */
public abstract class PayOrderService implements PayService {
    public final String MARK = ".";
    public final String NOTIFY_MARK = "/";
    final String NOTIFY_DEAL = "/deal";
    final String NOTIFY_WIT = "/wit";
    public static final Log log = LogFactory.get();
    private static final String ORDER = "orderid";
    @Autowired
    private AmountPublic amountPublic;
    @Autowired
    private AmountRunUtil amountRunUtil;
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Autowired
    private DealWitService dealWitServIceImpl;
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
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserFundService userFundService;

    public Result deal(DealOrderApp dealOrderApp, String channelId, String notify) {
        UserInfo userinfo = userInfoServiceImpl.findDealUrl(dealOrderApp.getOrderAccount());//查询渠道账户
        String dealUrl = userinfo.getDealUrl();
        if (StrUtil.isEmpty(dealUrl)) {
            return Result.buildFailMessage("当前账户未配置 交易url");
        }
        dealUrl = dealUrl.trim();
        DealOrder dealOrder = create(dealOrderApp, channelId, dealUrl + PayApiConstant.Notfiy.NOTFIY_API_WAI + notify + NOTIFY_DEAL);
        /*BigDecimal limitBalance = userinfo.getLimitBalance();
        UserFund userFund = userFundService.findUserInfoByUserId(userinfo.getUserId());
        BigDecimal accountBalance = userFund.getAccountBalance();
        if((limitBalance.compareTo(accountBalance)>0)){
            String msg = "当前渠道余额限制，暂停充值";
            orderEr(dealOrder,msg);
            return Result.buildFailMessage(msg);
        }*/
        return Result.buildSuccessResult(dealOrder);
    }
    public Result deal(ChannelLocalUtil util , DealOrder order) {
        Result result = util.channelDealPush(order, getChannelInfo(order.getOrderQrUser(), order.getPayType()));
        return result;
    }
    public Result withdraw(ChannelLocalUtil util ,  DealWit wit ) {
        Result result = util.channelWitPush(wit, getChannelInfo(wit.getChanenlId(), wit.getWitType()));
        return result;
    }


    @Override
    public String dealNotify(Map map) {
        return Result.buildFail().toJson();
    }

    @Override
    public String witNotify(Map map) {
        return Result.buildFail().toJson();
    }



    @Override
    public Result findBalance(String channelId, String payType) {
        return null;
    }

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
                //OrderAppServiceImpl.updateOrd
                // erEr(orderApp.getOrderId(), msg);
                return true;
            }
        }
        return false;
    }
    public Result orderEr(DealOrder order, String msg) {
        log.info("【订单错误，将订单置为失败：" + order.getOrderId() + "】");
        boolean b = orderServiceImpl.updateOrderStatus(order.getOrderId(), Common.Order.DealOrder.ORDER_STATUS_ER, msg);
        if (b) {
            return Result.buildSuccess();
        } else {
            return Result.buildFail();
        }
    }

    public boolean orderEr(String orderId, String msg) {
        log.info("【将当前订单置为失败，当前交易订单号：" + orderId + "】");
        DealOrder dealOrder = orderServiceImpl.findOrderByOrderId(orderId);
        if (ObjectUtil.isNotNull(dealOrder)) {
            boolean updateOrderStatus = orderServiceImpl.updateOrderStatus(dealOrder.getOrderId(), Common.Order.DealOrder.ORDER_STATUS_ER, msg);
            if (updateOrderStatus) {
                //OrderAppServiceImpl.updateOrd
                // erEr(orderApp.getOrderId(), msg);
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


    public DealOrder create(DealOrderApp orderApp, String channeId, String notify) {
        log.info("【开始创建本地订单，当前创建订单的商户订单为：" + orderApp.toString() + "】");
        log.info("【当前交易的渠道账号为：" + channeId + "】");
        DealOrder order = new DealOrder();
        ChannelFee channelFee = channelFeeDao.findChannelFee(channeId, orderApp.getRetain1());
        order.setAssociatedId(orderApp.getOrderId());
        order.setDealDescribe("正常交易订单");
        order.setActualAmount(orderApp.getOrderAmount().subtract(new BigDecimal(orderApp.getRetain3())));
        order.setDealAmount(orderApp.getOrderAmount());
        order.setDealFee(new BigDecimal(orderApp.getRetain3()));
        order.setExternalOrderId(orderApp.getAppOrderId());
        order.setOrderAccount(orderApp.getOrderAccount());
        order.setNotify(  notify);
        order.setGenerationIp(orderApp.getOrderIp());
        String orderQrCh = GenerateOrderNo.Generate("SD");
        order.setOrderId(orderQrCh);
        order.setOrderQrUser(channeId);
        order.setOrderStatus(Common.Order.DealOrder.ORDER_STATUS_DISPOSE.toString());
        order.setOrderType(Common.Order.ORDER_TYPE_DEAL.toString());
        order.setRetain1(orderApp.getRetain1());
        order.setBack(orderApp.getBack());
        order.setOpenType(orderApp.getOpenType());
        order.setCurrency(orderApp.getCurrency());
        order.setMcRealName(orderApp.getPayName());
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
        return order;
    }

    ;

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
        String orderQrCh = GenerateOrderNo.Generate("JS");
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
    }

    ;

    public DealWit create(Withdraw wit, String channeId, String notify) {
        log.info("【开始创建本地订单，当前创建订单的商户订单为：" + wit.toString() + "】");
        log.info("【当前交易的渠道账号为：" + channeId + "】");
        DealWit order = new DealWit();
        UserInfo userinfo = userInfoServiceImpl.findDealUrl(channeId);//查询渠道账户
        ChannelFee channelFee = channelFeeDao.findChannelFee(channeId, wit.getWitType());
        order.setAssociatedId(wit.getOrderId());
        order.setActualAmount(wit.getAmount().add(new BigDecimal(channelFee.getChannelDFee())));
        order.setDealAmount(wit.getAmount());
        order.setDealFee(new BigDecimal(channelFee.getChannelDFee()));
        order.setExternalOrderId(wit.getAppOrderId());
        order.setNotify(notify);
        order.setChanenlId(channeId);
        String orderQrCh = GenerateOrderNo.Generate("SW");
        order.setOrderId(orderQrCh);
        order.setOrderAccount(wit.getUserId());
        order.setOrderStatus(Common.Order.DealOrder.ORDER_STATUS_DISPOSE.toString());
        order.setWitType(wit.getWitType());
        order.setCurrency(wit.getCurrency());
        WitInfo info = new WitInfo();
        info.setAccount(wit.getAccname());
        info.setBankName(wit.getBankName());
        info.setBankNo(wit.getBankNo());
        info.setBankCode(wit.getBankcode());
        order.setWitInfo(JSONUtil.toJsonStr(info));
        order.setWitType(wit.getWitType());
        boolean a = dealWitServIceImpl.add(order);
        return order;
    }

    ;

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
        } finally {
            //	lock.unlock();
        }
        return Result.buildSuccess();

    }

    /**
     * <p>代付失败</p>
     *
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
        withdrawServiceImpl.updateMsg(wit.getOrderId(), msg);
        return Result.buildSuccess();
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
        channelInfo.setBalanceUrl(userInfo.getBalanceUrl());
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

    protected static boolean isNumber(String str) {
        BigDecimal a = new BigDecimal(str);
        double dInput = a.doubleValue();
        long longPart = (long) dInput;
        BigDecimal bigDecimal = new BigDecimal(Double.toString(dInput));
        BigDecimal bigDecimalLongPart = new BigDecimal(Double.toString(longPart));
        double dPoint = bigDecimal.subtract(bigDecimalLongPart).doubleValue();
        System.out.println("整数部分为:" + longPart + "\n" + "小数部分为: " + dPoint);
        return dPoint > 0;
    }


    public boolean orderDealEr(String orderId, String msg) {
        log.info("【将当前订单置为失败，当前交易订单号：" + orderId + "】");
        boolean updateOrderStatus = orderServiceImpl.updateOrderStatus(orderId, Common.Order.DealOrder.ORDER_STATUS_ER, msg);
        return updateOrderStatus;
    }

    public static String name = "付款人：";

    public String getPayName(String payInfo, String orderId) {
        if (StrUtil.isNotEmpty(payInfo)) {
            String[] split = payInfo.split(name);
            String payName = split[1];
            return payName;
        }
        return "";

    }
    public void logRequestDeal(String channel, Object paras, String orderId, String mcOrderId) {
        String msg = "【请求 " + channel + " 上游渠道的参数为：" + paras.toString() + "，当前订单我方订单号为：" + orderId + "，当前商户订单号为：" + mcOrderId + "，请求时间：" + DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_FORMAT) + "】";
        ThreadUtil.execute(() -> {
            orderServiceImpl.updateOrderRequest(orderId, msg.toString());
        });
        log.info(msg);
    }

    public void logResponseDeal(String channel, Object paras, String orderId, String mcOrderId) {
        String msg = "【响应 " + channel + " 响应参数为：" + paras.toString() + "，当前订单我方订单号为：" + orderId + "，当前商户订单号为：" + mcOrderId + "，响应时间：" + DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_FORMAT) + "】";
        ThreadUtil.execute(() -> {
            orderServiceImpl.updateOrderResponse(orderId, msg.toString());
        });
        log.info(msg);
    }

}
