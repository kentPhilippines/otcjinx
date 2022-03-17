package alipay.manage.api;

import alipay.config.redis.RedisUtil;
import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.UserRate;
import alipay.manage.service.ExceptionOrderService;
import alipay.manage.service.UserInfoService;
import alipay.manage.util.CheckUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.api.alipay.Common;
import otc.result.Result;
import otc.util.RSAUtils;
import otc.util.enums.UserStatusEnum;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class VendorRequestApi {
    static Logger log = LoggerFactory.getLogger(VendorRequestApi.class);
    @Autowired
    private AccountApiService accountApiServiceImpl;
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Autowired
    private ExceptionOrderService exceptionOrderServiceImpl;

    /**
     * <p>商户下单提现接口</p>
     *
     * @param request 请求
     * @param flag    是否为商户后台提现			true  是   false  否
     * @return
     */

    static BigDecimal higMoney = new BigDecimal("10000000");

    public static String getIpAddress(HttpServletRequest request, String userId) {
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");
        log.info("【获取用户IP地址为：" + XFor + ",商户账号：" + userId + "】");
        if (null == XFor) {
            log.info("【重新获取用户IP地址为：" + HttpUtil.getClientIP(request) + ",商户账号：" + userId + "】");
            return HttpUtil.getClientIP(request);
        }
        if (!StrUtil.isEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (null == XFor) {
                log.info("【重新获取用户IP地址为：" + HttpUtil.getClientIP(request) + ",商户账号：" + userId + "】");
                return HttpUtil.getClientIP(request);
            } else {
                String[] split = XFor.split(",");
                return split[split.length - 1];
            }
        }
        XFor = Xip;
        if (!StrUtil.isEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            return XFor;
        }
        if (StrUtil.isEmpty(XFor.trim()) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("Proxy-Client-IP");
        }
        if (StrUtil.isEmpty(XFor.trim()) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StrUtil.isEmpty(XFor.trim()) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StrUtil.isEmpty(XFor.trim()) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StrUtil.isEmpty(XFor.trim()) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getRemoteAddr();
        }
        return XFor;
    }

    /**
     * 商户下单支付请求接口
     *
     * @param request
     * @return
     */
    public Result pay(HttpServletRequest request) {
        String userId = request.getParameter("userId");//商户号
        log.info("reuserid:{}",userId);
        //根据商户号查询商户实体
        UserInfo userInfo = accountApiServiceImpl.findClick(userId);
        UserInfo key = accountApiServiceImpl.findPrivateKey(userId);
        log.info("userinfo:{},key:{},",JSONUtil.toJsonStr(userInfo),JSONUtil.toJsonStr(key));
        if (null == userInfo || null == key) {
            return Result.buildFailMessage("商户不存在");
        }
        log.info("--------------【用户开始RSA解密】----------------");
        String rsaSign = request.getParameter("cipherText");//商户传过来的密文
        log.info("【报文：" + rsaSign + "】");
        Map<String, Object> paramMap = RSAUtils.getDecodePrivateKey(rsaSign, key.getPrivateKey());
        log.info("【商户RSA解密的参数：" + paramMap.toString() + "】 ");
        log.info("keyobj:{}", JSONUtil.toJsonStr(key));
        //验证结果
        Result result = CheckUtils.requestVerify(request, paramMap, key.getPayPasword());
        log.info("result:{}",result);
        if (!result.isSuccess()) {
            return result;
        }
        //验证商户是否配置费率
        Integer remitOrderState = userInfo.getReceiveOrderState();// 1 接单 2 暂停接单
        log.info("remitOrderState:{}",remitOrderState);
        if (Common.Order.DEAL_OFF.equals(remitOrderState)) {
            ThreadUtil.execute(() -> {
                exceptionOrderServiceImpl.addDealEx(paramMap.get("appId").toString(), paramMap.get("amount").toString(), "商户相应提示：当前账户交易权限未开通；处理方法：检查商户交易权限或商户状态是否开启,", HttpUtil.getClientIP(request), paramMap.get("orderId").toString());
            });
            return Result.buildFailMessage("当前账户交易权限未开通");
        }
        String passCode = paramMap.get("passCode").toString();
        UserFund userFund = accountApiServiceImpl.findUserFundByUserId(userId);
        log.info("userFund:{}",JSONUtil.toJsonStr(userFund));
        BigDecimal totalAmount = userInfo.getTotalAmount();
        if (null != totalAmount) {
            if (userFund.getAccountBalance().compareTo(userInfo.getTotalAmount()) > -1) {
                BigDecimal accountBalance = userFund.getAccountBalance();
                ThreadUtil.execute(() -> {
                    exceptionOrderServiceImpl.addDealEx(paramMap.get("appId").toString(), paramMap.get("amount").toString(), "商户相应提示：当前留存过多，请及时下发；" + "处理方法：提示商户及时下发，当前商户余额：" + accountBalance + "，受限额度：" + userInfo.getTotalAmount() + "，当前商户传入通道编码：" + passCode, HttpUtil.getClientIP(request), paramMap.get("orderId").toString());
                });
                return Result.buildFailMessage("当前留存过多，请及时下发");
            }
        }
        totalAmount = null;
        userFund = null;
        return Result.buildSuccessResult(paramMap);
    }
    @Autowired  private RedisUtil redis;
    public Result withdrawal(HttpServletRequest request, boolean flag) {
        String userId = request.getParameter("userId");//商户号
        UserInfo userInfo = accountApiServiceImpl.findClick(userId);
        UserInfo key = accountApiServiceImpl.findPrivateKey(userId);
        if (null == userInfo || null == key) {
            return Result.buildFailMessage("商户不存在");
        }
        userInfo.setPayPasword(key.getPayPasword());
        log.info("--------------【用户开始RSA解密】----------------");
        String rsaSign = request.getParameter("cipherText");//商户传过来的密文
        log.info("【获取参数为：" + rsaSign + "】");
        Map<String, Object> paramMap = RSAUtils.getDecodePrivateKey(rsaSign, key.getPrivateKey());
        log.info("【商户RSA解密的参数：" + paramMap.toString() + "】 ");
        if (CollUtil.isEmpty(paramMap)) {
            return Result.buildFailMessage("RSA解密参数为空");
        }
        Result result = CheckUtils.requestWithdrawalVerify(request, paramMap, key.getPayPasword());
        if (!result.isSuccess()) {
            return result;
        }
        UserRate userRate = accountApiServiceImpl.findUserRateWitByUserId(userId, paramMap.get("amount").toString());
        if (ObjectUtil.isNull(userRate)) {
            ThreadUtil.execute(() -> {
                exceptionOrderServiceImpl.addWitEx(userId, paramMap.get("amount").toString(), "商户相应提示：代付费率为开通或状态未开启；" + "处理方法：请重点检查商户代付费率是否配置开启", HttpUtil.getClientIP(request), paramMap.get("apporderid").toString());
            });
            return Result.buildFailMessage("代付费率为开通或状态未开启");
        }
        Integer remitOrderState = userInfo.getRemitOrderState();// 1 接单 2 暂停接单
        if (Common.Order.DAPY_OFF.equals(remitOrderState)) {
            log.info("【当前账户代付权限未开通】");
            ThreadUtil.execute(() -> {
                exceptionOrderServiceImpl.addWitEx(userId, paramMap.get("amount").toString(), "商户相应提示：当前账户代付权限未开通；" + "处理方法：请重点检查商户代付状态是否开启", HttpUtil.getClientIP(request), paramMap.get("apporderid").toString());
            });
            return Result.buildFailMessage("当前账户代付权限未开通");
        }
        Integer switchs = userInfo.getSwitchs();// 1 接单 2 暂停接单
        if (Common.Order.OFF.equals(switchs)) {
            log.info("【当前账户代付权限未开通】");
            ThreadUtil.execute(() -> {
                exceptionOrderServiceImpl.addWitEx(userId, paramMap.get("amount").toString(), "商户相应提示：当前账户代付权限未开通；" + "处理方法：请开启账户总开关", HttpUtil.getClientIP(request), paramMap.get("apporderid").toString());
            });
            return Result.buildFailMessage("当前账户代付权限未开通");
        }
        if (!flag) {

            String acctno = paramMap.get("acctno").toString();//银行卡号
            String appid = paramMap.get("appid").toString();//商户号
            Object wi = redis.get(acctno + appid + paramMap.get("amount").toString());
            if(null != wi){
                log.info("当值当前商户重复提交，限制key = "+acctno + appid + paramMap.get("amount").toString());
                return Result.buildFailMessage("限制提交");
            }
             redis.set(acctno + appid + paramMap.get("amount").toString(),acctno + appid + paramMap.get("amount").toString(),300);
            String clientIP = getIpAddress(request, userInfo.getUserId());
            if (StrUtil.isBlank(clientIP)) {
                clientIP = HttpUtil.getClientIP(request);
                if (null == clientIP) {
                    return Result.buildFailMessage("未获取到代付ip");
                }
            }
            if (StrUtil.isBlank(StrUtil.trim(clientIP))) {
                clientIP = HttpUtil.getClientIP(request);
            }
            clientIP = StrUtil.trim(clientIP);
            log.info("【当前用户代付ip为：" + clientIP + "】");
            String witip = userInfo.getWitip();
            log.info("【获取当前允许代付ip为：" + witip + "】");
            String[] split = witip.split(",");
            List<String> asList = Arrays.asList(split);
            if ((!asList.contains(StrUtil.trim(clientIP)))) {
                String finalClientIP = StrUtil.trim(clientIP);
                ThreadUtil.execute(() -> {
                    exceptionOrderServiceImpl.addWitEx(userId, paramMap.get("amount").toString(), "商户相应提示：请绑定正确的代付ip；" + "处理方法：当前商户代付ip存疑，请要求商户仔细核对；当前商户代付ip：" + finalClientIP + "，我方登记ip：" + asList.toString(), finalClientIP, paramMap.get("apporderid").toString());
                });
                return Result.buildFailMessage("请绑定正确的代付ip");
            }
            UserStatusEnum userStatusEnum = UserStatusEnum.resolve(UserStatusEnum.CLOSE.getCode());
            if (!userStatusEnum.matches(userInfo.getEnterWitOpen())) {
                String amount = paramMap.get("amount").toString();
                Result result1 = CheckUtils.enterWit(userInfo, paramMap.get("apporderid").toString(), Integer.valueOf(new BigDecimal(amount).intValue()));
                if (!result1.isSuccess()) {
                    log.info(result1.getMessage());
                    ThreadUtil.execute(() -> {
                        exceptionOrderServiceImpl.addWitEx(userId, paramMap.get("amount").toString(), "商户相应提示：" + result1.getMessage() + "；" + "处理方法：联系技术", HttpUtil.getClientIP(request), paramMap.get("apporderid").toString());
                    });
                    closeObject(amount, switchs, key, paramMap, userId, userInfo);
                    return Result.buildFailMessage(result1.getMessage());
                }
            }
        } else {
            String acctno = paramMap.get("acctno").toString();//银行卡号
            String appid = paramMap.get("appid").toString();//商户号
            Object wi = redis.get(acctno + appid + paramMap.get("amount").toString());
            if(null != wi){
                log.info("当值当前商户重复提交，限制key = "+acctno + appid + paramMap.get("amount").toString());
                return Result.buildFailMessage("限制提交");
            }
            redis.set(acctno + appid + paramMap.get("amount").toString(),acctno + appid + paramMap.get("amount").toString(),300);
        }
        String amount1 = paramMap.get("amount").toString();
        BigDecimal witAmount = new BigDecimal(amount1);
        /*
        UserFund userFund = userInfoServiceImpl.fundUserFundAccounrBalace(userId);
        BigDecimal accountBalance = userFund.getAccountBalance();
        BigDecimal quota = userFund.getQuota();
        accountBalance = accountBalance.add(quota);
        if (accountBalance.compareTo(witAmount.add(userRate.getFee())) == -1) {
            ThreadUtil.execute(() -> {
                exceptionOrderServiceImpl.addWitEx(userId, paramMap.get("amount").toString(), "商户相应提示：当前账户金额不足；" + "处理方法：请检查商户金额是否足够，或者要求商户更换金额提交", HttpUtil.getClientIP(request), paramMap.get("apporderid").toString());
            });
            closeObject(witAmount, accountBalance, quota, amount1, userFund, amount1, switchs, key, paramMap, userId, userInfo);
            return Result.buildFailMessage("当前账户金额不足");
        }*/
        BigDecimal money = new BigDecimal(userRate.getRetain2());
        if (!(money.compareTo(witAmount) == -1)) {
            log.info("【当前代付最低金额为+" + userRate.getRetain2() + "】");
            BigDecimal finalWitAmount = witAmount;
            UserRate finalUserRate = userRate;
            ThreadUtil.execute(() -> {
                exceptionOrderServiceImpl.addWitEx(userId, finalWitAmount.toString(), "商户相应提示：当前代付最低金额为；" + finalUserRate.getRetain2() + "处理方法：金额限制为" + finalUserRate.getRetain2(), HttpUtil.getClientIP(request), paramMap.get("apporderid").toString());
            });
            closeObject(witAmount, money, amount1, amount1, switchs, key, paramMap, userId, userInfo, finalWitAmount, finalUserRate);
            return Result.buildFailMessage("当前代付最低金额为300");
        }
        if (!(higMoney.compareTo(witAmount) > -1)) {
            log.info("【当前代付最高金额为50000】");
            BigDecimal finalWitAmount1 = witAmount;
            ThreadUtil.execute(() -> {
                exceptionOrderServiceImpl.addWitEx(userId, finalWitAmount1.toString(), "商户相应提示：当前代付最高金额为50000；" + "处理方法：金额限制为300-49999", HttpUtil.getClientIP(request), paramMap.get("apporderid").toString());
            });
            closeObject(witAmount, money, amount1, amount1, switchs, key, paramMap, userId, userInfo);
            return Result.buildFailMessage("金额限制为300-49999");
        }
        if (CheckUtils.isNumber(witAmount)) {
            ThreadUtil.execute(() -> {
                exceptionOrderServiceImpl.addWitEx(userId, paramMap.get("amount").toString(), "商户相应提示：代付金额不能存在小数；" + "处理方法：提醒商户更换代付金额", HttpUtil.getClientIP(request), paramMap.get("apporderid").toString());
            });
            closeObject(witAmount, money, amount1, amount1, switchs, key, paramMap, userId, userInfo);
            return Result.buildFailMessage("代付金额不能存在小数");
        }
        witAmount = null;
        userInfo = null;
        userRate = null;
        return Result.buildSuccessResult(paramMap);
    }


    /**
     * 关闭对象
     *
     * @param obj
     */
    void closeObject(Object... obj) {
        for (Object ob : obj) {
            ob = null;
        }
        obj = null;
    }
}
