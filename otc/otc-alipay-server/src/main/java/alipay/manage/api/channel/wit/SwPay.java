package alipay.manage.api.channel.wit;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.channel.util.qiangui.MD5;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Component("SwPay")
public class SwPay extends PayOrderService {
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Override
    public Result withdraw(Withdraw wit) {
        log.info("【进入SwPay代付】");
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
            String createDpay = createDpay(userInfo.getDealUrl().toString() + PayApiConstant.Notfiy.NOTFIY_API_WAI + "/sw-notify", wit,channelInfo);
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
        String key =channelInfo.getChannelPassword();
        String payurl = channelInfo.getWitUrl();
        String money = wit.getAmount().intValue()+".00"; // 金额
        String payType = channelInfo.getChannelType(); // '银行编码
        String user = channelInfo.getChannelAppId();// 商户id
        String orderId = wit.getOrderId();// 20位订单号 时间戳+6位随机字符串组成
        String notifyUrl = notify;
//        String notifyUrl = "http://34.92.251.112:9010/notfiy-api-pay/newXiangyunWit-notify";
        String name = wit.getAccname();
        String card = wit.getBankNo();
        String bankCode = wit.getBankcode();

        Map<String, String> body = new HashMap();
        body.put("OrderNo", orderId);
        body.put("Timestamp", System.currentTimeMillis() / 1000 +"" );
        body.put("AccessKey",user);
        body.put("PayChannelId",payType);
        body.put("Payee", name);
        body.put("PayeeNo", card);
        body.put("PayeeAddress", bankCode);
        body.put("Amount", money);
        body.put("CallbackUrl", notify);
        String param1 = createParam(body)+"&SecretKey="+key;
        System.out.println("加密前串为：" + param1);
        String sign1 = md5( param1);
        body.put("Sign", sign1.toLowerCase());
        JSONObject param = JSONUtil.parseFromMap(body);
        System.out.println("请求参数为：" + param);
        Map headers = new HashMap();
        headers.put("Content-Type", "application/json");
        HttpResponse execute = HttpRequest.post(payurl)
                .addHeaders(headers).body(param).execute();
        String ruselt = execute.body().toString();
        //{"status":1,"info":"订单提交成功","data":null,"url":""}
        log.info("shunfu response:{}",ruselt);
        JSONObject parseObj = JSONUtil.parseObj(ruselt);
        String object = parseObj.get("Code")+"";
        if ("0".equals(object)) {
            witComment(orderId);
            return "SUCCESS";
        }
        return "error";
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
