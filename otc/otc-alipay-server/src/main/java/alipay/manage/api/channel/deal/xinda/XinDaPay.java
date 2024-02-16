package alipay.manage.api.channel.deal.xinda;

import alipay.config.redis.RedisUtil;
import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("XinDaPay")
public class XinDaPay extends PayOrderService {
    private static final String MARS = "SHENFU";
    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Autowired
    private RedisUtil redis;
    @Autowired
    private OrderService orderServiceImpl;
    @Value("${otc.payInfo.url}")
    public String url;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channeId) {
        log.info("【进入信达支付】");
        String create = create(dealOrderApp, channeId);
        if (StrUtil.isNotBlank(create)) {
            log.info("【本地订单创建成功，开始请求远程三方支付】");
            String payInfo = "";
            if (dealOrderApp.getDealDescribe().contains("付款人")) {
                payInfo = dealOrderApp.getDealDescribe();
            }
            UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
            if (StrUtil.isBlank(userInfo.getDealUrl())) {
                orderEr(dealOrderApp, "当前商户交易url未设置");
                return Result.buildFailMessage("请联系运营为您的商户号设置交易url");
            }
            log.info("【回调地址ip为：" + userInfo.getDealUrl() + "】");
            Result url = createOrder(userInfo.getDealUrl() + PayApiConstant.Notfiy.NOTFIY_API_WAI +
                            "/xd-pay-notify",
                    dealOrderApp.getOrderAmount(),
                    create,
                    getChannelInfo(channeId, dealOrderApp.getRetain1()), payInfo
            );
            String payInfo1 = "";
            if (url.isSuccess()) {
               /* try {
                    Map map = new HashMap();
                    Map<Object, Object> hmget = redis.hmget(MARS + create);
                    log.info(hmget.toString());
                    if (ObjectUtil.isNotNull(hmget)) {
                        Object bank_name = hmget.get("bank_name");
                        Object card_no = hmget.get("card_no");
                        Object card_user = hmget.get("card_user");
                        Object money_order = hmget.get("money_order");
                        Object address = hmget.get("address");
                        map.put("amount", money_order);
                        map.put("bankCard", card_no);
                        map.put("bankName", bank_name);
                        map.put("name", card_user);
                        map.put("bankBranch", address);
                        JSONObject jsonObject = JSONUtil.parseFromMap(map);
                        payInfo1 = jsonObject.toString();
                    }
                    return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrlAndPayInfo1(url.getResult(), url.getMessage(), payInfo1));

                } catch (Throwable e) {*/
                    return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(url.getResult()));
           //     }
            }
        }
        return Result.buildFailMessage("支付错误");
    }

    /***
     * 参数	参数名称	类型(长度)	可否为空	备註
     * version	版本	String(3)	必填	版本号，值为：1.6
     * cid	商户编号	String(16)	必填	由信达支付分配商户编号
     * tradeNo	商户订单号	String(32)	必填	商户自定义，需在商户下保证唯一性，可包含字母
     * amount	订单金额	String(10)	必填	整数，单位为分
     * 单笔限额为 **100-50000 **元
     * payType	付款方式	String(2)	必填	交易银行卡，值为：17
     * 交易USDT，值为：18
     * 交易数字人民币，值为：20
     * acctName	转出人姓名	String(32)	非必填	用来付款的银行卡持卡人姓名
     * accId	转出帐号	String(19)	非必填	用来付款的银行卡号
     * bankCode	银行代号	String(4)	非必填	银行代号
     * remark	附言验证码	String(5)	非必填	即附言，商户可指定付款者填写特定附言
     * requestTime	时间字符串	String(14)	必填	發送请求时间，格式为
     * yyyyMMddHHmmss
     * notifyUrl	通知地址	String(128)	必填	交易结果通知地址
     * returnType	页面返回参数	String(1)	必填	0：JSON格式返回
     * confirmType	匹配模式	String(1)	非必填	0：实名匹配
     * Sign	签名		必填
     */
    private static SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");

    private Result createOrder(String notify, BigDecimal orderAmount, String orderId, ChannelInfo channelInfo, String payInfo) {
        String version = "1.6";
        String cid = channelInfo.getChannelAppId();
        String tradeNo = orderId;
        String amount = orderAmount.multiply(new BigDecimal(100)).intValue() + "";
        String payType = channelInfo.getChannelType();
        String requestTime = d.format(new Date());
        //    String acctName = d.format(new Date());
        String notifyUrl = notify;
        Map map = new HashMap();
        map.put("version", version);
        map.put("cid", cid);
        map.put("tradeNo", tradeNo);
        map.put("amount", amount);
        map.put("acctName", getPayName1(payInfo, orderId));
        map.put("payType", payType);
        map.put("requestTime", requestTime);
        map.put("notifyUrl", notifyUrl);
        map.put("returnType", "0");
        String param = createParam(map);
        String s = param + "&key=" + channelInfo.getChannelPassword();
        String Sign = md5(s);
        map.put("Sign", Sign);
        log.info("【请求参数：" + JSONUtil.parseObj(map).toString() + "】");
        log.info("【请求渠道实体：" + JSONUtil.parseObj(channelInfo).toString() + "】");
        String post = HttpUtil.post(channelInfo.getDealurl(), JSONUtil.parseObj(map).toString());
        log.info("【响应参数：" + post + " tradeNo "+ tradeNo+"】");
        String retcode = JSONUtil.parseObj(post).getStr("retcode");
        if ("0".equals(retcode)) {
            String status = JSONUtil.parseObj(post).getStr("status");
            if ("2".equals(status)) {
                try {
                    return Result.buildSuccessResult(JSONUtil.parseObj(post).getStr("link"));
                   /* Map cardmap = new HashMap();
                    cardmap.put("bank_name", JSONUtil.parseObj(post).getStr("payeeBankName"));
                    cardmap.put("card_no", JSONUtil.parseObj(post).getStr("payeeAcctNo"));
                    cardmap.put("card_user", JSONUtil.parseObj(post).getStr("payeeName"));
                    cardmap.put("money_order", orderAmount);
                    cardmap.put("no_order", orderId);
                    cardmap.put("oid_partner", orderId);
                    cardmap.put("address", JSONUtil.parseObj(post).getStr("branchName"));
                    orderServiceImpl.updateBankInfoByOrderId(payInfo + " 收款信息：" + JSONUtil.parseObj(post).getStr("payeeName") + ":" + JSONUtil.parseObj(post).getStr("payeeBankName") + ":" + JSONUtil.parseObj(post).getStr("payeeAcctNo"), orderId);
                    redis.hmset(MARS + orderId, cardmap, 600);
                    return Result.buildSuccessResult(JSONUtil.parseObj(post).getStr("payeeName") + ":" + JSONUtil.parseObj(post).getStr("payeeBankName") + ":" + JSONUtil.parseObj(post).getStr("payeeAcctNo"), url + "/pay?orderId=" + orderId + "&type=203");
    */            } catch (Throwable e) {
                    log.error("请求失败异常：",e);
                    return Result.buildSuccessResult(JSONUtil.parseObj(post).getStr("link"));
                }
                /**
                 * {
                 * "retcode":"0",
                 * "status":"2",
                 * "tradeNo":"JS20230807215513826346440",
                 * "amount":"705500","postScript":"",
                 * "payeeName":"霍大千",
                 * "payeeBankName": "天津农村商业银行",
                 * "branchName":"天津支行",
                 * "payeeAcctNo":"6231039919018333964",
                 * "link":"https://biz.fwuc0i9.net/checkoutCounter?m=77624376&o=wanc_6280&s=744e5690312ce96c0b71c6d624824f59"}
                 * {"code":"0000",
                 * "data":{
                 * "systemOrderId":"LHPL00007903",
                 * "amount":4001,
                 * "displayAmount":4001,
                 * "upstreamLink":"",
                 * "cardName":"韦耀庆",
                 * "cardAccount":"6217852000014693736",
                 * "cardBank":"中国银行",
                 * "cardBranch":""}}
                 */
                //      return Result.buildSuccessResult(JSONUtil.parseObj(post).getStr("link"));
            }else {
                String msg = "渠道失败";
                orderDealEr(orderId,retcode);
            }
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

        ChannelInfo info = new ChannelInfo();
        info.setChannelAppId("GT0016");
        info.setDealurl("https://www.rolexpays.com/louis/gateway.do");
        //  info.setChannelPassword("413b08ff2125aa2a004ed62bc0e532246bd4d78b");
        info.setChannelPassword("11111111111111");
        info.setChannelType("17");

        //     createOrder( "www.bbss.cc", new BigDecimal("4001"), System.currentTimeMillis()+"",  info, "付款人：尚食2局");
    }

    public static String getPayName1(String payInfo, String orderId) {
        if (StrUtil.isNotEmpty(payInfo)) {
            String[] split = payInfo.split(name);
            String payName = split[1];
            return payName;
        }
        return "";

    }


}
