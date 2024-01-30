package alipay.manage.api.channel.deal.CGPay;

import alipay.manage.api.channel.deal.anxin.util.PayDigestUtil;
import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component("CGWit")
public class CGWit extends PayOrderService {
    private static final Log log = LogFactory.get();
    private static final String WIT_RESULT = "SUCCESS";
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Override
    public Result withdraw(Withdraw wit) {
        log.info("【进入CG手动代付代付】");
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
            String createDpay = createDpay(userInfo.getDealUrl() +
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + "/CGWit-noyfit",
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

    private String createDpay(String notify, Withdraw wit, ChannelInfo channelInfo) {
        try {
            String pay_customer_id = channelInfo.getChannelAppId();
            String pay_card_no = wit.getBankNo();
            String pay_bank_name = wit.getBankName();
            String pay_account_name = wit.getAccname();
            String pay_apply_date = System.currentTimeMillis() / 1000 + "";
            String pay_order_id = wit.getOrderId();
            String pay_notify_url = notify;
            String pay_amount = wit.getAmount() + "";
            String key = channelInfo.getChannelPassword();
            Map map = new HashMap();
            map.put("pay_customer_id", pay_customer_id);
            map.put("pay_apply_date", pay_apply_date);
            map.put("pay_order_id", pay_order_id);
            map.put("pay_notify_url", pay_notify_url);
            map.put("pay_amount", pay_amount);
            map.put("pay_bank_name", pay_bank_name);
            map.put("pay_account_name", pay_account_name);
            map.put("pay_card_no", pay_card_no);
            String reqSign = PayDigestUtil.getSign(map, key);
            map.put("pay_md5_sign", reqSign.toUpperCase(Locale.ROOT));
            HttpResponse header = HttpRequest.post(channelInfo.getWitUrl())
                    .header("Header", "Content-Type: application/json")
                    .body(JSONUtil.parseObj(map)).execute();
            String s = header.body().toString();
            log.info(s);
            JSONObject jsonObject = JSONUtil.parseObj(s);
            String code = jsonObject.getStr("code");
            if ("0".equals(code)) {
                witComment(wit.getOrderId());
                return WIT_RESULT;
            } else {
                String message = jsonObject.getStr("message");
                withdrawErMsg(wit, message, wit.getRetain2());
            }
            return "";
        } catch (Exception e) {
            log.error("请求代付异常", e);
            withdrawErMsg(wit, "代付异常,网络异常", wit.getRetain2());
            return "";
        }
    }
}
