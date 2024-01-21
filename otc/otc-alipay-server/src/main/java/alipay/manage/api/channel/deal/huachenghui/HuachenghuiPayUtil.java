package alipay.manage.api.channel.deal.huachenghui;

import alipay.manage.api.channel.util.qiangui.MD5;
import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.ChannelLocalUtil;
import alipay.manage.api.config.ChannelUtil;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealWit;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.bean.util.WitInfo;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import otc.api.alipay.Common;
import otc.bean.alipay.OrderDealStatus;
import otc.bean.dealpay.Withdraw;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class HuachenghuiPayUtil extends ChannelUtil implements ChannelLocalUtil {

    static Map<String, String> PayTypeIdFormat = new HashMap();
    static Map<String, String> orderStatusDeal = new HashMap();
    static Map<String, String> orderStatusWit = new HashMap();

    static {
        init();
    }

    static void init() {
        /**
         * 0 待支付
         * 100 支付成功
         * -90 支付失败
         */
        orderStatusDeal.put("0", Common.Order.DealOrder.ORDER_STATUS_DISPOSE);
        orderStatusDeal.put("100", Common.Order.DealOrder.ORDER_STATUS_SU);
        orderStatusDeal.put("-90", Common.Order.DealOrder.ORDER_STATUS_ER);
        orderStatusWit.put("100", Common.Order.Wit.ORDER_STATUS_SU);
        orderStatusWit.put("-90", Common.Order.Wit.ORDER_STATUS_ER);
    }

    @Override
    public Result channelDealPush(DealOrder order, ChannelInfo channelInfo) {
        String payurl = channelInfo.getDealurl();
        String orderId = order.getOrderId(); // 金额
        String payAmount = order.getActualAmount().intValue() + ""; // 金额
        String tradeType = channelInfo.getChannelType(); // '银行编码
        String mchId = channelInfo.getChannelAppId();// 商户id
        String notifyUrl = order.getNotify();// 通知地址
        String key = channelInfo.getChannelPassword();
        String amount = payAmount; // 金额
        String payTypeId = tradeType; // '银行编码
        String payTypeIdFormat = "URL"; // '银行编码
        String clientRealName = "张三";
        String remark = "test";
        String ip = "127.0.0.1";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();
        param.add("Amount", amount);
        param.add("ClientRealName", clientRealName);
        param.add("Ip", ip);
        param.add("MerchantId", mchId);
        param.add("MerchantUniqueOrderId", orderId);
        param.add("NotifyUrl", notifyUrl);
        param.add("PayTypeId", payTypeId);
        param.add("PayTypeIdFormat", payTypeIdFormat);
        param.add("Remark", remark);
        String originalStr = super.createParam(param.toSingleValueMap()) + "" + key;
        log.info(originalStr);
        String sign = MD5.MD5Encode(originalStr).toLowerCase();
        param.add("sign", sign);
        logRequestDeal(this.getClass().getName(), param.toSingleValueMap(), order.getOrderId(), order.getExternalOrderId());
        log.info(JSONUtil.toJsonStr(param.toSingleValueMap()));
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(param, headers);
        String rString = restTemplate.postForObject(payurl, request, String.class);
        log.info(rString);
        logResponseDeal(this.getClass().getName(), rString, order.getOrderId(), order.getExternalOrderId());
        JSONObject jsonObject = JSONUtil.parseObj(rString);
        if ("0".equals(jsonObject.getStr("Code"))) {
            //业务处理
            String resultUrl = jsonObject.getStr("Url");
            return Result.buildSuccessResult("支付处理中", resultUrl);
        } else {
            String str = jsonObject.getStr("MessageForUser");
            orderEr(order, str);
            return Result.buildFail();
        }
    }

    @Override
    public Result channelWitPush(DealWit order, ChannelInfo channelInfo) {
        String Amount = order.getDealAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        String witInfo = order.getWitInfo();
        WitInfo witInfo1 = JSONUtil.toBean(JSONUtil.parseObj(witInfo), WitInfo.class);
        String BankCardBankName = witInfo1.getBankName();
        String BankCardNumber = witInfo1.getBankNo();
        String BankCardRealName = witInfo1.getAccount();
        String MerchantId = channelInfo.getChannelAppId();
        String MerchantUniqueOrderId = order.getOrderId();
        String NotifyUrl = order.getNotify();
        String Remark = MerchantUniqueOrderId;
        String WithdrawTypeId = "0";
        Map map = new HashMap();
        map.put("Amount", Amount);
        map.put("BankCardBankName", BankCardBankName);
        map.put("BankCardNumber", BankCardNumber);
        map.put("BankCardRealName", BankCardRealName);
        map.put("MerchantId", MerchantId);
        map.put("MerchantUniqueOrderId", MerchantUniqueOrderId);
        map.put("NotifyUrl", NotifyUrl);
        map.put("Remark", Remark);
        map.put("WithdrawTypeId", WithdrawTypeId);
        String param = createParam(map);
        String s = param + channelInfo.getChannelPassword();
        String s1 = md5(s).toLowerCase(Locale.ROOT);
        String Sign = s1;
        map.put("Sign", Sign);
        logRequestWit(this.getClass().getName(), map, order.getOrderId(), order.getExternalOrderId());
        String post = "";
        try {
            post = HttpUtil.post(channelInfo.getDealurl() + "/InterfaceV9/CreateWithdrawOrder/", map, 2000);
        } catch (Throwable e) {
            log.info("【渠道异常】", e.getMessage());
            return Result.buildFail();
        }
        logResponseWit(this.getClass().getName(), post, order.getOrderId(), order.getExternalOrderId());
        JSONObject jsonObject = JSONUtil.parseObj(post);
        String code = jsonObject.getStr("Code");
        if ("0".equals(code)) {
            witOrdererSu(order.getOrderId(), "推送成功，等待三方出款");
            return Result.buildSuccessMessage("提交成功，请等待处理完成后的回调或调用查询接口查询状态");
        } else {
            witOrdererEr(order.getOrderId(), jsonObject.getStr("Message"));
            return Result.buildFailMessage("渠道错误");
        }
    }

    public Result witNotify(Map<String, Object> map) {
        String Sign = map.get("Sign").toString();
        map.remove("Sign");
        String merchantUniqueOrderId = map.get("MerchantUniqueOrderId").toString();
        String channelKey = getWitChannelKey(merchantUniqueOrderId);
        String param = createParam(map);
        String s = param + channelKey;
        String s1 = md5(s).toLowerCase(Locale.ROOT);
        if (Sign.equals(s1)) {//签名成功
            String withdrawOrderStatus = orderStatusWit.get(map.get("WithdrawOrderStatus").toString());
            Result result = witNotfy(merchantUniqueOrderId, withdrawOrderStatus);
            return result;
        }
        return Result.buildFail();
    }

    public Result dealNotify(Map map) {
        String Sign = map.get("Sign").toString();
        map.remove("Sign");
        String merchantUniqueOrderId = map.get("MerchantUniqueOrderId").toString();
        String channelKey = getChannelKey(merchantUniqueOrderId);
        String param = createParam(map);
        String s = param + channelKey;
        String s1 = md5(s).toLowerCase(Locale.ROOT);
        if (s1.equals(Sign)) {
            String payOrderStatus = orderStatusDeal.get(map.get("PayOrderStatus").toString());
            if (payOrderStatus.equals(OrderDealStatus.成功.getIndex().toString())) {
                Result result = dealNotfiy(merchantUniqueOrderId, "三方系统回调成功");
                return result;
            }
        }
        return Result.buildFail();
    }
    public void findBalance(String channelId, ChannelInfo channelInfo) {
        log.info("【调用查询渠道余额方法，当前渠道信息为："+channelInfo.toString()+"】");
        String MerchantId = channelInfo.getChannelAppId();
        Map map = new HashMap();
        map.put("MerchantId", MerchantId);
        String param = createParam(map);
        String s = param + channelInfo.getChannelPassword();
        String s1 = md5(s).toLowerCase(Locale.ROOT);
        map.put("Sign", s1);
        String post = HttpUtil.post(channelInfo.getDealurl() + "/InterfaceV9/GetBalanceAmount/", map, 2000);
        JSONObject jsonObject = JSONUtil.parseObj(post);
        String code = jsonObject.getStr("Code");
        if ("0".equals(code)) {
            String balanceAmount = jsonObject.getStr("BalanceAmount");
            updateAmountChannel(new BigDecimal(balanceAmount),channelId);
        }
    }
}
