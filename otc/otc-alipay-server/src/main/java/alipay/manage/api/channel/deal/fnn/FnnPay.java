package alipay.manage.api.channel.deal.fnn;

import alipay.manage.api.channel.util.yifu.YiFuUtil;
import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component("fnnPay")
public class FnnPay extends PayOrderService {
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入fnn支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
        String orderId = create(dealOrderApp, channel);
        UserInfo userInfo = userInfoServiceImpl.findDealUrl(dealOrderApp.getOrderAccount());
        if (StrUtil.isBlank(userInfo.getDealUrl())) {
            orderDealEr(orderId, "当前商户交易url未设置");
            return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
        }
        Result result = createOrder(
                userInfo.getDealUrl() +
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/fnn-notify",
                dealOrderApp.getOrderAmount(),
                orderId,
                getChannelInfo(channel, dealOrderApp.getRetain1()), dealOrderApp);
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            return result;
        }

    }

    /**
     商户ID mchId 是 String(30) 20001222 ⽀付中⼼分配的商户号
     应⽤ID appId 否 String(30) cbsgB1T0SL6tfflFYoBX 商户应⽤ID
     ⽀付产品ID productId 是 String(24) 8001
     商户订单号 mchOrderNo 是 String(30) 20160427210604000490 商户⽣成的订单号
     ⽀付⾦额 amount 是 int 100 ⽀付⾦额,单位分
     币种 currency 是 String(3) cny 三位货币代码,⼈⺠币:cny
     异步回调地
     址
     notifyUrl 是 String(128) http://shop.xx.com/notify.htm ⽀付结果异步回调URL
     商品主题 subject 是 String(64) 测试商品1 商品主题
     商品描述信
     息
     body 是 String(256) 测试商品描述 商品描述信息
     请求时间 reqTime 是 String(30) 20190723141000
     请求接⼝时间， yyyyMMddHHmmss
     格式
     接⼝版本 version 是 String(3) 1.0 接⼝版本号，固定：1.0
     签名 sign 是 String(32) C380BEC2BFD727A4B6845133519F3AD6 签名值，详⻅签名算法
     * @param orderAmount
     * @param orderId
     * @param channelInfo
     * @param dealOrderApp
     * @return
     */
    private Result createOrder(String notifyUrl, BigDecimal orderAmount, String orderId, ChannelInfo channelInfo, DealOrderApp dealOrderApp) {
        log.info("进入fnn支付请求");
        Map<String, Object> mapp = new HashMap<>();
        String channelAppId = channelInfo.getChannelAppId();
        String productId = channelInfo.getChannelType();
        String mchOrderNo = orderId;
        String currency = "cny";
        Integer amount = orderAmount.intValue();
        String url = channelInfo.getDealurl();


        Map<String,Object> map = new HashMap<String,Object>();
        map.put("mch_id", channelAppId);
        map.put("ptype", productId);
        map.put("money", amount);
        map.put("order_sn", orderId);
        map.put("goods_desc",  "test");
        map.put("notify_url", notifyUrl);
        map.put("client_ip", "127.0.0.1");
        map.put("format", "url");
        map.put("time", System.currentTimeMillis()/1000);
        String createParam = YiFuUtil.createParam(map)+ "key="+channelInfo.getChannelPassword();
        System.out.println("【fnn签名前参数："+createParam+"】");
        String md5 = YiFuUtil.md5(createParam );
        String sign = md5.toLowerCase();
        map.put("sign", sign);
        System.out.println("【请求fnn参数："+map.toString()+"】");
        String post = HttpUtil.post(url, map);

        log.info("【fnn响应参数：" + post + "】");
        JSONObject jsonObject = JSONUtil.parseObj(post);
        String code = jsonObject.getStr("code");
        if ("1".equals(code)) {
            JSONObject data = jsonObject.getJSONObject("data");
            String payUrl = data.getStr("qrcode");
            return Result.buildSuccessResult("支付处理中", payUrl);
        } else {
            orderDealEr(orderId, jsonObject.getStr("retMsg"));
            return Result.buildFailMessage(jsonObject.getStr("retMsg"));
        }
    }

}


