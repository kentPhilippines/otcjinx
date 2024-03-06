package alipay.manage.api.channel.deal.wt;

import alipay.manage.api.config.ChannelInfo;
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
import org.springframework.stereotype.Component;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


@Component("wtWit")
public class wtWit extends PayOrderService {

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
                withdrawEr(wit,"url未设置",wit.getRetain2());
                return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
            }
            String createDpay = createDpay(userInfo.getDealUrl()+
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + "/wt-wit-notify",
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

    private   String createDpay(String notify, Withdraw wit, ChannelInfo channelInfo) {
        /**
         * platform_id	是	String(7)	商户Id
         * service_id	是	String(7)	服务Id
         * payout_cl_id	是	String(32)	商户订单号
         * amount	是	Integer(10)	金额(分)
         * notify_url	否	String(256)	交易结果通知地址
         * bank_name	否	String(16)	银行名称（仅韩国支付渠道必填）
         * name	是	String(16)	受款人户名
         * number	是	String(32)	受款人帐号
         * request_time	是	Integer(10)	请求时间(秒)
         */
        String platform_id = channelInfo.getChannelAppId();
        String service_id = channelInfo.getChannelType();
        String payout_cl_id = wit.getOrderId();
        String amount = wit.getAmount().multiply(new BigDecimal(100)).intValue() + "";
        String notify_url = notify;
        String bank_name = wit.getBankName();
        String name = wit.getAccname();
        String number = wit.getBankNo();
        String request_time = System.currentTimeMillis() / 1000 + "";
        Map map = new HashMap();
        map.put("platform_id",platform_id);
        map.put("service_id",service_id);
        map.put("payout_cl_id",payout_cl_id);
        map.put("amount",amount);
        map.put("notify_url",notify_url);
        map.put("bank_name",bank_name);
        map.put("name",name);
        map.put("number",number);
        map.put("request_time",request_time);
        String param = createParam(map);
        log.info("签名参数为：" + param);
        String s = md5(param + "&" + channelInfo.getChannelPassword()).toLowerCase(Locale.ROOT);
        map.put("sign", s);
        JSONObject jsonObject = JSONUtil.parseObj(map);
        log.info("请求参数为:" + jsonObject.toString());
        HttpResponse execute = HttpRequest.post(channelInfo.getWitUrl() + "/gateway/api/v2/payouts")
                .body(jsonObject).execute();
        String ruselt = execute.body().toString();
        log.info("响应参数为：" + ruselt);
        //响应参数为：{"error_code":"0012","error_msg":"不支援的银行!"}
        //响应参数为：{"error_code":"0018","error_msg":"商户余额不足!"}
        // 响应参数为：{"error_code":"0000","data":{"payout_id":"WUTONGPOT00362728"}}
        JSONObject jsonObject1 = JSONUtil.parseObj(ruselt);
        String error_code = jsonObject1.getStr("error_code");
        if("0000".equals(error_code)){
            witComment(wit.getOrderId());
            return WIT_RESULT;
        }else {
            withdrawErMsg(wit, jsonObject1.getStr("error_msg"),wit.getRetain2());
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
        wit.setAmount(new BigDecimal(200));
        wit.setAccname("李志平");
        wit.setBankNo("6230580000175121453");
        wit.setOrderId("2132131232213213");

        ChannelInfo  info = new ChannelInfo();
        info.setChannelPassword("bwWXDVFGDUGsTIMM");
        info.setWitUrl("https://hbj168.club");
        info.setChannelAppId("PF0361");
        info.setChannelType("SVC0004");


      //  createDpay("http://asdsd.dads.com",wit,info);


    }
}
