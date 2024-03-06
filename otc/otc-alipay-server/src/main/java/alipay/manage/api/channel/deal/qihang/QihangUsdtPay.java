package alipay.manage.api.channel.deal.qihang;

import alipay.config.redis.RedisUtil;
import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component("QihangPay")
public class QihangUsdtPay extends PayOrderService {
    @Autowired
    private UserInfoService userInfoServiceImpl;
    private static final String MARS = "SHENFU";
    @Autowired
    private RedisUtil redis;
    @Autowired
    private OrderService orderServiceImpl;
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入qihangPay支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
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
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/qihang-pay-notify",
                dealOrderApp.getOrderAmount(),
                orderId,
                getChannelInfo(channel, dealOrderApp.getRetain1()), payInfo);
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            orderEr(dealOrderApp, result.getMessage());
            return result;
        }
    }

    private String name = "付款人：";
    private Result createOrder(String notify, BigDecimal orderAmount,
                               String orderId,
                               ChannelInfo channelInfo, String payInfo) throws IOException {

        String signKey = channelInfo.getDealurl();
        String key = channelInfo.getChannelPassword();
        String order_no = orderId;
        String amount = orderAmount.toString();
        String ip = "127.0.0.1";
        String mid = channelInfo.getChannelAppId();
        String time = System.currentTimeMillis() / 1000 + "";
        String notify_url = notify;
        Map headers = new HashMap();
        headers.put("Authorization", "api-key " + key.trim());
        headers.put("Content-Type", "application/json");
        Map<String, String> body = new HashMap();
        body.put("order_no", order_no);
        body.put("gateway", channelInfo.getChannelType());
        /*body.put("coin_type", "USDT_TRC20");*/
        body.put("amount", amount);
        body.put("ip", ip);
        body.put("time", time);
        body.put("mid", mid);
        body.put("notify_url", notify_url);
        String param1 = createParam(body);
        System.out.println("加密前串为：" + param1);
        String sign1 = sign( signKey.trim(),param1);
        body.put("sign", sign1);
        JSONObject param = JSONUtil.parseFromMap(body);
        System.out.println("请求参数为：" + param);
        HttpResponse execute = HttpRequest.post(channelInfo.getWitUrl())
                .addHeaders(headers).body(param).execute();
        String ruselt = execute.body().toString();
        JSONObject resultMap = JSONUtil.parseFromXml(ruselt);
        if(resultMap.containsKey("ErrorResponse"))
        {
            JSONObject errorResponse = resultMap.getJSONObject("ErrorResponse");
            return Result.buildFailMessage(errorResponse.getStr("message")+"");
        }
        JSONObject successResponse = resultMap.getJSONObject("SuccessResponse");
        if(successResponse.getInt("code")!=200)
        {
            return Result.buildFailMessage(successResponse.getInt("code")+"");
        }
        log.info("QihangUsdtPay result:{}" ,ruselt);
        //<SuccessResponse><code>200</code><message>OK</message><data><no>9820220911141703765636665</no><action>jump</action><amount>100.000000</amount><url>https://ybf.usdtlab.net/zh/?no=AD13820220911141703783976834</url><order_no>358776053</order_no></data></SuccessResponse>
        String payUrl = successResponse.getJSONObject("data").getStr("url");
        return Result.buildSuccessResult("支付处理中", payUrl);
    }
    public static String sign(String secretKey, String data) {

        byte[] bytes = HmacUtils.hmacSha1(secretKey, data);
        return Base64.getEncoder().encodeToString(bytes);
    }
    private String createParam(Map<String, String> map) {
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
