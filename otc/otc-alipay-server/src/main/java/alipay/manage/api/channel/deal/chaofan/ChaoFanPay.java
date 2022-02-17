package alipay.manage.api.channel.deal.chaofan;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;



@Component("ChaoFanPay")
public class ChaoFanPay extends PayOrderService {
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入chaofan支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
        String orderId = create(dealOrderApp, channel);
        UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
        if (StrUtil.isBlank(userInfo.getDealUrl())) {
            orderEr(dealOrderApp, "当前商户交易url未设置");
            return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
        }

        Result result = createOrder(
                userInfo.getDealUrl() +
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/chaofanPayNotify",
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
        String amount = orderAmount.intValue() + ".00";
        String channel = channelInfo.getChannelType();
        String url = channelInfo.getDealurl();
        String merchantTradeNo = orderId;
        String key = "acd01a514ea08f30e4b38c131bf921bf";
        Map<String, String> map = new TreeMap();
        map.put("service", channel);
        map.put("version", "1.0");
        map.put("merchantId",channelInfo.getChannelAppId());
        map.put("orderNo", merchantTradeNo);

//        map.put("tradeDate", "20220127");
        map.put("tradeDate", DateUtils.dateTime());
        map.put("tradeTime", DateUtils.time());
//        map.put("tradeTime", "123015");
        map.put("amount", amount);
        map.put("clientIp", "127.0.0.1");
        map.put("notifyUrl", notify);
        map.put("resultType", "json");
        map.put("sign", createSign(map, channelInfo.getChannelPassword()));

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
                Map<String, String> resultMap = SignUtils.parseQuery(content);
                log.info("请求结果：" + content);

                if (resultMap.containsKey("sign")) {
                    if (!SignUtils.checkParam(resultMap, key)) {
                        res = "验证签名不通过";
                    } else {
                        if (resultMap.get("repCode").equals("0001")) {
                            // 扫码使用此网址显示二维码
                            // 其他直接跳转至该网址
                            String resultUrl = resultMap.get("resultUrl");
                            return Result.buildSuccessResult("支付处理中", resultUrl);
                        }
                    }
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
        return Result.buildFailMessage("订单失败");
        //return Result.buildSuccessResult("支付处理中", "payUrl");
    }

    private static String createSign(Map map, String key) {
        Map<String, String> params = SignUtils.paraFilter(map);
        params.put("key", key);
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        String preStr = buf.toString();
        log.info("original:{}",preStr);
        String sign = MD5.sign(preStr, "UTF-8");
        log.info("sign:{}",sign);

        return sign;
    }
    //amount=1000.00&appId=jinsha888&applyDate=20220217152028&notifyUrl=http://34.96.135.66:5055/api-alipay&orderId=c4866ce376424932ae049ee3e8c2388b&pageUrl=http://34.96.135.66:5055/api-alipay&passCode=ALIPAYTOBANK&subject=1000.00&userid=王富贵

}
