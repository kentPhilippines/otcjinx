package alipay.manage.api.channel.deal.anxin;

import alipay.config.redis.RedisUtil;
import alipay.manage.api.channel.deal.anxin.util.HttpClientUtils;
import alipay.manage.api.channel.deal.anxin.util.PayDigestUtil;
import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;

@Component("AnXinPay")
public class AnXinPay extends PayOrderService {
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Autowired
    private RedisUtil redis;
    @Autowired
    private OrderService orderServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) {
        log.info("【进入安心支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
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
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/anxin-bank-notify",
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
        try {
            String key = channelInfo.getChannelPassword();
            JSONObject paramMap = new JSONObject();
            paramMap.put("mchId", channelInfo.getChannelAppId());
            paramMap.put("mchOrderNo", orderId);
            paramMap.put("amount", orderAmount.multiply(new BigDecimal(100)).toBigInteger());
            paramMap.put("realName", getPayName(payInfo, orderId));
            paramMap.put("type", channelInfo.getChannelType());
            paramMap.put("notifyUrl", "127.0.0.1");
            String reqSign = PayDigestUtil.getSign(paramMap, key);
            paramMap.put("sign", reqSign);
            String reqData = "params=" + paramMap.toJSONString();
            log.info("签名::::" + reqSign);
            log.info("请求支付中心下单接口,请求数据:" + reqData);
            //   String post = HttpUtil.post("http://payapi.avg2kv9s.anpay.cc:6080/api/obpay/get_cashierurl", paramMap);
            String url = channelInfo.getDealurl();
            log.info("url::::" + url + reqData);
            String result = HttpClientUtils.doPost(url, paramMap);
            log.info("请求支付中心下单接口,响应数据:" + result);
            //{"data":{"cashier":"http://syt.rhkuq6x3.anpay.cc:8088/pays/banktransferfirststep.html?realName=%E5%BC%A0%E4%B8%89&ct=1678546369938&amount=61000&mchId=1331&mchOrderNo=I-4422111Y-202302261320510-vip&notifyUrl=127.0.0.1&type=1&sign=29EAFEA1C84A08382C2EB951D319C53B"},"sign":"FB41384DC9647E85431BA1060789BC20","retCode":"SUCCESS","retMsg":"下单成功"}
            cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(result);
            if ("SUCCESS".equals(jsonObject.getStr("retCode"))) {
                cn.hutool.json.JSONObject data = JSONUtil.parseObj(jsonObject.getStr("data"));
                String payUrl = data.getStr("cashier");
                return Result.buildSuccessResult(payUrl);
            } else {
                String errDes = jsonObject.getStr("errDes");
                return Result.buildFailMessage(errDes);
            }
        }catch (Exception w ){
            return Result.buildFailMessage("支付错误");
        }
    }
}
