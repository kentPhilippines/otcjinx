package alipay.manage.api.channel.deal.huachenghui.v1;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.channel.util.qiangui.MD5;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

@Component("HuachenghuiPayv1")
@Slf4j
public class HuachenghuiPay extends PayOrderService {

    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入HuachenghuiPay支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
        String orderId = create(dealOrderApp, channel);
        UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
        if (StrUtil.isBlank(userInfo.getDealUrl())) {
            orderDealEr(orderId, "当前商户交易url未设置");
            return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
        }

        Result result = createOrder(
                userInfo.getDealUrl() +
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/huachenghuiPayNotify",
                dealOrderApp.getOrderAmount(),
                orderId,
                getChannelInfo(channel, dealOrderApp.getRetain1()),dealOrderApp);
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            orderDealEr(orderId, result.getMessage());
            return result;
        }
    }

    private Result createOrder(
            String notify,
            BigDecimal orderAmount,
            String orderId,
            ChannelInfo channelInfo,
            DealOrderApp dealOrderApp
    )  {
        String payurl = channelInfo.getDealurl();
        String payAmount = orderAmount.intValue()+""; // 金额
        String tradeType = channelInfo.getChannelType(); // '银行编码
        String mchId = channelInfo.getChannelAppId();// 商户id
        String notifyUrl = notify;// 通知地址

//        String key ="knC2cwLbt8948861lZkEb4uB4GRZ70y8nO5a3Q8TTd";
        String key =channelInfo.getChannelPassword();
//        String payurl = "https://apimvzyda.ercuy.xyz/InterfaceV9/CreatePayOrder/";
        String amount = payAmount; // 金额
        String payTypeId = tradeType; // '银行编码
        String payTypeIdFormat = "URL"; // '银行编码
//        String mchId = "43675";// 商户id
        String clientRealName = dealOrderApp.getPayName();
        String remark = "test";
        String ip = "127.0.0.1";

        MultiValueMap<String, String> param= new LinkedMultiValueMap<String, String>();
        param.add("Amount", amount);
        param.add("ClientRealName", clientRealName);
        param.add("Ip", ip);
        param.add("MerchantId", mchId);
        param.add("MerchantUniqueOrderId", orderId);
        param.add("NotifyUrl", notifyUrl);
        param.add("PayTypeId", payTypeId);
        param.add("PayTypeIdFormat", payTypeIdFormat);
        param.add("Remark", remark);


        String originalStr = createParam(param.toSingleValueMap())+""+key;
        log.info(originalStr);
        String sign = MD5.MD5Encode(originalStr).toLowerCase();
        param.add("sign", sign);
//        param.add("nurl", notifyUrl);
        log.info(JSONUtil.toJsonStr(param.toSingleValueMap()));
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(param, headers);
        String rString = restTemplate.postForObject(payurl, request, String.class);
        //{"Code":"0","MessageForUser":"OK","MessageForSystem":"OK","MerchantUniqueOrderId":"9257847107","Amount":"100","RealAmount":"100","Url":"http://www.hchqa.xyz/n/D43675230203205703021","BankCardRealName":"","BankCardNumber":"","BankCardBankName":"","BankCardBankBranchName":"","ExpiryTime":""}
        log.info(rString);
        JSONObject jsonObject =  JSONUtil.parseObj(rString);
        if ("0".equals(jsonObject.getStr("Code"))) {
            //业务处理
            String resultUrl = jsonObject.getStr("Url");
            return Result.buildSuccessResult("支付处理中", resultUrl);
        }
        return null;
    }

    private static String createParam(Map<String, String> map) {
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


        String notify = "http://sdadasdsadas.dasdasda.com";
        BigDecimal orderAmount = new BigDecimal(550);
        String orderId = System.currentTimeMillis()+"";
        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setChannelAppId("45149");
        channelInfo.setDealurl("https://apimvzyda.ercuy.xyz/InterfaceV9/CreatePayOrder/\n");
        channelInfo.setChannelType("kzk");
        channelInfo.setChannelPassword("k4nO5nN8Rg2q662KUH60DtaRyyPyP9HquWfsk0m0X3");
        DealOrderApp dealOrderApp = new DealOrderApp();
        dealOrderApp.setPayName("扬中就");
        HuachenghuiPay pay = new HuachenghuiPay();
        pay.createOrder(notify,orderAmount,orderId,channelInfo,dealOrderApp);


    }
}
