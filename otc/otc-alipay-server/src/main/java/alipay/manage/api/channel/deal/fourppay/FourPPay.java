package alipay.manage.api.channel.deal.fourppay;

import alipay.config.redis.RedisUtil;
import alipay.manage.api.channel.util.yifu.YiFuUtil;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component("FourPPay")
public class FourPPay extends PayOrderService {
    private static final String WIT_RESULT = "SUCCESS";
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Autowired
    private RedisUtil redis;
    @Autowired
    private OrderService orderServiceImpl;
    @Value("${otc.payInfo.url}")
    public String url_pay;
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入4ppay，当前请求产品：{}，当前请求渠道：{} 】", JSONUtil.toJsonStr(dealOrderApp),channel);
        String orderId = create(dealOrderApp, channel);
        UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
        log.info("userInfo:{}",JSONUtil.toJsonStr(userInfo));
        if (StrUtil.isBlank(userInfo.getDealUrl())) {
            orderEr(dealOrderApp, "当前商户交易url未设置");
            return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
        }
        ChannelInfo channelInfo = getChannelInfo(channel, dealOrderApp.getRetain1());
        log.info("channel:{},",JSONUtil.toJsonStr(channelInfo));
        Result result = createOrder(dealOrderApp,
                userInfo.getDealUrl() +
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/fourPPayNotify",
                dealOrderApp.getOrderAmount(),
                orderId,channelInfo
        );
        if (result.isSuccess()) {
            String payInfo1 = "";
            try {
                Map map = new HashMap();
                Map<Object, Object> hmget = redis.hmget(MARS + orderId);
                log.info(hmget.toString());
                if (ObjectUtil.isNotNull(hmget)) {
                    Object bank_name = hmget.get("bank_name");
                    Object card_no = hmget.get("card_no");
                    Object card_user = hmget.get("card_user");
                    Object money_order = hmget.get("money_order");
                    Object address = hmget.get("address");
                    map.put("amount", money_order);
                    map.put("bankCard", card_no);
                    map.put("bankName", bank_name);
                    map.put("name", card_user);
                    map.put("bankBranch", address);
                    JSONObject jsonObject = JSONUtil.parseFromMap(map);
                    payInfo1 = jsonObject.toString();
                }
            } catch (Throwable e) {
                log.error(e);
                log.info("详细数据解析异常，当前订单号：" + dealOrderApp.getAppOrderId());
                return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
            }
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrlAndPayInfo1(result.getResult(), result.getMessage(), payInfo1));
        } else {
            orderEr(dealOrderApp, result.getMessage());
            return result;
        }
    }
    private Result createOrder(DealOrderApp dealOrderApp,String notify, BigDecimal orderAmount,
                               String orderId,
                               ChannelInfo channelInfo) throws IOException {
        String amount = orderAmount.intValue()+"";
        String channel = channelInfo.getChannelType();
        String merchantId = channelInfo.getChannelAppId();
        String url = channelInfo.getDealurl();

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("merchantCode", merchantId);
        map.put("channelTypeId", new Integer(channel));
        map.put("merchantOrderId",orderId);
        map.put("amount",  amount);
        map.put("notifyUrl", notify);
        map.put("payerName", dealOrderApp.getPayName());
        String createParam = YiFuUtil.createParam(map)+ ""+channelInfo.getChannelPassword();
        System.out.println("【fourp 签名前参数："+createParam+"】");
        String md5 = YiFuUtil.md5(createParam );
        String sign = md5.toLowerCase();
        map.put("sign", sign);

        String reqUrl = url;
        log.info(" 4ppay reqUrl：" + reqUrl);
        log.info(" 4ppay param：" + JSONUtil.toJsonStr(map));

        //{
        //    "code": "0000",
        //    "data": {
        //        "systemOrderId": "LHPL00000201",
        //        "upstreamOrderId": "P01037920231203012606385",
        //        "upstreamLink": "https://tggwer23.com/orders/a92023d1-fa33-4001-a32b-1ff32f05beef?token=eyJhbGciOiJSUzI1NiJ9.eyJvcmRlcl9pZCI6ImE5MjAyM2QxLWZhMzMtNDAwMS1hMzJiLTFmZjMyZjA1YmVlZiJ9.NrGTACGRm9owRK4FLDXpt3rP25fzvd_JN4x2U9GrLDjm8dvwiI1UxaE-iKOFSnhBIwOeX2oGyss0jAqOdHSu1u0sowZqXa_68yWure2ZwZKfPhag4T5YoZSYL44D1PsWmC_MVOJEkbqLzYRWZqaNY3j7lFuQ_MuLZF0mfj59jteIbN6NRuxX61X1uSjyjJ3_yGndDlbne0bz_EOXWzLGbSa6NUw-LdAUkYv8x2gGOud-zzWojkb-xHDdJC5ZBmEYuxE_chjSb6_c3WPyeh4K_n7ix5zv3EGEIiM3A6kC6txvZgBelOExNNpwZHC09I1lvOMmGIlws7Sy6mrcedTZSg"
        //    }
        //}
        String res = null;
        try {
            String post = HttpUtil.post(url, JSONUtil.toJsonStr(map));
            log.info(" 4ppay 请求结果：" + post);
            if (StringUtils.isNotEmpty(post)) {
                JSONObject jsonObject = JSONUtil.parseObj(post);
                String code = jsonObject.getStr("code");
                if ("0000".equals(code)) {
                    /**
                     * {"code":"0000",
                     *                  * "data":{
                     *                  * "systemOrderId":"LHPL00007903",
                     *                  * "amount":4001,
                     *                  * "displayAmount":4001,
                     *                  * "upstreamLink":"",
                     *                  * "cardName":"韦耀庆",
                     *                  * "cardAccount":"6217852000014693736",
                     *                  * "cardBank":"中国银行",
                     *                  * "cardBranch":""}}
                     */
                    try {
                        String cardBank = JSONUtil.parseObj(post).getJSONObject("data").getStr("cardBank");
                        String resultUrl = JSONUtil.toBean(jsonObject.get("data")+"",Map.class).get("upstreamLink")+"";
                        if(StrUtil.isNotEmpty(resultUrl) && StrUtil.isEmpty(cardBank)){
                            return Result.buildSuccessResult("支付处理中", resultUrl);
                        }
                    }catch ( Throwable e ){
                        String resultUrl = JSONUtil.toBean(jsonObject.get("data")+"",Map.class).get("upstreamLink")+"";
                        if(StrUtil.isNotEmpty(resultUrl)  ){
                            return Result.buildSuccessResult("支付处理中", resultUrl);
                        }
                    }
                    Map cardmap = new HashMap();
                    cardmap.put("bank_name", JSONUtil.parseObj(post).getJSONObject("data").getStr("cardBank"));
                    cardmap.put("card_no", JSONUtil.parseObj(post).getJSONObject("data").getStr("cardAccount"));
                    cardmap.put("card_user", JSONUtil.parseObj(post).getJSONObject("data").getStr("cardName"));
                    cardmap.put("money_order", orderAmount);
                    cardmap.put("no_order", orderId);
                    cardmap.put("oid_partner", orderId);
                    cardmap.put("address", JSONUtil.parseObj(post).getJSONObject("data").getStr("cardBranch"));
                    orderServiceImpl.updateBankInfoByOrderId(dealOrderApp.getPayName() + " 收款信息：" +
                            JSONUtil.parseObj(post).getJSONObject("data").getStr("cardBank") + ":" +
                            JSONUtil.parseObj(post).getJSONObject("data").getStr("cardAccount")+ ":" +
                            JSONUtil.parseObj(post).getJSONObject("data").getStr("cardName"), orderId);
                    redis.hmset(MARS + orderId, cardmap, 600);
                    return Result.buildSuccessResult(
                            JSONUtil.parseObj(post).getJSONObject("data").getStr("cardBank") + ":" +
                                    JSONUtil.parseObj(post).getJSONObject("data").getStr("cardAccount") + ":" +
                                    JSONUtil.parseObj(post).getJSONObject("data").getStr("cardName"),
                            url_pay + "/pay?orderId=" + orderId + "&type=203");

                   /* String resultUrl = JSONUtil.toBean(jsonObject.get("data")+"",Map.class).get("upstreamLink")+"" ;
                    return Result.buildSuccessResult("支付处理中", resultUrl);*/
                }
            } else {
                res = "操作失败";
                orderEr(orderId,post);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = "系统异常";
            orderEr(orderId,res);
        }
        return Result.buildFailMessage(res);
    }
    private static final String MARS = "SHENFU";

    public static void main(String[] args) throws Exception {
        DealOrderApp dealOrderApp;
        String notifyUrl = "http://34.92.251.112:9010/notfiy-api-pay/fourPPayNotify";
        BigDecimal orderAmount;
        String orderId;
        ChannelInfo channelInfo;
        dealOrderApp = new DealOrderApp();
        dealOrderApp.setBack("http://dsada.asddad.adsdasdas");
        orderAmount = new BigDecimal(1000);
        orderId = System.currentTimeMillis() + "";
        channelInfo = new ChannelInfo();
        channelInfo.setChannelPassword("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb2RlIjoiY24wMTIiLCJpYXQiOjE3MDQ4OTk4MzcsImV4cCI6NDg1ODQ5OTgzNywiYXVkIjoiZm91ci1wYXJ0eTphdXRoIiwiaXNzIjoiZm91ci1wYXJ0eTphdXRoIiwic3ViIjoiNDQiLCJqdGkiOiJmb3VyLXBhcnR5LWp3dGlkIn0.MjoPPl-z7yHzIH_VXdGhGSVpUdBYhnS7IqupK7OjCBPRVayBfnstsb5r8V6EysXRu3p8cX2N5R0E9hHlSlMi9uYCyvg5dATlBp_zL3jtNWDt2b4qIT0aysgK2Weywqp7LYz0OIcqYBLyJ1OIuLfSggd6xfWL3ZM7p1lp2FFNPLdc7rHUoLaFuKTARDhoGEpDx8o25OoLOLuquO_w8drDS-xTb11D5FWBz8t-SDDKinKDjEZmpAhtGamZ4ByH1oG6nL7-XqTz8rIrhReM0FAaFw6ZW5fWjf53l7C0dvr8GIGAJFaD7fjh-imDUvHop8Mog4iieNk1LQW3s_3LdZwNkjvPOpdWHpY0lZJI_Ahp0RYX6WdsmOP0AOUw7iibYzodZSHDb7I_kLmziFaQwJIAZe7uWSR7fE_a0aNw3FMavtevmaD1tyHCjo3vyrw0jaQCPKX04WwJYarbzwexd5_51-QrfBGc2leink3acv7zq8xMzUEj0_c-7UvOx5bS8yMK2iZ-hEa4tbPj7eDQ0wmpOr8knqDcPttrvxNNM69wJEInZJPsR7yiJI5OkCUpo-ybestwLJ6GxcnltFupGD8COE7QLCWIUZv5cSO8y7S_EZxRH87iXLHYa9wDnye7CtiRgQv5HJsconS4BI80h4ZPhnAqfLj76ew4uzIEVR-R0vU");
        channelInfo.setChannelAppId("cn012");
        channelInfo.setChannelType("2");
        channelInfo.setDealurl("https://api.65258723.com/v1/b2b/payment-orders/place-order");
        FourPPay pay = new FourPPay();
        Result order = pay.createOrder(dealOrderApp, notifyUrl, orderAmount, orderId, channelInfo);

        System.out.printf(order.toJson());
    }

}
