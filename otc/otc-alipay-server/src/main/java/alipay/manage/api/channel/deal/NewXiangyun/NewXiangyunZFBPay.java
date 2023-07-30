package alipay.manage.api.channel.deal.NewXiangyun;

import alipay.config.redis.RedisUtil;
import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.channel.util.qiangui.MD5;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@Component("NewXiangyunZFBPay")
public class NewXiangyunZFBPay extends PayOrderService {
    @Autowired
    private UserInfoService userInfoServiceImpl;
    private static final String MARS = "SHENFU";
    @Autowired
    private RedisUtil redis;
    @Autowired
    private OrderService orderServiceImpl;
    @Value("${otc.payInfo.url}")
    public   String url;
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入NewXiangyunPay支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
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
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/newXiangyunPayNotify",
                dealOrderApp.getOrderAmount(),
                orderId,
                getChannelInfo(channel, dealOrderApp.getRetain1()), payInfo);
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            return result;
        }
    }

    private String name = "付款人：";

    private Result createOrder(String notify, BigDecimal orderAmount,
                               String orderId,
                               ChannelInfo channelInfo, String payInfo) throws IOException {
        String key = channelInfo.getChannelPassword();
        String payurl = channelInfo.getDealurl();
        String totalAmt = orderAmount.abs().toPlainString(); // 金额
        String payType = channelInfo.getChannelType(); // '银行编码
        String storeCode = channelInfo.getChannelAppId();// 商户id
        String storeOrderNo = orderId;// 20位订单号 时间戳+6位随机字符串组成
        String backUrl = notify;// 通知地址
        String notifyUrl = notify;// 通知地址
        String playerName = "";
        if (StrUtil.isNotEmpty(payInfo)) {
            String[] split = payInfo.split(name);
            String payName = split[1];
            playerName = payName;
        }
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("storeCode", storeCode);
        param.add("storeOrderNo", storeOrderNo);
        param.add("payType", payType);
        param.add("totalAmt", totalAmt);
        param.add("backUrl", backUrl);
        param.add("notifyUrl", notifyUrl);
        param.add("playerName", playerName);

        String originalStr = createParam(param.toSingleValueMap()) + "&key=" + key;
        String sign = MD5.MD5Encode(originalStr);
        param.add("sign", sign);
        log.info("请求canshu：" + JSONUtil.toJsonStr(param));
        RestTemplate restTemplate = new RestTemplate();
        String rString = restTemplate.postForObject(payurl, param.toSingleValueMap(), String.class);
        log.info("xiangyun请求结果：" + rString);
        Map<String, String> resultMap = JSONUtil.toBean(rString, Map.class);
        if (resultMap.containsKey("errorCode")) {
            orderDealEr(orderId,resultMap.get("errorMsg"));
            return Result.buildFailMessage(resultMap.get("errorMsg"));
        } else {

            String payUrl = resultMap.get("payUrl");
            return Result.buildSuccessResult("支付处理中", payUrl);
        }
    }

    private String createParam(Map<String, Object> map) {
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
