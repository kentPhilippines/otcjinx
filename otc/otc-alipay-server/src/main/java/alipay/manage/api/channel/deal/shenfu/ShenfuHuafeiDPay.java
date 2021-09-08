package alipay.manage.api.channel.deal.shenfu;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.NotfiyChannel;
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
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("ShenfuHuafeiDPay")
public class ShenfuHuafeiDPay extends PayOrderService   {
    private static final String WIT_RESULT = "SUCCESS";
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
                withdrawEr(wit,"url未设置",wit.getRetain2());
                return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
            }
            String createDpay = createDpay(userInfo.getDealUrl()+
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + "/huitongfuwitToBank-noyfit",
                    wit, getChannelInfo(channel, wit.getWitType()));
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
    private String createDpay(String notify, Withdraw wit, ChannelInfo channelInfo) {


/**
 * 请求参数
 * 参数	必须	说明
 * p1_merchantno		商户号: 请访问商户后台来获取您的商户号。
 * p2_amount		代付金额: 以元为单位，精确到小数点后 2 位。如 2015.50 及 2015.5 都是合法的参数值。
 * p3_orderno		代付订单号: 您的每一笔代付都应具有唯一的代付订单号，此订单号也用于后续代付订单查询阶段。 请参考代付订单查询接口。
 * p4_truename		收款人姓名: 收款卡在相关银行注册时使用的真实姓名 (账户名)。收款卡与账户名不符将导致代付失败。
 * p5_cardnumber		收款人账号: 收款人银行卡号。目前绅付2支付平台仅支持对私代付，且不支持信用卡。
 * p6_branchbankname		开户支行名称: 办理银行开户手续的银行营业网点，如 中国邮政储蓄银行XX市分行XX支行XX分理处。
 * 特别提醒：请尽量提供完整及正确的开户支行名称。错误的知乎支行名称可能导致到账延迟或银行打款失败。
 * sign		MD5 签名: HEX 大写, 32 字节。
 */
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            String oid_partner = channelInfo.getChannelAppId();
            String no_order = wit.getOrderId();
            double v = wit.getActualAmount().doubleValue();
            String acct_name = wit.getAccname();
            String card_no = wit.getBankNo();
            String bank_name = wit.getBankName();
            map.put("p1_merchantno", oid_partner);
            map.put("p2_amount", v);
            map.put("p3_orderno", no_order);
            map.put("p4_truename", acct_name);
            map.put("p5_cardnumber", card_no);
            map.put("p6_branchbankname", bank_name);
            log.info("【绅付话费代付加签参数：" + map + "】");
            String createParam = PayUtil.createParam(map);
            String md5 = PayUtil.md5(createParam + "&key=" + channelInfo.getChannelPassword());
            map.put("sign", md5.toUpperCase());
            log.info("【绅付话费代付请求参数为：" + map.toString() + "】");
            String post = HttpUtil.post( channelInfo.getWitUrl(), map, 2000);
            log.info("【绅付话费代付代付响应参数为：" + post + "】");
            JSONObject parseObj = JSONUtil.parseObj(post);
            String object = parseObj.getStr("rspcode");
            if ("A0".equals(object)) {
                witComment(wit.getOrderId());
                return WIT_RESULT;
            } else {
                withdrawEr(wit, parseObj.getStr("rspmsg"), wit.getRetain2());
            }
            return "";
        } catch (Throwable e) {
            log.error("请求绅付话费代付异常", e);
            withdrawErMsg(wit, "代付异常,网络异常", wit.getRetain2());
            return "";
        }
    }




    public Result findOrder(){






        return null;

    }

}
