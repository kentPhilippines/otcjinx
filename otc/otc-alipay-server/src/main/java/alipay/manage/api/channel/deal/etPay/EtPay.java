package alipay.manage.api.channel.deal.etPay;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.OrderService;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component("EtPay")
public class EtPay extends PayOrderService {
    @Autowired
    OrderService orderServiceImpl;
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channelId) throws Exception {
        log.info("【进入RTPAY支付】");
        String orderId = create(dealOrderApp, channelId);
        if (StrUtil.isNotBlank(orderId)) {
            UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
            if (StrUtil.isBlank(userInfo.getDealUrl())) {
                orderDealEr(orderId, "当前商户交易url未设置");
                return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
            }
            log.info("【回调地址ip为：" + userInfo.getDealUrl() + "】");
            log.info("【本地订单创建成功，开始请求远程三方支付】");
            String payInfo = "";
            if (dealOrderApp.getDealDescribe().contains("付款人")) {
                payInfo = dealOrderApp.getDealDescribe();
            }
            Result url = createOrder(
                    dealOrderApp,
                    userInfo.getDealUrl() + PayApiConstant.Notfiy.NOTFIY_API_WAI + "/etPay-notfiy",
                    dealOrderApp.getOrderAmount(),
                    orderId,
                    getChannelInfo(channelId, dealOrderApp.getRetain1())
            );
            if (!url.isSuccess()) {
                return Result.buildFailMessage("支付失败");
            } else {
                return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(url.getResult()));
            }
        }
        return Result.buildFailMessage("支付失败");
    }

    private Result createOrder(DealOrderApp dealOrderApp, String notify, BigDecimal orderAmount, String orderId, ChannelInfo channelInfo) {
        String code = "";
        JSONObject ruselt = new JSONObject();
        String pay_customer_id = channelInfo.getChannelAppId();
        String pay_apply_date = System.currentTimeMillis() / 1000 + "";
        String pay_order_id = orderId;
        String pay_notify_url = notify;
        String pay_amount = orderAmount.toString();
        String pay_channel_id = channelInfo.getChannelType();
        String pay_product_name = "";
        String user_name = dealOrderApp.getPayName();
        Map map = new HashMap();
        map.put("pay_customer_id", pay_customer_id);
        map.put("pay_apply_date", pay_apply_date);
        map.put("pay_order_id", pay_order_id);
        map.put("pay_notify_url", pay_notify_url);
        map.put("pay_amount", pay_amount);
        map.put("pay_channel_id", pay_channel_id);
        map.put("user_name", user_name);
        String sign = createParam(map) + "&key=" + channelInfo.getChannelPassword();
        log.info("签名前参数为:" + sign);
        sign = PayUtil.md5(sign).toUpperCase();
        map.put("pay_md5_sign", sign);
        try {
            JSONObject jsonObject = JSONUtil.parseObj(map);
            log.info("etPay请求参数：" + jsonObject.toString());
            String post = HttpUtil.post(channelInfo.getDealurl(), jsonObject.toString());
            log.info("etPay响应参数：" + post.toString());
            ruselt = JSONUtil.parseObj(post);
            code = ruselt.getStr("code");
        } catch (Throwable t) {
            log.error("渠道请求异常", t);
            orderDealEr(orderId, "暂无合适通道，请更换金额重新拉单");
            return Result.buildFail();
        }
        if ("0".equals(code)) {
            String payUrl = JSONUtil.parseObj(ruselt.getStr("data")).getStr("view_url");
            return Result.buildSuccessResult(payUrl);
        } else {
            orderDealEr(orderId, "暂无合适通道，请更换金额重新拉单");
            return Result.buildFail();
        }
    }

    public String createParam(Map<String, Object> map) {
        try {
            if (map == null || map.isEmpty()) {
                return null;
            }
            Object[] key = map.keySet().toArray();
            Arrays.sort(key);
            StringBuffer res = new StringBuffer(128);
            for (int i = 0; i < key.length; i++) {
                if (ObjectUtil.isNotNull(map.get(key[i]))) {
                    res.append(key[i] + "=" + map.get(key[i]) + "&");
                }
            }
            String rStr = res.substring(0, res.length() - 1);
            return rStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        DealOrderApp dealOrderApp = new DealOrderApp();
        dealOrderApp.setPayName("扎努");
        String notify = "http://dsadad.dasdasdsa.com";
        BigDecimal orderAmount = new BigDecimal(3000);
        String orderId = System.currentTimeMillis() / 1000 + "";
        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setChannelPassword("3ce5aca758860fbf0c26ef4839ac9785b5a262e25e8518d6eaabb14931bbbbac");
        channelInfo.setChannelAppId("53320");
        channelInfo.setChannelType("3308");
        channelInfo.setDealurl("https://apii.dkk888.com/api/pay_order");

        //      createOrder(dealOrderApp, notify, orderAmount, orderId, channelInfo);


    }


}
