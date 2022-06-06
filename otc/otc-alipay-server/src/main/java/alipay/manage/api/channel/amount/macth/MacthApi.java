package alipay.manage.api.channel.amount.macth;

import alipay.config.redis.RedisLockUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.service.OrderAppService;
import alipay.manage.service.OrderService;
import alipay.manage.service.WithdrawService;
import alipay.manage.util.NotifyUtil;
import alipay.manage.util.OrderUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.api.alipay.Common;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.exception.order.OrderException;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI+"/macth")
@RestController
public class MacthApi extends PayOrderService {
    public static final String ERROR_MSG = "当前订单暂无充值通道，请重新发起拉单";
    public static final String ERROR_MSG_1 = "当前订单暂无充值通道，更换以下金额：";
    @Autowired
    private OrderAppService orderAppService;
    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private OrderService orderServiceImpl;
    @Autowired
    private OrderUtil orderUtilImpl;
    @Autowired
    private WithdrawService withdrawServiceImpl;
    @Autowired
    private RedisLockUtil redisLockUtil;
    @Autowired
    NotifyUtil notifyUtil;
    /**
     * 1，根据传入的订单号获取会员的支付订单数据
     * 2，根据订单信息获取合适的代付订单进行匹配
     * 3，根据查询到的订单号对代付订单进行锁定，并且延长代付订单审核时间
     * 4，返回出款数据
     */

    @RequestMapping("/successOrder")
    public Result successOrder(String orderId,String ip ){
        log.info("收到成功回调通知，当前回滴订单号："+ orderId);
        if (StrUtil.isEmpty(orderId) || StrUtil.isEmpty(ip)) {
            return Result.buildFail();
        }
        DealOrder order = orderServiceImpl.findAssOrder(orderId);
        if(ObjectUtil.isNull(order)){
            return Result.buildFail();
        }
        Result result = witNotfy(order.getWitOrderId(), ip);
        if(result.isSuccess()){
            boolean w = withdrawService.macthLock(order.getWitOrderId(),2,0);//撮合未支付并锁定 订单
            Result aaa = dealpayNotfiy(order.getOrderId(), ip, "支付助手确认成功");
            if(aaa.isSuccess()){
                orderServiceImpl.macthLock(order.getOrderId(), 2);//撮合已支付
                return Result.buildSuccess();
            }
            return Result.buildFail();
        }
        return Result.buildSuccess();
    }
    @RequestMapping("/failOrder")
    public Result failOrder(String orderId,String msg){
        if (StrUtil.isEmpty(orderId)) {
            return Result.buildFail();
        }
        DealOrder order = orderServiceImpl.findAssOrder(orderId);
        String macth = order.getMacthMsg();
        macth = macth +" | " + msg;
        boolean macthMsg = orderServiceImpl.macthMsg(order.getOrderId(), macth);
        if(macthMsg){
            return Result.buildSuccess();
        }
        return Result.buildFail();
    }





    @RequestMapping("/putMacth")
    public Result putMacth(String orderId , String msg){
        if (StrUtil.isEmpty(orderId)) {
            return Result.buildFail();
        }
        DealOrder order = orderServiceImpl.findAssOrder(orderId);
        String macth = order.getMacthMsg();
        macth = macth +" | " + msg;
        boolean macthMsg = orderServiceImpl.macthMsg(order.getOrderId(),macth);//
        if(macthMsg){
            return Result.buildFailMessage("添加撮合描述失败，当前订单号："+order.getOrderId()+",当前描述为:"+msg);
        }
        return Result.buildSuccess();
    }

