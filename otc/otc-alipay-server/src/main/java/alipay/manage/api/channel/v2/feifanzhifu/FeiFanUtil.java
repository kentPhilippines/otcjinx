package alipay.manage.api.channel.v2.feifanzhifu;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.ChannelLocalUtil;
import alipay.manage.api.config.ChannelUtil;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealWit;
import alipay.manage.bean.util.v2.ResultDeal;
import alipay.manage.util.CheckUtils;
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
import otc.result.Result;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class FeiFanUtil extends ChannelUtil implements ChannelLocalUtil {
    private static SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");

    public Result channelDealPush(DealOrder order, ChannelInfo channelInfo) {

        /**
         * 参数	类型	参与签名	是否必填	描述
         * merchant_id	string	是	是	商户id
         * order_sn	string	是	是	商户订单id 唯一 不能重复 重复会创建订单失败 由商户控制
         * amount	double	是	是	金额 最大两位小数
         * channel	string	是	是	渠道编码 见 可用渠道列表
         * notify_url	string	是	是	异步通知地址 必须http:或者https:格式
         * return_url	string	否	否	同步通知地址 必须http:或者https:格式
         * time_stamp	string	是	是	时间如20180824030711
         * sign	string	否	是	签名 见签名规则
         */
        String merchant_id = channelInfo.getChannelAppId();
        String order_sn = order.getOrderId();
        String amount = order.getDealAmount().setScale(2, BigDecimal.ROUND_UP).toString();
        String channel = channelInfo.getChannelType();
        String notify_url = order.getNotify();
        String time_stamp = d.format(new Date());
        Map<String, Object> map = new HashMap<>();
        map.put("merchant_id", merchant_id);
        map.put("order_sn", order_sn);
        map.put("channel", channel);
        map.put("amount", amount);
        map.put("notify_url", notify_url);
        map.put("time_stamp", time_stamp);
        String createParam = PayUtil.createParam(map);
        log.info(" feifan 加密前参数：{}", createParam);
        String md5 = PayUtil.md5(createParam + "&key=" + channelInfo.getChannelPassword()).toLowerCase();
        map.put("sign", md5);
        log.info("feifan  请求参数为" + map.toString());
        logRequestDeal(this.getClass().getName(), JSONUtil.parse(map).toString(), order.getOrderId(), order.getExternalOrderId());
        String rString = HttpUtil.post(channelInfo.getDealurl(), map);
      /*  RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
        String rString = restTemplate.postForObject(channelInfo.getDealurl(), request, String.class);*/
        logResponseDeal(this.getClass().getName(), rString, order.getOrderId(), order.getExternalOrderId());
        log.info("feifan  响应参数" + rString);
        JSONObject jsonObject = JSONUtil.parseObj(rString);
        String str = jsonObject.getStr("code");
        if ("1".equals(str)) {
//{"code":1,"msg":"下单成功","data":{"pay_url":"https:\/\/www.feifan2024.com?order_no=202403141604566682092189"},"time":1710403496}
            String str1 = jsonObject.getJSONObject("data").getStr("pay_url");
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
         * amount	string	是	是	金额
         * order_no	string	是	是	平台订单号
         * order_sn	string	是	是	商户订单号
         * status	string	是	是	状态 1 成功
         * sign	string	否	是	签名 见签名规则
         */

        log.info("【收到feifan支付成功回调】");
        String sign = map.get("sign") + "";
        String apporderid = map.get("order_sn") + "";
        map.remove("sign");
        String channelKey = super.getChannelKey(apporderid);
        String createParam =  PayUtil.createParam(map);
        String md5 = PayUtil.md5(createParam + "&key=" + channelKey).toLowerCase();
        if (md5.equalsIgnoreCase(sign)) {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名成功】");
        } else {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名失败】");
            return Result.buildFailMessage("签名失败");
        }
        Result dealpayNotfiy = dealNotfiy(apporderid, "三方系统回调成功");
        if (dealpayNotfiy.isSuccess()) {
            return dealpayNotfiy;
        }
        return Result.buildFail();
    }

    public Result witNotify(Map map) {


        return Result.buildFail();
    }

    public void findBalance(String channelId, ChannelInfo channelInfo) {

    }


    public static void main(String[] args) {
        FeiFanUtil feifan = new FeiFanUtil();
        DealOrder order = new DealOrder();
        ChannelInfo channelInfo = new ChannelInfo();
        order.setOrderId(System.currentTimeMillis() + "");
        order.setDealAmount(new BigDecimal(200));
        order.setNotify("http://dadsdadasds.dasd.com");
        channelInfo.setChannelType("DigitalRMBPay");
        channelInfo.setChannelAppId("10007");
        channelInfo.setChannelPassword("kilTfpMxZYyferulDBTNLHyEmxEfibBr1");
        channelInfo.setDealurl("https://gateway.feifanpingtai.com/api/unifiedorder");
        feifan.channelDealPush(order, channelInfo);


    }
}
