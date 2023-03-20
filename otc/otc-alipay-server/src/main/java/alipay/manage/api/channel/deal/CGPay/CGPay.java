package alipay.manage.api.channel.deal.CGPay;

import alipay.config.redis.RedisUtil;
import alipay.manage.api.channel.deal.anxin.util.PayDigestUtil;
import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component("CGPay")
public class CGPay extends PayOrderService {
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Autowired
    private RedisUtil redis;
    @Autowired
    private OrderService orderServiceImpl;
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) {
        log.info("【进入CG支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
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
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/CGPay-notify",
                dealOrderApp.getOrderAmount(),
                orderId,
                getChannelInfo(channel, dealOrderApp.getRetain1()), dealOrderApp, payInfo
        );
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            orderEr(dealOrderApp, "错误消息：" + result.getMessage());
            return result;
        }
    }
    private Result createOrder(String notify, BigDecimal orderAmount, String orderId, ChannelInfo channelInfo, DealOrderApp dealOrderApp, String payInfo) {
        String pay_customer_id = channelInfo.getChannelAppId();
        String pay_apply_date = System.currentTimeMillis() / 1000 + "";
        String pay_order_id = orderId;
        String pay_notify_url = notify;
        String pay_amount = orderAmount.toString();
        String pay_channel_id = channelInfo.getChannelType();
        String key = channelInfo.getChannelPassword();
        Map map = new HashMap();
        map.put("pay_customer_id", pay_customer_id);
        map.put("pay_apply_date", pay_apply_date);
        map.put("pay_order_id", pay_order_id);
        map.put("pay_notify_url", pay_notify_url);
        map.put("pay_amount", pay_amount);
        map.put("pay_channel_id", pay_channel_id);
        String reqSign = PayDigestUtil.getSign(map, key);
        map.put("pay_md5_sign", reqSign.toUpperCase(Locale.ROOT));
        HttpResponse header = HttpRequest.post(channelInfo.getDealurl())
                .header("Header", "Content-Type: application/json")
                .body(JSONUtil.parseObj(map)).execute();
        String s = header.body().toString();
        log.info(s);
        JSONObject jsonObject = JSONUtil.parseObj(s);
        String code = jsonObject.getStr("code");
        if ("0".equals(code)) {
            JSONObject data = JSONUtil.parseObj(jsonObject.getStr("data"));
            String view_url = data.getStr("view_url");
            log.info(view_url);
            return Result.buildSuccessResult(view_url);
        } else {
            String message = jsonObject.getStr("message");
            log.info(message);
            return Result.buildFailMessage(message);
        }
    }

}
