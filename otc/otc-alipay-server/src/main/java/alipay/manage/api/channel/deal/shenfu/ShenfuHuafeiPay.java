package alipay.manage.api.channel.deal.shenfu;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.channel.util.shenfu.PayUtil;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("ShenfuHuafeiPay")
public class ShenfuHuafeiPay extends PayOrderService {
    /**
     * @param notify
     * @param orderAmount
     * @param orderId
     * @param channelInfo
     * @return
     */
    private static SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmss");
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入申付话费支付，当前请求产品：" + dealOrderApp.getRetain1() +
                "，当前请求渠道：" + channel + "】");
        String orderId = create(dealOrderApp, channel);
        UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
        if (StrUtil.isBlank(userInfo.getDealUrl())) {
            orderEr(dealOrderApp, "当前商户交易url未设置");
            return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
        }
        String back = dealOrderApp.getBack();
        if (StrUtil.isBlank(back)) {
            orderEr(dealOrderApp, "当前交易产品支付成功同步跳跃地址未传");
            return Result.buildFailMessage("当前交易产品支付成功同步跳跃地址未传");
        }
        Result result = createOrder(
                userInfo.getDealUrl() +
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/shenfu-huafei-notify",
                dealOrderApp.getOrderAmount(),
                orderId,
                getChannelInfo(channel, dealOrderApp.getRetain1()),
                back
        );
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            orderEr(dealOrderApp, "错误消息：" + result.getMessage());
            return result;
        }
    }

    private Result createOrder(String notify, BigDecimal orderAmount, String orderId,
                               ChannelInfo channelInfo, String back) {
        /**
         * 参数	必须	说明
         * p1_merchantno		商户号: 请访问商户后台来获取您的商户号。
         * p2_amount		支付金额: 以元为单位，精确到小数点后 2 位。如 15.00 及 15 都是合法的参数值。
         * p3_orderno		订单号: 唯一标识您的支付平台的一笔订单，必须保证此订单号的唯一性。
         * p4_paytype		支付产品类型编码: 如 AlipayScan 表示支付宝扫码, WechatH5 表示微信 H5 等。请参阅支付产品类型编码表。
         * p5_reqtime		支付发起时间: 14 字节长的数字串，格式布局如 yyyyMMddHHmmss. (如 20060102150405 表示 2006年1月2日下午3点04分05秒)
         * p6_goodsname		商品名称: 请勿使用空白字符串值。
         * p7_bankcode		银行编码: 付款银行的编码，仅在网关支付产品中有意义, 其他支付产品请传递空白字符串或忽略该参数。 具体支持银行范围和相关银行编码请参考银行编码表。
         * p8_returnurl		同步跳转 URL: 支付过程完成后，用户设备上的浏览器将被跳转到此 URL.
         * p9_callbackurl		后台异步通知 (回调) 地址: 支付成功后，我方服务器将向该地址发起 GET 异步通知请求。
         * 请注意，此 URL 不能携带查询参数。
         * sign		MD5 签名: HEX 大写, 32 字节。
         */
        String p1_merchantno = channelInfo.getChannelAppId();
        String p2_amount = orderAmount.intValue() + ".00";
        String p3_orderno = orderId;
        String p4_paytype = channelInfo.getChannelType();
        String p5_reqtime = time.format(new Date());
        String p6_goodsname = channelInfo.getChannelType();
        String p8_returnurl = back;
        String p9_callbackurl = notify;
        Map map = new HashMap();
        map.put("p1_merchantno", p1_merchantno);
        map.put("p2_amount", p2_amount);
        map.put("p3_orderno", p3_orderno);
        map.put("p4_paytype", p4_paytype);
        map.put("p5_reqtime", p5_reqtime);
        map.put("p6_goodsname", p6_goodsname);
        map.put("p8_returnurl", p8_returnurl);
        map.put("p9_callbackurl", p9_callbackurl);
        log.info("【绅付话费加签参数：" + map + "】");
        String createParam = PayUtil.createParam(map);
        String md5 = PayUtil.md5(createParam + "&key=" + channelInfo.getChannelPassword());
        map.put("sign", md5.toUpperCase());
        String post = HttpUtil.post(channelInfo.getDealurl(), map);
        log.info("【绅付话费返回数据：" + post + "】");
        JSONObject jsonObject = JSONUtil.parseObj(post);
        String rspcode = jsonObject.getStr("rspcode");
        if (rspcode.equals("A0")) {
            String payurl = jsonObject.getStr("data");
            return Result.buildSuccessResult(payurl);
        } else {
            String errmsg = jsonObject.getStr("rspmsg");
            return Result.buildFailMessage(errmsg);
        }
    }
}
