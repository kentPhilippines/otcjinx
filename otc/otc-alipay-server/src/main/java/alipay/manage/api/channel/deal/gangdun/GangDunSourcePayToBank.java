package alipay.manage.api.channel.deal.gangdun;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;
import otc.util.MapUtil;
import otc.util.encode.XRsa;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 惠付通手动 入款
 */
@Component("GangDunSourcePayToBank")
public class GangDunSourcePayToBank extends PayOrderService {
    private static SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Autowired
    private OrderService orderServiceImpl;
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel)  {
        log.info("【进入刚盾支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
        String orderId = create(dealOrderApp, channel);
        UserInfo userInfo = userInfoServiceImpl.findDealUrl(dealOrderApp.getOrderAccount());
        if (StrUtil.isBlank(userInfo.getDealUrl())) {
            orderEr(dealOrderApp, "当前商户交易url未设置");
            return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
        }
        Result result = createOrder(
                userInfo.getDealUrl() +
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/gangdun-notify",
                dealOrderApp.getOrderAmount(),
                orderId,
                getChannelInfo(channel, dealOrderApp.getRetain1()),dealOrderApp
                );
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
          //  orderEr(dealOrderApp, "错误消息：" + result.getMessage());
            return result;
        }
    }

    static  final  String PUBLIC_KEY  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKOHJwxgtnVsqDL5d7vgXVxE1babwye/ijK9cJ8f/r0H3zDMCq2dC2NOiVCszDCdU8UCQn7zw1Nj7K8a4HMMHZCBfx84quS/MHuzVk9koGTKeks+0haTo4ViQGKd5nwG+PkgOgYLWEIFCAtCEvayWgUuUt4+VYxtdFnbz/n8zIPwIDAQAB";


    private Result createOrder(String notify, BigDecimal orderAmount, String orderId, ChannelInfo channelInfo, DealOrderApp dealOrderApp) {
        try {
            Deal deal = new Deal();
            deal.setAmount(""+orderAmount);//金额
            deal.setAppId(channelInfo.getChannelAppId());//商户号
            deal.setApplyDate(d.format(new Date()));
            deal.setNotifyUrl(notify);
            deal.setPageUrl(dealOrderApp.getBack());
            deal.setOrderId(orderId);
            deal.setPassCode( channelInfo.getChannelType());
            deal.setSubject("deal_order");
            Map<String, Object> objectToMap = MapUtil.objectToMap(deal);
            String createParam = createParam(objectToMap);
            log.info("签名前请求串：" + createParam);
            String md5 = PayUtil.md5(createParam + channelInfo.getChannelPassword());
            log.info("签名：" + md5);
            deal.setSign(md5);
            Map<String, Object> objectToMap2 = MapUtil.objectToMap(deal);
            String createParam2 = createParam(objectToMap2);
            log.info("加密前字符串：" + createParam2);
            XRsa rsa = new XRsa(PUBLIC_KEY);
            String publicEncrypt = rsa.publicEncrypt(createParam2);
            log.info("加密后字符串：" + publicEncrypt);
            Map<String, Object> postMap = new HashMap<String, Object>();
            postMap.put("cipherText", publicEncrypt);
            postMap.put("userId", channelInfo.getChannelAppId());
            log.info("请求参数：" + postMap.toString());
            String post = HttpUtil.post( channelInfo.getDealurl(), postMap);
            log.info("相应结果集：" + post);
         //   {"success":false,"message":"当前账户交易权限未开通","result":null,"code":null}


            //：{"success":true,"message":"支付处理中","result":{"sussess":true,"cod":1,"openType":1,"returnUrl":"https://fpay510.5ga.xyz/pay9.html?order_id=20211031133850000764785&t=1635658730&sign=958ede34e9efa8859f2ff1a5444919ec","payInfo":""},"code":1}
            JSONObject jsonObject = JSONUtil.parseObj(post);
            String success = jsonObject.getStr("success");
            if("true".equals(success)){//请求支付成功
                String result = jsonObject.getStr("result");
                //{"sussess":true,"cod":0,"openType":1,"returnUrl":"http://api.tjzfcy.com/gateway/bankgateway/payorder/order/60326816340490956.html"}
                JSONObject resultObject = JSONUtil.parseObj(result);
                String returnUrl = resultObject.getStr("returnUrl");//支付链接
                return Result.buildSuccessResult(returnUrl);
            } else {
                orderAppEr(dealOrderApp,jsonObject.getStr("message"));
                return Result.buildFailMessage("支付失败");
            }
        } catch (Exception e){
            orderAppEr(dealOrderApp,"请求异常,联系技术处理");
            return Result.buildFailMessage("支付失败");
        }
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
}


class Deal {
    private String appId;
    private String orderId;
    private String notifyUrl;
    private String amount;
    private String passCode;
    private String sign;
    private String applyDate;
    private String subject;
    private String userid;
    private String pageUrl;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPassCode() {
        return passCode;
    }

    public void setPassCode(String passCode) {
        this.passCode = passCode;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    @Override
    public String toString() {
        return "Deal{" +
                "appId='" + appId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", pageUrl='" + pageUrl + '\'' +
                ", amount='" + amount + '\'' +
                ", passCode='" + passCode + '\'' +
                ", sign='" + sign + '\'' +
                ", applyDate='" + applyDate + '\'' +
                ", subject='" + subject + '\'' +
                ", userid='" + userid + '\'' +
                '}';
    }
}