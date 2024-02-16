package alipay.manage.api.channel.deal.lelipay;

import alipay.config.redis.RedisUtil;
import alipay.manage.api.channel.util.qiangui.MD5;
import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

@Component("LeliCardPay")
public class LeliCardPay extends PayOrderService {
    @Autowired
    private UserInfoService userInfoServiceImpl;
    private static final String MARS = "SHENFU";
    @Autowired
    private RedisUtil redis;
    @Autowired
    private OrderService orderServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入lelicardPay支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
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
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/leliPayNotify",
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

    private String name1 = "付款人：";

    private Result createOrder(String notify, BigDecimal orderAmount,
                               String orderId,
                               ChannelInfo channelInfo, String payInfo) throws IOException {
        String key =channelInfo.getChannelPassword();
//        String payurl = "https://paygate.lelipay.com:9043/lelipay-gateway-onl/txn";
        String payurl = channelInfo.getDealurl();
        String money = orderAmount.multiply(new BigDecimal(100)).intValue()+""; // 金额
        String payType = "401"; // '银行编码
//        String user = "988001944000367";// 商户id
        String user = channelInfo.getChannelAppId();// 商户id
        String osn = orderId;// 20位订单号 时间戳+6位随机字符串组成
        String notifyUrl = notify;
        String name = payInfo;

        MultiValueMap<String, String> param= new LinkedMultiValueMap<String, String>();
        param.add("txnType", "01");
        param.add("txnSubType", "21");
        param.add("secpVer", "icp3-1.1");
        param.add("secpMode", "perm");
        param.add("macKeyId", user);
        param.add("orderDate", DateTime.now().toString("yyyyMMdd"));
        param.add("orderTime", DateTime.now().toString("HHmmss"));
        param.add("merId", user);
        param.add("orderId", osn);
        param.add("payerId", RandomStringUtils.randomNumeric(16));
        param.add("pageReturnUrl", notifyUrl);
        param.add("notifyUrl", notifyUrl);
        param.add("productTitle", "test");
        param.add("txnAmt", money);
        param.add("currencyCode", "156");
        param.add("timeStamp", DateTime.now().toString("yyyyMMddHHmmss"));
        param.add("cardType", "DT01");
        param.add("bankNum", "01020000");
        param.add("accName", name.replace(name1,""));
        param.add("sthtml", "1");
        String originalStr = createParam(param.toSingleValueMap())+"&k="+key;
        System.out.println(originalStr);
        String sign = MD5.MD5Encode(originalStr);
        param.add("mac", sign.toLowerCase());
        log.info(JSONUtil.toJsonStr(param.toSingleValueMap()));
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(param, headers);
        String rString = restTemplate.postForObject(payurl, request, String.class);
        log.info("result:{}",rString);
        JSONObject jsonObject = JSONUtil.parseObj(rString);
        if(jsonObject.getStr("respCode").equals("0000"))
        {
            String url = jsonObject.getStr("extInfo");
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
