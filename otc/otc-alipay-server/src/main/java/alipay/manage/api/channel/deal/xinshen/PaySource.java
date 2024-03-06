package alipay.manage.api.channel.deal.xinshen;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Component("XinShenHuafeiPay")
public class PaySource extends PayOrderService {
    private static SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmss");

    @Autowired
    private UserInfoService userInfoServiceImpl;
    private String name = "付款人：";
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入申付话费支付，当前请求产品：" + dealOrderApp.getRetain1() +
                "，当前请求渠道：" + channel + "】");
        String orderId = create(dealOrderApp, channel);
        UserInfo userInfo = userInfoServiceImpl.findDealUrl(dealOrderApp.getOrderAccount());
        if (StrUtil.isBlank(userInfo.getDealUrl())) {
            orderEr(dealOrderApp, "当前商户交易url未设置");
            return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
        }
        String payInfo = "";
        if(dealOrderApp.getDealDescribe().contains("付款人")){
            payInfo = dealOrderApp.getDealDescribe();
        }
        ChannelInfo channelInfo = getChannelInfo(channel, dealOrderApp.getRetain1());
        Result result = createOrder(
                userInfo.getDealUrl() +
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/xinshen-huafei-notify",
                dealOrderApp.getOrderAmount(),
                orderId,
                getChannelInfo(channel, dealOrderApp.getRetain1()),
                payInfo
        );
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            orderEr(dealOrderApp, "错误消息：" + result.getMessage());
            return result;
        }
    }

    private Result createOrder(String notify, BigDecimal orderAmount, String orderId, ChannelInfo channelInfo, String payInfo) {

        /**
         * pid	商户号	是	是	平台分配商户号
         * order_no	订单号	是	是	保证唯一值
         * amount	金额	是	是	单位：元
         * pay_type	支付类型	是	是	支付类型，详见附表支付类型
         * realname	真实姓名	是	否	 支付玩家真实姓名，必须真实填写
         * notify_url	回调地址	是	是	回调地址
         * return_type	返回类型	否	否	传json，支付将返回支付地址pay_url,默认使用表单方式跳转至平台收银台,
         * 数据结构参考查询接口，接口data.pay_url
         * sign	签名	是	否	参照签名算法
         */
        String pid = channelInfo.getChannelAppId();
        String amount = orderAmount.intValue() + ".00";
        String order_no = orderId;
        String pay_type = channelInfo.getChannelType();
        String notify_url = notify;
        Map map = new HashMap();
        map.put("pid", pid);
        map.put("order_no", order_no);
        map.put("amount", amount);
        map.put("pay_type", pay_type);
        map.put("notify_url", notify_url);
        String post  = "";
        try {
            log.info("【新盛话费加签参数：" + map + "】");
            String createParam = PayUtil.createParam(map);
            String md5 = PayUtil.md5(createParam + "&key=" + channelInfo.getChannelPassword());
            map.put("sign", md5);
            map.put("realname", getPayName(payInfo, orderId));
            map.put("return_type", "json");
            try{
                log.info(channelInfo.getDealurl());
                log.info(map.toString());
                post = HttpUtil.post(channelInfo.getDealurl(), map);
            }catch (Throwable e){
                log.error("异常",e);
            }
        }catch (Throwable e){
            log.error("兴盛异常",e);
        }
        log.info("【新盛话费返回数据：" + post + "】");
        JSONObject jsonObject = JSONUtil.parseObj(post);
        String status = jsonObject.getStr("status");
        if ("1".equals(status)) {
            String payUrlInfo = jsonObject.getStr("data");
            JSONObject payinfo = JSONUtil.parseObj(payUrlInfo);
            String pay_url = payinfo.getStr("pay_url");
            return Result.buildSuccessResult(pay_url);
        } else {
            String errmsg = jsonObject.getStr("info");
            return Result.buildFailMessage(errmsg);
        }
    }
}
