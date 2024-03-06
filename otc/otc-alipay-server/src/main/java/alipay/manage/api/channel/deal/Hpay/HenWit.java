package alipay.manage.api.channel.deal.Hpay;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component("HenWit")
public class HenWit extends PayOrderService {
    private static final String WIT_RESULT = "SUCCESS";
    private static SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmss");
    @Autowired
    private UserInfoService userInfoServiceImpl;


    @Override
    public Result withdraw(Withdraw wit) {
        log.info("【进入申付话费代付】");
        try {
            log.info("【本地订单创建成功，开始请求远程三方代付接口】");
            String channel = "";
            if (StrUtil.isNotBlank(wit.getChennelId())) {//支持运营手动推送出款
                channel = wit.getChennelId();
            } else {
                channel = wit.getWitChannel();
            }

            if (isNumber(wit.getAmount().toString())) {
                Result result = withdrawEr(wit, "代付订单不支持小数提交", wit.getRetain2());
                if (result.isSuccess()) {
                    return Result.buildFailMessage("代付订单不支持小数提交");
                }
            }
            UserInfo userInfo = userInfoServiceImpl.findDealUrl(wit.getUserId());
            if (StrUtil.isBlank(userInfo.getDealUrl())) {
                withdrawEr(wit, "url未设置", wit.getRetain2());
                return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
            }
            ChannelInfo channelInfo = getChannelInfo(channel, wit.getWitType());
            String createDpay = createDpay(userInfo.getDealUrl() +
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + "/henWit-noyfit",
                    wit, channelInfo);
            if (StrUtil.isNotBlank(createDpay) && createDpay.equals(WIT_RESULT)) {
                return Result.buildSuccessMessage("代付成功等待处理");
            } else {
                return Result.buildFailMessage(createDpay);
            }
        } catch (Throwable e) {
            log.error("【错误信息打印】" + e.getMessage());
            return Result.buildFailMessage("代付失败");
        }
    }

    private static SimpleDateFormat d = new SimpleDateFormat("YYYYMMDDhhmmss");

    private String createDpay(String notify, Withdraw wit, ChannelInfo channelInfo) {
        try {
        String merchId = channelInfo.getChannelAppId();
        String money = wit.getAmount().toString();
        String userId = wit.getAccname();
        String orderId = wit.getOrderId();
        String bankType = "0";
        String payType = "88";
        String curType = "CNY";
        String name = wit.getAccname();
        String bank_key = "OTHER";
        String bank = wit.getBankName();
        String card = wit.getBankNo();
        String time = d.format(new Date());
        String notifyUrl = notify;
        String signType = "MD5";
        Map<String, Object> map = new HashMap<>();
        map.put("merchId", merchId);
        map.put("money", money);
        map.put("userId", userId);
        map.put("curType", curType);
        map.put("orderId", orderId);
        map.put("time", time);
        map.put("notifyUrl", notifyUrl);
        map.put("payType", payType);
        map.put("bankType", bankType);
        map.put("name", name);
        map.put("signType", signType);
        map.put("bank_key", bank_key);
        map.put("bank", bank);
        map.put("card", card);
        String param = createParam(map);
        log.info("恒支付签名参数：" + param);
        String k = param + "&md5_key=" + channelInfo.getChannelPassword();
        log.info("恒支付签名参数："+k);
        String md5 = PayUtil.md5(k);
        String sign = md5;
        log.info(md5);
        map.put("sign", sign);
        log.info("恒支付代付请求参数："+map.toString());
        String post1 = HttpUtil.post( channelInfo.getWitUrl(), map);
        log.info(post1);
        JSONObject resultObject = JSONUtil.parseObj(post1);
      if(  "PROCESSING".equals(resultObject.getStr("code")))  {
          witComment(wit.getOrderId());
          return WIT_RESULT;
      }else {
          withdrawErMsg(wit, "异常，请切换通道",wit.getRetain2());
          return "";
      }
        } catch (Exception e) {
            log.error("请求代付异常", e);
            withdrawErMsg(wit, "代付异常,网络异常", wit.getRetain2());
            return "";
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
