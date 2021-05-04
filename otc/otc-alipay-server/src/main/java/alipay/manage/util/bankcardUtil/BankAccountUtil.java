package alipay.manage.util.bankcardUtil;


import alipay.manage.bean.*;
import alipay.manage.service.MediumService;
import alipay.manage.service.RunOrderService;
import alipay.manage.service.UserInfoService;
import alipay.manage.service.UserRateService;
import alipay.manage.util.amount.AmountPublic;
import alipay.manage.util.amount.AmountRunUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.api.alipay.Common;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.List;

/**
 * 卡商订单   交易结算
 */

@Component
public class BankAccountUtil {
    Logger log = LoggerFactory.getLogger(BankAccountUtil.class);
    @Autowired
    private UserRateService userRateServiceImpl;
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Autowired
    private AmountPublic amountPublic;
    @Autowired
    private MediumService mediumServiceImpl;
    @Autowired
    private RunOrderService runOrderServiceImpl;
    @Autowired
    private AmountRunUtil amountRunUtil;

    public Result accountOrderDealChannel(DealOrder order, String ip, Boolean flag) {
        if (order.getOrderType().toString().equals(Common.Order.ORDER_TYPE_BANKCARD_W.toString())) {
            ThreadUtil.execute(() -> {
                String orderQr = order.getOrderQr();
                String[] split = orderQr.split(":");
                String bankAccount = split[2];
                mediumServiceImpl.updateMountNow(bankAccount, order.getDealAmount(), "add");
            });

            //出款订单 卡商结算
            UserInfo channel = userInfoServiceImpl.findUserByOrder(order.getOrderQrUser());
            UserFund userFund = new UserFund();
            userFund.setUserId(order.getOrderQrUser());
            log.info("【进入卡商账户结算，当前结算类型为 卡商出款，当前卡商：" + channel.getUserId() + "】");
            userFund.setUserId(channel.getUserId());
            Integer feeId = order.getFeeId();
            UserRate rateFee = userRateServiceImpl.findRateFee(feeId);

            Result result = amountPublic.addAmounRechargeWit(userFund, order.getDealAmount(), order.getOrderId());
            if (!result.isSuccess()) {
                return result;
            }
            Result result1 = amountRunUtil.addOrderBankCardWit(order, ip, flag);
            if (!result1.isSuccess()) {
                return result1;
            }
            BigDecimal fee = rateFee.getFee();
            fee = fee.multiply(order.getDealAmount());
            Result addDeal = amountPublic.addDealWit(userFund, fee, order.getOrderId());
            if (!addDeal.isSuccess()) {
                return addDeal;
            }
            Result result2 = amountRunUtil.addOrderBankCardWitFee(order, ip, fee, flag);
            if (!result2.isSuccess()) {
                return result2;
            }
            log.info("【金额修改完毕，流水生成成功】");
            return Result.buildSuccessResult();
            /**
             * 出款订单类型
             * 1， 卡商提款 卡商出款， 当前渠道商户出款费率为 我放支付一定费率金额作为手续费
             * 2， 商户提款 卡商出款  当前渠道商户出款费率为 我方支付一定费率金额作为手续费
             */
        } else if (Common.Order.ORDER_TYPE_BANKCARD_R.toString().equals(order.getOrderType().toString())) {
            //入款订单  渠道结算【卡商结算】
            /**
             *
             * 入款订单不做时间限制，因我放是靠回调信息做入款凭证
             * 入款订单类型
             * 1， 商户入款 卡商入款，当前渠道商户入款费率为 我放支付一定费率金额作为手续费
             * 2， 卡商入款 卡商入款  当前渠道商户入款费率为 我方支付一定费率金额作为手续费
             * 以上不管是任何一方入款，结算方都是渠道账号， 以渠道账号作为唯一入款结算
             */
              /*  @fee
                生成订单时后做的 渠道费率计算   当前我们需要计算好费率后拿到这个数据进行对比以验证 费率计算的一致性
                若当前费率和计算费率不一致，采取费率较低的一方作为结算费率
               */
              /*
                当前渠道结算费率实体
               */
            ThreadUtil.execute(() -> {
                String orderQr = order.getOrderQr();
                String[] split = orderQr.split(":");
                String bankAccount = split[2];
                mediumServiceImpl.updateMountNow(bankAccount, order.getDealAmount(), "sub");
            });
            UserInfo channel = userInfoServiceImpl.findUserByOrder(order.getOrderQrUser());
            UserFund userFund = new UserFund();
            userFund.setUserId(order.getOrderQrUser());
            log.info("【进入卡商账户结算，当前卡商：" + channel.getUserId() + "】");
            userFund.setUserId(channel.getUserId());
            Result deleteDeal = amountPublic.deleteDeal(userFund, order.getDealAmount(), order.getOrderId());//扣除交易点数账户变动
            if (!deleteDeal.isSuccess()) {
                return deleteDeal;
            }
            Result deleteRechangerNumber = amountRunUtil.deleteRechangerNumber(order, ip, flag);//扣除交易点数 流水生成
            if (!deleteRechangerNumber.isSuccess()) {
                return deleteRechangerNumber;
            }
            UserRate findUserRateById = userInfoServiceImpl.findUserRateById(order.getFeeId());
            BigDecimal dealAmount = order.getDealAmount();
            log.info("【当前交易金额：" + dealAmount + "】");
            BigDecimal fee = findUserRateById.getFee();
            BigDecimal multiply = dealAmount.multiply(fee);
            log.info("【当前分润费率：" + fee + "】");
            log.info("【当前分润金额：" + multiply + "】");
            Result addDeal = amountPublic.addDeal(userFund, multiply, dealAmount, order.getOrderId());
            if (!addDeal.isSuccess()) {
                return addDeal;
            }
            Result addDealAmount = amountRunUtil.addDealAmount(order, ip, flag);
            if (!addDealAmount.isSuccess()) {
                return addDealAmount;
            }
            log.info("【金额修改完毕，流水生成成功】");
            return Result.buildSuccessResult();
        }
        agentChannelBankCard(order, false, ip);
        return Result.buildFail();
    }

