package alipay.manage.api.channel.deal.wt;

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
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component("wtPay")
public class wtPay extends PayOrderService {
    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channeId) {
        log.info("【进入wt支付】");
        String create = create(dealOrderApp, channeId);
        if (StrUtil.isNotBlank(create)) {
            log.info("【本地订单创建成功，开始请求远程三方支付】");
            String payInfo = "";
            if (dealOrderApp.getDealDescribe().contains("付款人")) {
                payInfo = dealOrderApp.getDealDescribe();
            }
            UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
            if (StrUtil.isBlank(userInfo.getDealUrl())) {
                orderEr(dealOrderApp, "当前商户交易url未设置");
                return Result.buildFailMessage("请联系运营为您的商户号设置交易url");
            }
            log.info("【回调地址ip为：" + userInfo.getDealUrl() + "】");
            Result url = createOrder(userInfo.getDealUrl() + PayApiConstant.Notfiy.NOTFIY_API_WAI +
                            "/wt-pay-notify",
                    dealOrderApp.getOrderAmount(),
                    create,
                    getChannelInfo(channeId, dealOrderApp.getRetain1()),payInfo
            );
            if (url.isSuccess()) {
                return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(url.getResult()));
            }
        }
        return Result.buildFailMessage("支付错误");


    }
    @Autowired
    private OrderService orderServiceImpl;
    private   Result createOrder(
            String notify,
            BigDecimal orderAmount,
            String orderId,
            ChannelInfo channelInfo, String payInfo
    ) {
        String platform_id = channelInfo.getChannelAppId();
        String service_id = channelInfo.getChannelType();
        String payment_cl_id = orderId;
        String amount = orderAmount.multiply(new BigDecimal(100)).intValue() + "";
        String notify_url = notify;
        String request_time = System.currentTimeMillis() / 1000 + "";
        Map map = new HashMap();
        map.put("platform_id", platform_id);
        map.put("service_id", service_id);
        map.put("payment_cl_id", payment_cl_id);
        map.put("amount", amount);
        map.put("name", getPayName(payInfo,orderId));
        map.put("notify_url", notify_url);
        map.put("request_time", request_time);
        String param = createParam(map);
        log.info("签名参数为：" + param);
        String s = md5(param + "&" + channelInfo.getChannelPassword()).toLowerCase(Locale.ROOT);
        map.put("sign", s);
        JSONObject jsonObject = JSONUtil.parseObj(map);
        log.info("请求参数为:" + jsonObject.toString());
        HttpResponse execute = HttpRequest.post(channelInfo.getDealurl() + "/gateway/api/v1/payments")
                .body(jsonObject).execute();
        String ruselt = execute.body().toString();
        log.info("响应参数为：" + ruselt);
        /// 响应参数为：{"error_code":"0000","data":{"link":"https://hbj168.club/gateway/portal/v2/payments/?payment_id=WUTONGPM00426415&sign=d3c2792b688c3d80a65530d19bdf0609"}}
        JSONObject jsonObject1 = JSONUtil.parseObj(ruselt);
        String error_code = jsonObject1.getStr("error_code");
        if ("0000".equals(error_code)) {
            String url = JSONUtil.parseObj(JSONUtil.parseObj(ruselt).getStr("data")).getStr("link");
            orderServiceImpl.updateBankInfoByOrderId(payInfo , orderId);
            return Result.buildSuccessResult(url);
        }else{
            orderEr(orderId,jsonObject1.getStr("error_msg"));
        }
        return Result.buildFailMessage("三方错误");
    }



    /**
     * 参数名	        必选	        类型	        说明
     * platform_id	    是	        String(7)	商户Id
     * service_id	    是	        String(7)	服务Id
     * payment_cl_id	是	        String(32)	商户订单号
     * amount	        是	        Integer(10)	金额(分)
     * notify_url	    是	        String(256)	交易结果通知地址
     * bank_id	        否	        String(6)	汇款人银行Id
     * name	            否	        String(16)	汇款人户名(如果为实名制渠道必填)
     * last_number	    否	        String(6)	汇款人帐户末六码
     * request_time	    是	        Integer(10)	请求时间(秒)
     * sign	            是	        String(32)	订单签名
     */
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

    public static String md5(String a) {
        String c = "";
        MessageDigest md5;
        String result = "";
        try {
            md5 = MessageDigest.getInstance("md5");
            md5.update(a.getBytes("utf-8"));
            byte[] temp;
            temp = md5.digest(c.getBytes("utf-8"));
            for (int i = 0; i < temp.length; i++) {
                result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
        return result;
    }
}
