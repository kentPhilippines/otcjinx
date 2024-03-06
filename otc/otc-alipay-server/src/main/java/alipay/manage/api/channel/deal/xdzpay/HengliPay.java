package alipay.manage.api.channel.deal.xdzpay;

import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("XdzPay")
public class HengliPay extends PayOrderService {
    @Autowired
    OrderService orderServiceImpl;
    @Autowired
    private UserInfoService userInfoServiceImpl;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 获取当前时间年月日时分秒毫秒字符串
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入xdzpay支付】");
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
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + "/xdzpay-notify",
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
     * pay_memberid	        商户号	    是	是	平台分配商户号
     * pay_orderid	        商户订单号	是	是	上送订单号唯一, 字符长度20
     * pay_applydate	    提交时间	    是	是	时间格式：2018-12-28 18:18:18
     * pay_bankcode	        通道编码	    是	是	开发文档[附件]银行编码参考，或通道费率中查看
     * pay_notifyurl	    异步回调	    是	是	请给出有效的地址,地址后可以携带自定义get(?a=1&b=2)参数。（POST返回数据）
     * pay_callbackurl	    同步回调	    是	是	请给出有效的地址,地址后可以携带自定义get(?a=1&b=2)参数。（POST返回数据）
     * pay_amount	        订单金额	    是	是	订单金额 如：100 单位：元
     * pay_md5sign	        MD5数据签名	是	否	请看MD5签名处理
     *
     * @return
     */
    private Result createOrder(DealOrderApp dealOrderApp, String notify, BigDecimal orderAmount,
                               String orderId, ChannelInfo channelInfo) throws Exception {
        String key = channelInfo.getChannelPassword();
        String pay_memberid = channelInfo.getChannelAppId();
        String pay_amount = orderAmount.toString();
        String pay_bankcode = channelInfo.getChannelType();
        String pay_orderid = orderId;
        String pay_applydate = sdf.format(new Date());
        String pay_notifyurl = notify;
        String pay_callbackurl = dealOrderApp.getBack();
        String pay_md5sign = "";
        Map<String, String> body = new HashMap();
        body.put("OrderNo", orderId);
        body.put("Timestamp", System.currentTimeMillis() / 1000 +"" );
        body.put("AccessKey",pay_memberid);
        body.put("PayChannelId",pay_bankcode);
        body.put("Amount", pay_amount);
        body.put("CallbackUrl", notify);
        String param1 = createParam(body)+"&SecretKey="+key;
        System.out.println("加密前串为：" + param1);
        String sign1 = md5( param1);
        body.put("Sign", sign1.toLowerCase());
        JSONObject param = JSONUtil.parseFromMap(body);
        System.out.println("请求参数为：" + param);
        Map headers = new HashMap();
        headers.put("Content-Type", "application/json");
        HttpResponse execute = HttpRequest.post(channelInfo.getDealurl())
                .addHeaders(headers).body(param).execute();
        String ruselt = execute.body().toString();
        log.info("【hengli返回：" + ruselt.toString() + "】");
        JSONObject jsonObject = JSONUtil.parseObj(ruselt);
        String str = "";
        try {
            str = jsonObject.getJSONObject("Data").getJSONObject("PayeeInfo").getStr("CashUrl");
            return Result.buildSuccessResult(str);
        } catch (Throwable e) {
            return Result.buildFail();
        }

    }

    public static void main(String[] args) throws Exception {
        DealOrderApp dealOrderApp;
        String notifyUrl = "http://34.92.251.112:9010/notfiy-api-pay/zhongfeipay-notify";
        BigDecimal orderAmount;
        String orderId;
        ChannelInfo channelInfo;
        dealOrderApp = new DealOrderApp();
        dealOrderApp.setBack("http://dsada.asddad.adsdasdas");
        orderAmount = new BigDecimal(800);
        orderId = System.currentTimeMillis() + "";
        channelInfo = new ChannelInfo();
        //channelInfo.setChannelPassword("36zkP2byovcR3yOGXbPxFVMx1d1JMxs5Nl9kNKny");
        channelInfo.setChannelPassword("vv6qbnXlxkUg3lPkZGlziEkB1AKnm9c6v5emG883");
     //   channelInfo.setChannelAppId("a1lkqWrgnZfXnxxrOWoxTegZ");
        channelInfo.setChannelAppId("4Z8lNbwev5IGZPEPPNA2UZNXMW");
        channelInfo.setChannelType("1080");
        channelInfo.setDealurl("https://merchant.xdzzhifu.xyz/api/PayV2/submit");
        HengliPay pay = new HengliPay();
        Result order = pay.createOrder(dealOrderApp, notifyUrl, orderAmount, orderId, channelInfo);
        System.out.printf(order.toJson());
    }



    public static String md5(String a) {
        String c = "";
        MessageDigest md5;
        String result = "";
        try {
            md5 = MessageDigest.getInstance("md5");
            md5.update(a.getBytes("utf-8"));
            byte[] temp;
            temp = md5.digest(c.getBytes("utf-8"));
            for (int i = 0; i < temp.length; i++)
                result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
        return result;
    }

    public static String createParam(Map<String, String> map) {
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
