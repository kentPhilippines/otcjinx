package alipay.manage.api.deal;

import alipay.config.redis.RedisUtil;
import alipay.manage.api.V2.vo.BalanceRequestVo;
import alipay.manage.api.V2.vo.OrderRequestVo;
import alipay.manage.api.V2.vo.OrderResponseVo;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.Fund;
import alipay.manage.service.OrderAppService;
import alipay.manage.service.UserInfoService;
import alipay.manage.service.WithdrawService;
import alipay.manage.util.CheckUtils;
import cn.hutool.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.bean.dealpay.Withdraw;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class QueryV2Api {
    Logger log = LoggerFactory.getLogger(FindOrderAndUserInfo.class);
    @Autowired
    private OrderAppService orderAppServiceImpl;
    @Autowired
    private WithdrawService withdrawServiceImpl;
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Autowired
    private RedisUtil redis;

    public Result queryBalance(BalanceRequestVo requery) {
        UserInfo userInfo = userInfoServiceImpl.findPassword(requery.getUserId());
        if (ObjectUtil.isNull(userInfo)) {
            return Result.buildFailMessage("商户不存在");
        }
        Boolean sign = requery.isSign(userInfo.getPayPasword());
        if (!sign) {
            return Result.buildFailMessage("签名错误");
        }
        UserFund fund = userInfoServiceImpl.findBalace(requery.getUserId());
        if (ObjectUtil.isNull(fund)) {
            log.info("【当前查询的商户号不存在，请核实，商户号为：" + requery.getUserId() + "】");
            return Result.buildFailMessage("当前查询的账户不存在，请核实");
        }
        Map<String, Object> map = new ConcurrentHashMap<String, Object>();
        map.put("userId", fund.getUserId());
        map.put("userName", fund.getUserName());
        map.put("balance", fund.getAccountBalance());
        String sign2 = CheckUtils.getSign(map, userInfo.getPayPasword());
        Fund fundInfo = new Fund();
        fundInfo.setBalance(fund.getAccountBalance());
        fundInfo.setSign(sign2);
        fundInfo.setUserId(fund.getUserId());
        fundInfo.setUserName(fund.getUserName());
        return Result.buildSuccessResult(fundInfo);
    }
    String TYPE_WIT = "wit";
    String TYPE_PAY = "pay";
    public Result queryOrder(OrderRequestVo queryOrder) {
        UserInfo userInfo = userInfoServiceImpl.findPassword(queryOrder.getUserId());
        if (ObjectUtil.isNull(userInfo)) {
            return Result.buildFailMessage("商户不存在");
        }
        Boolean sign = queryOrder.isSign(userInfo.getPayPasword());
        if (!sign) {
            return Result.buildFailMessage("签名错误");
        }
        Object obj = null;
        if (queryOrder.getType().equals(TYPE_WIT)) {
            obj = withdrawServiceImpl.findOrderByApp(queryOrder.getUserId(), queryOrder.getOrderId());
        } else if (queryOrder.getType().equals(TYPE_PAY)) {
            obj = orderAppServiceImpl.findOrderByApp(queryOrder.getUserId(), queryOrder.getOrderId());
        }

        if (null == obj) {
            log.info("【当前查询的订单不存在，请核实，订单号为：" + queryOrder.getOrderId() + "】");
            return Result.buildFailMessage("当前查询的订单不存在，请核实");
        }
        String orderId = "";
        String orderNo = "";
        String userId = "";
        String amount = "";
        String orderStatus = "";
        if (obj instanceof Withdraw) {
            Withdraw wit = (Withdraw)obj;
            orderId = wit.getOrderId();
            orderNo = wit.getAppOrderId();
            userId = wit.getUserId();
            amount = wit.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            orderStatus = wit.getOrderStatus();
        } else if (obj instanceof DealOrderApp) {
            DealOrderApp app = (DealOrderApp) obj;
            orderId = app.getOrderId();
            orderNo = app.getAppOrderId();
            userId = app.getOrderAccount();
            amount = app.getOrderAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            orderStatus = app.getOrderStatus();
        }
        OrderResponseVo order = new OrderResponseVo();
        Map<String, Object> map = new ConcurrentHashMap<String, Object>();
        map.put("orderId", orderId);
        map.put("orderNo", orderNo);
        map.put("userId", userId);
        map.put("amount", amount);
        map.put("orderStatus", orderStatus);
        String sign2 = CheckUtils.getSign(map, userInfo.getPayPasword());
        order.setOrderId(orderId);
        order.setOrderNo(orderNo);
        order.setAmount(amount);
        order.setOrderStatus(orderStatus);
        order.setSign(sign2);
        order.setUserId(userId);
        return Result.buildSuccessResult(order);
    }


}
