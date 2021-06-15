package alipay.config.redis;

import alipay.config.task.OrderTask;
import alipay.manage.bean.UserFund;
import alipay.manage.service.WithdrawService;
import alipay.manage.util.NotifyUtil;
import alipay.manage.util.OrderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import otc.api.alipay.Common;
import otc.bean.dealpay.Withdraw;
import otc.result.Result;

import java.util.concurrent.CountDownLatch;

public class ReceiverRedisMessage {
    private static final Logger log = LoggerFactory.getLogger(ReceiverRedisMessage.class);
    private CountDownLatch latch;
    @Autowired
    private WithdrawService withdrawServiceImpl;
    @Autowired
    private NotifyUtil notifyUtilImpl;
    @Autowired
    private OrderUtil orderUtilImpl;

    public ReceiverRedisMessage(CountDownLatch latch) {
        this.latch = latch;
    }

    public void receiveMessage(String jsonMsg) {
        log.info("【开始消费REDIS消息队列deal数据...】");
        try {
            jsonMsg = jsonMsg.substring(0, jsonMsg.length() - 1);
            jsonMsg = jsonMsg.substring(1);
            log.info("【接受消息为：" + jsonMsg + "】");
            String[] split = jsonMsg.split("&");
            Result dealAmount = orderUtilImpl.updataDealOrderSu(split[0], "三方成功", split[1], false);
            if (dealAmount.isSuccess()) {
                log.info("【订单修改成功，向下游发送回调：" + split[0] + "】");
                //    notifyUtilImpl.sendMsg(split[0]);
            }
        } catch (Exception e) {
            log.error("[消费REDIS消息队列deal数据失败，失败信息:{}]", e.getMessage());
        }
        latch.countDown();
    }
    @Autowired
    OrderTask orderTask;
     public void taskOrder(String jsonMsg) {
            log.info("【开始消费REDIS消息队列Task数据...】");
            try {
             /*   log.info("【开始进行每秒订单清算】");
                orderTask.orderTask();
                log.info("【开始进行10秒代付订单推送】");
                orderTask.orderWitTask();*/
            } catch (Exception e) {
                log.error("[消费REDIS消息队列Task数据失败，失败信息:{}]", e.getMessage());
            }
            latch.countDown();
        }

    /**
     * 队列消息接收方法
     *
     * @param jsonMsg
     */
    public void receiveMessage2(String jsonMsg) {
        log.info("[开始消费REDIS消息队列wit数据...]");
        try {
            jsonMsg = jsonMsg.substring(0, jsonMsg.length() - 1);
            jsonMsg = jsonMsg.substring(1);
            log.info("【接受消息为：" + jsonMsg + "】");
            String[] split = jsonMsg.split("&");
            log.info("【进入代付回调抽象类，当前代付成功订单号：" + split[0] + "】");
            Withdraw wit = withdrawServiceImpl.findOrderId(split[0]);
            if (!wit.getStatus().toString().equals(Common.Order.Wit.ORDER_STATUS_YU)) {
                log.info("【 当前代付回调重复，当前代付订单号：" + split[0] + "】");
                return;
            }
            wit.setComment("代付成功");
            Result withrawOrderSu = orderUtilImpl.withrawOrderSu1(wit);
            if (withrawOrderSu.isSuccess()) {
                notifyUtilImpl.wit(split[0]);
            }
            UserFund userFund = new UserFund();
            userFund.setUserId(wit.getWitChannel());
            orderUtilImpl.channelWitSu(split[0], wit, split[1], userFund);
            orderUtilImpl.agentDpayChannel(wit, split[1], true);
            log.info("[消费REDIS消息队列phoneTest2数据成功.]");
        } catch (Exception e) {
            log.error("[消费REDIS消息队列phoneTest2数据失败，失败信息:{}]", e.getMessage());
        }
        latch.countDown();
    }
}
