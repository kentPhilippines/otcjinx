package alipay.manage.api.channel.deal.pineaple;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.channel.util.qiangui.MD5;
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
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component("PineapplePay")
public class PineapplePay extends PayOrderService {
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入pineapple，当前请求产品：{}，当前请求渠道：{} 】", JSONUtil.toJsonStr(dealOrderApp),channel);
        String orderId = create(dealOrderApp, channel);
        UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
        log.info("userInfo:{}",JSONUtil.toJsonStr(userInfo));
        if (StrUtil.isBlank(userInfo.getDealUrl())) {
            orderEr(dealOrderApp, "当前商户交易url未设置");
            return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
        }
        ChannelInfo channelInfo = getChannelInfo(channel, dealOrderApp.getRetain1());
        log.info("channel:{},",JSONUtil.toJsonStr(channelInfo));
        Result result = createOrder(
                userInfo.getDealUrl() +
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/pineapplePayNotify",
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

    //{"msg":"success","code":200,"data":{"pc_pay_url":"http://api.55880.top/gateway/selfpay/paycode.do?id=4537308","qrcode_url":"http://api.55880.top/gateway/selfpay/paycode.do?id=4537308","qrcode":"http://api.55880.top/gateway/selfpay/paycode.do?id=4537308","trade_no":"4613820230628535252","sdk":"","order_id":"4537308","pay_url":"http://api.55880.top/gateway/selfpay/paycode.do?id=4537308"}}】
    private Result createOrder(String notify, BigDecimal orderAmount,
                               String orderId,
                               ChannelInfo channelInfo) throws IOException {
        String amount = orderAmount.intValue()+"";
        String channel = channelInfo.getChannelType();
        String merchantId = channelInfo.getChannelAppId();
        String url = channelInfo.getDealurl();
        //String key = "SCSD7lwUYyv2xVCU9grlV2ByuRtJPmv3LbiGcEF6SLUWsao8k7rc8Y8HtWYgVgs3";
//        String key = channelInfo.getChannelPassword().split(",")[0];
//        String token = channelInfo.getChannelPassword().split(",")[1];
        //String token = "P7q2kLuGb0K0QQu7LGSkds7XG5Q0772m";
        String key =channelInfo.getChannelPassword();
        String payurl = channelInfo.getDealurl();
        String money = amount; // 金额
        String payType = channel; // '银行编码
        String user = merchantId;// 商户id
        String osn = orderId;
        String notifyUrl = notify;



        Map<String,Object> param = new HashMap<String,Object>();
        param.put("mchid", user);
        param.put("out_trade_no", osn);
        param.put("amount", money);
        param.put("channel", payType);
        param.put("return_url", notifyUrl);
        param.put("time_stamp", DateTime.now().toString("YYYYMMDDhhmmss"));
        param.put("notify_url", notifyUrl);
        param.put("body", "test");
        String originalStr = createParam(param)+"&key="+key;
        System.out.println(originalStr);
        String sign = MD5.MD5Encode(originalStr).toLowerCase();
        param.put("sign", sign);

        String reqUrl = payurl;
        log.info("reqUrl：" + reqUrl);

        String res = null;
        try {
            String post = HttpUtil.post(reqUrl, param);
            log.info("请求结果：" + post);
            if (StringUtils.isNotEmpty(post)) {
                JSONObject jsonObject = JSONUtil.parseObj(post);
                String code = jsonObject.getStr("code");
                if ("0".equals(code)) {
                    String resultUrl = JSONUtil.toBean(jsonObject.get("data")+"",Map.class).get("request_url")+"" ;
                    return Result.buildSuccessResult("支付处理中", resultUrl);
                }
            } else {
                res = "操作失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = "系统异常";
        }
        //{"code":0,"msg":"\u8bf7\u6c42\u6210\u529f","data":{"request_url":"http:\/\/101.34.216.142\/CnyNumber.php?is_bzk=0&account_name=ACSHKko9LrA2s3S9mPBJxA%3D%3D&bank_name=jomLb6rwrlgRoxxEWcqj9Q%3D%3D&account_number=6VR7arFw%2FBiVu9L2KzE8%2FrKFJlqU1nolGVuNLIBA3bQ%3D&trade_no=6123139761&order_pay_price=99.98&sign=7ce3f1fb4fbab6430f20f5e652bb391f&user=mxDcQroBKsZt2YGVYY9KlyBnk7k9n0YITC3MlLxlRmyO7niR5PElEmruMbEJ%2B0d5&remark=1427423&is_pay_name=1"}}
        return Result.buildFailMessage(res);
        //return Result.buildSuccessResult("支付处理中", "payUrl");
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
