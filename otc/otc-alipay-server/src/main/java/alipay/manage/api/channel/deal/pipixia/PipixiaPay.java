package alipay.manage.api.channel.deal.pipixia;

import alipay.config.redis.RedisUtil;

import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Component("PipixiaPay")
public class PipixiaPay extends PayOrderService {
    @Autowired
    OrderService orderServiceImpl;
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Value("${otc.payInfo.url}")
    public String url;
    @Autowired
    private RedisUtil redis;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    // 获取当前时间年月日时分秒毫秒字符串
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入苍穹支付】");
        String create = create(dealOrderApp, channel);
        if (StrUtil.isNotBlank(create)) {
            log.info("【本地订单创建成功，开始请求远程三方支付】");
            UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
            if (StrUtil.isBlank(userInfo.getDealUrl())) {
                orderDealEr(create, "当前商户交易url未设置");
                return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
            }
            log.info("【回调地址ip为：" + userInfo.getDealUrl() + "】");
            Result url = createOrder(dealOrderApp, userInfo.getDealUrl() +
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + PipixiaUtil.NOTIFY,
                    dealOrderApp.getOrderAmount(), create,
                    getChannelInfo(channel, dealOrderApp.getRetain1())
            );
            if (url.isSuccess()) {
                return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(url.getResult()));
            }
        }
        return Result.buildFailMessage("支付失败");
    }

    /**

     *  member_code     string 必选 16 商户号
     *  req_time        int 必选 11 接口请求时间 （上海时区）列如 1705151479
     *  rk              string 必选 32 随机32位字母（a-zA-Z）
     *  out_trade_no    string 必选 50 平台订单号
     *  pay_amount      string 必选 8 支付金额，单位元。支持小数点后两位 如：0.1
     *  product_code    string 必选 16 产品代号：如ALISCAN
     *  notify_url      string 必选 250 回调地址
     *  sign            string 必选 250 签名
     * @param dealOrderApp
     * @param notify
     * @param orderAmount
     * @param orderId
     * @param channelInfo
     * @return
     */
    private Result createOrder(DealOrderApp dealOrderApp, String notify, BigDecimal orderAmount,
                               String orderId, ChannelInfo channelInfo) {
        String mchid = channelInfo.getChannelAppId();
        String out_trade_no = orderId;
        String amount = orderAmount.toString();
        String channel = channelInfo.getChannelType();
        String notify_url = notify;
        String return_url = dealOrderApp.getBack();
        String time_stamp = sdf.format(new Date());
        String random = RandomStringUtils.randomAlphabetic(32);
        Map map = new HashMap();
        map.put("member_code", mchid);
        map.put("req_time", new Date().getTime()/1000);
        map.put("rk", random);
        map.put("out_trade_no", out_trade_no);
        map.put("pay_amount", amount);
        map.put("product_code", channel);
        map.put("notify_url", notify_url);
        String sign = sign(map,channelInfo.getChannelPassword(),random);
        map.put("sign", sign);
        log.info(" 请求参数: " + map.toString());
        String post = HttpUtil.post(channelInfo.getDealurl() , MapUrlParamsUtils.getUrlParamsByMap(map));
        log.info("请求三方响应：" + post);

        JSONObject jsonObject = JSONUtil.parseObj(post);
        String code = jsonObject.getStr("code");
        if ("0".equals(code)) {
            String data = jsonObject.getJSONObject("data").getStr("pay_url");
            return Result.buildSuccessResult(data);
        } else {
            orderDealEr(orderId, post );
            return Result.buildFailMessage(jsonObject.getStr("msg"));
        }
    }

    public static String sign(final Map<String, Object> hashMap, String token, String randKey) {
        Set<String> keySet = hashMap.keySet();
        String[] keyArray = keySet.toArray(new String[0]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyArray.length; i++) {
            if (String.valueOf(hashMap.get(keyArray[i])).length() > 0) {
                sb.append(keyArray[i]).append("=").append(hashMap.get(keyArray[i])).append("&");
            }
        }

        sb.append("key" + randKey + "=" + token);

        return MD5Util.encrypt(MD5Util.encrypt(sb.toString())).toUpperCase();
    }

    public static void main(String[] args) {
        DealOrderApp dealOrderApp;
        String notify;
        BigDecimal orderAmount;
        String orderId;
        ChannelInfo channelInfo;
        dealOrderApp = new DealOrderApp();
        dealOrderApp.setBack("http://dsada.asddad.adsdasdas");
        notify = "http://dsada.asddad.adsdasdas";
        orderAmount = new BigDecimal(800);
        orderId = System.currentTimeMillis() + "";
        channelInfo = new ChannelInfo();
        channelInfo.setChannelPassword("6791F29B3FFC38781CA660D4FA6357F2");
        channelInfo.setChannelAppId("80F3D9820E18F929");
        channelInfo.setChannelType("BANK");
        channelInfo.setDealurl("https://pxsf-api.pxia.fun/pay-v1/order/pay");
        PipixiaPay pay = new PipixiaPay();
        Result order = pay.createOrder(dealOrderApp, notify, orderAmount, orderId, channelInfo);

        System.out.printf(order.toJson());
    }


}
