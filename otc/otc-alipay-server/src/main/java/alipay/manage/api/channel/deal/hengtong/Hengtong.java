package alipay.manage.api.channel.deal.hengtong;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component("hengtongpay")
public class Hengtong extends PayOrderService {
    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入恒通支付，当前请求产品：" + dealOrderApp.getRetain2() + "，当前请求渠道：" + channel + "】");
        String orderId = create(dealOrderApp, channel);
        UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
        if (StrUtil.isBlank(userInfo.getDealUrl())) {
            orderEr(dealOrderApp, "当前商户交易url未设置");
            return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
        }
        Result result = createOrder(userInfo.getDealUrl() +
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + HengtongUtil.NOTIFY,
                dealOrderApp.getOrderAmount()
                , orderId, getChannelInfo(channel, dealOrderApp.getRetain1()), dealOrderApp);
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            return result;
        }


    }

    private Result createOrder(String notify, BigDecimal orderAmount, String orderId, ChannelInfo channelInfo, DealOrderApp order) {
        String key = channelInfo.getChannelPassword();
        String merchant = channelInfo.getChannelAppId();
        String amount = orderAmount.toString();
        String paymentType = channelInfo.getChannelType();
        String username = StrUtil.uuid();
        String depositRealname = "";
        String callback = notify;
        String remark = "";
        String paymentReference = orderId;
        String clientIp = "";
        String sign = "";
        Map<String, Object> map = new HashMap<>();
        map.put("merchant", merchant);
        map.put("paymentType", paymentType);
        map.put("amount", amount);
        map.put("username", username);
        map.put("depositRealname", depositRealname);
        map.put("callback", callback);
        map.put("remark", remark);
        map.put("paymentReference", paymentReference);
        map.put("clientIp", clientIp);
        String param = HengtongUtil.createParam(map);
        log.info("【恒通加密前参数：" + param + "】");
        String s = HengtongUtil.md5(param + "&key=" + key);
        sign = s.toUpperCase();
        map.put("sign", sign);
        log.info("【恒通请求前参数：" + map.toString() + "】");
        String post = HttpUtil.post(channelInfo.getDealurl(), map);
        log.info("【恒通返回参数：" + post.toString() + "】");
        //  {"whether":"YES","code":1,"message":"","data":{"paymentReference":"bb9bd22795174e49ab9a18c57ae0b8ad","price":100,"qrCodeType":"BANK_BANK","drecType":6,"depositRealname":"","reference":"20210101163813667fxgbq","createTime":1609490293667,"redirect":"https://app.qeboqbl.cn//static/dp/bankbank2_1.html?reference=20210101163813667fxgbq","statusSt":0,"interfaceType":0,"revisedPrice":100,"recAccount":null,"recRealname":null,"recBank":null,"recBankName":null,"recBankBranch":null,"qrCodeContent":""},"success":true}
        //{"whether":"NO","code":65568,"message":"sign不存在","data":null,"success":false}
        JSONObject jsonObject = JSONUtil.parseObj(post);
        String whether = jsonObject.getStr("whether");
        if (StrUtil.isNotEmpty(whether) && whether.equals("YES")) {
            String data = jsonObject.getStr("data");
            JSONObject jsonObject1 = JSONUtil.parseObj(data);
            String url = jsonObject1.getStr("redirect");
            return Result.buildSuccessResult(url);
        } else {
            orderEr(order, jsonObject.getStr("message"));
            return Result.buildFailMessage(jsonObject.getStr("message"));
        }
    }
}
