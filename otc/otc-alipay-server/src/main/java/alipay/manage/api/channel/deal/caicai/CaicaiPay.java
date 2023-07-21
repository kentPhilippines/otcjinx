package alipay.manage.api.channel.deal.caicai;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.net.MediaType;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static alipay.manage.api.channel.util.shenfu.PayUtil.URL;
import static cn.hutool.core.lang.Console.log;

@Component("CaicaiPay")
public class CaicaiPay extends PayOrderService {
    @Autowired
    private UserInfoService userInfoServiceImpl;
    private static SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入财财支付】");
        String orderId = create(dealOrderApp, channel);
        if (StrUtil.isNotBlank(orderId)) {
            log.info("【本地订单创建成功，开始请求远程三方支付】");
            UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
            if (StrUtil.isBlank(userInfo.getDealUrl())) {
                orderEr(dealOrderApp, "当前商户交易url未设置");
                return Result.buildFailMessage("请联系运营为您的商户号设置交易url");
            }
            log.info("【回调地址ip为：" + userInfo.getDealUrl() + "】");
            String url = createOrder(userInfo.getDealUrl() + PayApiConstant.Notfiy.NOTFIY_API_WAI +
                            "/caicai-notfiy",
                    dealOrderApp.getOrderAmount(),
                    orderId,
                    getChannelInfo(channel, dealOrderApp.getRetain1())
            );
            if (StrUtil.isNotEmpty(url)) {
                return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(url));
            }
        }
        return Result.buildFailMessage("支付错误");
    }

    private static String createOrder(String notify,
                                      BigDecimal orderAmount,
                                      String orderId,
                                      ChannelInfo channelInfo
    ) throws IOException {
        String mchid = channelInfo.getChannelAppId();
        String out_trade_no = orderId;
        String amount = orderAmount.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
        String channel = channelInfo.getChannelType();
        String notify_url = notify;
        String return_url = notify;
        String time_stamp = d.format(new Date());
        String body = "21";

        Map<String, Object> map = new HashMap<>();
        map.put("mchid", mchid);
        map.put("out_trade_no", out_trade_no);
        map.put("amount", amount);
        map.put("channel", channel);
        map.put("notify_url", notify_url);
        map.put("return_url", return_url);
        map.put("time_stamp", time_stamp);
        map.put("body", body);
        Object[] objects = map.keySet().toArray();
        for (Object o : objects) {
            System.out.println(o.toString());
        }
        System.out.println(objects.toString());
        Arrays.sort(objects);
        System.out.println(objects.toString());
        StringBuilder sb = new StringBuilder();
        for (Object o : objects) {
            sb.append(o).append("=").append(map.get(o.toString())).append("&");
        }
        String sign = sb.substring(0, sb.length() - 1).toString();
        System.out.println(sign.toString());



        sign =   sign+"&key="+channelInfo.getChannelPassword();
        log("签名数据：" + sign.toString());
        String md5 = PayUtil.md5(sign).toLowerCase(Locale.ROOT);
        map.put("sign", md5);
        log("请求数据：" + map.toString());
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        RequestBody body1 = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("amount", amount)
                .addFormDataPart("mchid", mchid)
                .addFormDataPart("out_trade_no", out_trade_no)
                .addFormDataPart("time_stamp", time_stamp)
                .addFormDataPart("channel", channelInfo.getChannelType())
                .addFormDataPart("sign", sign)
                .addFormDataPart("return_url", return_url)
                .addFormDataPart("notify_url", notify)
                .addFormDataPart("body", body)
                .build();
        Request request = new Request.Builder()
                .url(channelInfo.getDealurl() + "/api/pay/unifiedorder")
                .method("POST", body1)
                .build();
        Response response = client.newCall(request).execute();
        ResponseBody body2 = response.body();
        InputStream inputStream = body2.byteStream();
        String s = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
        log("响应数据：" + s.toString());
//链式构建请求
      /*  String result2 = HttpRequest.post(channelInfo.getDealurl() + "/api/pay/unifiedorder")
                .header(Header.CONTENT_TYPE, "form-data")//头信息，多个头信息多次调用此方法即可

                .form(map)//表单内容
                .timeout(20000)//超时，毫秒
                .execute().body();
        log.info("响应：" + result2);*/


        return "";
    }

    /**
     * mchid	                string	是	是	商户id
     * out_trade_no	            string	是	是	商户订单id 唯一 不能重复 重复会创建订单失败 由商户控制
     * amount	                double	是	是	金额 最大两位小数
     * channel	                string	是	是	渠道 见附件 渠道列表
     * notify_url	            string	是	是	异步通知地址 必须http:或者https:格式
     * return_url	            string	是	是	同步通知地址 必须http:或者https:格式
     * time_stamp	            string	是	是	时间如20180824030711
     * body	                    string	是	是	宝转卡 必须传支付宝用户名 否则无法到账其它请填１２３
     * sign	                    string	是	否	签名 见签名规则
     */


    public static void main(String[] args) throws IOException {
        String notify = "http://www.bai22222du.com";
        String orderId = "HH2222111222222290099";
        BigDecimal amount = new BigDecimal(300);
        ChannelInfo info = new ChannelInfo();
        info.setChannelAppId("100747");
        info.setChannelPassword("6923e9be6f4cdcf142a3a43d864bc4c7");
        info.setDealurl("http://webkszf164api.koubofeixingq.com");
        info.setChannelType("alipayPassRed");
        createOrder(notify, amount, orderId, info);
    }
}
