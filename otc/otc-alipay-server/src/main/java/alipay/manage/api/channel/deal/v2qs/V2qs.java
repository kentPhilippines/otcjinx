package alipay.manage.api.channel.deal.v2qs;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.channel.util.qiangui.MD5;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("V2qs")
public class V2qs extends PayOrderService {
    @Autowired
    private UserInfoService userInfoServiceImpl;
    private static SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入  V2qs   Pay支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
        String orderId = create(dealOrderApp, channel);
        UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
        if (StrUtil.isBlank(userInfo.getDealUrl())) {
            orderEr(dealOrderApp, "当前商户交易url未设置");
            return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
        }
        String payInfo = "";
        if (dealOrderApp.getDealDescribe().contains("付款人")) {
            payInfo = dealOrderApp.getDealDescribe();
        }
        Result result = createOrder(
                userInfo.getDealUrl() +
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/V2qs-pay",
                dealOrderApp.getOrderAmount(),
                orderId,
                getChannelInfo(channel, dealOrderApp.getRetain1()), payInfo);
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            return result;
        }
    }

    /**
     * appId          是           账户id
     * orderId        是           订单号
     * notifyUrl      是           回调url
     * pageUrl        是           成功后跳转页面
     * amount         是           付款金额
     * passCode       是           通道代码
     * applyDate      是           时间格式：yyyyMMddHHmmss
     * sign           是           请求签名
     * userId         是           请传付款人姓名
     *
     * @return
     */
    private Result createOrder(
            String notify,
            BigDecimal orderAmount,
            String orderNo,
            ChannelInfo channelInfo,
            String payInfo
    ) {
        String appId = channelInfo.getChannelAppId();
        String orderId = orderNo;
        String notifyUrl = notify;
        String pageUrl = notifyUrl;
        String amount = orderAmount.toString();
        String passCode = channelInfo.getChannelType();
        String applyDate = d.format(new Date());
        String userId = getPayName(payInfo, orderNo);
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
        try {
            String post = HttpUtil.post(channelInfo.getDealurl() + "/v2/deal/pay", JSONUtil.parse(map).toString());
            log.info("响应数据为：" + post);
            //：{"success":true,"message":"支付处理中","result":{"sussess":true,"cod":1,"openType":1,"returnUrl":"http://20.188.122.189:8122/api/Pay/Cashier/8111c0c8479d4d418d9062293d82dae3","payInfo":"","payInfo2":""},"code":1}
            JSONObject jsonObject = JSONUtil.parseObj(post);
            String str = jsonObject.getStr("success");
            if ("true".equals(str)) {
                JSONObject result = jsonObject.getJSONObject("result");
                String str1 = result.getStr("returnUrl");
                return Result.buildSuccessResult(str1);
            } else {
              //  orderDealEr(orderId, jsonObject.getStr("message"));
            }
        } catch (Throwable e) {
            log.error(e);
        }
        return Result.buildFail();
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

    public static void main(String[] args) {
        String notify;
        BigDecimal orderAmount;
        String orderNo;
        ChannelInfo channelInfo;
        String payInfo;
        notify = "http://sdadadas.dasdsad.com";
        orderAmount = new BigDecimal(800);
        orderNo = System.currentTimeMillis() + "";
        channelInfo = new ChannelInfo();
        channelInfo.setChannelType("E_CNY");
        channelInfo.setChannelPassword("C3D0E098D16B4DAB9BE3E4903E741C18");
        channelInfo.setDealurl(" 47.242.205.138:8817");
        channelInfo.setChannelAppId("qixiing888");
        payInfo = "付款人：张纸";
        V2qs v2 = new V2qs();
        v2.createOrder(notify, orderAmount, orderNo, channelInfo, payInfo);
    }
}
