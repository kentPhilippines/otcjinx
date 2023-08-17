package alipay.manage.api.channel.deal.xinda;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component("XinDaWit")
public class XinDaWit extends PayOrderService {

    private static final String WIT_RESULT = "SUCCESS";
    @Autowired
    private UserInfoService userInfoServiceImpl;


    @Override
    public Result withdraw(Withdraw wit) {
        log.info("【进入信达代付】");
        try {
            log.info("【本地订单创建成功，开始请求远程三方代付接口】");
            String channel = "";
            if (StrUtil.isNotBlank(wit.getChennelId())) {//支持运营手动推送出款
                channel = wit.getChennelId();
            } else {
                channel = wit.getWitChannel();
            }

            UserInfo userInfo = userInfoServiceImpl.findDealUrl(wit.getUserId());
            if (StrUtil.isBlank(userInfo.getDealUrl())) {
                withdrawEr(wit, "url未设置", wit.getRetain2());
                return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
            }
            String createDpay = createDpay(userInfo.getDealUrl() +
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + "/xd-wit-notify",
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
        /**
         * version	        版本	                String(3)	    必填	版本号，值为1.0，未来更新后可新旧并存
         * cid	            商户编号	            String(10)	必填	由信达支付分配商户编号
         * tradeNo	        商户代付单号	        String(32)	必填	商户自定义，需在商户下保证唯一性，可包含字母
         * amount	        代付金额	            String(10)	必填	整数，单位为分
         * payType	        代付类型	            String(1)	必填	1：普通馀额 2:USDT
         * acctName	        收款人姓名	        String(16)	patType=1 必填	收款人姓名
         * acctNo	        收款人帐户	        String(20)	必填	收款帐户/USDT收款地址
         * bankCode	        银行代号	            String(4)	patType=1 必填	银行代号
         * Protocal	        协议	                String(5)	patType=2 必填	协议 ERC20/TRC20
         * notifyUrl	    通知地址	            String(128)	必填	交易结果通知地址(可公开访问)
         * sign	签名		    必填
         */
        String version = "1.0";
        String cid = channelInfo.getChannelAppId();
        String tradeNo = wit.getOrderId();
        String amount = wit.getAmount().multiply(new BigDecimal(100)).intValue() + "";
        String payType = "1";
        String acctName = wit.getAccname();
        String acctNo = wit.getBankNo();
        String bankCode = mapBankCode.get(wit.getBankcode()) + "";
        if (StrUtil.isEmpty(bankCode)) {
            bankCode = "001";
        }
        String notifyUrl = notify;
        Map map = new HashMap();
        map.put("version", version);
        map.put("cid", cid);
        map.put("tradeNo", tradeNo);
        map.put("amount", amount);
        map.put("payType", payType);
        map.put("acctName", acctName);
        map.put("acctNo", acctNo);
        map.put("bankCode", bankCode);
        map.put("notifyUrl", notifyUrl);
        String param = createParam(map);
        String s = param + "&key=" + channelInfo.getChannelPassword();
        String Sign = md5(s);
        map.put("sign", Sign);
        log.info("【请求参数：" + JSONUtil.parseObj(map).toString() + "】");
        log.info("【请求渠道实体：" + JSONUtil.parseObj(channelInfo).toString() + "】");
        String post = HttpUtil.post(channelInfo.getWitUrl(), JSONUtil.parseObj(map).toString());
        log.info("【响应参数：" + post + "】");
//{"retcode":"100","retmsg":"bankCode 参数有误"}
//{"retcode":"0","status":"2","rockTradeNo":"GT0016_468","tradeNo":"2132131232213213","amount":"202000"}
        JSONObject jsonObject1 = JSONUtil.parseObj(post);
        String retcode = jsonObject1.getStr("retcode");
        if ("0".equals(retcode)) {
            witComment(wit.getOrderId());
            return WIT_RESULT;
        } else {
            withdrawErMsg(wit, jsonObject1.getStr("retmsg"), wit.getRetain2());
        }
        return "";
    }


    /**
     * ,,,
     * ,,,
     * ,,,
     * ,,,
     * ,,,
     *
     * @param map
     * @return
     */
    static Map mapBankCode = new ConcurrentHashMap();

    static {
        mapBankCode.put("GDRCC", "111");
        mapBankCode.put("GDB", "144");
        mapBankCode.put("CITIC", "008");
        mapBankCode.put("ABC", "004");
        mapBankCode.put("ICBC", "001");
        mapBankCode.put("FJNX", "033");
        mapBankCode.put("CIB", "011");
        mapBankCode.put("BJBANK", "015");
        mapBankCode.put("PSBC", "007");
        mapBankCode.put("COMM", "005");
        mapBankCode.put("CEB", "012");
        mapBankCode.put("BJRCB", "022");
        mapBankCode.put("SPABANK", "013");
        mapBankCode.put("CMBC", "009");
        mapBankCode.put("CCB", "002");
        mapBankCode.put("HXBANK", "014");
        mapBankCode.put("SPDB", "010");
        mapBankCode.put("CMB", "006");
        mapBankCode.put("BOC", "003");
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

    public static String md5(String a) {
        String c = "";
        MessageDigest md5;
        String result = "";
        try {
            md5 = MessageDigest.getInstance("md5");
            md5.update(a.getBytes("utf-8"));
            byte[] temp;
            temp = md5.digest(c.getBytes("utf-8"));
            for (int i = 0; i < temp.length; i++) {
                result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
        return result;
    }


    public static void main(String[] args) {
        Withdraw wit = new Withdraw();
        wit.setAmount(new BigDecimal(2020));
        wit.setAccname("李志平");
        wit.setBankNo("6230580000175121453");
        wit.setBankcode("ICBC");
        wit.setOrderId("2132131232213213");

        ChannelInfo info = new ChannelInfo();
        info.setChannelPassword("413b08ff2125aa2a004ed62bc0e532246bd4d78b");
        info.setWitUrl("https://www.rolexpays.com/louis/ap.do");
        info.setChannelAppId("GT0016");
        //  info.setChannelType("SVC0004");


    //    createDpay("http://asdsd.dads.com", wit, info);


    }
}