    @RequestMapping("/getBankWit")
    public Result getBankWit(String orderId) {
        if (StrUtil.isEmpty(orderId)) {
            return Result.buildFailMessage(ERROR_MSG);
        }
        DealOrderApp orderApp = orderAppService.findOrderByOrderId(orderId);
        BigDecimal orderAmount = orderApp.getOrderAmount();
        String orderAccount = orderApp.getOrderAccount();
        //1，根据传入的订单号获取会员的支付订单数据
        List<Withdraw> witList = withdrawService.findMacthOrder(orderAccount); // 获取规则： 1 不是当前 商户的， 2，  订单为非锁定状态， 3，订单主状态为 审核中  4 ， 最后一次撮合时间已经过了10分钟 且 订单 为挂起状态
        if (CollUtil.isEmpty(witList)) {
            return Result.buildFailMessage(ERROR_MSG);
        }
        for(Withdraw wit : witList){
            if(wit.getAmount().compareTo(orderAmount) == 0 ){//金额合适
                Result result = enterWit(orderApp, wit);
                if(result.isSuccess()){
                    return result;
                }
            }
        }
        //这里说明金额不合适 选择合适的金额返回给他们
        Comparator<Withdraw> comparator = new Comparator<Withdraw>() {
            @Override
            public int compare(Withdraw o1, Withdraw o2) {
                return o2.getAmount().compareTo(o1.getAmount()) ;
            }
        };

         BigDecimal lastnumber = BigDecimal.ZERO;//上一个金额
         BigDecimal number = BigDecimal.ZERO;//当前金额
        for(Withdraw wit : witList){
            if(wit.getAmount().compareTo(orderAmount) == -1 ){// 当前金额小于  对当前金额金额记录
                lastnumber = wit.getAmount();
            }
            if(wit.getAmount().compareTo(orderAmount) == 1 ){// 当前金额大于  对当前金额金额记录
                number = wit.getAmount();
            }
            if(lastnumber.compareTo( BigDecimal.ZERO) != 0  && number.compareTo( BigDecimal.ZERO) != 0){
                //当前选出的金额是合适的
                return Result.buildFailMessage(ERROR_MSG_1+lastnumber + "，"+number +"，再次发起充值");
            }
        }
        return Result.buildFailMessage(ERROR_MSG);

    }


    @Transactional
    Result enterWit(DealOrderApp orderApp, Withdraw withdraw){
        //确认订单之后 要标记 入款主交易订单 要 锁定出款订单
        //入款主交易订单 标记为        撮合未结算      并不断持续填充撮合说明
        //代付订单 标记为          锁定撮合 macthStatus  = 1  撮合状态标记为 撮合未支付 ; macthLock  = 1 订单锁定 当前 除非解锁  不然不可做任何操作
        String macthWit = create(orderApp, "渠道id待定");
        boolean b = orderServiceImpl.updateBankInfoByOrderId(orderApp.getDealDescribe() + " 收款信息：" + withdraw.getAccname() + ":" + withdraw.getBankName() + ":" + withdraw.getBankNo()+":撮合", macthWit);
       // 添加代付订单号和  主交易订单的绑定
        boolean mac =  orderServiceImpl.setMacthOrderId(macthWit,withdraw.getOrderId());
        Integer macthStatus = 1 ;
        Integer macthLock = 1 ;
        boolean w = withdrawService.macthLock(withdraw.getOrderId(),macthStatus,macthLock);//撮合未支付并锁定 订单
        boolean push = withdrawService.macthCountPush(withdraw.getOrderId());
        boolean J = orderServiceImpl.macthLock(macthWit,macthStatus);//撮合未支付
        String macth = withdraw.getMacthMsg();
        String msg = "订单："+orderApp.getOrderId()+"撮合，订单："+withdraw.getOrderId()+ "，银行卡信息："+  " 收款信息：" + withdraw.getAccname() + ":" + withdraw.getBankName() + ":" + withdraw.getBankNo()+":撮合时间："+ DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_PATTERN);
        boolean macthMsg = orderServiceImpl.macthMsg(macthWit,macth + msg);//撮合未支付
        if(b&&J&&w&&mac){
            BankWitBean witBean = new BankWitBean();
            witBean.setAmount(withdraw.getAmount()+"");
            witBean.setBankAccount(withdraw.getAccname());
            witBean.setOrderId(orderApp.getOrderId());
            witBean.setBankName(withdraw.getBankName());
            witBean.setBankNo(withdraw.getBankNo());
            return Result.buildSuccessResult(witBean);
        }else{
            throw new OrderException("撮合异常", null);
        }
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


}
