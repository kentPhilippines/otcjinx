package alipay.manage.api.config;

import alipay.config.redis.RedisLockUtil;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserInfo;
import alipay.manage.mapper.ChannelFeeMapper;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import alipay.manage.service.WithdrawService;
import alipay.manage.util.*;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import otc.api.alipay.Common;
import otc.bean.dealpay.Withdraw;
import otc.result.Result;

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
    private static final String WIT_LOCK = "witNotfy";
    private static final String DEAL_LOCK = "dealpayNotfiy";
    static Lock lock = new ReentrantLock();
    @Autowired private UserInfoService userInfoServiceImpl;
    @Autowired private OrderService orderServiceImpl;
    @Autowired private NotifyUtil notifyUtilImpl;
    @Autowired private ChannelFeeMapper channelFeeDao;
    @Autowired private AmountUtil amountUtil;
    @Autowired private AmountRunUtil amountRunUtil;
    @Autowired private OrderUtil orderUtilImpl;
    @Autowired private WithdrawService withdrawServiceImpl;
    @Autowired private CheckUtils checkUtils;
    @Autowired private RedisLockUtil redisLockUtil;

    public Result witNotfy(String orderId, String ip) {
        String lock = this.getClass().getName() + WIT_LOCK + ip;
        log.info("【进入代付回调抽象类，当前代付成功订单号：" + orderId + "】");
        Withdraw wit = withdrawServiceImpl.findOrderId(orderId);
        if (!wit.getStatus().toString().equals(Common.Order.Wit.ORDER_STATUS_YU)) {
            log.info("【 当前代付回调重复，当前代付订单号：" + orderId + "】");
            return Result.buildFailMessage("当前代付回调重复");
        }
        wit.setComment("代付成功");
        redisLockUtil.redisLock(lock);
        Result withrawOrderSu = orderUtilImpl.withrawOrderSu1(wit);
        if (withrawOrderSu.isSuccess()) {
            ThreadUtil.execute(() -> {
                wit(orderId);
            });
        }
        UserFund userFund = new UserFund();
        userFund.setUserId(wit.getWitChannel());
        ThreadUtil.execute(() -> {
            orderUtilImpl.channelWitSu(orderId, wit, ip, userFund);
        });
        Result result = orderUtilImpl.agentDpayChannel(wit, ip,true);
        redisLockUtil.unLock(lock);
        return withrawOrderSu;
    }

    public Result witNotfyEr(String orderId, String ip, String msg) {
        log.info("【进入代付失败回调 抽象类：" + orderId + "】");
        Withdraw wit = withdrawServiceImpl.findOrderId(orderId);
        Result result = orderUtilImpl.withrawOrderErBySystem(orderId, ip, msg);
        if (result.isSuccess()) {
            ThreadUtil.execute(() -> {
                wit(orderId);
            });
        }
        return Result.buildFail();
    }

    public Result dealpayNotfiy(String orderId, String ip, String msg) {
        String redislock = this.getClass().getName() + DEAL_LOCK + ip;
        log.info("【进入支付成功回调处理类：" + orderId + "】");
        DealOrder order = orderServiceImpl.findOrderByOrderId(orderId);
        if (ObjectUtil.isNull(order)) {
            log.info("【当前回调订单不存在，当前回调订单号：" + orderId + "】");
            return Result.buildFailMessage("当前回调订单不存在");
        }
        lock.lock();
        try {
            redisLockUtil.redisLock(redislock);
            Result dealAmount = orderUtilImpl.updataDealOrderSu(order.getOrderId(), msg, ip, false);
            if (dealAmount.isSuccess()) {
                log.info("【订单修改成功，向下游发送回调：" + orderId + "】");
                ThreadUtil.execute(() -> {
                    notifyUtilImpl.sendMsg(orderId);
                });
                return Result.buildSuccessMessage("订单修改成功");
            }
        } finally {
            redisLockUtil.unLock(redislock);
            lock.unlock();
        }
        return Result.buildFail();
    }

    public Result dealpayNotfiy(String orderId, String ip) {
        return dealpayNotfiy(orderId, ip, "三方系统回调成功");
    }

    /**
     * <p>API下游代付通知</p>
     */
    void wit(String orderId) {
        log.info("【代付订单修改成功，现在开始通知下游，代付订单号：" + orderId + "】");
        Map<String, Object> map = new HashMap<String, Object>();
        Withdraw wit = withdrawServiceImpl.findOrderId(orderId);
        UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(wit.getUserId());
        map.put("apporderid", wit.getAppOrderId());
        map.put("tradesno", wit.getOrderId());
        map.put("status", wit.getOrderStatus());//0 预下单    1处理中  2 成功  3失败
        map.put("amount", wit.getAmount());
        map.put("appid", wit.getUserId());
        String sign = checkUtils.getSign(map, userInfo.getPayPasword());
        map.put("sign", sign);
        send(wit.getNotify(), orderId, map);
    }

    /**
     * <p>发送通知</p>
     *
     * @param url     发送通知的地址
     * @param orderId 发送订单ID
     * @param msg     发送通知的内容
     */
    private void send(String url, String orderId, Map<String, Object> msg) {
        String result = HttpUtil.post(url, msg, -1);
        log.info("服务器返回结果为: " + result.toString());
        log.info("【下游商户返回信息为成功,成功收到回调信息】");
        //更新订单是否通知成功状态
    }

}
