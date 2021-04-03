package alipay.manage.api.deal;

import alipay.config.redis.RedisUtil;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.Fund;
import alipay.manage.bean.util.FundBean;
import alipay.manage.bean.util.OrderAndUserInfo;
import alipay.manage.service.OrderAppService;
import alipay.manage.service.UserInfoService;
import alipay.manage.service.WithdrawService;
import alipay.manage.util.CheckUtils;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.bean.dealpay.Withdraw;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FindOrderAndUserInfo {
    static final String appid_key = "appId";
    static final String sign_key = "sign";
    static final String appOrderId_key = "appOrderId";
    static final String type_key = "type";
    Logger log = LoggerFactory.getLogger(FindOrderAndUserInfo.class);
    @Autowired
    private OrderAppService orderAppServiceImpl;
    @Autowired
    private WithdrawService withdrawServiceImpl;
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Autowired
    private RedisUtil redis;

    public Result findFund(HttpServletRequest request) {
        String appId = request.getParameter("appId");
        String sign = request.getParameter(sign_key);
        if (StrUtil.isBlank(appId) || StrUtil.isBlank(sign)) {
            return Result.buildFailMessage("必传参数为空");
        }
        UserInfo userInfo = userInfoServiceImpl.findPassword(appId);
        if (ObjectUtil.isNull(userInfo)) {
            return Result.buildFailMessage("商户不存在");
        }
        Map<String, Object> map = new ConcurrentHashMap<String, Object>(5);
        map.put(appid_key, appId);
        map.put(sign_key, sign);
        boolean verifySign = CheckUtils.verifySign(map, userInfo.getPayPasword());
        if (!verifySign) {
            return Result.buildFailMessage("签名错误");
        }
        UserFund fund = userInfoServiceImpl.findBalace(appId);
        if (ObjectUtil.isNull(fund)) {
            log.info("【当前查询的商户号不存在，请核实，商户号为：" + appId + "】");
            return Result.buildFailMessage("当前查询的订单不存在，请核实");
        }
        Map<String, Object> mapr = new ConcurrentHashMap<String, Object>();
        mapr.put("userId", fund.getUserId());
        mapr.put("userName", fund.getUserName());
        mapr.put("balance", fund.getAccountBalance());
        String sign2 = CheckUtils.getSign(mapr, userInfo.getPayPasword());
        userInfo = null;
        mapr = null;
        Fund fundInfo = new Fund();
        fundInfo.setBalance(fund.getAccountBalance());
        fundInfo.setSign(sign2);
        fundInfo.setUserId(fund.getUserId());
        fundInfo.setUserName(fund.getUserName());
        return Result.buildSuccessResult(fundInfo);
    }

    public Result findOrder(HttpServletRequest request) {
        String appId = request.getParameter(appid_key);
        String appOrderId = request.getParameter(appOrderId_key);
        String type = request.getParameter(type_key);
        String sign = request.getParameter(sign_key);
        if (StrUtil.isBlank(type)) {
            if (StrUtil.isBlank(appId) || StrUtil.isBlank(appOrderId) || StrUtil.isBlank(sign)) {
                return Result.buildFailMessage("必传参数为空");
            }
            UserInfo userInfo = userInfoServiceImpl.findPassword(appId);
            if (ObjectUtil.isNull(userInfo)) {
                return Result.buildFailMessage("商户不存在");
            }
            Map<String, Object> map = new ConcurrentHashMap<String, Object>();
            if (StrUtil.isBlank(type)) {
                type = null;
            }
            map.put(appid_key, appId);
            map.put(appOrderId_key, appOrderId);
            map.put(sign, sign);
            boolean verifySign = CheckUtils.verifySign(map, userInfo.getPayPasword());
            map = null;
            if (!verifySign) {
                return Result.buildFailMessage("签名错误");
            }
            DealOrderApp orderApp = orderAppServiceImpl.findOrderByApp(appId, appOrderId);
            Map<String, Object> mapr = new ConcurrentHashMap<String, Object>();
            mapr.put(appid_key, appId);
            mapr.put(appOrderId_key, orderApp.getAppOrderId());
            mapr.put("amount", orderApp.getOrderAmount());
            mapr.put("orderStatus", orderApp.getOrderStatus());
            String sign2 = CheckUtils.getSign(mapr, userInfo.getPayPasword());
            userInfo = null;
            mapr = null;
            FundBean fund = new FundBean();
            fund.setAmount(orderApp.getOrderAmount().toString());
            fund.setOrderId(orderApp.getAppOrderId());
            fund.setOrderStatus(orderApp.getOrderStatus());
            fund.setSign(sign2);
            orderApp = null;
            return Result.buildSuccessResult(fund);
        }
        if ("pay".equals(type)) {
            if (StrUtil.isBlank(appId) || StrUtil.isBlank(appOrderId) || StrUtil.isBlank(sign)) {
                return Result.buildFailMessage("必传参数为空");
            }
            UserInfo userInfo = userInfoServiceImpl.findPassword(appId);//缓存已加
            if (ObjectUtil.isNull(userInfo)) {
                return Result.buildFailMessage("商户不存在");
            }
            Map<String, Object> map = new ConcurrentHashMap<String, Object>();
            if (StrUtil.isBlank(type)) {
                type = null;
            }
            map.put(appid_key, appId);
            map.put(appOrderId_key, appOrderId);
            map.put(type_key, type);
            map.put(sign_key, sign);
            boolean verifySign = CheckUtils.verifySign(map, userInfo.getPayPasword());
            map = null;
            if (!verifySign) {
                return Result.buildFailMessage("签名错误");
            }
            DealOrderApp orderApp = orderAppServiceImpl.findOrderByApp(appId, appOrderId);
            if (ObjectUtil.isNull(orderApp)) {
                log.info("【当前查询的订单不存在，请核实，订单号为：" + appOrderId + "】");
                return Result.buildFailMessage("当前查询的订单不存在，请核实");
            }
            Map<String, Object> mapr = new ConcurrentHashMap<String, Object>();
            mapr.put(appid_key, appId);
            mapr.put(appOrderId_key, orderApp.getAppOrderId());
            mapr.put("amount", orderApp.getOrderAmount());
            mapr.put("orderStatus", orderApp.getOrderStatus());
            String sign2 = CheckUtils.getSign(mapr, userInfo.getPayPasword());
            userInfo = null;
            mapr = null;
            FundBean fund = new FundBean();
            fund.setAmount(orderApp.getOrderAmount().toString());
            fund.setOrderId(orderApp.getAppOrderId());
            fund.setOrderStatus(orderApp.getOrderStatus());
            fund.setSign(sign2);
            orderApp = null;
            return Result.buildSuccessResult(fund);
        }
        if ("wit".equals(type)) {
            if (StrUtil.isBlank(appId) || StrUtil.isBlank(appOrderId) || StrUtil.isBlank(sign)) {
                return Result.buildFailMessage("必传参数为空");
            }
            UserInfo userInfo = userInfoServiceImpl.findPassword(appId);
            if (ObjectUtil.isNull(userInfo)) {
                return Result.buildFailMessage("商户不存在");
            }
            Map<String, Object> map = new ConcurrentHashMap<String, Object>();
            if (StrUtil.isBlank(type)) {
                type = null;
            }
            map.put(appid_key, appId);
            map.put(appOrderId_key, appOrderId);
            map.put(type_key, type);
            map.put(sign_key, sign);
            boolean verifySign = CheckUtils.verifySign(map, userInfo.getPayPasword());
            map = null;
            if (!verifySign) {
                return Result.buildFailMessage("签名错误");
            }
            log.info("【当前进入代付订单查询，订单号为：" + appOrderId + "】");
            Withdraw witb = withdrawServiceImpl.findOrderByApp(appId, appOrderId);
            if (ObjectUtil.isNull(witb)) {
                log.info("【当前查询的订单不存在，请核实，订单号为：" + appOrderId + "】");
                return Result.buildFailMessage("当前查询的订单不存在，请核实");
            }
            String clientIP = HttpUtil.getClientIP(request);
            if (StrUtil.isBlank(clientIP)) {
                return Result.buildFailMessage("未获取到代付查询ip");
            }
            Map<String, Object> mapr = new ConcurrentHashMap<String, Object>();
            String commect = StrUtil.isBlank(witb.getComment()) ? "代付订单" : witb.getComment();
            mapr.put(appid_key, appId);
            mapr.put(appOrderId_key, witb.getAppOrderId());
            mapr.put("amount", witb.getAmount());
            mapr.put("orderStatus", witb.getOrderStatus());
            //mapr.put("msg", commect);
            String sign2 = CheckUtils.getSign(mapr, userInfo.getPayPasword());
            userInfo = null;
            mapr = null;
            FundBean fund = new FundBean();
            fund.setAmount(witb.getAmount().toString());
            fund.setOrderId(witb.getAppOrderId());
            fund.setOrderStatus(witb.getOrderStatus());
            //fund.setMsg(commect);
            fund.setSign(sign2);
            witb = null;
            return Result.buildSuccessResult(fund);
        }
        return Result.buildFailMessage("查询失败");
    }

    public Result findOrderSum(HttpServletRequest request) {
        String appId = request.getParameter(appid_key);
        Object o = redis.get(sign_key + appid_key + appId);
        if (null != o) {
            return Result.buildFailMessage("查询次数超限，当前接口仅支持20秒一次的频率查询");
        }
        String sign = request.getParameter(sign_key);
        if (StrUtil.isBlank(appId) || StrUtil.isBlank(sign)) {
            return Result.buildFailMessage("必传参数为空");
        }
        UserInfo userInfo = userInfoServiceImpl.findPassword(appId);
        if (ObjectUtil.isNull(userInfo)) {
            return Result.buildFailMessage("商户不存在");
        }
        Map<String, Object> map = new ConcurrentHashMap<String, Object>(5);
        map.put(appid_key, appId);
        map.put(sign_key, sign);
        boolean verifySign = CheckUtils.verifySign(map, userInfo.getPayPasword());
        if (!verifySign) {
            return Result.buildFailMessage("签名错误");
        }
        UserFund fund = userInfoServiceImpl.findBalace(appId);
        if (ObjectUtil.isNull(fund)) {
            log.info("【当前查询的商户号不存在，请核实，商户号为：" + appId + "】");
            return Result.buildFailMessage("当前查询的订单不存在，请核实");
        }
        Withdraw witb = withdrawServiceImpl.findOrderByAppSum(appId);
        DealOrderApp orderApp = orderAppServiceImpl.findOrderByAppSum(appId);
        OrderAndUserInfo info = new OrderAndUserInfo();
        info.setUserAmount(fund.getAccountBalance().toString());
        info.setDealSum(orderApp.getOrderAmount().toString());
        info.setWitSum(witb.getAmount().toString());
        redis.set(sign_key + appid_key + appId, sign_key + appid_key + appId, 20);
        return Result.buildSuccessResult(info);
    }
}
