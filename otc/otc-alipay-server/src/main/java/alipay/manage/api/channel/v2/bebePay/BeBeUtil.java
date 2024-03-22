package alipay.manage.api.channel.v2.bebePay;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.ChannelLocalUtil;
import alipay.manage.api.config.ChannelUtil;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealWit;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;
import otc.result.Result;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Component
public class BeBeUtil extends ChannelUtil implements ChannelLocalUtil {
    private static SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");

    public Result channelDealPush(DealOrder order, ChannelInfo channelInfo) {

        /**
         timestamp 是 String 单位为秒，从1970/1/1 00:00:00 至今的秒数(10位数)
         amount 是 String 单位RMB，订单金额
         appKey 是 String 商户身份标识，BeBePAY平台提供
         payType 是 String 支付方式，4表示银行卡支付 7表示支付宝支付 9表示数字人民币支付
         orderID 是 String 商户唯一订单号，由贵方提供
         real_name 是 String 付款人姓名，需签名
         callback_url 否 String 回调地址，无需签名(有值时依callback_url回调，无值时依后台设定回调)
         sign	是	string	签名
         */
        String appKey = channelInfo.getChannelAppId();
        String orderID = order.getOrderId();
        String payType = channelInfo.getChannelType();
        String amount = order.getDealAmount().toString();
        String notify_url = order.getNotify();
        String real_name = order.getMcRealName();
        String appSecret = channelInfo.getChannelPassword();
        String timestamp = System.currentTimeMillis() / 1000 + "";
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", timestamp);
        map.put("appKey", appKey);
        map.put("amount", amount);
        map.put("orderID", orderID);
        map.put("payType", payType);
        map.put("real_name", order.getMcRealName());
        map.put("callback_url", notify_url);
        String sign = timestamp + "&" + amount + "&" + appKey + "&" + payType + "&" + orderID + "&" + real_name + "&" + appSecret;
        log.info(" 签名前参数为：" + sign);
        sign = PayUtil.md5(sign);
        map.put("sign", sign);
        log.info("  BeBe  请求参数为" + JSONUtil.parseObj(map).toString());
        logRequestDeal(this.getClass().getName(), JSONUtil.parse(map).toString(), order.getOrderId(), order.getExternalOrderId());
        String rString = HttpUtil.post(channelInfo.getDealurl(), JSONUtil.parseObj(map).toString());
        logResponseDeal(this.getClass().getName(), rString, order.getOrderId(), order.getExternalOrderId());
        log.info("  BeBe  响应参数" + rString);
        /**
         * {
         *     "deadline": "1710748032",
         *     "message": "Success",
         *     "pay_url": "https://hash720.com/HgZcuWkYKne9A0Yy/pay?id=067024031826845",
         *     "success": true
         * }
         */
        //响应参数{"code":200,"data":{"have_code":1,"merchant_order_no":"1710681842384","order_no":"DJYZF1710681842819Z9DYGEoBYf63dk","pay_amount":900,"payurl":"http://huhu1234.cn/recharge/order/DJYZF1710681842819Z9DYGEoBYf63dk","sign":"7CEC399AD6A599605AB50821F6B70C69"},"msg":"操作成功","token":""}
        JSONObject jsonObject = JSONUtil.parseObj(rString);
        Boolean success = jsonObject.getBool("success");
        if (success) {
            String str1 = jsonObject.getStr("pay_url");
            return Result.buildSuccessResult(str1);
        } else {
            orderEr(order, rString);
        }
        return Result.buildFail();
    }

    public Result channelWitPush(DealWit result, ChannelInfo channelInfo) {
        return Result.buildFail();

    }

    public Result dealNotify(Map map) {

        /**
         {
         “type”: ‘1’,
         “amount”: “2000”,
         "real_amount": “1999”,
         “appKey”: “abcdefghijklmnopqrstuvwxyz012345”,
         “payType”: “4”,
         “status”: “4”,
         “orderID”: “FDM123399”,
         “sign”: “a60749efa8edb346900b63de6a8b1c06”
         }
         备注:
         1. type为订单类型，0为充值订单、1为代付订单
         2. 签名采用 MD5 加密，md5(type&amount&real_amount&appKey&payType&orderID&status&appSecret)
         * appSecret: 唯一凭证密钥，BeBePAY平台提供
         3. status=3 订单状态为已驳回；status=4 订单状态为已完成
         */

        log.info("【收到BeBe支付成功回调】");
        String sign = map.get("sign") + "";
        String apporderid = map.get("orderID") + "";
        String channelKey = super.getChannelKey(apporderid);
        String createParam =
                map.get("type").toString() + "&"
                + map.get("amount").toString() + "&"
                + map.get("real_amount").toString() + "&"
                + map.get("appKey").toString() + "&"
                + map.get("payType").toString() + "&"
                + map.get("orderID").toString() + "&"
                + map.get("status").toString() + "&" + channelKey;
        String md5 = PayUtil.md5(createParam);
        if (md5.equalsIgnoreCase(sign)) {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名成功】");
        } else {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【当前支付成功回调签名参数：" + createParam + "，当前我方验证签名结果：" + md5 + ",对方签名结果："+sign+"】");
            log.info("【签名失败】");
            return Result.buildFailMessage("签名失败");
        }
        if ("4".equalsIgnoreCase(map.get("status").toString()) && "0".equalsIgnoreCase(map.get("type").toString())) {
            Result dealpayNotfiy = dealNotfiy(apporderid, "三方系统回调成功");
            if (dealpayNotfiy.isSuccess()) {
                return dealpayNotfiy;
            }
        }
        return Result.buildFail();
    }

    public Result witNotify(Map map) {


        return Result.buildFail();
    }

    public void findBalance(String channelId, ChannelInfo channelInfo) {

    }


    public static void main(String[] args) {
        BeBeUtil feifan = new BeBeUtil();
        DealOrder order = new DealOrder();
        ChannelInfo channelInfo = new ChannelInfo();
        order.setOrderId(System.currentTimeMillis() + "");
        order.setDealAmount(new BigDecimal(1100));
        order.setNotify("http://dadsdadasds.dasd.com");
        order.setMcRealName("张三");
        channelInfo.setChannelType("7");
        channelInfo.setChannelAppId("JmowpcId8ZqdSPMYj2OlXupebt9A0efo");
        channelInfo.setChannelPassword("2d9hgunFvebKz1Tnxgfsys9uVYSVqRj2");
        channelInfo.setDealurl("https://bebepay.net/bebeApi/v1.1/order/create");
        feifan.channelDealPush(order, channelInfo);


    }
}
