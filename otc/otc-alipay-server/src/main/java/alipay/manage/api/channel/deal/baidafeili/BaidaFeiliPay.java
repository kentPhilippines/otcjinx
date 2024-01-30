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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("BaidaFeiliPay")
public class BaidaFeiliPay extends PayOrderService {
    @Autowired
    OrderService orderServiceImpl;
    @Autowired
    private UserInfoService userInfoServiceImpl;
    private static SimpleDateFormat d = new SimpleDateFormat("YYYYMMDDhhmmss");
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入百达斐丽支付】");
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
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + "/BaidaFeiliPay-notify",
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
     * merchId          √ ALL 商户号
     * money            √ ALL 订单金额(元)
     * userId           √ ALL 商户平台会员唯一标识(可 md5 加密的值),如 果无法传 平台用户唯一标识，请传 随机数， 注： 识别恶意支付，提高成功率
     * orderId          √ ALL 订单号
     * time             √ ALL 请求时间，时间格式：YYYYMMDDhhmmss 14 位 数字，精确到秒。如：20221024145654
     * notifyUrl        √ ALL 异步通知接口地址，用于通知支付结果
     * payType          √ ALL 见 1.4 支付类型
     * curType          √ ALL 币种简称，见 1.3 币种列表
     * reType           √ ALL 返回数据类型: LINK：付款连接 INFO：账号信息
     * signType         √ ALL 签名类型 MD5
     * <p>
     * sign             √ ALL
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
        String money = orderAmount.toString();
        String userId = dealOrderApp.getPayName();
        String orderId = create;
        String notifyUrl = notify;
        String payType = channelInfo.getChannelType();
        String curType = "CNY";
        String time = d.format(new Date());
        String reType = "LINK";
        String signType = "MD5";
        Map map = new HashMap();
        map.put("merchId", merchId);
        map.put("money", money);
        map.put("userId", userId);
        map.put("orderId", orderId);
        map.put("notifyUrl", notifyUrl);
        map.put("time", time);
        map.put("payType", payType);
        map.put("curType", curType);
        map.put("reType", reType);
        map.put("signType", signType);
        String param = SanYangUtil.createParam(map);
        String md5 = param + "&md5_key=" + channelInfo.getChannelPassword();
        log.info(" 百达斐丽 加密前 ：" + md5);
        String sign = SanYangUtil.encodeByMD5(md5).toLowerCase();
        map.put("sign", sign);
        log.info(" 百达斐丽请求参数 ： " + map.toString());
        String post = HttpUtil.post(channelInfo.getDealurl() + "/gateway/pay/index", map);
        log.info(" 百达斐丽 响应参数 ： " + post);
        // {"merchId":"662023102315523962","orderId":"1698144487614","curType":"CNY","money":"800","code":"0000","msg":"SUCCESS","payUrl":"http:\/\/aliapi.davincimanner.com\/pass\/?code=869814448904449693","sign":"404bca8e9b76f7551f771e01d700632c"}
        JSONObject jsonObject = JSONUtil.parseObj(post);
        String code = jsonObject.getStr("code");
        String msg = jsonObject.getStr("msg");
        if("0000".equals(code)){
            String str = jsonObject.getStr("payUrl");
            return Result.buildSuccessResult(str);
        }else {
            orderDealEr(orderId,msg);
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
        dealOrderApp.setPayName("张全蛋");
        notify = "http://sdasdsadsad.dasdad.net";
        orderAmount = new BigDecimal(800);
        create = System.currentTimeMillis() + "";
        channelInfo = new ChannelInfo();
        channelInfo.setChannelAppId("662023102315523962");
        channelInfo.setChannelType("205");
        channelInfo.setDealurl("http://api.hengzf.net");
        channelInfo.setChannelPassword("Wasde8rfd1vcE9qo");
        BaidaFeiliPay pay = new BaidaFeiliPay();
        pay.createOrder(dealOrderApp, notify, orderAmount, create, channelInfo);
    }

}
