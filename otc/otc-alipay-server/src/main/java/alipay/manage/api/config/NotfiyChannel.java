package alipay.manage.api.config;

import alipay.config.redis.RedisLockUtil;
import alipay.manage.bean.ChannelFee;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.UserInfo;
import alipay.manage.mapper.ChannelFeeMapper;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import alipay.manage.service.WithdrawService;
import alipay.manage.util.NotifyUtil;
import alipay.manage.util.OrderUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import otc.api.alipay.Common;
import otc.bean.dealpay.Withdraw;
import otc.result.Result;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>代付成功的抽象类</p>
 *
 * @author kent
 */
public abstract class NotfiyChannel {
    public static final Log log = LogFactory.get();
    private static final String WIT_LOCK = "witNotfy:";
    private static final String DEAL_LOCK = "dealpayNotfiy:";
    static Lock lock = new ReentrantLock();
    @Resource
    private ChannelFeeMapper channelFeeDao;
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Autowired
    private OrderService orderServiceImpl;
    @Autowired
    private NotifyUtil notifyUtilImpl;
    @Autowired
    private OrderUtil orderUtilImpl;
    @Autowired
    private WithdrawService withdrawServiceImpl;
    @Autowired
    private RedisLockUtil redisLockUtil;
    @Autowired
    NotifyUtil notifyUtil;
    /**
     * 队列结算
     *
     * @param orderId
     */
    static char mark = '&';

    public Result witNotfyEr(String orderId, String ip, String msg) {
        log.info("【进入代付失败回调 抽象类：" + orderId + "】");
        Withdraw wit = withdrawServiceImpl.findOrderId(orderId);
        Result result = orderUtilImpl.withrawOrderErBySystem(wit, ip, msg);
        if (result.isSuccess()) {
                notifyUtil.wit(orderId);
        }
        return Result.buildFail();
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Result dealpayNotfiy(String orderId, String ip) {
        return dealpayNotfiy(orderId, ip, "三方系统回调成功");
    }

    public Result witNotfy(String orderId, String ip) {
        log.info("【进入代付回调抽象类，当前代付成功订单号：" + orderId + "】");
        Withdraw wit = withdrawServiceImpl.findOrderId(orderId);
        if (!wit.getOrderStatus().toString().equals(Common.Order.Wit.ORDER_STATUS_PUSH)) {
            log.info("【 当前代付回调重复，当前代付订单号：" + orderId + "】");
            return Result.buildFailMessage("当前代付回调重复");
        }
        wit.setComment("代付成功");
        redisLockUtil.redisLock(RedisLockUtil.AMOUNT_USER_KEY + wit.getUserId());
        Result withrawOrderSu = orderUtilImpl.withrawOrderSu1(wit);
        if (withrawOrderSu.isSuccess()) {
                notifyUtil.wit(orderId);
        }
         redisLockUtil.unLock(RedisLockUtil.AMOUNT_USER_KEY + wit.getUserId());
        return withrawOrderSu;
    }
    public Result  witNotSuccess(String orderId){
        log.info("【进入代付回调抽象类，当前代付成功订单号：" + orderId + "】");
        log.info("【当前订单上游无法处理，驳回：" + orderId + "】");
        Withdraw wit = withdrawServiceImpl.findOrderId(orderId);
        if (!wit.getOrderStatus().toString().equals(Common.Order.Wit.ORDER_STATUS_PUSH)) {
            log.info("【 当前代付回调重复，当前代付订单号：" + orderId + "】");
            return Result.buildFailMessage("当前代付回调重复");
        }
        boolean flag = withdrawServiceImpl.updatePushAgent(orderId);//修改为订单可以再次推送
        if(flag){
            return Result.buildSuccess();
        }
        return Result.buildFail();
    }



    public Result dealpayNotfiy(String orderId, String ip, String msg) {
        log.info("【进入支付成功回调处理类：" + orderId + "】");
        DealOrder order = orderServiceImpl.findOrderStatus(orderId);
        if (ObjectUtil.isNull(order)) {
            log.info("【当前回调订单不存在，当前回调订单号：" + orderId + "】");
            return Result.buildFailMessage("当前回调订单不存在");
        }
        try {
            redisLockUtil.redisLock(RedisLockUtil.AMOUNT_USER_KEY + order.getOrderQrUser());
            Result dealAmount = orderUtilImpl.updataDealOrderSu(order.getOrderId(), msg, ip, false);
            if (dealAmount.isSuccess()) {
                return dealAmount;
            }
        } finally {
            redisLockUtil.unLock(RedisLockUtil.AMOUNT_USER_KEY + order.getOrderQrUser());
        }
        return Result.buildFail();
    }

    /**
     * 获取渠道密钥
     *
     * @param orderId
     * @return
     */
    public String getChannelKey(String orderId) {
        DealOrder orderInfo = orderServiceImpl.findOrderByOrderId(orderId);
        String orderQrUser = orderInfo.getOrderQrUser();
        UserInfo userInfoByUserId = userInfoServiceImpl.findPassword(orderQrUser);
        return userInfoByUserId.getPayPasword();
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
     * 获取渠道密钥
     *
     * @param orderId
     * @return
     */
    public UserInfo getChannel(String orderId) {
        DealOrder orderInfo = orderServiceImpl.findOrderByOrderId(orderId);
        String orderQrUser = orderInfo.getOrderQrUser();
        UserInfo userInfoByUserId = userInfoServiceImpl.findPassword(orderQrUser);
        return userInfoByUserId;
    }

    protected String getDPAyChannelKey(String orderId) {
        Withdraw wit = withdrawServiceImpl.findOrderId(orderId);
        String channel = "";
        if (StrUtil.isNotBlank(wit.getChennelId())) {//支持运营手动推送出款
            channel = wit.getChennelId();
        } else {
            channel = wit.getWitChannel();
        }
        UserInfo userInfoByUserId = userInfoServiceImpl.findPassword(channel);
        return userInfoByUserId.getPayPasword();
    }

    void pushDeal(String orderId, String msg, String ip, boolean flag) {
        redisTemplate.convertAndSend("order-deal", orderId + mark + ip);//推送消息
    }
    public String getKey(String channelNo) {
    UserInfo info = userInfoServiceImpl.findPassword(channelNo);
   //     UserInfo info = userInfoServiceImpl.findUserNode(channelNo);
        return info.getPayPasword();
    }

    void pushWit(String orderId, String ip) {
        redisTemplate.convertAndSend("order-wit", orderId + mark + ip);//推送消息
    }



    public Map<String, Object> getRequestBody(HttpServletRequest request ) {
        Map<String, Object> map = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = request.getParameter(key);
            map.put(key, value);
        }
        Map<String, Object> params = new HashMap<>();
        BufferedReader br;
        try {
            br = request.getReader();
            String str, wholeParams = "";
            while ((str = br.readLine()) != null) {
                wholeParams += str;
            }
            if (StringUtils.isNotBlank(wholeParams)) {
                params = JSON.parseObject(wholeParams, Map.class);
            }
        } catch (IOException e) {
            return params;
        }
        map.putAll(params);
        return map;
    }

}
