package alipay.manage.api.channel.deal.v2qs;

import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.ChannelLocalUtil;
import alipay.manage.api.config.ChannelUtil;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealWit;
import alipay.manage.util.CheckUtils;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;
import otc.bean.alipay.OrderDealStatus;
import otc.result.Result;
import alipay.manage.api.channel.util.qiangui.MD5;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


@Component
public class V2qsUtil extends ChannelUtil implements ChannelLocalUtil {
    private static SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");
    @Override
    public Result channelDealPush(DealOrder order, ChannelInfo channelInfo) {
        String appId = channelInfo.getChannelAppId();
        String orderId = order.getOrderId();
        String notifyUrl = order.getNotify();
        String pageUrl = notifyUrl;
        String amount = order.getDealAmount()+"";
        String passCode = channelInfo.getChannelType();
        String applyDate = d.format(new Date());
        String userId =  order.getMcRealName();
        Map map = new HashMap();
        map.put("appId", appId);
        map.put("orderId", orderId);
        map.put("notifyUrl", notifyUrl);
        map.put("pageUrl", pageUrl);
        map.put("amount", amount);
        map.put("passCode", passCode);
        map.put("applyDate", applyDate);
        map.put("userId", userId);
        String param = createParam(map);
        String md5 = MD5.MD5Encode(param + channelInfo.getChannelPassword());
        String sign = md5;
        map.put("sign", sign);
        log.info("请求前参数为: " + JSONUtil.parse(map).toString());
        logRequestDeal(this.getClass().getName(), JSONUtil.parse(map).toString(), order.getOrderId(), order.getExternalOrderId());
        try {
            String post = HttpUtil.post(channelInfo.getDealurl() + "/v2/deal/pay", JSONUtil.parse(map).toString());
            logResponseDeal(this.getClass().getName(), post, order.getOrderId(), order.getExternalOrderId());
            log.info("响应数据为：" + post);
            //：{"success":true,"message":"支付处理中","result":{"sussess":true,"cod":1,"openType":1,"returnUrl":"http://20.188.122.189:8122/api/Pay/Cashier/8111c0c8479d4d418d9062293d82dae3","payInfo":"","payInfo2":""},"code":1}
            JSONObject jsonObject = JSONUtil.parseObj(post);
            String str = jsonObject.getStr("success");
            if ("true".equals(str)) {
                JSONObject result = jsonObject.getJSONObject("result");
                String str1 = result.getStr("returnUrl");
                return Result.buildSuccessResult(str1);
            } else {
                orderEr(order, post);
            }
        } catch (Throwable e) {
           log.error(" v2qs 渠道异常 ",e);
        }
        return Result.buildFail();
    }

    @Override
    public Result channelWitPush(DealWit order, ChannelInfo channelInfo) {












        return null;
    }


    public Result witNotify(Map<String, Object> map) {






        return Result.buildFail();
    }

    public Result dealNotify(Map map) {
        log.info("【收到V2qsNotify手动支付成功回调】");
        String sign = map.get("sign")+"";
        String apporderid = map.get("apporderid")+"";
        map.remove("sign");
        map.put("statusdesc", "成功");
        String channelKey = super.getChannelKey(apporderid);
        String md5 = CheckUtils.getSign(map, channelKey);
        if (md5.equals(sign)) {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名成功】");
        } else {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名失败】");
          return Result.buildFailMessage("签名失败");
        }
        Result dealpayNotfiy =              dealNotfiy(apporderid, "三方系统回调成功");

        if (dealpayNotfiy.isSuccess()) {
            return dealpayNotfiy;
        }
        return Result.buildFail();
    }
    public void findBalance(String channelId, ChannelInfo channelInfo) {
        log.info("【调用查询渠道余额方法，当前渠道信息为："+channelInfo.toString()+"】");

    }
}
