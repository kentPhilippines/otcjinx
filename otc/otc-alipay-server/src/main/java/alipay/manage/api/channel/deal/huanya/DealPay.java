package alipay.manage.api.channel.deal.huanya;

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

@Component("huanyapay")
public class DealPay extends PayOrderService {
    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入家宝支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
        String orderId = create(dealOrderApp, channel);
        UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
        if (StrUtil.isBlank(userInfo.getDealUrl())) {
            orderEr(dealOrderApp, "当前商户交易url未设置");
            return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
        }
        Result result = createOrder(
                userInfo.getDealUrl() +
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + HuanYaUtil.NOTIFY,
                dealOrderApp.getOrderAmount(),
                orderId,
                getChannelInfo(channel, dealOrderApp.getRetain1()), dealOrderApp);
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            return result;
        }

    }

    /**
     * 商户ID	mchId	是	long	20001222	分配的商户号
     * 应用ID	appId	是	String(32)	0ae8be35ff634e2abe94f5f32f6d5c4f	该商户创建的应用对应的ID
     * 支付产品ID	productId	是	int	8000	支付产品ID
     * 商户订单号	mchOrderNo	是	String(30)	20160427210604000490	商户生成的订单号
     * 币种	currency	是	String(3)	cny	三位货币代码,人民币:cny
     * 支付金额	amount	是	int	100	支付金额,单位分
     * 支付结果前端跳转URL	returnUrl	否	String(128)	http://shop.anypay.org/return.htm	支付结果回调URL
     * 支付结果后台回调URL	notifyUrl	是	String(128)	http://shop.anypay.org/notify.htm	支付结果回调URL
     * 商品主题	subject	是	String(64)	anypay测试商品1	商品主题
     * 商品描述信息	body	是	String(256)	anypay测试商品描述	商品描述信息
     * 附加参数	extra	是	String(512)	{"openId":"o2RvowBf7sOVJf8kJksUEMceaDqo"}	特定渠道发起时额外参数,见下面说明
     * 签名	sign	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	签名值，详见签名算法
     * @param s
     * @param orderAmount
     * @param orderId
     * @param channelInfo
     * @param dealOrderApp
     * @return
     */
    private Result createOrder(String s, BigDecimal orderAmount, String orderId, ChannelInfo channelInfo, DealOrderApp dealOrderApp) {
        log.info("进入换环亚支付请求");
        Map<String, Object> mapp = new HashMap<>();
        String channelAppId = channelInfo.getChannelAppId();
        String[] split = channelAppId.split(",");
        String mchId = split[0];
        String appId = split[1];
        String productId = channelInfo.getChannelType();
        String mchOrderNo = orderId;
        String currency = "cny";
        Integer amount = orderAmount.intValue() * 100;
        String notifyUrl = s;
        String subject = "huanya";
        String body = "huanya";
        String extra = "huanya";
        mapp.put("mchId", mchId);
        mapp.put("appId", appId);
        mapp.put("productId", productId);
        mapp.put("mchOrderNo", mchOrderNo);
        mapp.put("currency", currency);
        mapp.put("amount", amount);
        mapp.put("notifyUrl", notifyUrl);
        mapp.put("subject", subject);
        mapp.put("body", body);
        mapp.put("extra", extra);
        String param = HuanYaUtil.createParam(mapp);
        param = param + "&key=" + channelInfo.getChannelPassword();
        log.info("【环亚加密前参数：" + param + "】");
        String sign = HuanYaUtil.md5(param);
        mapp.put("sign", sign);
        log.info("【环亚请求参数：" + param + "】");
        String post = HttpUtil.post(channelInfo.getDealurl(), mapp);
        log.info("【环亚响应参数：" + post + "】");
        JSONObject jsonObject = JSONUtil.parseObj(post);
        String code = jsonObject.getStr("retCode");
        if (code.equals("SUCCESS")) {
            String payParams = jsonObject.getStr("payParams");
            JSONObject pay = JSONUtil.parseObj(payParams);
            String payUrl = pay.getStr("payUrl");
            return Result.buildSuccessResult("支付处理中", payUrl);
        } else {
            orderEr(dealOrderApp, jsonObject.getStr("retMsg"));
            return Result.buildFailMessage(jsonObject.getStr("retMsg"));
        }
    }

}


