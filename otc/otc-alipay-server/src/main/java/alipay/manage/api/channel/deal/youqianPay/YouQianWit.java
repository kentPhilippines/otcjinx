package alipay.manage.api.channel.deal.youqianPay;

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
@Component("YouQianWit")
public class YouQianWit  extends PayOrderService {
    private static final String WIT_RESULT = "SUCCESS";
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Override
    public Result withdraw(Withdraw wit) {
        log.info("【进入有钱代付】");
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
                withdrawEr(wit,"url未设置",wit.getRetain2());
                return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
            }
            String createDpay = createDpay(userInfo.getDealUrl()+
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + "/yq-wit-notify",
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

    private   String createDpay(String notify, Withdraw wit, ChannelInfo channelInfo) {

        /**
         * mchid	        商户号	是	是	您的商户号，3-4位的纯数字
         * out_trade_no	    商户订单号	是	是	您的订单号，不允许重复
         * money	        金额	是	是	单位：元，暂不支持小数
         * bankname	        开户行名称	是	是	银行名称
         * subbranch	    支行名称	是	是	开户行地址，没有可填写银行名称
         * accountname	    开户名	是	是	卡主姓名
         * cardnumber	    银行卡号	是	是	银行卡号
         * notifyurl	    回调地址	是	是	服务端通知地址，POST返回数据 代付通知参数与支付通知参数通用
         * sign	            签名	否	否	请查阅文档末尾的签名算法
         */
        String mchid = channelInfo.getChannelAppId();
        String out_trade_no = wit.getOrderId();
        String money = wit.getAmount().intValue() + "";
        String notifyurl = notify;
        String bankname = wit.getBankName();
        String subbranch = wit.getBankName();
        String accountname = wit.getAccname();
        String cardnumber = wit.getBankNo();
        Map map = new HashMap();
        map.put("mchid",mchid);
        map.put("out_trade_no",out_trade_no);
        map.put("money",money);
        map.put("notifyurl",notifyurl);
        map.put("bankname",bankname);
        map.put("subbranch",subbranch);
        map.put("cardnumber",cardnumber);
        map.put("accountname",accountname);
        String param = createParam(map);
        log.info("签名参数为：" + param);
        String s = md5(param + "&key=" + channelInfo.getChannelPassword()).toUpperCase();
        map.put("sign", s);
        JSONObject jsonObject = JSONUtil.parseObj(map);
        log.info(" 有钱 请求参数为:" + jsonObject.toString());
        String post = HttpUtil.post(channelInfo.getWitUrl() + "/yqapi.html", map);
        log.info(" 有钱 响应参数为：" + post);
        //响应参数为：{"error_code":"0012","error_msg":"不支援的银行!"}
        //响应参数为：{"error_code":"0018","error_msg":"商户余额不足!"}
        // 响应参数为：{"error_code":"0000","data":{"payout_id":"WUTONGPOT00362728"}}
        JSONObject jsonObject1 = JSONUtil.parseObj(post);
        String error_code = jsonObject1.getStr("status");
        if("success".equals(error_code.trim())){
            witComment(wit.getOrderId());
            return WIT_RESULT;
        } else {
            withdrawErMsg(wit, jsonObject1.getStr("msg"),wit.getRetain2());
        }
        return "";
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
        String notify  = "https://payment.yqapi.vip";
        Withdraw wit = new Withdraw();
        wit.setAmount(new BigDecimal(200));
        wit.setAccname("李志平");
        wit.setBankNo("6230580000175121453");
        wit.setOrderId("2132131232213213");
        wit.setBankName("中国银行");
        ChannelInfo  info = new ChannelInfo();
        info.setChannelPassword("px169uyrf75lg68rescyc3qiyk5zthb9");
        info.setWitUrl("https://payment.yqapi.vip");
        info.setChannelAppId("614");
        YouQianWit  witPay = new YouQianWit();
        witPay.createDpay(notify,wit,info);
        //  createDpay("http://asdsd.dads.com",wit,info);
    }

}
