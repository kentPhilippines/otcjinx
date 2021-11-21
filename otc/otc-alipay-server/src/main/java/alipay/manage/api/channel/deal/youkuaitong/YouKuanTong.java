package alipay.manage.api.channel.deal.youkuaitong;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component("YouKuanTong")
public class YouKuanTong extends PayOrderService {
    private static final Log log = LogFactory.get();
    private static final String WIT_RESULT = "SUCCESS";
    @Autowired
    private UserInfoService userInfoServiceImpl;

    private static String getNowDateStr() {
        return DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMAT);
    }

    @Override
    public Result withdraw(Withdraw wit) {
        log.info("【进入优快通动代付代付】");
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
                withdrawEr(wit,"url未设置",wit.getRetain2());
                return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
            }



            String createDpay = createDpay(userInfo.getDealUrl()+
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + "/youkuaitong-wit-noyfit",
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

    private String createDpay(String s, Withdraw wit, ChannelInfo channelInfo) {
        try {

        String Amount = wit.getAmount().toString();
        String BankCardBankName = wit.getAccname();
        String MerchantId = channelInfo.getChannelAppId();
        String MerchantUniqueOrderId =  wit.getOrderId();
        String NotifyUrl = s;
        String BankCardNumber = wit.getBankNo();
        String Timestamp = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN);
        String WithdrawTypeId = "0";
        String BankCardRealName = wit.getBankName();
        Map<String,Object> map = new HashMap<>();
        map.put("Amount",Amount);
        map.put("WithdrawTypeId",WithdrawTypeId);
        map.put("MerchantId",MerchantId);
        map.put("MerchantUniqueOrderId",MerchantUniqueOrderId);
        map.put("NotifyUrl",NotifyUrl);
        map.put("BankCardBankName",BankCardBankName);
        map.put("BankCardNumber",BankCardNumber);
        map.put("BankCardRealName",BankCardRealName);
        map.put("Timestamp",Timestamp);
        String createParam = PayUtil.createParam(map);
        createParam.toLowerCase();
        log.info("优快通代付参数"+createParam);
        String md5 = PayUtil.md5(createParam + channelInfo.getChannelPassword());
        map.put("sign", md5);
        log.info("请求参数："+map.toString());
        String post = HttpUtil.post(channelInfo.getWitUrl()+"/InterfaceV5/CreateWithdrawOrder/", map);
        log.info(post);
        //{"Code":"0","Message":"提交成功，请等待处理完成后的回调或调用查询接口查询状态","WithdrawOrderStatus":"0"}
        JSONObject jsonObject = JSONUtil.parseObj(post);
        Object code = jsonObject.get("Code");
        if(null != code){
            if("0".equals(code)){
                witComment(wit.getOrderId());
                return WIT_RESULT;
            } else {
                withdrawEr(wit, jsonObject.getStr("Message"), wit.getRetain2());
            }
        }
        return "";
        } catch (Exception e) {
            log.error("请求优快通代付异常", e);
            withdrawErMsg(wit, "代付异常,网络异常", wit.getRetain2());
            return "";
        }
    }
}
