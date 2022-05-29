package alipay.manage.api.channel.deal.youqing;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.date.DateTime;
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

import java.util.LinkedHashMap;
import java.util.Map;

@Component("YouQingWit")
@Slf4j
public class YouQingWit extends PayOrderService {
    private static final String WIT_RESULT = "SUCCESS";
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result withdraw(Withdraw wit) {
        log.info("【进入YouQingWit代付】");
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
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + "/youqingWit-notify",
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
        String url ="https://apiw3ivfdkj1.zoxbj.xyz/InterfaceV5/CreateWithdrawOrder/";
        Map<String,Object> map   = new LinkedHashMap<>();
        map.put("Amount",wit.getAmount().intValue());
        map.put("BankCardBankName", wit.getBankName());
        map.put("BankCardNumber",wit.getBankNo());
        map.put("BankCardRealName",wit.getAccname());
        map.put("MerchantId",merchantId);
        map.put("MerchantUniqueOrderId",wit.getOrderId());
        map.put("NotifyUrl",notify);

        map.put("Timestamp",new DateTime().toString("yyyyMMddHHmmss"));
        map.put("WithdrawTypeId","0");

        String paramJson = PayUtil.createParam(map);
        log.info(paramJson);
        String md5 = PayUtil.md5(paramJson + privateKey).toLowerCase();
        log.info(md5);
        map.put("Sign",md5);
        log.info(JSONUtil.toJsonStr(map));
        RestTemplate restTemplate = new RestTemplate();
        String rString = restTemplate.postForObject(channelInfo.getDealurl(), map, String.class);
        log.info(rString);

        JSONObject resultJson = JSONUtil.parseObj(rString);
        if("0".equals(resultJson.getStr("Code")))
        {
            return WIT_RESULT;
        }

        return "";
    }

    public static void main(String[] args) {
        String orderId = RandomStringUtils.randomAlphabetic(16);
        String merchantId = "33172";
        String privateKey = "ky207vzlgz8pT1YY6Ce0fLbT8wNO2UPBx81";
        String url ="https://apiw3ivfdkj1.zoxbj.xyz/InterfaceV5/CreateWithdrawOrder/";
        Map<String,Object> map   = new LinkedHashMap<>();
        map.put("Amount","100.00");
        map.put("BankCardBankName", "招商银行");
        map.put("BankCardNumber","6339829384754893828");

        map.put("BankCardRealName","张三");
        map.put("MerchantId",merchantId);
        map.put("MerchantUniqueOrderId",orderId);
        map.put("NotifyUrl","http://34.92.251.112:9010//notfiy-api-pay/doudouWit-notify");

        map.put("Timestamp",new DateTime().toString("yyyyMMddHHmmss"));
        map.put("WithdrawTypeId","0");


//        String paramJson = JSONUtil.toJsonStr(map);
        String paramJson = PayUtil.createParam(map);
        log.info(paramJson);
        String md5 = PayUtil.md5(paramJson + privateKey);
        log.info(md5);
        map.put("Sign",md5);
        log.info(JSONUtil.toJsonStr(map));
        RestTemplate restTemplate = new RestTemplate();
        String rString = restTemplate.postForObject(url, map, String.class);
        System.out.println(rString);
    }
}
