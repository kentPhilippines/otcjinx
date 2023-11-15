package alipay.manage.api.channel.deal.tianyi;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component("TianYiPay")
public class TianYiPay extends PayOrderService {
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入xinxinPay支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
        String orderId = create(dealOrderApp, channel);
        UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
        if (StrUtil.isBlank(userInfo.getDealUrl())) {
            orderDealEr(orderId, "当前商户交易url未设置");
            return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
        }
        Result result = createOrder(
                userInfo.getDealUrl() +
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/TianyiPayNotify",
                dealOrderApp.getOrderAmount(),
                orderId,
                getChannelInfo(channel, dealOrderApp.getRetain1()));
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            return result;
        }
    }

    private Result createOrder(String notify,
                               BigDecimal orderAmount,
                               String orderId,
                               ChannelInfo channelInfo
    ) {
        String merchantId = channelInfo.getChannelAppId();
        String encrypt = "";

        String receiveNo = orderId;
        String amt = orderAmount + "";
        String notifyUrl = notify;
        String timestamp = System.currentTimeMillis() / 1000 + "";
        Map appNodticeDto = new HashMap();
        appNodticeDto.put("merchantId", merchantId);
        appNodticeDto.put("receiveNo", receiveNo);
        appNodticeDto.put("amt", amt);
        appNodticeDto.put("notifyUrl", notifyUrl);
        appNodticeDto.put("timestamp", timestamp);
        try {
            encrypt = RSAMerchantUtils.encryptByPubKey(JSONObject.toJSONString(appNodticeDto), channelInfo.getChannelPassword());
        } catch (Throwable e) {
            //请求渠道加密失败
            log.error("请求渠道加密失败", e);
        }
        Map params = new HashMap();
        params.put("merchantId", merchantId);
        params.put("encrypt", encrypt);
        log.info("加密数据后：" + JSONObject.toJSONString(params));
        String post = "";
        try {
            post = HttpUtil.post(channelInfo.getDealurl() + "/front/notice/receive/page", JSONObject.toJSONString(params));
            log.info("实际响应参数解密前：" + post);
        } catch (Throwable e) {
            log.error("请求渠道失败", e);
        }
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(post);
        cn.hutool.json.JSONObject data = jsonObject.getJSONObject("data");
        String code = jsonObject.getStr("code");
        String msg = jsonObject.getStr("msg");
        if (!"200".equals(code)) {
            orderDealEr(orderId, msg);
            return Result.buildFailMessage(msg);
        }
        String result = "";
        try {
            result = RSAMerchantUtils.decryptByPubKey(data.getStr("encrypt"), channelInfo.getChannelPassword());
            log.info("实际响应参数解密后：" + result);
        } catch (Exception e) {
            log.error("解密渠道响应数据失败：", e);
        }
        cn.hutool.json.JSONObject jsonObject1 = JSONUtil.parseObj(result);
        String url = jsonObject1.getStr("gatewayUrl");
        return Result.buildSuccessResults(url);
    }

    public static void main(String[] args) {
        String notify = "http://sdadsadasdas.adsdadasd.adasd";
        BigDecimal orderAmount = new BigDecimal("100");
        String orderId = "dfsffsa213213131223213122";
        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setChannelAppId("qiangsheng9988");
        channelInfo.setChannelPassword("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCe6miOoDm7mrhYYg3QHkZZFOC0AVfH8Au3cGuKbaQSLw7mhKknM3MOF9JO5RfkoG4qj5FMB8Mui/injuW76u5ffg9yZUu9btiGHrE/amT+yFAjGCFVNKdSZxyvDTDIFGA4MkRADroirNoBC7QPhNeeUoqWwhSuKvj63EBJF53oJQIDAQAB");
        channelInfo.setDealurl("http://103.145.86.133:18080");
        //  createOrder(notify,orderAmount,orderId,channelInfo);
    }
}
