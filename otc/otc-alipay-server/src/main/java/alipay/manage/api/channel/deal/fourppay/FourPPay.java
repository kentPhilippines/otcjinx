package alipay.manage.api.channel.deal.fourppay;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.channel.util.yifu.YiFuUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component("FourPPay")
public class FourPPay extends PayOrderService {
    private static final String WIT_RESULT = "SUCCESS";
    @Autowired
    private UserInfoService userInfoServiceImpl;

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
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
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
        log.info("reqUrl：" + reqUrl);
        log.info("param：" + JSONUtil.toJsonStr(map));

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
            log.info("请求结果：" + post);
            if (StringUtils.isNotEmpty(post)) {
                JSONObject jsonObject = JSONUtil.parseObj(post);
                String code = jsonObject.getStr("code");
                if ("0000".equals(code)) {
                    String resultUrl = JSONUtil.toBean(jsonObject.get("data")+"",Map.class).get("upstreamLink")+"" ;
                    return Result.buildSuccessResult("支付处理中", resultUrl);
                }
            } else {
                res = "操作失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = "系统异常";
        }
        return Result.buildFailMessage(res);
    }

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
        channelInfo.setChannelPassword("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb2RlIjoiem4wMDQiLCJpYXQiOjE3MDEyNDcwMDgsImV4cCI6NDg1NDg0NzAwOCwiYXVkIjoiZm91ci1wYXJ0eTphdXRoIiwiaXNzIjoiZm91ci1wYXJ0eTphdXRoIiwic3ViIjoiMjIiLCJqdGkiOiJmb3VyLXBhcnR5LWp3dGlkIn0.Wj_RDQdDAHVku3SP7M-Dcr1hQ8uGGro4-DmcQVKdSZrb8-JC9l_Lb33nSZ_Eqx9U5BI8MMClnhBUXCcSBUk9e4E7Qcv0Sjh_TlYf4LdnGZr66MAlDXpXOpjAignE5YPhu-M-jGGwENeM56VxdM6eHLfRmU9advgVOor4yakf_s3htZj55SlFZNYK2-_MrIcje8qcB0SaqqGR_eR3YiwPwo744C-MxVLg-oWPpqTDGbSZh2QmC9R9QHRHpofnyue083po5EjJtRdkncWvcKqsh9TvVK7t4uDHwKK0wqVPp7A0YEkBt0Gc0gBBiczAjj648kQltr7eR2rUVu4JofM59rJflgvLuYvyv9HNChzh7kUGfI7DxSOvhqfcuJdltC4Im1Hb8tEkcZQ_VgLSZ8zqv6CMVTYlRby03h7K-SBPfFNK1NF-i_O2qpgKto86i7tvERY10iABmAfJ4I_pX7OBkJzXL5ZYM1_sytlE425SNLLcB-_8zb7S3Y3eCluWZASWBl41KL2FpURcNGjqVVuDd7jh4FZZpWs_KzXim4sLmGgDsI8c-yi6myTKnqZRQ0GKc8ti1BLZctqm5AYNp7EW5jN6ndfXK8UX3mze0v6_ODUeDpEqyiJF-kuH-UoqieKtwaQh3jSB3vj1xByKFl3MhFzcIy3JndqW26BS_Zs2U_I");
        channelInfo.setChannelAppId("zn004");
        channelInfo.setChannelType("2");
        channelInfo.setDealurl("https://api.65258723.com/v1/b2b/payment-orders/place-order");
        FourPPay pay = new FourPPay();
        Result order = pay.createOrder(dealOrderApp, notifyUrl, orderAmount, orderId, channelInfo);

        System.out.printf(order.toJson());
    }

}
