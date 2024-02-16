package alipay.manage.api.channel.deal.linghang;

import alipay.config.redis.RedisUtil;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component("linhangPay")
public class linhangPay extends PayOrderService {
    static SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private UserInfoService userInfoServiceImpl;
    private static final String MARS = "SHENFU";
    @Autowired
    private RedisUtil redis;
    @Autowired
    private OrderService orderServiceImpl;
    @Value("${otc.payInfo.url}")
    public String url;
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入linghang支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
        String orderId = create(dealOrderApp, channel);
        UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
        if (StrUtil.isBlank(userInfo.getDealUrl())) {
            orderEr(dealOrderApp, "当前商户交易url未设置");
            return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
        }
        String payInfo = "";
        if (dealOrderApp.getDealDescribe().contains("付款人")) {
            payInfo = dealOrderApp.getDealDescribe();
        }
        Result result = createOrder(
                userInfo.getDealUrl() +
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/linghangNotify",
                dealOrderApp.getOrderAmount(),
                orderId,
                getChannelInfo(channel, dealOrderApp.getRetain1()), payInfo, dealOrderApp.getOrderAccount());
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            return result;
        }
    }
    private Result createOrder(String s, BigDecimal orderAmount, String orderId, ChannelInfo channelInfo, String payInfo, String orderAccount) {
        String code = "";
        JSONObject ruselt = new JSONObject();
        try {
            String key = channelInfo.getChannelPassword();
            String mchId = channelInfo.getChannelAppId();
            String subject = "购物";
            String wayCode = channelInfo.getChannelType();
            String outTradeNo = orderId;
            String amount = orderAmount.multiply(new BigDecimal(100)).setScale(BigDecimal.ROUND_UP).toString();
            String clientIp = "28.22.123.11";
            String notifyUrl = s;
            String reqTime = System.currentTimeMillis() + "";
            Map map = new HashMap();
            map.put("mchId", mchId);
            map.put("subject", subject);
            map.put("wayCode", wayCode);
            map.put("outTradeNo", outTradeNo);
            map.put("amount", amount);
            map.put("clientIp", clientIp);
            map.put("notifyUrl", notifyUrl);
            map.put("reqTime", reqTime);
            String sign = createParam(map) + "&key=" + key;
            sign = PayUtil.md5(sign).toUpperCase();
            map.put("sign", sign);
            JSONObject jsonObject = JSONUtil.parseObj(map);
            log.info("林航请求参数：" + jsonObject.toString());
            String post = HttpUtil.post(channelInfo.getDealurl(), jsonObject.toString());
            log.info("林航响应参数：" + post.toString());
            //         * {"code":0,"message":"ok","data":{"mchId":"M1685337144","tradeNo":"P1663057943442649088","outTradeNo":"86212d7e-fe12-4f69-98fc-90b0d1dc7ba9","originTradeNo":"1","amount":"20000","payUrl":"https://cdn1.onoz.xyz/api/payPage.html?id=5481553","expiredTime":"1685339186","sdkData":null},"sign":"e6f32fbe290583082347f48fd6a9b852"}
            ruselt = JSONUtil.parseObj(post);
            code = ruselt.getStr("code");
        }catch (Throwable t ){
            orderDealEr(orderId,"暂无合适通道，请更换金额重新拉单");
            return Result.buildFail();
        }
        if ("0".equals(code)){
            String payUrl = JSONUtil.parseObj(ruselt.getStr("data")).getStr("payUrl");
            return Result.buildSuccessResult(payUrl);
        }else {
            orderDealEr(orderId,"暂无合适通道，请更换金额重新拉单");
             return Result.buildFail();
        }
     }
    public static String createParam(Map<String, Object> map) {
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


}
