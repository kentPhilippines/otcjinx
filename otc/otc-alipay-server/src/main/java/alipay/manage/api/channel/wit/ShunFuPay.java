package alipay.manage.api.channel.wit;

import alipay.manage.api.channel.util.qiangui.MD5;
import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.util.Arrays;
import java.util.Map;


@Component("ShunFuPay")
public class ShunFuPay extends PayOrderService {
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Override
    public Result withdraw(Withdraw wit) {
        log.info("【进入shunfu代付】");
        try {
            log.info("【本地订单创建成功，开始请求远程三方代付接口】");
            UserInfo userInfo = userInfoServiceImpl.findDealUrl(wit.getUserId());
            if (StrUtil.isBlank(userInfo.getDealUrl())) {
                withdrawEr(wit, "当前商户交易url未设置", wit.getRetain2());
                return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
            }
            log.info("【回调地址ip为：" + userInfo.getDealUrl() + "】");
            String channel = "";
            if (StrUtil.isNotBlank(wit.getChennelId())) {//支持运营手动推送出款
                channel = wit.getChennelId();
            } else {
                channel = wit.getWitChannel();
            }
            ChannelInfo channelInfo = getChannelInfo(channel, wit.getWitType());
            String createDpay = createDpay(userInfo.getDealUrl().toString() + PayApiConstant.Notfiy.NOTFIY_API_WAI + "/ShunFu-notify", wit,channelInfo);
            if (StrUtil.isNotBlank(createDpay) && createDpay.equals("SUCCESS")) {
                return Result.buildSuccessMessage("代付成功等待处理");
            } else {
                return Result.buildFailMessage(createDpay);
            }
        } catch (Exception e) {
            log.error("【错误信息打印】" + e.getMessage());
            return Result.buildFailMessage("代付失败");
        }
    }

    String createDpay(String notify, Withdraw wit, ChannelInfo channelInfo) {
//        String key ="BB8E7131F29F110BB8E3BE5B6C3A936E";
        String key =channelInfo.getChannelPassword();
//        String payurl = "http://bolepay.co/newbankPay/crtAgencyOrder.do";
        String payurl = channelInfo.getWitUrl();
        String money = wit.getAmount().intValue()+".00"; // 金额
//        String payType = "401"; // '银行编码
        String payType = channelInfo.getChannelType(); // '银行编码
//        String user = "20275504";// 商户id
        String user = channelInfo.getChannelAppId();// 商户id
        String orderId = wit.getOrderId();// 20位订单号 时间戳+6位随机字符串组成
        String notifyUrl = notify;
//        String notifyUrl = "http://34.92.251.112:9010/notfiy-api-pay/newXiangyunWit-notify";
        String name = wit.getAccname();
        String card = wit.getBankNo();
        String bankCode = wit.getBankcode();

        MultiValueMap<String, String> param= new LinkedMultiValueMap<String, String>();
        param.add("pid", user);
        param.add("order_no", orderId);
        param.add("amount", money);
        param.add("bank_code", bankCode);
        param.add("bank_number", card);
        param.add("realname", name);
        param.add("notify_url", notifyUrl);


        String originalStr = key+ createParam(param.toSingleValueMap())+key;
        log.info(originalStr);
        String sign = MD5.MD5Encode(originalStr).toLowerCase();
        param.add("sign", sign);

        log.info(JSONUtil.toJsonStr(param.toSingleValueMap()));
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(param, headers);
        String rString = restTemplate.postForObject(payurl, request, String.class);
        //{"status":1,"info":"订单提交成功","data":null,"url":""}
        log.info("shunfu response:{}",rString);
        JSONObject parseObj = JSONUtil.parseObj(rString);
        String object = parseObj.getStr("status");
        if ("1".equals(object)) {
            witComment(orderId);
            return "SUCCESS";
        }
        return "error";
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
