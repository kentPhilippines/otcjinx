package alipay.manage.api.channel.deal.shaudeduo;

import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 刷的多支付实体
 */
@Component("DealShuaDeduoPay")
public class DealShuaDeduoPay extends PayOrderService {
    private static final Log log = LogFactory.get();
    static SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入刷的多支付，当前请求产品：" + dealOrderApp.getRetain2() + "，当前请求渠道：" + channel + "】");
        String orderId = create(dealOrderApp, channel);
        UserInfo userInfo = userInfoServiceImpl.findDealUrl(dealOrderApp.getOrderAccount());
        if (StrUtil.isBlank(userInfo.getDealUrl())) {
            orderEr(dealOrderApp, "当前商户交易url未设置");
            return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
        }
        Result result = createOrder(userInfo.getDealUrl() +
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/shuadeduo-notify",
                dealOrderApp.getOrderAmount()
                , orderId, getChannelInfo(channel, dealOrderApp.getRetain1()), dealOrderApp.getOrderIp());
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            return result;
        }
    }

    private Result createOrder(String notify, BigDecimal orderAmount,
                               String orderId, ChannelInfo channelInfo, String ip) {
        Map<String, Object> map = new HashMap<>();
        map.put("trade_no", orderId);
        map.put("amount", orderAmount.toString());
        map.put("notify_url", notify.toString());
        map.put("ip", ip.toString());
        log.info("【刷的多请求参数：" + map.toString() + "】");
        String header = "Bearer ";
        String result2 = HttpRequest.post(channelInfo.getDealurl())
                .header(Header.CONTENT_TYPE, "application/x-www-form-urlencoded")//头信息，多个头信息多次调用此方法即可
                .header("Authorization", header + channelInfo.getChannelPassword())//头信息，多个头信息多次调用此方法即可
                .form(map)//表单内容
                .timeout(20000)//超时，毫秒
                .execute().body();
        log.info("【刷的多返回数据：" + result2 + "】");
        /**
         * {"code":"200","success":true,"message":"transaction receive.",
         * "transaction_no":null,"trade_no":"70b4b228-6bb8-4868-a2b4-50c278a7b31b","trade_amount":"1000.00",
         * "uri":"https:\/\/4536251.net\/qrcode\/noAssignableTunnel\/70b4b228-6bb8-4868-a2b4-50c278a7b31b"}
         */
        JSONObject jsonObject = JSONUtil.parseObj(result2);
        if ("200".equals(jsonObject.getStr("code"))) {
            String uri = jsonObject.getStr("uri");
            return Result.buildSuccessResult(uri);
        } else {
            return Result.buildFailMessage(jsonObject.getStr("message"));
        }
    }
}
