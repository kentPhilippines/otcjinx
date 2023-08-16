package alipay.manage.api.config;

import alipay.config.redis.RedisLockUtil;
import alipay.config.redis.RedisUtil;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealWit;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.PayInfo;
import alipay.manage.service.*;
import alipay.manage.util.NotifyUtil;
import alipay.manage.util.OrderUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import otc.api.alipay.Common;
import otc.bean.alipay.OrderDealStatus;
import otc.bean.dealpay.Withdraw;
import otc.result.Result;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

public abstract class ChannelUtil extends NotfiyChannel {

    @Value("${otc.payInfo.url}")
    public String url;
    public static Logger log = LoggerFactory.getLogger(ChannelUtil.class);
    @Autowired
    private RedisUtil redisUtil;
    public static final String GET_BANK_INFO_MARK = "GET_BANK_INFO:";
    @Autowired
    private OrderService orderServiceImpl;
    @Autowired
    private UserFundService UserFundService;
    @Autowired
    private DealWitService dealWitServIceImpl;
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Autowired
    private RedisLockUtil redisLockUtil;
    @Autowired
    private NotifyUtil notifyUtil;
    @Autowired
    private OrderUtil orderUtilImpl;
    @Autowired
    private WithdrawService withdrawServiceImpl;

    public Result orderEr(DealOrder order, String msg) {
        log.info("【订单错误，将订单置为失败：" + order.getOrderId() + "】");
        boolean b = orderServiceImpl.updateOrderStatus(order.getOrderId(), Common.Order.DealOrder.ORDER_STATUS_ER, msg);
        if (b) {
            return Result.buildSuccess();
        } else {
            return Result.buildFail();
        }
    }


