package alipay.manage.api.channel.deal.baidafeili;

import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Component("NewBaidaFeiliPay")
public class NewBaidaFeiliPay extends PayOrderService {
    @Autowired
    OrderService orderServiceImpl;
    @Autowired
    private UserInfoService userInfoServiceImpl;
    private static SimpleDateFormat d = new SimpleDateFormat("YYYYMMDDhhmmss");

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入新百达斐丽支付】");
        String create = create(dealOrderApp, channel);
        if (StrUtil.isNotBlank(create)) {
            log.info("【本地订单创建成功，开始请求远程三方支付】");
            UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
            if (StrUtil.isBlank(userInfo.getDealUrl())) {
                orderDealEr(create, "当前商户交易url未设置");
                return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
            }
            log.info("【回调地址ip为：" + userInfo.getDealUrl() + "】");
            Result url = createOrder(dealOrderApp, userInfo.getDealUrl() +
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + "/NewBaidaFeiliPay-notify",
                    dealOrderApp.getOrderAmount(), create,
                    getChannelInfo(channel, dealOrderApp.getRetain1())
            );
            if (url.isSuccess()) {
                return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(url.getResult()));
            }
        }
        return Result.buildFailMessage("支付失败");
    }


    /**
     * mchId 商户 ID 是 是 平台分配商户号
     * appId 应用 ID 否 是 商户后台创建的应用ID
     * productId 通道编码 是 是 例：8007
     * mchOrderNo 商户订单号 是 是 商户订单号
     * amount 支付金额 是 是 支付金额,单位分
     * notifyUrl 异步回调 URL
     * 是 是 支付结果异步回调
     * sign 签名 是 否 签名值，详见上面签名算法
     *
     * @param dealOrderApp
     * @param notify
     * @param orderAmount
     * @param create
     * @param channelInfo
     * @return
     */
    private Result createOrder(
            DealOrderApp dealOrderApp,
            String notify,
            BigDecimal orderAmount,
            String create,
            ChannelInfo channelInfo
    ) {
        String merchId = channelInfo.getChannelAppId();
        String money =  orderAmount.multiply(new BigDecimal(100)).toBigInteger()+"";
        String orderId = create;
        String notifyUrl = notify;
        String payType = channelInfo.getChannelType();
        Map map = new HashMap();
        map.put("mchId", merchId);
        map.put("appId", channelInfo.getWitUrl());
        map.put("productId", payType);
        map.put("mchOrderNo", orderId);
        map.put("notifyUrl", notifyUrl);
        map.put("amount", money);
        String param = SanYangUtil.createParam(map);
        String md5 = param + "&key=" + channelInfo.getChannelPassword();
        log.info(" 新百达斐丽 加密前 ：" + md5);
        String sign = SanYangUtil.encodeByMD5(md5).toUpperCase();
        map.put("sign", sign);
        log.info(" 新百达斐丽 请求参数 ： " + map.toString());
        //https://a.xcpay.org
        String post = HttpUtil.post(channelInfo.getDealurl() + "/api/pay/create_order", map);
        log.info(" 新百达斐丽 响应参数 ： " + post);
        // {"merchId":"662023102315523962","orderId":"1698144487614","curType":"CNY","money":"800","code":"0000","msg":"SUCCESS","payUrl":"http:\/\/aliapi.davincimanner.com\/pass\/?code=869814448904449693","sign":"404bca8e9b76f7551f771e01d700632c"}
        JSONObject jsonObject = JSONUtil.parseObj(post);
        if("0".equals(jsonObject.getStr("retCode"))){
            String payurl = jsonObject.getStr("payJumpUrl");
            return Result.buildSuccessResult(payurl);
        }else {
            orderDealEr(orderId,jsonObject.getStr("retMsg"));
            return Result.buildFail();
        }
    }


    public static void main(String[] args) {
        DealOrderApp dealOrderApp;
        String notify;
        BigDecimal orderAmount;
        String create;
        ChannelInfo channelInfo;
        dealOrderApp = new DealOrderApp();
        notify = "http://sdasdsadsad.dasdad.net";
        orderAmount = new BigDecimal(500.00);
        create = System.currentTimeMillis() + "";
        channelInfo = new ChannelInfo();
        channelInfo.setChannelAppId("1517");
        channelInfo.setChannelType("8051");
        channelInfo.setDealurl("https://a.xcpay.org");
        channelInfo.setChannelPassword("A21CYS85ZNC3ZW6HA5QXL3OOH6JEVBUNMBLSVC4WOVAX65TDRXJWONFH0SF8SY28IEN9ZWKVZYH431RUH5X6QKLLLOUZH0AK38B7TXI050FBSP0VOQGJVKUGRGAPMKLR");
        NewBaidaFeiliPay pay = new NewBaidaFeiliPay();
        pay.createOrder(dealOrderApp, notify, orderAmount, create, channelInfo);
    }

}
