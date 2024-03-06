package alipay.manage.api.channel.deal.yf;

import alipay.config.redis.RedisUtil;
import alipay.manage.api.channel.util.qiangui.MD5;
import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import okhttp3.*;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@Component("YifuPay")
public class YfPay extends PayOrderService {
    @Autowired
    private UserInfoService userInfoServiceImpl;
    private static final String MARS = "SHENFU";
    @Autowired
    private RedisUtil redis;
    @Autowired
    private OrderService orderServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入yf   Pay支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
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
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/yfNotify",
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


        String key =channelInfo.getChannelPassword();
        String payurl = channelInfo.getDealurl();
        String money = orderAmount.intValue()+".00"; // 金额
        String payType = channelInfo.getChannelType(); // '银行编码
        String user = channelInfo.getChannelAppId();// 商户id
        String osn = orderId;//
        String notifyUrl = notify;


        MultiValueMap<String, String> param= new LinkedMultiValueMap<String, String>();
        param.add("merchantNo", user);
        param.add("customerNo", osn);
        param.add("userName", RandomStringUtils.randomAlphabetic(6));
        param.add("amount", money);
        param.add("returnType", "1");
        param.add("stype", payType);
        param.add("timestamp", new Date().getTime()+"");
        param.add("notifyUrl", notifyUrl);
        param.add("payIp", "127.0.0.1");

        String originalStr = createParam(param.toSingleValueMap())+"&key="+key;
        System.out.println(originalStr);
        String sign = MD5.MD5Encode(originalStr);
        param.add("sign", sign);

        param.add("depositName", RandomStringUtils.randomAlphabetic(5));
        param.add("realName", RandomStringUtils.randomAlphabetic(5));



        String requestJson = JSONUtil.toJsonStr(param.toSingleValueMap());
        log.info(requestJson);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(requestJson,mediaType);
        Request request = new Request.Builder()
                .url(payurl)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();

        String rString = response.body().string();
        log.info("result:{}",rString);
        JSONObject jsonObject = JSONUtil.parseObj(rString);
        if(jsonObject.getInt("code")==0)
        {
            JSONObject data =jsonObject.getJSONObject("data");
            String url = data.getStr("payUrl");
            return Result.buildSuccessResult("支付处理中", url);
        }else {
            return Result.buildFail();
        }
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
