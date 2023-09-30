package alipay.manage.api.channel.deal.maida;

import alipay.manage.api.channel.deal.maida.ctrl.MaiDaPayServer;
import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component("MaiDaPay")
public class MaiDaPay extends PayOrderService {
    @Autowired
    private OrderService orderServiceImpl;
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) {
        log.info("【进入恒支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
        String orderId = create(dealOrderApp, channel);
        UserInfo userInfo = userInfoServiceImpl.findDealUrl(dealOrderApp.getOrderAccount());
        if (StrUtil.isBlank(userInfo.getDealUrl())) {
            orderEr(dealOrderApp, "当前商户交易url未设置");
            return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
        }
        String payInfo = "";
        if (dealOrderApp.getDealDescribe().contains("付款人")) {
            payInfo = dealOrderApp.getDealDescribe();
        }
        Result result = createOrder(
                userInfo.getDealUrl() +
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/maida-notify",
                dealOrderApp.getOrderAmount(),
                orderId,
                getChannelInfo(channel, dealOrderApp.getRetain1()), dealOrderApp, payInfo
        );
        if(   result.isSuccess() ){
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        }
        orderEr(dealOrderApp, " 错误消息：请求三方失败 "  );
        return result;
    }

    private Result createOrder(
            String notify,
            BigDecimal orderAmount,
            String orderId,
            ChannelInfo channelInfo,
            DealOrderApp dealOrderApp,
            String payInfo) {
        Map<String, String> map = new HashMap();
        String amount =orderAmount.toString();
        String productId = channelInfo.getChannelType();
        String mchOrderNo = orderId;
        String notifyUrl = notify;
        String returnUrl = notifyUrl;
        String mchId = channelInfo.getChannelAppId();
        map.put("amount",amount);
        map.put("productId",productId);
        map.put("mchOrderNo",mchOrderNo);
        map.put("notifyUrl",notifyUrl);
        map.put("returnUrl",returnUrl);
        map.put("mchId",mchId);
        map.put("mchKey",channelInfo.getChannelPassword());
        map.put("payHost",channelInfo.getDealurl());
        try {
            return MaiDaPayServer.createOrder(map);
        } catch (Throwable e) {
            return Result.buildFail();
        }
    }
}