    private void agentChannelBankCard(DealOrder order, boolean flag, String ip) {
        String orderQrUser = order.getOrderQrUser();
        UserInfo userAgent = userInfoServiceImpl.findUserAgent(orderQrUser);//当前用户代理商
        if (StrUtil.isEmpty(userAgent.getAgent())) {
            log.info("【当前卡商不存在代理商结算，当前订单号为：" + order.getOrderQr() + "】");
            return;
        }
        Integer feeId = order.getFeeId();
        UserRate rate = userInfoServiceImpl.findUserRateById(feeId);
        Result channelBankRateList = findChannelBankRateList(userAgent.getUserId(), rate, order, flag, ip);

    }

    private Result findChannelBankRateList(String agent, UserRate rate, DealOrder orderApp, boolean flag, String ip) {
        UserInfo userInfo = userInfoServiceImpl.findUserAgent(agent);
        UserRate userRate = userRateServiceImpl.findAgentChannelFee(agent, rate.getUserType(), rate.getPayTypr(), rate.getFeeType());
        log.info("【当前代理商为：" + userRate.getUserId() + "】");
        log.info("【当前代理商结算费率：" + userRate.getFee() + "】");
        log.info("【当前当前我方：" + rate.getUserId() + "】");
        log.info("【当前我方结算费率：" + rate.getFee() + "】");
        BigDecimal fee = userRate.getFee();
        BigDecimal fee2 = rate.getFee();
        BigDecimal subtract = fee2.subtract(fee);
        log.info("【当前结算费率差为：" + subtract + "】");
        BigDecimal amount = orderApp.getDealAmount();
        BigDecimal multiply = amount.multiply(subtract);
        log.info("【当前结算订单金额为：" + amount + "，当前结算代理分润为：" + multiply + "】");
        log.info("【开始结算】");

        UserFund fund = new UserFund();
        fund.setUserId(agent);
        Result addAmounProfit = amountPublic.addAmounProfit(fund, multiply, orderApp.getOrderId());
        if (addAmounProfit.isSuccess()) {
            Result addAppProfit = amountRunUtil.addAppProfitBank(orderApp.getOrderId(), fund.getUserId(), multiply, ip, flag, userRate.getFeeType());
            if (addAppProfit.isSuccess()) {
                log.info("【流水成功】");
                if (StrUtil.isNotBlank(userInfo.getAgent())) {
                    return findChannelBankRateList(userInfo.getAgent(), userRate, orderApp, flag, ip);
                }
            }
        }
        return Result.buildSuccessMessage("结算成功");
    }


    /**
     * 当前订单置为失败【成功状态下置为失败】
     *
     * @param order
     * @param ip
     * @return
     */
    public Result accountOrderDealChannelError(DealOrder order, String ip) {
        String orderStatus = order.getOrderStatus();
        if (!orderStatus.equals(Common.Order.DealOrder.ORDER_STATUS_SU.toString())) {
            log.info("【当前订单回滚失败，当前订单号：" + order.getOrderId() + "，当前订单号状态为：" + orderStatus + "】");
            return Result.buildFailMessage("当前订单号状态有误");
        }
        if (order.getOrderType().toString().equals(Common.Order.ORDER_TYPE_BANKCARD_W.toString())) {
            List<RunOrder> assOrder = runOrderServiceImpl.findAssOrder(order.getOrderId());
            //当前只可能产生2笔流水  一笔为 增加的流水    一笔为增加的  手续费流水
            if (assOrder.size() != 2) {
                String msg = "【当前流水有误，当前订单号为：" + order.getOrderId() + "】";
                log.info(msg);
                return Result.buildFailMessage(msg);
            }


            /**
             * 卡商出款订单回滚逻辑
             *
             * 根据已生产的资金流水 扣除账号相关资金
             * 1，扣款
             * 2，扣除手续费
             * 3，统计数据扣回
             */
        } else if (Common.Order.ORDER_TYPE_BANKCARD_R.toString().equals(order.getOrderType().toString())) {
            /**
             * 卡商入款回滚逻辑
             * 根据已产生的资金流水，回滚相关资金
             * 1，加宽
             * 2，扣除手续费
             * 3，统计数据扣回
             */
            List<RunOrder> assOrder = runOrderServiceImpl.findAssOrder(order.getOrderId());
            //当前只可能产生2笔流水  一笔为 扣款的流水    一笔为增加的  手续费流水
            if (assOrder.size() != 2) {
                String msg = "【当前流水有误，当前订单号为：" + order.getOrderId() + "】";
                log.info(msg);
                return Result.buildFailMessage(msg);
            }


        }
        return Result.buildFail();
    }


}
