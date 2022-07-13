package alipay.manage.api.channel.deal.doudoupay;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@Component("DouDouWit")
@Slf4j
public class DouDouWit  extends PayOrderService {
    private static final String WIT_RESULT = "SUCCESS";
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result withdraw(Withdraw wit) {
        log.info("【进入DouDouWit代付】");
        try {
            log.info("【本地订单创建成功，开始请求远程三方代付接口】");
            String channel = "";
            if (StrUtil.isNotBlank(wit.getChennelId())) {//支持运营手动推送出款
                channel = wit.getChennelId();
            } else {
                channel = wit.getWitChannel();
            }
            if (isNumber(wit.getAmount().toString())) {
                Result result = withdrawEr(wit, "代付订单不支持小数提交", wit.getRetain2());
                if (result.isSuccess()) {
                    return Result.buildFailMessage("代付订单不支持小数提交");
                }
            }
            UserInfo userInfo = userInfoServiceImpl.findDealUrl(wit.getUserId());
            if (StrUtil.isBlank(userInfo.getDealUrl())) {
                withdrawEr(wit,"url未设置",wit.getRetain2());
                return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
            }
            String createDpay = createDpay(userInfo.getDealUrl()+
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + "/doudouWit-notify",
                    wit, getChannelInfo(channel, wit.getWitType()));
            if (StrUtil.isNotBlank(createDpay) && createDpay.equals(WIT_RESULT)) {
                return Result.buildSuccessMessage("代付成功等待处理");
            } else {
                return Result.buildFailMessage(createDpay);
            }
        } catch (Exception e) {
            log.error("【错误信息打印】" + e.getMessage());
            return Result.buildFailMessage("代付失败");
        }
    }

    private String createDpay(String notify, Withdraw wit, ChannelInfo channelInfo) {
        String merchantId = channelInfo.getChannelAppId();
        String privateKey = channelInfo.getChannelPassword();
        //String url ="https://api.doudoupays.com/gateway/pay/withdraw";
        String url = channelInfo.getDealurl();
        Map<String,Object> map   = new LinkedHashMap<>();
        map.put("mchCode",merchantId);
        map.put("orderId", wit.getOrderId());
        map.put("amount",wit.getAmount().intValue());

        map.put("cardId",wit.getBankNo());
        map.put("accountName",wit.getAccname());

        map.put("notifyUrl",notify);
        String paramJson = JSONUtil.toJsonStr(map);
        //String createParam = PayUtil.createParam(map);
        log.info(paramJson);
        String md5 = PayUtil.md5(paramJson + privateKey);
        log.info(md5);
        map.put("currency",wit.getCurrency());
        map.put("bankName",wit.getBankName());
        map.put("sign",md5);
        RestTemplate restTemplate = new RestTemplate();
        String rString = restTemplate.postForObject(url, map, String.class);
        log.info(rString);
        JSONObject resultJson = JSONUtil.parseObj(rString);
        if(resultJson.getInt("retcode")==0)
        {
            witComment(wit.getOrderId());
            return WIT_RESULT;
        }

        return "";
    }

    public static void main(String[] args) {
        //{"mchCode":"ad0b10fe78124aac","orderId":"W20220517170804029759053","amount":301,"cardId":"6339829384754893828","accountName":"张三","notifyUrl":"http://localhost:9010//notfiy-api-pay/doudouWit-notify"}434ef40df0354badb836d45d44d99598
        //{"mchCode":"ad0b10fe78124aac","orderId":"W20220517170804029759053","amount":300,"cardId":"6339829384754893828","accountName":"张三","notifyUrl":"http://localhost:9010//notfiy-api-pay/doudouWit-notify"}434ef40df0354badb836d45d44d99598
        //{"mchCode":"ad0b10fe78124aac","orderId":"W20220517170804029759053","amount":300,"cardId":"6339829384754893828","accountName":"张三","notifyUrl":"http://localhost:9010//notfiy-api-pay/doudouWit-notify"}434ef40df0354badb836d45d44d99598
        String orderId = RandomStringUtils.randomAlphabetic(16);
        String merchantId = "ad0b10fe78124aac";
        String privateKey = "434ef40df0354badb836d45d44d99598";
        String url ="https://api.doudoupays.com/gateway/pay/withdraw";
        Map<String,Object> map   = new LinkedHashMap<>();
        map.put("mchCode",merchantId);
        map.put("orderId", "W20220517170804029759053");
        map.put("amount",300);

        map.put("cardId","6339829384754893828");
        map.put("accountName","张三");

        map.put("notifyUrl","http://localhost:9010//notfiy-api-pay/doudouWit-notify");
        String paramJson = JSONUtil.toJsonStr(map);
        //String createParam = PayUtil.createParam(map);
        log.info(paramJson);
        String md5 = PayUtil.md5(paramJson + privateKey);
        log.info(md5);
        map.put("currency","CNY");
        map.put("bankName","建设银行");
        map.put("sign",md5);
        RestTemplate restTemplate = new RestTemplate();
        String rString = restTemplate.postForObject(url, map, String.class);

        log.info(rString);
    }
}
