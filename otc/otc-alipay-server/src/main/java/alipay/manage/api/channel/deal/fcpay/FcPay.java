package alipay.manage.api.channel.deal.fcpay;

import alipay.manage.api.channel.util.yifu.YiFuUtil;
import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component("FcPay")
public class FcPay extends PayOrderService {
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入olen支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
        String orderId = create(dealOrderApp, channel);
        UserInfo userInfo = userInfoServiceImpl.findDealUrl(dealOrderApp.getOrderAccount());
        if (StrUtil.isBlank(userInfo.getDealUrl())) {
            orderDealEr(orderId, "当前商户交易url未设置");
            return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
        }
        String payInfo = "";
        if (dealOrderApp.getDealDescribe().contains("付款人")) {
            payInfo = dealOrderApp.getDealDescribe();
        }
        Result result = createOrder(
                userInfo.getDealUrl() +
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/fcpay-notify",
                dealOrderApp.getOrderAmount(),
                orderId,
                getChannelInfo(channel, dealOrderApp.getRetain1()), dealOrderApp, payInfo);
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            return result;
        }

    }

    @Autowired
    private OrderService orderServiceImpl;
    private String name = "付款人：";


    private Result createOrder(String notifyUrl, BigDecimal orderAmount, String orderId, ChannelInfo channelInfo, DealOrderApp dealOrderApp, String
            payInfo) {
        log.info("进入maoerduo支付请求");
        Map<String, Object> mapp = new HashMap<>();
        String channelAppId = channelInfo.getChannelAppId();
        String productId = channelInfo.getChannelType();
        String mchOrderNo = orderId;
        Integer amount = orderAmount.multiply(new BigDecimal(100)).intValue();
        String url = channelInfo.getDealurl();
        String key = channelInfo.getChannelPassword();

        String merchant = channelAppId;
        String privateKey = key;

        Map<String, Object> map = new HashMap<>();
        map.put("method", "placeOrder");
        map.put("callerOrderId", mchOrderNo);
        map.put("memberId", merchant);
        map.put("timestamp", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        map.put("amount", amount);
        map.put("channelCode", productId);
        map.put("merchantCallbackUrl", notifyUrl);

        String queryString = map.keySet().stream()
                .sorted()
                .map(s -> s + "" + map.getOrDefault(s, ""))
                .collect(Collectors.joining(""));

        System.out.println(queryString);
        String sign = YiFuUtil.md5(privateKey + queryString + privateKey);
        map.put("sign", sign);
        log.info("【fcpay请求参数：" + map.toString() + "】");
        HttpResponse execute = HttpRequest.post(url).form(map).execute();
        String ruselt = execute.body().toString();
        log.info("【fcpay响应参数：" + ruselt + "】");
        JSONObject jsonObject = JSONUtil.parseObj(ruselt);
        String code = jsonObject.getStr("code");
        if ("1000".equalsIgnoreCase(code)) {
            String payUrl = jsonObject.getJSONObject("data").getJSONObject("message").getStr("url");
            return Result.buildSuccessResult("支付处理中", payUrl);
        } else {
            orderDealEr(orderId, jsonObject.getStr("message"));
            return Result.buildFailMessage(jsonObject.getStr("message"));
        }
    }

}


