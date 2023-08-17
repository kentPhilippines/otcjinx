package alipay.manage.api.channel.v2.hongyuntong;

import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.ChannelUtil;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealWit;
import alipay.manage.bean.util.PayInfo;
import alipay.manage.bean.util.v2.ResultDeal;
import alipay.manage.bean.util.WitInfo;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;
import otc.api.alipay.Common;
import otc.bean.alipay.OrderDealStatus;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class HongYunTongUtil extends ChannelUtil {

    static Map<String, String> PayTypeIdFormat = new HashMap();
    static Map<String, String> orderStatusDeal = new HashMap();
    static Map<String, String> orderStatusWit = new HashMap();

    static {
        init();
    }

    static void init() {
        PayTypeIdFormat.put(ResultDeal.INFO, "CARD");
        PayTypeIdFormat.put(ResultDeal.URL, "URL");
        /**
         * 0 待支付
         * 100 支付成功
         * -90 支付失败
         */
        orderStatusDeal.put("0", Common.Order.DealOrder.ORDER_STATUS_DISPOSE);
        orderStatusDeal.put("100", Common.Order.DealOrder.ORDER_STATUS_SU);
        orderStatusDeal.put("-90", Common.Order.DealOrder.ORDER_STATUS_ER);
        orderStatusWit.put("100", Common.Order.Wit.ORDER_STATUS_SU);
        orderStatusWit.put("-90", Common.Order.Wit.ORDER_STATUS_ER);
    }


    /**
     * Amount	                    是	string	订单金额	2000.88	单位元，最多2位小数，建议格式#.##
     * ClientRealName	            是	string	付款卡真实姓名	张三 name	部分渠道需要核对付款卡真实姓名，否则无法支付 不可为空，暂无数据时请传递固定值"name"（不含引号）
     * Ip	                        是	string	玩家IP地址  或玩家标识	8.8.8.8 user1234 若您无法获得IP地址，请传递可以最大限度区分玩家个体的标识，例如玩家ID、玩家账号的md5值、下游商户的ID值、下游商户账号的md5值等等
     * MerchantId	                是	string	商户ID	88888	见开户文档
     * MerchantUniqueOrderId	    是	string	商户唯一订单ID	uuid123456789	每次提交不可重复 建议使用guid、uuid或时间戳加随机数
     * NotifyUrl	                是	string	支付结果异步通知地址	http://yourdomain/a/b/c	支付完成后服务器会向此地址发送结果，无需预编码
     * PayTypeId	                是	string	支付编码	zfb	见开户文档
     * PayTypeIdFormat	            是	string	设定返回数据类型	URL CARD	URL：返回收银台地址（推荐使用）  CARD：返回收款卡信息（仅部分渠道支持，请与客服核实）
     * Remark	                    是	string	商户自定义备注	remark [空字符串]	必传，可传空字符串
     * Sign	                    是	string	签名结果	5267df4726e0c93142cce4a15cb26eed	将上述所有字段按照签名规则计算出的签名值，包括值为空的参数
     */
    public ResultDeal channelDealPush(DealOrder order, ChannelInfo channelInfo) {
        String Amount = order.getDealAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        String ClientRealName = order.getMcRealName();
        String Ip = order.getMcRealName();
        String MerchantId = channelInfo.getChannelAppId();
        String MerchantUniqueOrderId = order.getOrderId();
        String NotifyUrl = order.getNotify();
        String PayTypeId = channelInfo.getChannelType();
        String payTypeIdFormat = PayTypeIdFormat.get(order.getOpenType());
        String Remark = order.getOrderId();
        Map map = new HashMap();
        map.put("Amount", Amount);
        map.put("ClientRealName", ClientRealName);
        map.put("Ip", Ip);
        map.put("MerchantId", MerchantId);
        map.put("MerchantUniqueOrderId", MerchantUniqueOrderId);
        map.put("NotifyUrl", NotifyUrl);
        map.put("PayTypeId", PayTypeId);
        map.put("PayTypeIdFormat", payTypeIdFormat);
        map.put("Remark", Remark);
        String param = createParam(map);
        String s = param + channelInfo.getChannelPassword();
        String s1 = md5(s).toLowerCase(Locale.ROOT);
        String Sign = s1;
        map.put("Sign", Sign);
        logRequestDeal(this.getClass().getName(), map, order.getOrderId(), order.getExternalOrderId());
        String post = "";
        try {
            post = HttpUtil.post(channelInfo.getDealurl() + "/InterfaceV9/CreatePayOrder/", map, 2000);
        } catch (Throwable e) {
            log.info("【渠道异常】", e.getMessage());
            return ResultDeal.error();
        }
//{"Code":"0","MessageForUser":"OK","MessageForSystem":"OK","MerchantUniqueOrderId":"1686906213481","Amount":"326.00","RealAmount":"326.00","Url":"","BankCardRealName":"张三","BankCardNumber":"9876543210123456789","BankCardBankName":"中国工商银行","BankCardBankBranchName":"","ExpiryTime":"2023-06-16 17:11:34"}
//{"Code":"11","MessageForUser":"金额【326.00】并发量过大，请使用带小数点的金额【日志编号：2459915bf5a0462c9a58ab8dd23c7f75】","MessageForSystem":"金额【326.00】并发量过大，请使用带小数点的金额【日志编号：2459915bf5a0462c9a58ab8dd23c7f75】","MerchantUniqueOrderId":"1686906240493","Amount":"0.00","RealAmount":"0.00","Url":"","BankCardRealName":"","BankCardNumber":"","BankCardBankName":"","BankCardBankBranchName":"","ExpiryTime":""}
//{"Code":"0","MessageForUser":"OK","MessageForSystem":"OK","MerchantUniqueOrderId":"1686906274903","Amount":"327.00","RealAmount":"327.00","Url":"http://www.hytbimy25.xyz/k/D218783230616170435142","BankCardRealName":"","BankCardNumber":"","BankCardBankName":"","BankCardBankBranchName":"","ExpiryTime":""}
//{"Code":"11","MessageForUser":"通道正忙请稍后重试【日志编号：b22b9aa7fe71489ba6a2ac5f0d69cd8c】","MessageForSystem":"当前请求IP【152.32.99.9】不在白名单内，请联系客服并提供您的IP列表【日志编号：b22b9aa7fe71489ba6a2ac5f0d69cd8c】","MerchantUniqueOrderId":"1686905371188","Amount":"0.00","RealAmount":"0.00","Url":"","BankCardRealName":"","BankCardNumber":"","BankCardBankName":"","BankCardBankBranchName":"","ExpiryTime":""}
        logResponseDeal(this.getClass().getName(), post, order.getOrderId(), order.getExternalOrderId());
        JSONObject jsonObject = JSONUtil.parseObj(post);
        String code = jsonObject.getStr("Code");
        if ("0".equals(code)) {
            if (order.getOpenType().equals(ResultDeal.URL)) {
                return ResultDeal.sendUrl(jsonObject.getStr("Url"));
            }
            String bankCardRealName = jsonObject.getStr("BankCardRealName");
            String amount = jsonObject.getStr("Amount");
            String BankCardNumber = jsonObject.getStr("BankCardNumber");
            String BankCardBankName = jsonObject.getStr("BankCardBankName");
            String BankCardBankBranchName = jsonObject.getStr("BankCardBankBranchName");
            PayInfo payInfo = new PayInfo();
            payInfo.setAmount(amount);
            payInfo.setBankBranch(BankCardBankBranchName);
            payInfo.setBankCard(BankCardNumber);
            payInfo.setName(bankCardRealName);
            payInfo.setBankName(BankCardBankName);
            payInfo.setOrderId(order.getOrderId());
            payInfo.setTime(order.getCreateTime());
            cache(payInfo);
            return ResultDeal.sendUrlAndPayInfo(url + "/pay?orderId=" + order.getOrderId() + "&type=203", JSONUtil.parse(payInfo).toString());
            //TODO 渠道成功处理
        } else {
            String messageForSystem = jsonObject.getStr("MessageForSystem");
            //TODO 渠道充值非成功情况处理
            orderEr(order, messageForSystem);
        }
        return ResultDeal.error();
    }


    /**
     * Amount	                    是	string	订单金额	2000.88	单位元，建议格式#.##
     * BankCardBankName	            是	string	银行中文名称	中国建设银行	中文名，我方智能匹配
     * BankCardNumber	            是	string	银行卡号	622212345678901234
     * BankCardRealName	            是	string	银行卡户名	张三	无需url编码
     * MerchantId	                是	string	商户ID	8888	见开户文档
     * MerchantUniqueOrderId	    是	string	商户唯一订单ID	uuid123456789	每次提交不可重复，建议使用guid、uuid或时间戳加随机数
     * NotifyUrl	                是	string	通知回调地址	http://yourdomain/a/b/c
     * Remark	                    是	string	自定义备注	Remark
     * WithdrawTypeId	            是	string	下发类型	0	0 通用模式
     * Sign	                        是	string	签名	5267df4726e0c93142cce4a15cb26eed	详见签名章节
     *
     * @param order
     * @param channelInfo
     * @return
     */
    Result channelWitPush(DealWit order, ChannelInfo channelInfo) {
        String Amount = order.getDealAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        String witInfo = order.getWitInfo();
        WitInfo witInfo1 = JSONUtil.toBean(JSONUtil.parseObj(witInfo), WitInfo.class);
        String BankCardBankName = witInfo1.getBankName();
        String BankCardNumber = witInfo1.getBankNo();
        String BankCardRealName = witInfo1.getAccount();
        String MerchantId = channelInfo.getChannelAppId();
        String MerchantUniqueOrderId = order.getOrderId();
        String NotifyUrl = order.getNotify();
        String Remark = MerchantUniqueOrderId;
        String WithdrawTypeId = "0";
        Map map = new HashMap();
        map.put("Amount", Amount);
        map.put("BankCardBankName", BankCardBankName);
        map.put("BankCardNumber", BankCardNumber);
        map.put("BankCardRealName", BankCardRealName);
        map.put("MerchantId", MerchantId);
        map.put("MerchantUniqueOrderId", MerchantUniqueOrderId);
        map.put("NotifyUrl", NotifyUrl);
        map.put("Remark", Remark);
        map.put("WithdrawTypeId", WithdrawTypeId);
        String param = createParam(map);
        String s = param + channelInfo.getChannelPassword();
        String s1 = md5(s).toLowerCase(Locale.ROOT);
        String Sign = s1;
        map.put("Sign", Sign);
        logRequestWit(this.getClass().getName(), map, order.getOrderId(), order.getExternalOrderId());
        String post = "";
        try {
            post = HttpUtil.post(channelInfo.getDealurl() + "/InterfaceV9/CreateWithdrawOrder/", map, 2000);
        } catch (Throwable e) {
            log.info("【渠道异常】", e.getMessage());
            return Result.buildFail();
        }
        logResponseWit(this.getClass().getName(), post, order.getOrderId(), order.getExternalOrderId());
//{"Code":"21","Message":"您当前余额0不足以支付本次申请（提现金额6000.00，手续费44）","WithdrawOrderStatus":"0"}
// {"Code":"0","Message":"提交成功，请等待处理完成后的回调或调用查询接口查询状态","WithdrawOrderStatus":"0"}
        JSONObject jsonObject = JSONUtil.parseObj(post);
        String code = jsonObject.getStr("Code");
        if ("0".equals(code)) {
            witOrdererSu(order.getOrderId(),"推送成功，等待三方出款");
            return Result.buildSuccessMessage("提交成功，请等待处理完成后的回调或调用查询接口查询状态");
        } else {
            witOrdererEr(order.getOrderId(), jsonObject.getStr("Message"));
            return Result.buildFailMessage("渠道错误");
        }
    }

    public static void main(String[] args) {
        String Amount = "600.00";
        String BankCardBankName = "中國建設銀行";
        String BankCardNumber = "65525555666617262";
        String BankCardRealName = "張屋";
        String MerchantId = "218774";
        String MerchantUniqueOrderId = System.currentTimeMillis() + "";
        String NotifyUrl = "www.iishaj.s";
        String Remark = MerchantUniqueOrderId;
        String WithdrawTypeId = "0";
        Map map = new HashMap();
        map.put("Amount", Amount);
        map.put("BankCardBankName", BankCardBankName);
        map.put("BankCardNumber", BankCardNumber);
        map.put("BankCardRealName", BankCardRealName);
        map.put("MerchantId", MerchantId);
        map.put("MerchantUniqueOrderId", MerchantUniqueOrderId);
        map.put("NotifyUrl", NotifyUrl);
        map.put("Remark", Remark);
        map.put("WithdrawTypeId", WithdrawTypeId);
        String param = createParam(map);
        String s = param + "ku9T367v1GKq5lc637EWETIDOXSm862Za7j1S31IpDT";
        String s1 = md5(s).toLowerCase(Locale.ROOT);
        String Sign = s1;
        map.put("Sign", Sign);
        String post = "";
        try {
            post = HttpUtil.post("https://apinxd2e6b.auuye.xyz/InterfaceV9/CreateWithdrawOrder/", map, 2000);
        } catch (Throwable e) {
            log.info("【渠道异常】", e.getMessage());
        }
        //{"Code":"21","Message":"您当前余额0不足以支付本次申请（提现金额6000.00，手续费44）","WithdrawOrderStatus":"0"}
        // {"Code":"0","Message":"提交成功，请等待处理完成后的回调或调用查询接口查询状态","WithdrawOrderStatus":"0"}
        System.out.println(post);
    }


    public void findBalance(String channelId, ChannelInfo channelInfo) {
        log.info("【调用查询渠道余额方法，当前渠道信息为："+channelInfo.toString()+"】");
        String MerchantId = channelInfo.getChannelAppId();
        Map map = new HashMap();
        map.put("MerchantId", MerchantId);
        String param = createParam(map);
        String s = param + channelInfo.getChannelPassword();
        String s1 = md5(s).toLowerCase(Locale.ROOT);
        map.put("Sign", s1);

        String post = HttpUtil.post(channelInfo.getDealurl() + "/InterfaceV9/GetBalanceAmount/", map, 2000);
        /**
         *  {
         *   "Code": "0",
         *   "Message": "OK",
         *   "BalanceAmount": "18888.88",
         *   "FreezingBalanceAmount": "0.00"
         *  }
         */
        JSONObject jsonObject = JSONUtil.parseObj(post);
        String code = jsonObject.getStr("Code");
        if ("0".equals(code)) {
            String balanceAmount = jsonObject.getStr("BalanceAmount");
            updateAmountChannel(new BigDecimal(balanceAmount),channelId);
        }
    }

    public Result witNotify(Map<String, Object> map) {

        /**
         * Amount	                string	2000.88	提现本金，原样返回，单位元	是
         * CommissionAmount	        string	1.88	订单手续费，单位元	是
         * FinishTime	            string	20190102030405	订单完成时间，格式yyyyMMddHHmmss，北京时间	是
         * MerchantId	            string	8888	您的商户ID，原样返回	是
         * MerchantUniqueOrderId	string	uuid123456789	商户唯一订单ID，原样返回	是
         * Remark	                string	Remark
         * WithdrawOrderId	        string	W123456789	平台方系统代付订单号	是
         * WithdrawOrderStatus	    string	100 -90	【代付状态】  100：代付成功 -90：代付失败	是
         * Sign	string	5267df4726e0c93142cce4a15cb26eed	平台方计算出的签名	否
         */
        String Sign = map.get("Sign").toString();
        map.remove("Sign");
        String merchantUniqueOrderId = map.get("MerchantUniqueOrderId").toString();
        String channelKey = getWitChannelKey(merchantUniqueOrderId);
        String param = createParam(map);
        String s = param + channelKey;
        String s1 = md5(s).toLowerCase(Locale.ROOT);
        if (Sign.equals(s1)) {//签名成功
            String withdrawOrderStatus = orderStatusWit.get(map.get("WithdrawOrderStatus").toString());
            Result result = witNotfy(merchantUniqueOrderId, withdrawOrderStatus);
            return result;
        }
        return Result.buildFail();
    }

    public Result dealNotify(Map map) {

        /**
         * Amount	                string	订单原始金额，单位元，原样返回
         * CommissionAmount	        string	手续费，单位元
         * FinishTime	            string	订单完成时间，格式yyyyMMddHHmmss
         * MerchantId	            string	您的商户ID，原样返回
         * MerchantUniqueOrderId	string	商户唯一订单ID，原样返回
         * PayOrderId	            string	我方平台系统订单号
         * PayOrderStatus	        string	0 待支付 100 支付成功 -90 支付失败
         * RealAmount	            string	订单实际（应）支付金额,单位元
         * Remark	                string	商户自定义备注，原样返回
         * Sign	                    string	平台方计算出的签名
         */
        String Sign = map.get("Sign").toString();
        map.remove("Sign");
        String merchantUniqueOrderId = map.get("MerchantUniqueOrderId").toString();
        String channelKey = getChannelKey(merchantUniqueOrderId);
        String param = createParam(map);
        String s = param + channelKey;
        String s1 = md5(s).toLowerCase(Locale.ROOT);
        if (s1.equals(Sign)) {
            String payOrderStatus = orderStatusDeal.get(map.get("PayOrderStatus").toString());
            if (payOrderStatus.equals(OrderDealStatus.成功.getIndex().toString())) {
                Result result = dealNotfiy(merchantUniqueOrderId, "三方系统回调成功");
                return result;
            }
        }
        return Result.buildFail();
    }


}
