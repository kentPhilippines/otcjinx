package alipay.manage.api.config;

import alipay.config.redis.RedisUtil;
import alipay.manage.bean.*;
import alipay.manage.bean.util.WitInfo;
import alipay.manage.mapper.ChannelFeeMapper;
import alipay.manage.mapper.WithdrawMapper;
import alipay.manage.service.*;
import alipay.manage.util.NotifyUtil;
import alipay.manage.util.amount.AmountPublic;
import alipay.manage.util.amount.AmountRunUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import otc.api.alipay.Common;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;
import otc.util.number.GenerateOrderNo;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

public class PayOrderServiceV2 implements PayServiceV2 {
    public final String MARK = ".";
    public final String NOTIFY_MARK = "/";
    final String NOTIFY_DEAL = "/deal";
    final String NOTIFY_WIT = "/wit";
    public static final Log log = LogFactory.get();
    @Autowired
    private AmountPublic amountPublic;
    @Autowired
    private AmountRunUtil amountRunUtil;
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Autowired
    private NotifyUtil notifyUtil;
    @Autowired
    private OrderService orderServiceImpl;
    @Autowired
    private DealWitService dealWitServIceImpl;
    @Autowired
    private OrderAppService OrderAppServiceImpl;
    @Autowired
    private UserRateService userRateServiceImpl;
    @Resource
    private ChannelFeeMapper channelFeeDao;
    @Autowired
    private WithdrawService withdrawServiceImpl;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserFundService UserFundService;
    @Resource
    private WithdrawMapper withdrawDao;


    @Override
    public String dealNotify(Map map) {
        return Result.buildFail().toJson();
    }

    @Override
    public String witNotify(Map map) {
        return Result.buildFail().toJson();
    }

    public Result deal(ChannelLocalUtil util , DealOrder order) {
        Result result = util.channelDealPush(order, getChannelInfo(order.getOrderQrUser(), order.getPayType()));
        return result;
    }
    public Result withdraw(ChannelLocalUtil util ,  DealWit wit ) {
        Result result = util.channelWitPush(wit, getChannelInfo(wit.getChanenlId(), wit.getWitType()));
        return result;
    }


        public Result deal(DealOrderApp dealOrderApp, String channelId, String notify) {
        UserInfo userinfo = userInfoServiceImpl.findDealUrl(dealOrderApp.getOrderAccount());//查询渠道账户
        String dealUrl = userinfo.getDealUrl();
        if (StrUtil.isEmpty(dealUrl)) {
            return Result.buildFailMessage("当前账户未配置 交易url");
        }
        dealUrl = dealUrl.trim();
        DealOrder dealOrder = create(dealOrderApp, channelId, dealUrl + PayApiConstant.Notfiy.NOTFIY_API_WAI + notify + NOTIFY_DEAL);
        BigDecimal limitBalance = userinfo.getLimitBalance();
        UserFund userFund = UserFundService.findUserInfoByUserId(userinfo.getUserId());
        BigDecimal accountBalance = userFund.getAccountBalance();
        if((limitBalance.compareTo(accountBalance)>0)){
            String msg = "当前渠道余额限制，暂停充值";
            orderEr(dealOrder,msg);
            return Result.buildFailMessage(msg);
        }
        return Result.buildSuccessResult(dealOrder);
    }


    @Override
    public Result deal(DealOrderApp dealOrderApp, String payType) {
        return deal(dealOrderApp, payType, "");
    }

    @Override
    public Result withdraw(Withdraw wit) {
        return null;
    }

    public Result withdraw(Withdraw wit, String channelId, String notify) {
        UserInfo userinfo = userInfoServiceImpl.findDealUrl(wit.getUserId());//查询渠道账户
        String dealUrl = userinfo.getDealUrl();
        if (StrUtil.isEmpty(dealUrl)) {
            return Result.buildFailMessage("当前账户未配置 交易url");
        }
        dealUrl = dealUrl.trim();
        DealWit dealWit = create(wit, channelId, dealUrl + PayApiConstant.Notfiy.NOTFIY_API_WAI + notify + NOTIFY_WIT);
        return Result.buildSuccessResult(dealWit);
    }

    @Override
    public Result withdraw(Withdraw wit, String channelId) {
        return withdraw(wit, channelId, "");
    }


    @Override
    public Result findBalance(String channelId, String payType) {
        return null;
    }

    public boolean orderAppEr(DealOrderApp orderApp, String msg) {
        log.info("【将当前订单置为失败，当前交易订单号：" + orderApp.getOrderId() + "】");
        OrderAppServiceImpl.updateOrderEr(orderApp.getOrderId(), msg);
        return true;
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
    };

    public DealWit create(Withdraw wit, String channeId, String notify) {
        log.info("【开始创建本地订单，当前创建订单的商户订单为：" + wit.toString() + "】");
        log.info("【当前交易的渠道账号为：" + channeId + "】");
        DealWit order = new DealWit();
        ChannelFee channelFee = channelFeeDao.findChannelFee(channeId, wit.getWitType());
        order.setAssociatedId(wit.getOrderId());
        order.setActualAmount(wit.getAmount().add(new BigDecimal(channelFee.getChannelDFee())));
        order.setDealAmount(wit.getAmount());
        order.setDealFee(new BigDecimal(channelFee.getChannelDFee()));
        order.setExternalOrderId(wit.getAppOrderId());
        order.setNotify(   notify);
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
    };


    /**
     * <p>代付</p>
     */
    public Result accountWitPay(Withdraw wit) {
        /**
         * #####################################
         * 代付扣款操作
         */
        //	lock.lock();
        try {
            UserFund userFund = new UserFund();// userInfoServiceImpl.findUserFundByAccount(wit.getUserId());
            userFund.setUserId(wit.getUserId());
            Result deleteWithdraw = amountPublic.deleteWithdraw(userFund, wit.getActualAmount(), wit.getOrderId());
            if (!deleteWithdraw.isSuccess()) {
                return deleteWithdraw;
            }
            Result deleteAmount = amountRunUtil.deleteAmount(wit, wit.getRetain2(), false);
            if (!deleteAmount.isSuccess()) {
                return deleteAmount;
            }
            Result deleteWithdraw2 = amountPublic.deleteWithdraw(userFund, wit.getFee(), wit.getOrderId());
            if (!deleteWithdraw2.isSuccess()) {
                return deleteWithdraw2;
            }
            Result deleteAmountFee = amountRunUtil.deleteAmountFee(wit, wit.getRetain2(), false);
            if (!deleteAmountFee.isSuccess()) {
                return deleteAmountFee;
            }
            return deleteAmountFee;

        } finally {
            //	lock.unlock();
        }

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
        channelInfo.setEmail(userInfo.getEmail());
        channelInfo.setPrivateKey(userInfo.getPrivateKey());
        if (StrUtil.isNotBlank(userInfo.getWitip())) {
            channelInfo.setWitUrl(userInfo.getWitip());
        }
        return channelInfo;
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
    public Result orderEr(DealOrder order, String msg) {
        log.info("【订单错误，将订单置为失败：" + order.getOrderId() + "】");
        boolean b = orderServiceImpl.updateOrderStatus(order.getOrderId(), Common.Order.DealOrder.ORDER_STATUS_ER, msg);
        if (b) {
            return Result.buildSuccess();
        } else {
            return Result.buildFail();
        }
    }

    public Result  withdrawErByAmount(String orderId ,String msg){
        withdrawServiceImpl.updateMsg(orderId,msg);
        return Result.buildSuccess();
    }
    public Result  witEr(String orderId){
        withdrawServiceImpl.updateEr(orderId);
        return Result.buildSuccess();
    }

}