package alipay.manage.api.channel.deal.huafei;

import alipay.manage.api.channel.deal.chaofan.HttpClient;
import alipay.manage.api.channel.deal.chaofan.MD5;
import alipay.manage.api.channel.deal.chaofan.SignUtils;
import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;
import otc.util.date.DateUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Component("HuaFeiPay")
public class HuafeiPay extends PayOrderService {
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入HuaFeiPay支付，当前请求产品：{}，当前请求渠道：{} 】", JSONUtil.toJsonStr(dealOrderApp),channel);
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
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/huafeiPayNotify",
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


    private Result createOrder(String notify, BigDecimal orderAmount,
                               String orderId,
                               ChannelInfo channelInfo) throws IOException {
        String amount = orderAmount.intValue()+"";
        String channel = channelInfo.getChannelType();
        String url = channelInfo.getDealurl();
        //String key = "SCSD7lwUYyv2xVCU9grlV2ByuRtJPmv3LbiGcEF6SLUWsao8k7rc8Y8HtWYgVgs3";
        String key = channelInfo.getChannelPassword().split(",")[0];
        String token = channelInfo.getChannelPassword().split(",")[1];
        //String token = "P7q2kLuGb0K0QQu7LGSkds7XG5Q0772m";
        Map<String, String> map = new TreeMap();
        map.put("mch_id", channelInfo.getChannelAppId());
        map.put("total", amount);
        map.put("out_trade_no", orderId);
        map.put("timestamp", new Date().getTime()/1000+"");
        //map.put("type", "9018");
        map.put("type", channel);
        map.put("return_url", notify);
        //map.put("notify_url", "http://152.32.107.70:802/chaofanPayNotify");
        map.put("notify_url", notify);
        map.put("sign", createSign(map, key,token));
        map.put("request_token", token);

        String reqUrl = url;
        log.info("reqUrl：" + reqUrl);

        CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
        String res = null;
        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            List<NameValuePair> nvps = new ArrayList();
            for (String k : map.keySet()) {
                nvps.add(new BasicNameValuePair(k, map.get(k)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            httpPost.addHeader("Connection", "close");
            client = HttpClient.createHttpClient();
            response = client.execute(httpPost);
            if (response != null && response.getEntity() != null) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                Map<String, Object> resultMap = JSONUtil.toBean(content,Map.class);
                log.info("请求结果：" + content);
                //{"code":"200","status":"10000","msg":"下单成功","data":{"mch_id":1105986,"out_trade_no":"12018011512301500007","total":100,"type":"9018","timestamp":1646580768,"qr_url":"http:\/\/47.104.134.244\/api\/Cashier?UserId=1005986&CustomerOrderId=G100226-2203067054025&PayType=unicomalipay&Parvalue=100&Sign=947f2eaf90f2bbb9c7caeb4751d7851d","h5_url":"http:\/\/47.104.134.244\/api\/Cashier?UserId=1005986&CustomerOrderId=G100226-2203067054025&PayType=unicomalipay&Parvalue=100&Sign=947f2eaf90f2bbb9c7caeb4751d7851d"}}
                if ("200".equals(resultMap.get("code")+"")) {
                    // 扫码使用此网址显示二维码
                    // 其他直接跳转至该网址
                    String resultUrl = JSONUtil.toBean(resultMap.get("data")+"",Map.class).get("qr_url")+"" ;
                    return Result.buildSuccessResult("支付处理中", resultUrl);
                }else if(resultMap.containsKey("error_msg"))
                {
                    res=resultMap.get("error_msg")+"";
                }
            } else {
                res = "操作失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = "系统异常";
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
        return Result.buildFailMessage(res);
        //return Result.buildSuccessResult("支付处理中", "payUrl");
    }

    private static String createSign(Map map, String key,String token)
    {
        Map<String, String> params = SignUtils.paraFilter(map);
        //params.put("key", key);
        //params.put("token", token);
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams1(buf, params, false);
        String preStr = buf.toString()+key+token;
        log.info("preStr:{}",preStr);
        String sign = MD5.sign(preStr, "UTF-8");

        return sign;
    }

    public static void main(String[] args) {
        String str="{\"code\":\"200\",\"status\":\"10000\",\"msg\":\"下单成功\",\"data\":{\"mch_id\":1105986,\"out_trade_no\":\"12018011512301500007\",\"total\":100,\"type\":\"9018\",\"timestamp\":1646580768,\"qr_url\":\"http:\\/\\/47.104.134.244\\/api\\/Cashier?UserId=1005986&CustomerOrderId=G100226-2203067054025&PayType=unicomalipay&Parvalue=100&Sign=947f2eaf90f2bbb9c7caeb4751d7851d\",\"h5_url\":\"http:\\/\\/47.104.134.244\\/api\\/Cashier?UserId=1005986&CustomerOrderId=G100226-2203067054025&PayType=unicomalipay&Parvalue=100&Sign=947f2eaf90f2bbb9c7caeb4751d7851d\"}}";
        Map<String, Object> resultMap = JSONUtil.toBean(str,Map.class);
        String resultUrl = JSONUtil.toBean(resultMap.get("data").toString(),Map.class).get("qr_url")+"" ;
    }
    //amount=1000.00&appId=jinsha888&applyDate=20220217152028&notifyUrl=http://34.96.135.66:5055/api-alipay&orderId=c4866ce376424932ae049ee3e8c2388b&pageUrl=http://34.96.135.66:5055/api-alipay&passCode=ALIPAYTOBANK&subject=1000.00&userid=王富贵

}
