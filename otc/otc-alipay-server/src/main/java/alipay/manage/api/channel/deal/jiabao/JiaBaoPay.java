package alipay.manage.api.channel.deal.jiabao;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Component("JiaBaoPay")
public class JiaBaoPay extends PayOrderService {
    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入家宝支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
        String orderId = create(dealOrderApp, channel);
        UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
        if (StrUtil.isBlank(userInfo.getDealUrl())) {
            orderEr(dealOrderApp, "当前商户交易url未设置");
            return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
        }
        Result result = createOrder(
                userInfo.getDealUrl() +
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/jiabao-source-notify",
                dealOrderApp.getOrderAmount(),
                orderId,
                getChannelInfo(channel, dealOrderApp.getRetain1()));
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            orderEr(dealOrderApp, result.getMessage());
            return result;
        }

    }

    private Result createOrder(String notify, BigDecimal orderAmount,
                               String orderId,
                               ChannelInfo channelInfo) {

        String amount = orderAmount.intValue() + ".00";
        String channel = channelInfo.getChannelType();
        String url = channelInfo.getDealurl();
        String merchantTradeNo = orderId;
        HashMap<String, Object> map = new HashMap<>();
        map.put("merchantCode", channelInfo.getChannelAppId());
        map.put("merchantTradeNo", merchantTradeNo);
        map.put("userId", channelInfo.getChannelAppId()); //玩家 用户ID
        map.put("amount", amount);
        map.put("notifyUrl", notify);//服务器回调地址
        map.put("terminalType", 2);
        map.put("channel", channel);
        String signStr = RSAUtil.createParam(map);
        log.info("signStr ====== {}", signStr);
        String sign = RSAUtil.md5(signStr.toString() + channelInfo.getChannelPassword());
        map.put("sign", sign);
        JSONObject merJson = new JSONObject();
        merJson.put("merchantCode", channelInfo.getChannelAppId());
        merJson.put("content", "" + JSONUtil.parseObj(map) + "");
        merJson.put("signType", "md5");
        log.info("请求参数：{}", merJson.toString());
        String result = JiaobaoUtil.sendHttpRequest(url, merJson.toString());
        log.info("返回结果：{}", result);
        JSONObject resJSON = JSONUtil.parseObj(result);
        if ("FAIL".equalsIgnoreCase(resJSON.getStr("status"))) {
            return Result.buildFailMessage(resJSON.getStr("msg"));
        }
        JSONObject data = JSONUtil.parseObj(resJSON.getStr("data"));
        String resContent = data.getStr("content");
        JSONObject resData = JSONUtil.parseObj(resContent);
        String payUrl = resData.getStr("payUrl");
        Map<String, Object> returnMap = new TreeMap<>();
        returnMap.put("merchantCode", resData.getStr("merchantCode"));
        returnMap.put("merchantTradeNo", merchantTradeNo);
        returnMap.put("tradeNo", resData.getStr("tradeNo"));
        returnMap.put("payUrl", payUrl);
        log.info("【家宝返回数据：" + payUrl + "】");
        return Result.buildSuccessResult("支付处理中", payUrl);

    }
}
