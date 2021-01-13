package alipay.manage.api;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.api.alipay.Common;
import otc.result.Result;
import otc.util.RSAUtils;
import otc.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class VendorRequestApi {
    Logger log = LoggerFactory.getLogger(VendorRequestApi.class);
    @Autowired
    private AccountApiService accountApiServiceImpl;
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Autowired
    private ExceptionOrderService exceptionOrderServiceImpl;

    /**
     * 商户下单支付请求接口
     *
     * @param request
     * @return
     */
    public Result pay(HttpServletRequest request) {
        String userId = request.getParameter("userId");//商户号
        //根据商户号查询商户实体
        UserInfo userInfo = accountApiServiceImpl.findUserInfo(userId);
        if (userInfo == null) {
            return Result.buildFailMessage("商户不存在");
        }
        log.info("--------------【用户开始RSA解密】----------------");
        String rsaSign = request.getParameter("cipherText");//商户传过来的密文
        log.info("【报文：" + rsaSign + "】");
        Map<String, Object> paramMap = RSAUtils.getDecodePrivateKey(rsaSign, userInfo.getPrivateKey());
        log.info("【商户RSA解密的参数：" + paramMap.toString() + "】 ");
        //验证结果
        Result result = CheckUtils.requestVerify(request, paramMap, userInfo.getPayPasword());
        if (result.isSuccess()) {
            log.info("【requestVerif】方法验证通过");
        } else {
            ThreadUtil.execute(() -> {
                Object appId = paramMap.get("appId");
                if (ObjectUtil.isNull(appId)) {
                    appId = "订单号为获取到";
                }
                Object amount = paramMap.get("amount");
                if (ObjectUtil.isNull(amount)) {
                    amount = "金额未获取到";
                }
                exceptionOrderServiceImpl.addDealEx(
                        appId.toString(),
                        amount.toString(),
                        result.getMessage(), HttpUtil.getClientIP(request), paramMap.get("orderId").toString());

            });
            return result;
        }
        //验证商户是否配置费率
        Integer remitOrderState = userInfo.getReceiveOrderState();// 1 接单 2 暂停接单
        if (Common.Order.DEAL_OFF.equals(remitOrderState)) {
            log.info("【当前账户交易权限未开通】");
            ThreadUtil.execute(() -> {
                exceptionOrderServiceImpl.addDealEx(
                        paramMap.get("appId").toString(),
                        paramMap.get("amount").toString(),
                        "商户相应提示：当前账户交易权限未开通；处理方法：检查商户交易权限或商户状态是否开启,",
                        HttpUtil.getClientIP(request), paramMap.get("orderId").toString());
            });
            return Result.buildFailMessage("当前账户交易权限未开通");
        }
        String passCode = paramMap.get("passCode").toString();
        //后期优化从缓存读取数据
        UserRate userRate = null;
        try {
            userRate = accountApiServiceImpl.findUserRateByUserId(userId, passCode, paramMap.get("amount").toString());
        } catch (Exception e) {
            ThreadUtil.execute(() -> {
                exceptionOrderServiceImpl.addDealEx(
                        paramMap.get("appId").toString(),
                        paramMap.get("amount").toString(),
                        "商户相应提示：当前通道编码设置有误，产品类型设置重复；" +
                                "处理方法：请反复确认当前商户传入通道编号是否和商户开启的通道编号相匹配，当前商户传入通道编码：" + passCode + "," +
                                "，请确认商户相同产品下是否有同时开启2个通道",
                        HttpUtil.getClientIP(request), paramMap.get("orderId").toString());
            });
            log.info("【当前通道编码设置有误，产品类型设置重复：" + e.getMessage() + "】");
            return Result.buildFailMessage("当前通道编码设置有误，产品类型设置重复");
        }
        if (userRate == null) {
            log.info("--------------【 商户交易费率未配置或未开通】----------------");
            ThreadUtil.execute(() -> {
                exceptionOrderServiceImpl.addDealEx(
                        paramMap.get("appId").toString(),
                        paramMap.get("amount").toString(),
                        "商户相应提示：当前通道编码设置有误；" +
                                "处理方法：请反复确认当前商户传入通道编号是否和商户开启的通道编号相匹配，当前商户传入通道编码：" + passCode + "," +
                                "，请检查商户通道编码是否正确",
                        HttpUtil.getClientIP(request), paramMap.get("orderId").toString());
            });
            return Result.buildFailMessage("商户交易费率未配置或未开通");
        }
        log.info("--------------【验证是否有时间限制】----------------");
        String start = userInfo.getStartTime();
        String end = userInfo.getEndTime();
        if (StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(end)) {
            log.info("--------------【未设置时间风控】----------------");
            boolean isTime = CheckUtils.verifyTimeZone(new Date(), userInfo.getStartTime(), userInfo.getEndTime());
            if (isTime) {//在时间内
                return Result.buildFailResult("在风控时间段不允许下单");
            }
        }
        log.info("--------------【验证单笔交易金额】----------------");
        Double orderAmount = Double.valueOf(paramMap.get("amount").toString());//商户请求单笔金额
        if (StringUtils.isNotEmpty(userInfo.getMinAmount()) && StringUtils.isNotEmpty(userInfo.getMaxAmount())) {
            if (orderAmount <= Double.parseDouble(userInfo.getMinAmount()) || orderAmount >= Double.parseDouble(userInfo.getMaxAmount())) {
                ThreadUtil.execute(() -> {
                    exceptionOrderServiceImpl.addDealEx(
                            paramMap.get("appId").toString(),
                            paramMap.get("amount").toString(),
                            "商户相应提示：单笔交易金额不在区间范围内；" +
                                    "处理方法：提示商户调整交易金额后重新发起订单，当前商户传入通道编码：" + passCode,
                            HttpUtil.getClientIP(request), paramMap.get("orderId").toString());
                });
                return Result.buildFailMessage("单笔交易金额不在区间范围内");
            }
        }

        UserFund userFund = accountApiServiceImpl.findUserFundByUserId(userId);
        log.info("--------------【验证单日成功下单次数】----------------");
        if (!"".equals(userInfo.getTimesTotal()) && null != userInfo.getTimesTotal()) {
            if (userFund.getTodayOrderCount() > userInfo.getTimesTotal()) {
                return Result.buildFailMessage("今日下单次数已受限");
            }
        }
        BigDecimal totalAmount = userInfo.getTotalAmount();
        if (null != totalAmount) {
            if (userFund.getAccountBalance().compareTo(userInfo.getTotalAmount()) > -1) {
                log.info("--------------【当前交易金额以受限制】----------------");
                ThreadUtil.execute(() -> {
                    exceptionOrderServiceImpl.addDealEx(
                            paramMap.get("appId").toString(),
                            paramMap.get("amount").toString(),
                            "商户相应提示：当前留存过多，请及时下发；" +
                                    "处理方法：提示商户及时下发，当前商户余额：" + userFund.getAccountBalance()
                                    + "，受限额度：" + userInfo.getTotalAmount() + "，当前商户传入通道编码：" + passCode,
                            HttpUtil.getClientIP(request), paramMap.get("orderId").toString());
                });
                return Result.buildFailMessage("当前留存过多，请及时下发");
            }
        }
        return Result.buildSuccessResult(paramMap);
    }

    /**
     * <p>商户下单提现接口</p>
     *
     * @param request 请求
     * @param flag    是否为商户后台提现			true  是   false  否
     * @return
     */
    public Result withdrawal(HttpServletRequest request, boolean flag) {
        String userId = request.getParameter("userId");//商户号
        UserInfo userInfo = accountApiServiceImpl.findUserInfo(userId);
        if (userInfo == null) {
            return Result.buildFailMessage("商户不存在");
        }
        log.info("--------------【用户开始RSA解密】----------------");
        String rsaSign = request.getParameter("cipherText");//商户传过来的密文
        log.info("【获取参数为：" + rsaSign + "】");
        log.info("【获取商户为：" + userId + "】");
        log.info("【获取商户解密秘钥为：" + userInfo.getPrivateKey() + "】");
        Map<String, Object> paramMap = RSAUtils.getDecodePrivateKey(rsaSign, userInfo.getPrivateKey());
        log.info("【商户RSA解密的参数：" + paramMap.toString() + "】 ");
        if (CollUtil.isEmpty(paramMap)) {
            return Result.buildFailMessage("RSA解密参数为空");
        }
        Result result = CheckUtils.requestWithdrawalVerify(request, paramMap, userInfo.getPayPasword());
        if (result.isSuccess()) {
            log.info("【requestVerif】方法验证通过");
        } else {
            ThreadUtil.execute(() -> {
                Object apporderid = paramMap.get("apporderid");
                if (ObjectUtil.isNull(apporderid)) {
                    apporderid = "订单号未获取到";
                }
                Object amount = paramMap.get("amount");
                if (ObjectUtil.isNull(amount)) {
                    amount = "金额未获取到";
                }
                exceptionOrderServiceImpl.addWitEx(
                        userId.toString(),
                        amount.toString(),
                        result.getMessage(), HttpUtil.getClientIP(request), apporderid.toString());
            });
            return result;
        }

        UserRate userRate = accountApiServiceImpl.findUserRateWitByUserId(userId);
        if (ObjectUtil.isNull(userRate)) {
            ThreadUtil.execute(() -> {
                exceptionOrderServiceImpl.addWitEx(
                        userId,
                        paramMap.get("amount").toString(),
                        "商户相应提示：代付费率为开通或状态未开启；" +
                                "处理方法：请重点检查商户代付费率是否配置开启",
                        HttpUtil.getClientIP(request), paramMap.get("apporderid").toString());
            });
            return Result.buildFailMessage("代付费率为开通或状态未开启");
        }

        Integer remitOrderState = userInfo.getRemitOrderState();// 1 接单 2 暂停接单
        if (Common.Order.DAPY_OFF.equals(remitOrderState)) {
            log.info("【当前账户代付权限未开通】");
            ThreadUtil.execute(() -> {
                exceptionOrderServiceImpl.addWitEx(
                        userId,
                        paramMap.get("amount").toString(),
                        "商户相应提示：当前账户代付权限未开通；" +
                                "处理方法：请重点检查商户代付状态是否开启",
                        HttpUtil.getClientIP(request), paramMap.get("apporderid").toString());
            });
            return Result.buildFailMessage("当前账户代付权限未开通");
        }

        Integer switchs = userInfo.getSwitchs();// 1 接单 2 暂停接单
        if (Common.Order.OFF.equals(switchs)) {
            log.info("【当前账户代付权限未开通】");
            ThreadUtil.execute(() -> {
                exceptionOrderServiceImpl.addWitEx(
                        userId,
                        paramMap.get("amount").toString(),
                        "商户相应提示：当前账户代付权限未开通；" +
                                "处理方法：请开启账户总开关",
                        HttpUtil.getClientIP(request), paramMap.get("apporderid").toString());
            });
            return Result.buildFailMessage("当前账户代付权限未开通");
        }
        if (!flag) {
            String clientIP = HttpUtil.getClientIP(request);
            if (StrUtil.isBlank(clientIP)) {
                return Result.buildFailMessage("未获取到代付ip");
            }
            log.info("【当前用户代付ip为：" + clientIP + "】");
            String witip = userInfo.getWitip();
            log.info("【获取当前允许代付ip为：" + witip + "】");
            String[] split = witip.split(",");
            List<String> asList = Arrays.asList(split);
            if (!asList.contains(clientIP)) {
                ThreadUtil.execute(() -> {
                    exceptionOrderServiceImpl.addWitEx(
                            userId,
                            paramMap.get("amount").toString(),
                            "商户相应提示：请绑定正确的代付ip；" +
                                    "处理方法：当前商户代付ip存疑，请要求商户仔细核对；当前商户代付ip：" + clientIP + "，我方登记ip：" + asList.toString(),
                            HttpUtil.getClientIP(request), paramMap.get("apporderid").toString());
                });
                return Result.buildFailMessage("请绑定正确的代付ip");
            }

        } else {
            log.info("【商户从后台提现不验证代付ip】");
        }
        String bankNo = paramMap.get("acctno").toString();
        String amount = paramMap.get("amount").toString();
        /*
        BankList bank = bankListServiceImpl.findBankByNo(bankNo);
        BigDecimal limitAmount = bank.getLimitAmount();
        BigDecimal bankAmount = bank.getBankAmount();
        BigDecimal add = bankAmount.add(new BigDecimal(amount));
        if (limitAmount.compareTo(add) == -1) {
            log.info("【当前银行卡超出当日可用金额】");
            return Result.buildFailMessage("当前银行卡超出当日可用金额");
        }
        */
        UserFund userFund = userInfoServiceImpl.fundUserFundAccounrBalace(userId);
        BigDecimal accountBalance = userFund.getAccountBalance();
        BigDecimal quota = userFund.getQuota();
        accountBalance = accountBalance.add(quota);
        if (accountBalance.compareTo(new BigDecimal(amount).add(userRate.getFee())) == -1) {
            log.info("【当前账户金额不足】");
            ThreadUtil.execute(() -> {
                exceptionOrderServiceImpl.addWitEx(
                        userId,
                        paramMap.get("amount").toString(),
                        "商户相应提示：当前账户金额不足；" +
                                "处理方法：请检查商户金额是否足够，或者要求商户更换金额提交",
                        HttpUtil.getClientIP(request), paramMap.get("apporderid").toString());
            });
            return Result.buildFailMessage("当前账户金额不足");
        }
        BigDecimal money = new BigDecimal(userRate.getRetain2());
        if (!(money.compareTo(new BigDecimal(amount)) == -1)) {
            log.info("【当前代付最低金额为+" + userRate.getRetain2() + "】");
            ThreadUtil.execute(() -> {
                exceptionOrderServiceImpl.addWitEx(
                        userId,
                        paramMap.get("amount").toString(),
                        "商户相应提示：当前代付最低金额为；" + userRate.getRetain2() +
                                "处理方法：金额限制为" + userRate.getRetain2(),
                        HttpUtil.getClientIP(request), paramMap.get("apporderid").toString());
            });
            return Result.buildFailMessage("当前代付最低金额为300");
        }
        BigDecimal higMoney = new BigDecimal("49999");
        if (!(higMoney.compareTo(new BigDecimal(amount)) > -1)) {
            log.info("【当前代付最高金额为50000】");
            ThreadUtil.execute(() -> {
                exceptionOrderServiceImpl.addWitEx(
                        userId,
                        paramMap.get("amount").toString(),
                        "商户相应提示：当前代付最高金额为50000；" +
                                "处理方法：金额限制为300-49999",
                        HttpUtil.getClientIP(request), paramMap.get("apporderid").toString());
            });
            return Result.buildFailMessage("金额限制为300-49999");
        }
        if (CheckUtils.isNumber(amount)) {
            log.info("【代付金额不能存在小数】");
            ThreadUtil.execute(() -> {
                exceptionOrderServiceImpl.addWitEx(
                        userId,
                        paramMap.get("amount").toString(),
                        "商户相应提示：代付金额不能存在小数；" +
                                "处理方法：提醒商户更换代付金额",
                        HttpUtil.getClientIP(request), paramMap.get("apporderid").toString());
            });
            return Result.buildFailMessage("代付金额不能存在小数");
        }


        return Result.buildSuccessResult(paramMap);
    }

    /**
     * <p>核对订单</p>
     *
     * @param request
     * @param
     * @return
     */
    public Result checkOrderl(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        UserInfo userInfo = accountApiServiceImpl.findUserInfo(userId);
        if (userInfo == null) {
            return Result.buildFailMessage("商户不存在");
        }
        log.info("--------------【用户开始RSA解密】----------------");
        String rsaSign = request.getParameter("cipherText");//商户传过来的密文
        log.info("【获取参数为：" + rsaSign + "】");
        log.info("【获取商户为：" + userId + "】");
        log.info("【获取商户解密秘钥为：" + userInfo.getPrivateKey() + "】");
        Map<String, Object> paramMap = RSAUtils.getDecodePrivateKey(rsaSign, userInfo.getPrivateKey());
        log.info("【商户RSA解密的参数：" + paramMap.toString() + "】 ");
        if (CollUtil.isEmpty(paramMap)) {
            return Result.buildFailMessage("RSA解密参数为空");
        }
        Result result = CheckUtils.requestWithdrawalVerify(request, paramMap, userInfo.getPayPasword());
        if (result.isSuccess()) {
            log.info("【requestVerif】方法验证通过");
        } else {
            return result;
        }
        return Result.buildSuccessResult(paramMap);
    }
}
