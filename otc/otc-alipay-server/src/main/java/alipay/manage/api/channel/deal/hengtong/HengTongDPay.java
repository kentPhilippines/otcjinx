package alipay.manage.api.channel.deal.hengtong;

import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.UserInfoService;
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

import java.util.HashMap;
import java.util.Map;


@Component("HengTongDPay")
public class HengTongDPay extends PayOrderService {
    private static final Log log = LogFactory.get();
    private static final String WIT_RESULT = "SUCCESS";
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result withdraw(Withdraw wit) {
        log.info("【进入恒通代付】");
        try {
            UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(wit.getUserId());
            if (StrUtil.isBlank(userInfo.getDealUrl())) {
                withdrawEr(wit, "当前商户交易url未设置", wit.getRetain1());
                return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
            }
            String channel = "";
            if (StrUtil.isNotBlank(wit.getChennelId())) {//支持运营手动推送出款
                channel = wit.getChennelId();
            } else {
                channel = wit.getWitChannel();
            }
            ChannelInfo channelInfo = getChannelInfo(channel, wit.getWitType());
            String createDpay = createDpay(userInfo.getDealUrl() +
                    PayApiConstant.Notfiy.NOTFIY_API_WAI + HengtongUtil.D_NOTIFY, wit, channelInfo);
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
        /**
         * merchant	M	商户号
         * requestReference	M	商户订单号
         * merchantBank	M	取款银行编号。例如，ICBC，见下边《银行编号》
         * merchantBankCardRealname	M	取款银行卡持卡人
         * merchantBankCardAccount	M	取款银行卡账户
         * merchantBankCardProvince	M	取款银行卡开户省
         * merchantBankCardCity	M	取款银行卡开户市
         * merchantBankCardBranch	M	取款银行卡开户分行
         * amount	M	提款额度
         * remark	O	备注
         * callback	O	回调地址。
         * 只有成功的订单会回调。
         * 收到回调后，请返回 "success" 字符串。
         * sign	M	签名，签名方式参考“签名说明”
         *
         *
         *
         * merchant	M	商户号
         * requestReference	M	商户订单号
         * merchantBank	M	取款银行编号。例如，ICBC，见下边《银行编号》
         * merchantBankCardRealname	M	取款银行卡持卡人
         * merchantBankCardAccount	M	取款银行卡账户
         * merchantBankCardProvince	M	取款银行卡开户省
         * merchantBankCardCity	M	取款银行卡开户市
         * merchantBankCardBranch	M	取款银行卡开户分行
         * amount	M	提款额度
         * remark	O	备注
         * callback	O	服务端回调地址。
         * 订单成功或失败时，会回调。
         * 收到回调后，请返回 "success" 字符串。
         * 否则会再次回调（共6次）。不填写则不会回调。
         * sign	M	签名，签名方式参考“签名说明”
         */
        String key = channelInfo.getChannelPassword();
        String merchant = channelInfo.getChannelAppId();
        String amount = wit.getAmount().toString();
        String callback = notify;
        String remark = "";
        String requestReference = wit.getOrderId();
        String merchantBank = wit.getBankcode();
        String merchantBankCardRealname = wit.getAccname();
        String merchantBankCardAccount = wit.getBankNo();
        String merchantBankCardProvince = "北京市";
        String merchantBankCardCity = "朝阳区";
        String merchantBankCardBranch = "1";
        String sign = "";
        Map<String, Object> map = new HashMap<>();
        map.put("merchant", merchant);
        map.put("merchantBank", merchantBank);
        map.put("amount", amount);
        map.put("merchantBankCardRealname", merchantBankCardRealname);
        map.put("merchantBankCardAccount", merchantBankCardAccount);
        map.put("callback", callback);
        map.put("remark", remark);
        map.put("requestReference", requestReference);
        map.put("merchantBankCardProvince", merchantBankCardProvince);
        map.put("merchantBankCardCity", merchantBankCardCity);
        map.put("merchantBankCardBranch", merchantBankCardBranch);
        String param = HengtongUtil.createParam(map);
        log.info("【恒通加密前参数：" + param + "】");
        String s = HengtongUtil.md5(param + "&key=" + key);
        sign = s.toUpperCase();
        map.put("sign", sign);
        log.info("【恒通请求前参数：" + map.toString() + "】");
        String post = HttpUtil.post(channelInfo.getWitUrl(), map);
        log.info("【恒通返回参数：" + post.toString() + "】");
        /**
         * success	M	true，处理成功。false，处理失败。
         * code	M	如果网络错误，网络超时，可以重试，但必须使用相同的商户订单号。不同的订单号将视为新订单。如果订单已到达服务器并正在处理，传递相同的商户订单号，服务端会返回重复提交，即，success=false,code=65559
         * message	O	结果说明，通常用于说明失败信息
         * data	C	商户取款提案编号（如果失败则没有）
         */
        JSONObject jsonObject = JSONUtil.parseObj(post);
        String whether = jsonObject.getStr("success");
        if ("true".equals(whether)) {
            return WIT_RESULT;
        } else {
            withdrawEr(wit, jsonObject.getStr("message"), wit.getRetain2());
        }
        return "";
    }
}
