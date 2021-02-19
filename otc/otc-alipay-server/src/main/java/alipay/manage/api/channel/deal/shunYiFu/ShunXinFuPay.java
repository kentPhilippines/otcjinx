package alipay.manage.api.channel.deal.shunYiFu;

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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component("ShunXinFuPay")
public class ShunXinFuPay extends PayOrderService {
    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;


    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) {
        log.info("【进入顺心支付】");
        String channelId = channel;//配置的渠道账号
        String orderId = create(dealOrderApp, channelId);
        log.info("【本地订单创建成功，开始请求远程三方支付】");
        UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
        if (StrUtil.isBlank(userInfo.getDealUrl())) {
            orderEr(dealOrderApp, "当前商户交易url未设置");
            return Result.buildFailMessage("请联系运营为您的商户号设置交易url");
        }
        log.info("【回调地址ip为：" + userInfo.getDealUrl() + "】");
        Result result = createOrder(
                userInfo.getDealUrl() +
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/shunxinfu-source-notify",
                dealOrderApp.getOrderAmount(),
                orderId,
                getChannelInfo(channel, dealOrderApp.getRetain1()));
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            orderEr(dealOrderApp, "错误消息：" + result.getMessage());
            return result;
        }

    }


    /**
     * 参数 类型 是否必填 是否参与加签 说明
     * partner str(32) 是 是 商户编号，系统提供
     * service str(32) 是 是 参考service字典
     * tradeNo str(32) 是 是 交易订单号
     * amount str(32) 是 是 交易⾦额
     * notifyUrl str(256) 是 是 异步通知地址
     * extra str(32) 否 是 附加信息，查询、回调时返回商户
     * sign str(128) 是 否 签名
     *
     * @param notify
     * @param orderAmount
     * @param orderId
     * @param channelInfo
     * @return
     */
    private Result createOrder(String notify, BigDecimal orderAmount, String orderId, ChannelInfo channelInfo) {
        String partner = channelInfo.getChannelAppId();//1000043363
        String service = channelInfo.getChannelType();//10108
        String tradeNo = orderId;
        String amount = orderAmount.toString();
        String notifyUrl = notify;
        String extra = orderId;
        String key = "&" + channelInfo.getChannelPassword();//zzwyolmbhyledwlnta
        Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("partner", partner);
        map.put("service", service);
        map.put("tradeNo", tradeNo);
        map.put("amount", amount);
        map.put("resultType", "json");
        map.put("notifyUrl", notifyUrl);
        map.put("extra", extra);
        String param = ShunXinFuUtil.createParam(map);
        param += key;
        log.info("【顺心付签名参数为：" + param + "】");
        String s = ShunXinFuUtil.md5(param);
        map.put("sign", s);
        log.info("【顺心付请求前参数为：" + map + "】");
        String post = HttpUtil.post(channelInfo.getDealurl(), map);
        log.info("【顺心付响应数据：" + post + "】");
        JSONObject jsonObject = JSONUtil.parseObj(post);
        String isSuccess = jsonObject.getStr("isSuccess");
        if ("T".equals(isSuccess)) {
            return Result.buildSuccessResult(jsonObject.getStr("url"));
        } else {
            return Result.buildFailMessage(jsonObject.getStr("msg"));
        }
/**
 {"isSuccess":"T","failCode":null,"failMsg":null,"sign":"ebb2c30fb2f663b5426fe5be97af96aa","charset":"utf-8","signType":"md5","url":"https://ap5xt6p0w.vanns.vip/api/ali/bank/48ea725ea6df4269804377a392e9d59a"}
 */
/**
 * {"msg":"notifyUrl为空;","code":"E090001","isSuccess":"F"}
 */
    }
}
