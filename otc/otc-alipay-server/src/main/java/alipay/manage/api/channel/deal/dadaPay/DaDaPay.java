package alipay.manage.api.channel.deal.dadaPay;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.*;

@Component("DaDaPay")
public class DaDaPay  extends PayOrderService {
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入dadaPay支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
        String orderId = create(dealOrderApp, channel);
        UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
        if (StrUtil.isBlank(userInfo.getDealUrl())) {
            orderEr(dealOrderApp, "当前商户交易url未设置");
            return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
        }

        Result result = createOrder(
                userInfo.getDealUrl() +
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/dadaPayNotify",
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


    private Result createOrder(String notify, BigDecimal orderAmount, String orderId, ChannelInfo channelInfo) {
        String amount = orderAmount.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_UP).toString();
        String key  = "RDBECCGUN2OQ6NMS8WUDOZGAXME9UYPA0RJPTR89COWMISQ2X2JWHRE4YHX2FMCUGOPVF5KGOIO7ZKWXOCNDIFKIWPDK9LGI4OWJCOTDWFTWU1KVZ3KLTDONCD364FC3";
        String appId  =  channelInfo.getChannelPassword();
        String mchId  =  channelInfo.getChannelAppId();
        String productId  =  channelInfo.getChannelType();
        String notifyUrl  = notify ;
        String subject  =  "今晚打老虎";
        String body  =  "干就完了";
        String url =  channelInfo.getDealurl();
        Map<String,Object> map   = new HashMap<>();
        map.put("mchId",mchId);
        map.put("appId", appId);
        map.put("mchOrderNo",orderId);
        map.put("currency","cny");
        map.put("amount",amount);
        map.put("notifyUrl",notifyUrl);
        map.put("subject",subject);
        map.put("body",body);
        String createParam = PayUtil.createParam(map);
        log.info(createParam);
        String md5 = PayUtil.md5(createParam + "&key="+key).toUpperCase(Locale.ROOT);
        log.info(md5);
        map.put("sign",md5);
        String payUrl = url+"/api/cashier/h5_recharge?"+createParam+"&sign="+md5;
        log.info(payUrl);
        return   Result.buildSuccessResult(payUrl);
    }
}
