package alipay.manage.api.channel.deal.wangfu;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;
import otc.util.MapUtil;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component("WangFuWit")
public class WangFuWit extends PayOrderService {
    private static final String WIT_RESULT = "SUCCESS";
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result withdraw(Withdraw wit) {
        log.info("【进入wt代付】");
        try {
            log.info("【本地订单创建成功，开始请求远程三方代付接口】");
            String channel = "";
            if (StrUtil.isNotBlank(wit.getChennelId())) {//支持运营手动推送出款
                channel = wit.getChennelId();
            } else {
                channel = wit.getWitChannel();
            }

            UserInfo userInfo = userInfoServiceImpl.findDealUrl(wit.getUserId());
            if (StrUtil.isBlank(userInfo.getDealUrl())) {
                withdrawEr(wit, "url未设置", wit.getRetain2());
                return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
            }
            String createDpay = createDpay(userInfo.getDealUrl() +
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + "/WangFuWit-wit-notify",
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

    /**
     * merchantId       integer             商户号             必需
     * merchantOrderId  string              商户订单号          必需
     * payType          integer             支付方式            必需
     * payAmount        number              支付金额            必需
     * bankType         string              银行类型            必需
     * bankNum          string              会员银行卡号          必需
     * bankAccount      string              银行卡账户名          必需
     * bankAddress      string              开户地址            必需
     * notifyUrl        string              商户通知接收地址        必需
     * userId           string              会员ID                必需
     * userAccount      string              会员名称
     * userIp           string              会员IP
     * sign             string              验签字符串               必需
     */
    private   String createDpay(String notify, Withdraw wit, ChannelInfo channelInfo) {
        String merchantId = channelInfo.getChannelAppId();
        String merchantOrderId = wit.getOrderId();
        String payType = channelInfo.getChannelType();
        String payAmount = wit.getAmount().toBigInteger() + ".00";
        String bankType = wit.getBankcode();
        String bankNum = wit.getBankNo();
        String bankAccount = wit.getAccname();
        String bankAddress = "北京市朝阳区第九胡同支行";
        String notifyUrl = notify;
        String userId = md5(wit.getUserId());
        Map map = new HashMap();
        map.put("merchantId", merchantId);
        map.put("merchantOrderId", merchantOrderId);
        map.put("payType", payType);
        map.put("payAmount", payAmount);
        map.put("bankType", bankType);
        map.put("bankNum", bankNum);
        map.put("bankAccount", bankAccount);
        map.put("bankAddress", bankAddress);
        map.put("notifyUrl", notifyUrl);
        map.put("userId", userId);
        String param = "merchantId=" + channelInfo.getChannelAppId() + "&merchantOrderId=" + wit.getOrderId() + "&payAmount=" + payAmount;
        param = param + "&key=" + channelInfo.getChannelPassword();
        log.info("签名参数为：" + param);
        String s = md5(param);
        map.put("sign", s);
        log.info("请求参数：" + JSONUtil.parseObj(map).toString());
        String post = HttpUtil.post(channelInfo.getWitUrl() + "/hpay/wd/ct", JSONUtil.parseObj(map).toString());
        log.info("响应参数为：" + post);
        //响应参数为：{"status":1,"msg":"聚合代付下单成功","data":{"merchantId":600006,"merchantOrderId":"213213122232213213","payOrderId":"6000n230721165932307fb400","payAmount":199.00,"vcPayAmount":199.00,"orderStatus":0,"sign":"f8fb710ac2244c997bebb31f2fda0ed8","expireTime":1690016372306,"createTime":1689929972306}}
            //{"status":-2,"msg":"提单异常:3010","data":null}
        String status = JSONUtil.parseObj(post).getStr("status");
        if("1".equals(status)){
            witComment(wit.getOrderId());
            return WIT_RESULT;
        }else {
            withdrawErMsg(wit, JSONUtil.parseObj(post).getStr("msg"),wit.getRetain2());
        }
        return "";
    }

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

    public static void main(String[] args) {
        Withdraw wit = new Withdraw();
        wit.setAmount(new BigDecimal(199.00));
        wit.setAccname("赵志波");
        wit.setBankNo("6231310501003697965");
        wit.setOrderId("213213122232213213");
        wit.setUserId("11122212321");
        wit.setBankcode("JLBANK");
        ChannelInfo info = new ChannelInfo();
        info.setChannelPassword("tBB&hUoJaC*2LnjO");
        info.setWitUrl("http://api3852.888mail.co");
        info.setChannelAppId("600006");
        info.setChannelType("1060");
     //   createDpay("http://asdsd.dads.com", wit, info);


    }
}
