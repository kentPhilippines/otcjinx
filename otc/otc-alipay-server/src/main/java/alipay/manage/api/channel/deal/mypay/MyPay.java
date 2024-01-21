package alipay.manage.api.channel.deal.mypay;

import alipay.config.redis.RedisUtil;
import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import alipay.manage.util.RSAUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;
import otc.util.MapUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("MyPay")
public class MyPay extends PayOrderService {
    @Autowired
    OrderService orderServiceImpl;
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Value("${otc.payInfo.url}")
    public String url;
    @Autowired
    private RedisUtil redis;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    // 获取当前时间年月日时分秒毫秒字符串
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入苍穹支付】");
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
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + MyPayUtil.NOTIFY,
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
     * mchid	            string	是	是	商户id
     * out_trade_no	        string	是	是	商户订单id 唯一 不能重复 重复会创建订单失败 由商户控制
     * amount	            double	是	是	金额 最大两位小数
     * channel	            string	是	是	渠道 见附件 渠道列表
     * notify_url	        string	是	是	异步通知地址 必须http:或者https:格式
     * return_url	        string	是	是	同步通知地址 必须http:或者https:格式
     * time_stamp	        string	是	是	时间如20180824030711
     * body	                string	是	是	宝转卡 必须传支付宝用户名 否则无法到账其它请填１２３
     * sign	                string	是	否	签名 见签名规则
     *
     * @param dealOrderApp
     * @param notify
     * @param orderAmount
     * @param orderId
     * @param channelInfo
     * @return
     */
    private Result createOrder(DealOrderApp dealOrderApp, String notify, BigDecimal orderAmount,
                               String orderId, ChannelInfo channelInfo) throws Exception {
        String mchid = channelInfo.getChannelAppId();
        String out_trade_no = orderId;
        String amount = orderAmount.toString();
        String channel = channelInfo.getChannelType();
        String notify_url = notify;
        String return_url = dealOrderApp.getBack();
        String time_stamp = sdf.format(new Date());
        String body = "1";
        Map map = new HashMap();
        map.put("merchantId", mchid);
        map.put("version", "1.0.0");
        map.put("merchantOrderNo", out_trade_no);
        map.put("amount", amount);
        map.put("model", channel);
        map.put("notifyUrl", notify_url);
        String sign = RSAUtil.signByPrivateKeyStr(MyPayUtil.PRIVATE_KEY,MapUtil.createParam(map));
        map.put("sign", sign);
//        Unirest.setTimeouts(2, 2);
        log.info(" 请求参数: " + map.toString());
        String post = HttpUtil.post(channelInfo.getDealurl(), JSONUtil.toJsonStr(map));
        log.info("请求三方响应：" + post);
//{
//    "code": 0,
//    "msg": "操作成功",
//    "data": {
//        "request_url": "http://xxxxxxxx/payThird.jsp?orderId=easypay2019071831607197005045326668",
//    }
//}
//
        JSONObject jsonObject = JSONUtil.parseObj(post);
        String code = jsonObject.getStr("code");
        if ("0".equals(code)) {
            String url = jsonObject.getStr("url");
            return Result.buildSuccessResult(url);
        } else {
            orderDealEr(orderId, jsonObject.getStr("msg"));
            return Result.buildFailMessage(jsonObject.getStr("msg"));
        }
    }

    public static void main(String[] args) throws Exception {
        DealOrderApp dealOrderApp;
        String notifyUrl = "http://34.92.251.112:9010/notfiy-api-pay/mypay-notify";
        BigDecimal orderAmount;
        String orderId;
        ChannelInfo channelInfo;
        dealOrderApp = new DealOrderApp();
        dealOrderApp.setBack("http://dsada.asddad.adsdasdas");
        orderAmount = new BigDecimal(800);
        orderId = System.currentTimeMillis() + "";
        channelInfo = new ChannelInfo();
        channelInfo.setChannelPassword(MyPayUtil.PRIVATE_KEY);
        channelInfo.setChannelAppId("10334");
        channelInfo.setChannelType("813");
        channelInfo.setDealurl("https://gateway.guming.vip/api/payOrder/create");
        MyPay pay = new MyPay();
        Result order = pay.createOrder(dealOrderApp, notifyUrl, orderAmount, orderId, channelInfo);

        System.out.printf(order.toJson());
    }


}
