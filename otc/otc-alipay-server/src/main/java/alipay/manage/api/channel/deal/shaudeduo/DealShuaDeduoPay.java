package alipay.manage.api.channel.deal.shaudeduo;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
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
        UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
        if (StrUtil.isBlank(userInfo.getDealUrl())) {
            orderEr(dealOrderApp, "当前商户交易url未设置");
            return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
        }
        Result result = createOrder(userInfo.getDealUrl() +
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/shuadeduo-notify",
                dealOrderApp.getOrderAmount()
                , orderId, getChannelInfo(channel, dealOrderApp.getRetain2()));
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            return result;
        }
    }

    private Result createOrder(String notify, BigDecimal orderAmount, String orderId, ChannelInfo channelInfo) {
        Map<String, Object> map = new HashMap<>();

        String result2 = HttpRequest.post(channelInfo.getDealurl())
                .header("Content-Type", "application/x-www-form-urlencoded")//头信息，多个头信息多次调用此方法即可
                .form(map)//表单内容
                .timeout(20000)//超时，毫秒
                .execute().body();


        return Result.buildFail();
    }
}
