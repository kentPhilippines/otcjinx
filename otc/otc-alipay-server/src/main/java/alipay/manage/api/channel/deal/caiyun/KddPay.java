package alipay.manage.api.channel.deal.caiyun;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.channel.util.yifu.YiFuUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component("KddPay")
public class KddPay extends PayOrderService {
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入恒利支付，当前请求产品：{}，当前请求渠道：{} 】", JSONUtil.toJsonStr(dealOrderApp),channel);
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
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/henglPayNotify",
                dealOrderApp.getOrderAmount(),
                orderId,channelInfo
                );
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            orderDealEr(orderId, result.getMessage());
            return result;
        }
    }

    //{"msg":"请求成功","code":0,"data":{"request_url":"http://web85sytpay.hengaofangfuwuo95.com/alipaycode.php?is_bzk=0&account_name=MVihPsgwtGEJf1u0te1x4A%3D%3D&bank_name=MVihPsgwtGEJf1u0te1x4A%3D%3D&account_number=xGFtbSbMPuRy93rWybniHJgpepfX2Xq6URkIhId6YpuTHLzeXfl94qZ%2ByWO5leIVbtghxtF%2B%2Fc2R11RoEmp6DA%3D%3D&trade_no=facccc18-2ced-4dd6-a1a1-66e33e665daf&order_pay_price=99.65&sign=9b703d629c20b061cdd39b58f9e87b4c&user=kD11g46NpjYA%2B0xLHA18BxTlSl3zHmq6tyiv69BYuxD4YL9OnX0CuVZffpszw1JH&remark=1077307&is_pay_name=1&initialization_h5=2"}}】
    private Result createOrder(String notify, BigDecimal orderAmount,
                               String orderId,
                               ChannelInfo channelInfo) throws IOException {
        String amount = orderAmount.intValue()+"";
        String channel = channelInfo.getChannelType();
        String url = channelInfo.getDealurl();
        //String key = "SCSD7lwUYyv2xVCU9grlV2ByuRtJPmv3LbiGcEF6SLUWsao8k7rc8Y8HtWYgVgs3";
//        String key = channelInfo.getChannelPassword().split(",")[0];
//        String token = channelInfo.getChannelPassword().split(",")[1];
        //String token = "P7q2kLuGb0K0QQu7LGSkds7XG5Q0772m";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("mchid", channelInfo.getChannelAppId());
//        map.put("channel", "alipayCodeSmall");
        map.put("channel", channel);
        map.put("out_trade_no", orderId);
        map.put("amount",  orderAmount.intValue());
        map.put("notify_url", notify);
        map.put("return_url", notify);
        map.put("body", "test");
        map.put("time_stamp", DateTime.now().toString("yyyyMMddHHmmss"));
//        String createParam = YiFuUtil.createParam(map)+ "key="+"8848bea76e619eb001bd1c034a7048a1";
        String createParam = YiFuUtil.createParam(map)+ "key="+channelInfo.getChannelPassword();
        log.info("【kdd付签名前参数："+createParam+"】");
        String md5 = YiFuUtil.md5(createParam );
        String sign = md5.toLowerCase();
        map.put("sign", sign);

        String reqUrl = url;
        log.info("reqUrl：" + reqUrl);

        String res = null;
        try {
            String post = HttpUtil.post(url, map);
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
        return Result.buildFailMessage(res);
        //return Result.buildSuccessResult("支付处理中", "payUrl");
    }


    public static void main(String[] args) {
        String str="{\"code\":\"200\",\"status\":\"10000\",\"msg\":\"下单成功\",\"data\":{\"mch_id\":1105986,\"out_trade_no\":\"12018011512301500007\",\"total\":100,\"type\":\"9018\",\"timestamp\":1646580768,\"qr_url\":\"http:\\/\\/47.104.134.244\\/api\\/Cashier?UserId=1005986&CustomerOrderId=G100226-2203067054025&PayType=unicomalipay&Parvalue=100&Sign=947f2eaf90f2bbb9c7caeb4751d7851d\",\"h5_url\":\"http:\\/\\/47.104.134.244\\/api\\/Cashier?UserId=1005986&CustomerOrderId=G100226-2203067054025&PayType=unicomalipay&Parvalue=100&Sign=947f2eaf90f2bbb9c7caeb4751d7851d\"}}";
        Map<String, Object> resultMap = JSONUtil.toBean(str,Map.class);
        String resultUrl = JSONUtil.toBean(resultMap.get("data").toString(),Map.class).get("qr_url")+"" ;
    }
    //amount=1000.00&appId=jinsha888&applyDate=20220217152028&notifyUrl=http://34.96.135.66:5055/api-alipay&orderId=c4866ce376424932ae049ee3e8c2388b&pageUrl=http://34.96.135.66:5055/api-alipay&passCode=ALIPAYTOBANK&subject=1000.00&userid=王富贵

}
