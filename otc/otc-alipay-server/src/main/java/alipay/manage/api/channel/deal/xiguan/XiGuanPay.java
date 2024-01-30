package alipay.manage.api.channel.deal.xiguan;

import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Component("XiGuanPay")
public class XiGuanPay extends PayOrderService {
    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入习西瓜支付】");
        String channelId = channel;//配置的渠道账号
        String create = create(dealOrderApp, channelId);
        if (StrUtil.isNotBlank(create)) {
            log.info("【本地订单创建成功，开始请求远程三方支付】");
            UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
            if (StrUtil.isBlank(userInfo.getDealUrl())) {
                orderEr(dealOrderApp, "当前商户交易url未设置");
                return Result.buildFailMessage("请联系运营为您的商户号设置交易url");
            }
            log.info("【回调地址ip为：" + userInfo.getDealUrl() + "】");
            Result url = createOrder(userInfo.getDealUrl() + PayApiConstant.Notfiy.NOTFIY_API_WAI +
                            "/xiguan-notfiy",
                    dealOrderApp.getOrderAmount().intValue(),
                    create,
                    getChannelInfo(channelId, dealOrderApp.getRetain1())
            );
            if (url.isSuccess()) {
                return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(url.getResult()));
            }
        }
        return Result.buildFailMessage("支付错误");
    }
    /**
     * merchantOrderNo	是	string	商戶订单号
     * deptId	是	Long	商户ID
     * amount	是	BigDecimal	金额
     * callbackUrl	是	string	回调URL
     * currency	是	string	默认固定值 ”RMB“
     * payType	是	Integer	支付类型（查看后台通道费率模块）
     * ip	否	string	提高成功率
     * sign	是	string	签名后的值
     * @param notify
     * @param orderAmount
     * @param orderId
     * @param channelInfo
     * @return
     */
    private   Result createOrder(String notify, Integer orderAmount, String orderId, ChannelInfo channelInfo) {
        String merchantOrderNo = orderId;
        String deptId = channelInfo.getChannelAppId();
        Integer amount = orderAmount.intValue();
        String callbackUrl = notify;
        String currency = "RMB";
        String payType = channelInfo.getChannelType();
        String sign = "";
        Map<String, Object> body = new HashMap();
        body.put("merchantOrderNo", merchantOrderNo);
        body.put("deptId", deptId);
        body.put("amount", amount);
        body.put("callbackUrl", callbackUrl);
        body.put("currency", currency);
        body.put("payType", payType);
        String param1 = createParam(body);
        log.info("加密前串为：" + param1);
        String sign1 = md5(param1 + "&key=" + channelInfo.getChannelPassword());
        body.put("sign", sign1);
        JSONObject param = JSONUtil.parseFromMap(body);
        log.info("西瓜请求参数为：" + param);
        HttpResponse execute = HttpRequest.post(channelInfo.getDealurl())
                .body(param).execute();
        String ruselt = execute.body().toString();
        log.info("西瓜响应参数为：" + ruselt);
        JSONObject jsonObject = JSONUtil.parseObj(ruselt);
        String code = jsonObject.getStr("code");
        if (code.equals("200")) {
            String url = jsonObject.getStr("data");
            return Result.buildSuccessResult(url);
        }else {
            orderEr(orderId, "请求支付失败");
        }
        return Result.buildFailMessage("");
    }

    public static String md5(String a) {
        log.info("签名参数为：" + a);
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
    public static String createParam(Map<String, Object> map) {
        try {
            if (map == null || map.isEmpty())
                return null;
            Object[] key = map.keySet().toArray();
            Arrays.sort(key);
            StringBuffer res = new StringBuffer(128);
            for (int i = 0; i < key.length; i++)
                if (ObjectUtil.isNotNull(map.get(key[i])))
                    res.append(key[i] + "=" + map.get(key[i]) + "&");
            String rStr = res.substring(0, res.length() - 1);
            return rStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        ChannelInfo info = new ChannelInfo();
        info.setChannelAppId("219");
        info.setChannelPassword("f6a465b08b793c2f6094b6a528dd292c");
        info.setChannelType("109");
        info.setDealurl("http://yuechuanggo.com/wms-api/thirdPay/doPay");
    //    createOrder("http://orsss.sss.com", new BigDecimal("200.1").intValue(), "sas111dad1222211121212121", info);

        // 西瓜响应参数为：{"msg":"取码成功","code":200,"data":"http://43.134.210.64/api/pay/cardkeyPay?payOrderId=CA1676433196104945664&extd=MTgwMDQ4MjAyMjJ8YWExMjMxMjN8NTU3MjYwODZ8ZGZjOTNjMzkxN2NjZWM1ZmFiMDFkNzhmMmZlYjkyNWI="}
    }
}