    /**
     * 按照Key值字典序排序，并使用&拼接
     *
     * @param map
     * @return
     */
    public static String createParam(Map<String, Object> map) {
        try {
            if (map == null || map.isEmpty()) {
                return null;
            }
            Object[] key = map.keySet().toArray();
            Arrays.sort(key);
            StringBuffer res = new StringBuffer(128);
            for (int i = 0; i < key.length; i++) {
                if (ObjectUtil.isNotNull(map.get(key[i]))) {
                    res.append(key[i] + "=" + map.get(key[i]) + "&");
                }
            }
            String rStr = res.substring(0, res.length() - 1);
            return rStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String createParamValue(Map<String, Object> map) {
        try {
            if (map == null || map.isEmpty()) {
                return null;
            }
            Object[] key = map.keySet().toArray();
            Arrays.sort(key);
            StringBuffer res = new StringBuffer();
            for (int i = 0; i < key.length; i++) {
                if (ObjectUtil.isNotNull(map.get(key[i]))) {
                    res.append(map.get(key[i]));
                }
            }
            String rStr = res.substring(0, res.length());
            return rStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * md5 加密
     *
     * @param a
     * @return
     */
    public static String md5(String a) {
        String c = "";
        MessageDigest md5;
        String result = "";
        try {
            md5 = MessageDigest.getInstance("md5");
            md5.update(a.getBytes("utf-8"));
            byte[] temp;
            temp = md5.digest(c.getBytes("utf-8"));
            for (int i = 0; i < temp.length; i++) {
                result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
        return result;
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

    public void logRequestWit(String channel, Object paras, String orderId, String mcOrderId) {
        String msg = "【请求 " + channel + " 上游渠道的参数为：" + paras.toString() + "，当前订单我方订单号为：" + orderId + "，当前商户订单号为：" + mcOrderId + "，请求时间：" + DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_FORMAT) + "】";
        ThreadUtil.execute(() -> {
            dealWitServIceImpl.updateOrderRequest(orderId, msg.toString());
        });
        log.info(msg);
    }

    public void logResponseWit(String channel, Object paras, String orderId, String mcOrderId) {
        String msg = "【响应 " + channel + " 响应参数为：" + paras.toString() + "，当前订单我方订单号为：" + orderId + "，当前商户订单号为：" + mcOrderId + "，响应时间：" + DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_FORMAT) + "】";
        ThreadUtil.execute(() -> {
            dealWitServIceImpl.updateOrderResponse(orderId, msg.toString());
        });
        log.info(msg);
    }


    /**
     * 缓存收款人数据 做时间刷新
     *
     * @param info
     */
    public void cache(PayInfo info) {
        log.info("【当前缓存银行详细信息为：" + info.toString() + "】");
        redisUtil.set(GET_BANK_INFO_MARK + info.getOrderId(), JSONUtil.parseObj(info).toString(), 300);
    }


    public void updateAmountChannel(BigDecimal amount, String userId) {
        UserFundService.updateAmount(amount, userId);
    }


    public void witOrdererEr(String orderId, String msg) {
        log.info("【当前渠道代付订单失败：" + orderId + "，失败原因：" + msg + "】");
        dealWitServIceImpl.updateWitEr(orderId,msg);
    }
    public void witOrdererSu(String orderId, String msg) {
        log.info("【当前渠道代付订单失败：" + orderId + "，失败原因：" + msg + "】");
        dealWitServIceImpl.witOrdererSu(orderId,msg);
    }

    public String getChannelKey(String orderId) {
        DealOrder orderInfo = orderServiceImpl.findOrderByOrderId(orderId);
        String orderQrUser = orderInfo.getOrderQrUser();
        UserInfo userInfoByUserId = userInfoServiceImpl.findPassword(orderQrUser);
        return userInfoByUserId.getPayPasword();
    }
    public String getWitChannelKey(String orderId) {
        DealWit orderByOrderId = dealWitServIceImpl.findOrderByOrderId(orderId);
        String chanenlId = orderByOrderId.getChanenlId();
        UserInfo userInfoByUserId = userInfoServiceImpl.findPassword(chanenlId);
        return userInfoByUserId.getPayPasword();
    }
    public String getChannelPublicKey(String orderId) {
        DealOrder orderInfo = orderServiceImpl.findOrderByOrderId(orderId);
        String orderQrUser = orderInfo.getOrderQrUser();
        UserInfo userInfoByUserId = userInfoServiceImpl.findPassword(orderQrUser);
        return userInfoByUserId.getPublicKey();
    }
    public String getWitChannelPublicKey(String orderId) {
        DealWit orderByOrderId = dealWitServIceImpl.findOrderByOrderId(orderId);
        String chanenlId = orderByOrderId.getChanenlId();
        UserInfo userInfoByUserId = userInfoServiceImpl.findPassword(chanenlId);
        return userInfoByUserId.getPublicKey();
    }

    public Result witNotfy(String orderId, String orderStatus) {
        DealWit dealWit = dealWitServIceImpl.findOrderByOrderId(orderId);
        if(dealWit.getOrderStatus().equals(Common.Order.Wit.ORDER_STATUS_PUSH)){
            log.info("【当前订单状态错误，逻辑中断：" + orderId + "，"+dealWit.toString()+"】");
            return Result.buildFailMessage("状态匹配错误");
        }
        dealWitServIceImpl.updateOrderStatus(orderId, orderStatus);
        dealWit = dealWitServIceImpl.findOrderByOrderId(orderId);
        if (dealWit.getOrderStatus().equals(Common.Order.Wit.ORDER_STATUS_SU)) {
            log.info("【进入代付回调抽象类，当前代付成功订单号：" + orderId + "】");
            Withdraw wit = withdrawServiceImpl.findOrderId(dealWit.getAssociatedId());
            redisLockUtil.redisLock(RedisLockUtil.AMOUNT_USER_KEY + wit.getUserId());
            Result withrawOrderSu = orderUtilImpl.withrawOrderSu1(wit);
            if (withrawOrderSu.isSuccess()) {
                notifyUtil.wit(orderId);
            }
            redisLockUtil.unLock(RedisLockUtil.AMOUNT_USER_KEY + wit.getUserId());
            return withrawOrderSu;
        } else {
            return Result.buildSuccess();
        }

    }




    public Result dealNotfiy(String orderId,  String msg) {
        log.info("【进入支付成功回调处理类：" + orderId + "】");
        DealOrder order = orderServiceImpl.findOrderStatus(orderId);
        if (ObjectUtil.isNull(order)) {
            log.info("【当前回调订单不存在，当前回调订单号：" + orderId + "】");
            return Result.buildFailMessage("当前回调订单不存在");
        }
        try {
            redisLockUtil.redisLock(RedisLockUtil.AMOUNT_USER_KEY + order.getOrderQrUser());
            boolean b = orderServiceImpl.updateOrderStatus(order.getOrderId(), OrderDealStatus.成功.getIndex().toString(), msg);
            if(b){
                return Result.buildSuccess();
            }
         } finally {
            redisLockUtil.unLock(RedisLockUtil.AMOUNT_USER_KEY + order.getOrderQrUser());
        }
        return Result.buildFail();
    }
}
