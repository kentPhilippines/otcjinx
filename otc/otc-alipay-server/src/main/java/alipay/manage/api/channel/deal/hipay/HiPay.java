package alipay.manage.api.channel.deal.hipay;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Component("HiPay")
public class HiPay extends PayOrderService {
    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channeId) {
        log.info("【进入WyPay支付】");
        String create = create(dealOrderApp, channeId);
        if (StrUtil.isNotBlank(create)) {
            log.info("【本地订单创建成功，开始请求远程三方支付】");
            String payInfo = "";
            payInfo = dealOrderApp.getPayName();
            UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
            if (StrUtil.isBlank(userInfo.getDealUrl())) {
                orderDealEr(create, "当前商户交易url未设置");
                return Result.buildFailMessage("请联系运营为您的商户号设置交易url");
            }
            log.info("【回调地址ip为：" + userInfo.getDealUrl() + "】");
            Result url = createOrder(userInfo.getDealUrl() + PayApiConstant.Notfiy.NOTFIY_API_WAI +
                            "/hi-pay-notify",
                    dealOrderApp.getOrderAmount(),
                    create,
                    getChannelInfo(channeId, dealOrderApp.getRetain1()), payInfo
            );
            if (url.isSuccess()) {
                return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(url.getResult()));
            }
        }
        return Result.buildFailMessage("支付错误");


    }

    @Autowired
    private OrderService orderServiceImpl;

    private Result createOrder(
            String notify,
            BigDecimal orderAmount,
            String orderId,
            ChannelInfo channelInfo, String payInfo
    ) {
        String recvid = channelInfo.getChannelAppId();
        String paychannelid = channelInfo.getChannelType();
        String orderid = orderId;
        String amount = orderAmount.setScale(BigDecimal.ROUND_CEILING, RoundingMode.HALF_UP).toString();
        String memuid = payInfo;
        String notifyurl = notify;
        Map map = new HashMap();

        /**
         * recvid           字符串 是 商户编号 由 API 平台提供，在注册后发送给商户
         * orderid          字符串 是 订单号 商户应确保在自己的系统内唯一
         * amount           字符串 是 充币数量 格式为 0.00，例如 5 元表示为 5.00
         * sign             字符串 是 签名 算法如下（示例将表格下方）： sign=md5("pay"+ recvid + orderid  + amount + apikey)
         * paychannelid     字符串 是 充值通道编号 由 API 平台提供，注册后发给商户，商 户根据充值时选择的充值通道编号值
         * notifyurl        字符串 否 回调地址 普通用户支付成功后，由 API 回调商户 的地址，采用 HTTP POST JSON 方式
         * returnurl        字符串 否 前端跳转地址
         * memuid           字符串 是 商户端用户编号 该字段用于标识商户端的用户，商户不 必直接将自己系统内的编号直接传递
         */
        map.put("recvid", recvid);
        map.put("orderid", orderid);
        map.put("amount", amount);
        map.put("paychannelid", paychannelid);
        map.put("notifyurl", notifyurl);
        map.put("memuid", memuid);
//merchantId=500000&merchantOrderId=88&payAmount=100.00&key=7ce012051e20d9d956df4e2ee5e36c38

        String param = "pay"+recvid+ orderid+ amount   ;
        log.info("签名参数为：" + param);
        String s = md5(param +  channelInfo.getChannelPassword());
        map.put("sign", s);
        JSONObject jsonObject = JSONUtil.parseObj(map);
        log.info("请求参数为:" + jsonObject.toString());
     //   String post = HttpUtil.post(channelInfo.getDealurl(), map);


  //      log.info(post);
        HttpResponse execute = HttpRequest.post(channelInfo.getDealurl()  )
                .body(jsonObject).execute();
        String ruselt = execute.body().toString();
        log.info(ruselt);
        /// 响应参数为：{"code":1,"msg":null,"data":{"note":null,"amount":300.00,"createtime":"2024-01-16T16:06:28.000+08:00","qrurl":"http://127.0.0.1:9500/order/pay","recvcharge":36.000000,"orderid":"1705392386769","qrcode":"http://127.0.0.1:9500/order/pay","recvid":"271274d1dc24b8a8f2b431718c34ccbb","sign":"e24d126dad558140ed1246ed56bbfb17","payurl":"http://127.0.0.1:9500/order/pay","recvnickname":"苹果","remark":null,"transtime":null,"notifyurl":"http://sdadadas.dasdsad.com","id":"86fbb860e5a9443290eb9a53bd33bffc","state":2,"returnurl":null,"ordertype":1}}
        // /*    String error_code = jsonObject1.getStr("error_code");

        if ("1".equals(JSONUtil.parseObj(ruselt).getStr("code"))) {
            orderServiceImpl.updatePayer(payInfo, orderId);
            String url =  JSONUtil.parseObj(ruselt).getJSONObject("data").getStr("payurl");
            return Result.buildSuccessResult(url);
        } else {
            orderDealEr(orderId,JSONUtil.parseObj(ruselt).getStr("msg"));
        }
        return Result.buildFailMessage("三方错误");
    }

    public static void main(String[] args) {
        String notify;
        BigDecimal orderAmount;
        String orderNo;
        ChannelInfo channelInfo;
        String payInfo;
        notify = "http://sdadadas.dasdsad.com";
        orderAmount = new BigDecimal(300);
        orderNo = System.currentTimeMillis() + "";
        channelInfo = new ChannelInfo();
        channelInfo.setChannelPassword("494d2a7273bc40a089317297353d111f");
        channelInfo.setDealurl("https://xahnob7yqke3.hipay.one/pay/pay/createpay");
        channelInfo.setChannelAppId("271274d1dc24b8a8f2b431718c34ccbb");
        channelInfo.setChannelType("00003");
        payInfo = "张三1";
        HiPay hi = new HiPay();
        Result order = hi.createOrder(notify, orderAmount, orderNo, channelInfo, payInfo);
        System.out.println(
                order.toString()
        );
    }

    public static String md5(String a) {
        String c = "";
        MessageDigest md5;
        String result = "";
        try {
            md5 = MessageDigest.getInstance("md5");
            md5.update(a.getBytes("utf-8"));
            byte[] temp;
            temp = md5.digest(c.getBytes("utf-8"));
            for (int i = 0; i < temp.length; i++) {
                result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
        return result;
    }
}
