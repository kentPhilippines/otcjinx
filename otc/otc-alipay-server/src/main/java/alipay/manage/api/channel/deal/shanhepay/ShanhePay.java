package alipay.manage.api.channel.deal.shanhepay;

import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Component("ShanhePay")
@Slf4j
public class ShanhePay extends PayOrderService {
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入ShanhePay支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
        String orderId = create(dealOrderApp, channel);
        UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
        if (StrUtil.isBlank(userInfo.getDealUrl())) {
            orderEr(dealOrderApp, "当前商户交易url未设置");
            return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
        }

        Result result = createOrder(
                userInfo.getDealUrl() +
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/shanhePayNotify",
                dealOrderApp.getOrderAmount(),
                orderId,
                getChannelInfo(channel, dealOrderApp.getRetain1()));
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            orderEr(dealOrderApp, result.getMessage());
            return result;
        }
    }

    private Result createOrder(String notify, BigDecimal orderAmount,
                               String orderId,
                               ChannelInfo channelInfo) throws IOException {
        String payurl = channelInfo.getDealurl();
        String payAmount = orderAmount.intValue()+""; // 金额
        String tradeType = channelInfo.getChannelType(); // '银行编码
        String mchId = channelInfo.getChannelAppId();// 商户id
        String outTradeNo = orderId;// 20位订单号 时间戳+6位随机字符串组成
        String nonceStr = UUID.randomUUID().toString();//
        String notifyUrl = notify;// 通知地址

        //String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCF3utQV6OJHCzwHnSRAay1ra3y8kzPbanfadbWiotZyVSZPcP5oFdg1JhQ4gwwMZBhnAn/0MozO0XeakY+ey6IeIyHdpoeamDLCop/DT3PQXSDhFpzSPvdKnB6U6izCLLlrYX15j3EQ5JNh4WgCFbI1nuLZJCJANjamIJMLIi9B0cpKLewDTjCFSb7KS60WZ2IY3NviGEGAc6aYUWVzPWsoIrrb/8QAmTXMRxe28aGgIurzh0QGd/CIXrT6ERIum2Q5i3uluw/MLDIuzmto+91RBiQg6Ybv2PgwaM8Sj6xs/zESG1OkeuucIFqGTV7FZbrjFy1DD5eGWwgBgUu6DJxAgMBAAECggEAC1t2K0LEJQW26kgrK4Iw3Nj3QP09dvuexc185Iase9mQy89pqOvpfdWLdpE253/M9/r8/i8AeIg2zT/G5dYdhIZ1pahyOtJbk945Eb5V2Bd9gwrfgZhXhdPr5vTiAw5h3wpxqwL1iokRCHLO61zJwBAyOwk1Gepwe1sk8sF0eFTcsUV0tHDvCD4WpkltrpUl4krkKVy04t+dDtdVfWgJRMhD7kfWLQnpO2/CpSvXHtjzy2zkbY/alt3pGmk5NAxg6Y41zDlZ0t2FlniukpIX36kpFRUbYigH0ydzTPyrHKP6rie1RfBxomOKIJYcodZ8hm7EeM/pUB5+3jifQzQAAQKBgQD/Nsp2bFSzffiPM+CKqYWN6WEpmzppobmlu3V1dhmxZkMd0HD0EjmW046TY+2OA6iulqj99uGH8N19jcCjZ/YztWsjI6bn6RdPbTzB2dQhezEp6IxteNiN/5mY2kJ5OJYSzkoG2igkT4sO4Wuv5txI//ue0N5DnH7sB0KolphocQKBgQCGSHZKYsVAsx2/ugUNBSxCHDXtjBGjbabT2qrI6DGrjtu8XdyU5Xg3e6t2tEcpp05ZYbqtKrQZoECs9b+WsJ/mVoxiWMoq0YZjwIAT2yXihf9gL/fvvOWjo07Jt9sycWxnGZz/y3KjgsGCjRZCit8imOeJmwugJFQxbAhbrqFqAQKBgQCdsA/mncaz5Jioeog5hMa1SUa0e2nbDIb1ZN8NRXxYhPPhPwIbfAtDKtIENZD41z/pJ8Ogr2LoKKXhxq0JCdowVt6spoGg9v9lHvyUVe/hBAn7d+kUVr+O9SfFLs41sgXf9r/8fdyhmtgzyIpN9BmVrTyeAzXhlpdBZLcGDY57sQKBgG9VqwX3qHYBTBwHnmJyNITHoQrIRGOM6XHjbhBPO8dzFcqyw82MCEVwOvSbehkWB0biWYVzz4kjrhv5URUeciTnA1QTK+Oeft873rUX0OxkjxzwCJBzvK4VG6Dx2EKVefxtZxdKVk0tf0W/toILY//qaKZVr6jiFhA38yIYwuoBAoGAUJ4tTS8NiB3vCYV9uLzTGU+UyAIzCzObXjbkb7DwperzrawPHQh/iTFPHhQQHyVjp1yQaYONNmuhLZTjIbu7XwlfQvpDaSEY9/pBcgRvR5MFjHpIK7ACRhiBJhFXfbvq0b3zbTVY5lFvUQDzH3beZU5o6PZtSesLJZwLcXi2hjE=";// 用户私钥
        String privateKey = channelInfo.getWitUrl().split(",")[0];// 用户私钥
        StringBuffer s = new StringBuffer();
        s.append("mchId=" + mchId + "&");
        s.append("outTradeNo=" + outTradeNo + "&");
        s.append("payAmount=" + payAmount + "&");
        s.append("nonceStr=" + nonceStr + "&");
        s.append("tradeType=" + tradeType + "&");
        s.append("notifyUrl=" + notifyUrl);
        log.info("request str:{},{}",s.toString(),privateKey);
        String sign = RSASignature.sign(s.toString(), privateKey);//
        log.info("request sign:{}",sign);

        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();

        param.add("sign", sign);

        param.add("mchId", mchId);
        param.add("outTradeNo", outTradeNo);
        param.add("payAmount", payAmount);
        param.add("nonceStr", nonceStr);
        param.add("tradeType", tradeType);
        param.add("notifyUrl", notifyUrl);


        RestTemplate restTemplate = new RestTemplate();
        String rString = restTemplate.postForObject(payurl, param, String.class);
        System.out.println(rString);
        JSONObject josn = new JSONObject();
        josn = josn.parseObject(rString);
        if (josn.getInteger("code") == 200) {
            //业务处理
            String resultUrl = josn.getString("redirect");
            return Result.buildSuccessResult("支付处理中", resultUrl);
        }
        return null;
    }

    public String generateOrderId() {
        String keyup_prefix = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String keyup_append = String.valueOf(new Random().nextInt(899999) + 100000);
        String pay_orderid = keyup_prefix + keyup_append;// 订单号
        return pay_orderid;
    }
}
