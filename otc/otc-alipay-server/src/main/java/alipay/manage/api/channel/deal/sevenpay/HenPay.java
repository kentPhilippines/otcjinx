package alipay.manage.api.channel.deal.sevenpay;

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
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("Hen1Pay")
public class HenPay extends PayOrderService {
    private static final String MARS = "SHENFU";
    private static SimpleDateFormat d = new SimpleDateFormat("YYYYMMDDhhmmss");
    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Autowired
    private RedisUtil redis;
    @Autowired
    private OrderService orderServiceImpl;
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) {
        log.info("【进入恒支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
        String orderId = create(dealOrderApp, channel);
        UserInfo userInfo = userInfoServiceImpl.findDealUrl(dealOrderApp.getOrderAccount());
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
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/heng1Pay-bank-notify",
                dealOrderApp.getOrderAmount(),
                orderId,
                getChannelInfo(channel, dealOrderApp.getRetain1()), dealOrderApp, payInfo
        );
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));

            //     return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            orderEr(dealOrderApp, "错误消息：" + result.getMessage());
            return result;
        }
    }
    private Result createOrder(String notify, BigDecimal orderAmount, String orderId, ChannelInfo channelInfo, DealOrderApp dealOrderApp, String payInfo) {
        try {
            String merchId = channelInfo.getChannelAppId();
            String money = orderAmount.toString();
            String userId =getPayName(payInfo,orderId) ;
            orderId = orderId;
            String time = d.format(new Date());
            String notifyUrl = notify;
            String payType = channelInfo.getChannelType();
            String curType = "CNY";
            String reType = "LINK";
            String signType = "MD5";
            String payName = userId;
            Map<String, Object> map = new HashMap<>();
            map.put("merchId", merchId);
            map.put("money", money);
            map.put("userId", userId);
            map.put("orderId", orderId);
            map.put("time", time);
            map.put("notifyUrl", notifyUrl);
            map.put("payType", payType);
            map.put("curType", curType);
            map.put("reType", reType);
            map.put("signType", signType);
            map.put("payName", payName);
            String param = createParam(map);
            log.info("恒支付签名参数：" + param);
            String md5 = PayUtil.md5(param + "&md5_key=" + channelInfo.getChannelPassword());
            String sign = md5;
            map.put("sign", sign);
            log.info("恒支付请求参数：" + map.toString());
            String post1 = HttpUtil.post(channelInfo.getDealurl(), map);
            log.info("恒支付响应参数：" + post1);
            //  恒支付响应参数：{"merchId":"662022111914064912","orderId":"7699696969861198696","curType":"CNY","money":"888","code":"0000","msg":"SUCCESS","payUrl":"https:\/\/hengpay.cc\/Apipay\/checkpage\/ordernum\/466901318391797767","sign":"544d4f0d558c34e065aec03454d26328"}
//恒支付响应参数：{"merchId":"662022111914064912","orderId":"76996922226969861198696","curType":"CNY","money":"888","code":"0000","msg":"SUCCESS","payInfo":"{\"name\":\"胡正\",\"bank_card\":\"6230521380040099577\",\"bank\":\"农业银行\",\"bank_branch\":\"农行桃江县支行营业室\"}","payUrl":"http:\/\/api.sunnydoom.com\/pass\/?code=867090946232200249","sign":"c3c8ebffe0aeedc2d7bb112441dd15e3"}
            JSONObject resultObject = JSONUtil.parseObj(post1);
            if ("0000".equals(resultObject.getStr("code"))) {
                String returnUrl = resultObject.getStr("payUrl");//支付链接

                return Result.buildSuccessResult("支付处理中", returnUrl);
            } else {
                return Result.buildFailMessage("支付失败");
            }
        } catch (Exception e) {
            orderAppEr(dealOrderApp, "请求异常,联系技术处理");
            return Result.buildFailMessage("支付失败");
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
