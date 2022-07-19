package alipay.manage.api.channel.deal.NewXiangyun;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.channel.util.qiangui.MD5;
import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Component("NewXiangyunWit")
@Slf4j
public class NewXiangyunWit extends PayOrderService {
    private static final String WIT_RESULT = "SUCCESS";
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result withdraw(Withdraw wit) {
        log.info("【进入newxiangyun代付】");
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
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + "/newXiangyunWit-notify",
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

        String key =channelInfo.getChannelPassword();
        String payurl = channelInfo.getWitUrl();
        String transAmt = wit.getAmount().intValue()+""; // 金额
        String storeCode = channelInfo.getChannelAppId();// 商户id
        String storeOrderNo = wit.getOrderId();// 20位订单号 时间戳+6位随机字符串组成
        String notifyUrl = notify;
        String accountName = wit.getAccname();
        String accountNo = wit.getBankNo();
        String bankCode = wit.getBankcode();
        String bankBranchName = wit.getBankName();

        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("storeCode", storeCode);
        param.add("storeOrderNo", storeOrderNo);
        param.add("accountName", accountName);
        param.add("accountNo", accountNo);
        param.add("transAmt", transAmt);
        param.add("notifyUrl", notifyUrl);
        param.add("bankCode", bankCode);
        param.add("bankBranchName", bankBranchName);
        param.add("bankAccountType", "1");
        param.add("province", "湖南");
        param.add("city", "长沙");
        param.add("userIdCard", "123");
        param.add("userMobile", "1234");
        param.add("transMemo", "5555");

        String originalStr = createParam(param.toSingleValueMap())+"&key="+key;
        log.info("request:{}",originalStr);
        String sign = MD5.MD5Encode(originalStr);
        param.add("sign", sign);

        RestTemplate restTemplate = new RestTemplate();
        String rString = restTemplate.postForObject(payurl, param.toSingleValueMap(), String.class);
        log.info("祥云代付 result:{}",rString);
        Map<String, String> resultMap = JSONUtil.toBean(rString,Map.class);
        if(!resultMap.containsKey("errorCode"))
        {
            witComment(wit.getOrderId());
            return WIT_RESULT;
        }
        withdrawErMsg(wit,resultMap.get("errorMsg"),wit.getRetain2());
        return resultMap.get("errorMsg");
    }
    private   String createParam(Map<String, Object> map) {
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
