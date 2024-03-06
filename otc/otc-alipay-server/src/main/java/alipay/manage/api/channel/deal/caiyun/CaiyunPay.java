package alipay.manage.api.channel.deal.caiyun;

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
import org.springframework.beans.factory.annotation.Autowired;
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

@Component("CaiyunPay")
public class CaiyunPay extends PayOrderService {
    @Autowired
    private UserInfoService userInfoServiceImpl;
    private static final String MARS = "SHENFU";
    @Autowired
    private RedisUtil redis;
    @Autowired
    private OrderService orderServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入caiyun    Pay支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
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
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/caiyunNotify",
                dealOrderApp.getOrderAmount(),
                orderId,
                getChannelInfo(channel, dealOrderApp.getRetain1()), payInfo);

        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            orderDealEr(orderId, result.getMessage());
            return result;
        }
    }

    private String name = "付款人：";

    private Result createOrder(String notify, BigDecimal orderAmount,
                               String orderId,
                               ChannelInfo channelInfo, String payInfo) throws IOException {
        String key =channelInfo.getChannelPassword();
//        String payurl = "https://paygate.lelipay.com:9043/lelipay-gateway-onl/txn";
        String payurl = channelInfo.getDealurl();
        String money = orderAmount.intValue()+".00"; // 金额
//        String payType = "QX801705"; // '银行编码
        String payType = channelInfo.getChannelType(); // '银行编码
//        String user = "988001944000367";// 商户id
        String user = channelInfo.getChannelAppId();// 商户id
        String osn = orderId;// 20位订单号 时间戳+6位随机字符串组成
        String notifyUrl = notify;
        String name = payInfo;

//        String key ="eac29f43410e8cecc210605ce52fb994";
//        String payurl = "http://hgapi2.facaishop.com/v1/order/create";
//        String money = "800.00"; // 金额
//        String payType = "QX801705"; //
//        String user = "35508142";// 商户id
//        String osn = RandomStringUtils.randomNumeric(10);// 20位订单号 时间戳+6位随机字符串组成
//        String notifyUrl = "http://34.92.251.112:9010/notfiy-api-pay/caiyun-notify";

        MultiValueMap<String, String> param= new LinkedMultiValueMap<String, String>();
        param.add("merchant_no", user);
        param.add("pay_code", payType);
        param.add("order_amount", money);
        param.add("order_no", osn);
        param.add("callback_url", notifyUrl);
        param.add("ts", DateTime.now().getTime()+"");

        String originalStr = createParam(param.toSingleValueMap())+"&key="+key;
        log.info(originalStr);
        String sign = MD5.MD5Encode(originalStr);
        param.add("sign", sign.toLowerCase());

        log.info(JSONUtil.toJsonStr(param.toSingleValueMap()));
        RestTemplate restTemplate = new RestTemplate();
        String rString = restTemplate.postForObject(payurl, param.toSingleValueMap(), String.class);
        log.info("result:{}",rString);
        JSONObject jsonObject = JSONUtil.parseObj(rString);
        if(jsonObject.getStr("code").equals("200"))
        {
            JSONObject data =jsonObject.getJSONObject("data");
            String url = data.getStr("pay_url");
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
