package alipay.manage.api.channel.deal.fourppay;

import alipay.manage.api.channel.deal.anxin.util.PayDigestUtil;
import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.channel.util.yifu.YiFuUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component("FourPWitPay")
public class FourPWitPay extends PayOrderService {
    private static final String WIT_RESULT = "SUCCESS";
    @Autowired
    private UserInfoService userInfoServiceImpl;

    private static Map<String,String> bankMap = new HashMap() {
        {
            put("CCB", "CN0001");
            put("ABC", "CN0002");
            put("ICBC", "CN0003");
            put("BOC", "CN0004");
            put("CMBC", "CN0005");
            put("CMB", "CN0006");
            put("CIB", "CN0007");
            put("BJBANK", "CN0008");
            put("COMM", "CN0009");
            put("CEB", "CN0010");
            put("SPABANK", "CN0011");
            put("PSBC", "CN0012");
            put("CITIC", "CN0013");
            put("HXBANK", "CN0014");
            put("GCB", "CN0015");
            put("SPDB", "CN0016");
            put("GDB", "CN0017");
            put("SRCB", "CN0018");
            put("SCCB", "CN0019");
            put("BGB", "CN0020");
            put("YQCCB", "CN0021");
            put("SHBANK", "CN0022");
            put("JLBANK", "CN0023");
            put("SHRCB", "CN0024");
            put("WHCCB", "CN0025");
            put("BANKWF", "CN0026");
            put("BOZK", "CN0027");
            put("CSRCB", "CN0028");
            put("KORLABANK", "CN0029");
            put("SDEB", "CN0030");
            put("HURCB", "CN0031");
            put("WRCB", "CN0032");
            put("BOCY", "CN0033");
            put("CZBANK", "CN0034");
            put("HDBANK", "CN0035");
            put("TACCB", "CN0036");
            put("BOD", "CN0037");
            put("LYCB", "CN0038");
            put("GDRCC", "CN0039");
            put("LZYH", "CN0040");
            put("SXCB", "CN0041");
            put("BOHAIB", "CN0042");
            put("CZCB", "CN0043");
            put("GZRCU", "CN0044");
            put("ZJKCCB", "CN0045");
            put("BOJZ", "CN0046");
            put("JLRCU", "CN0047");
            put("BOP", "CN0048");
            put("SRBANK", "CN0049");
            put("SDRCU", "CN0050");
            put("SJBANK", "CN0051");
            put("HKB", "CN0052");
            put("GXRCU", "CN0053");
            put("NXRCU", "CN0054");
            put("BSB", "CN0055");
            put("JSBANK", "CN0056");
            put("NYNB", "CN0057");
            put("GRCB", "CN0058");
            put("BOSZ", "CN0059");
            put("HZCB", "CN0060");
            put("DZBANK", "CN0061");
            put("ORBANK", "CN0062");
            put("HBC", "CN0063");
            put("JXBANK", "CN0064");
            put("ZYCBANK", "CN0065");
            put("BODD", "CN0066");
            put("HNRCC", "CN0067");
            put("AYCB", "CN0068");
            put("DYCCB", "CN0069");
            put("CZRCB", "CN0070");
            put("EGBANK", "CN0071");
            put("CDB", "CN0072");
            put("HSBK", "CN0073");
            put("ZGCCB", "CN0074");
            put("CDCB", "CN0075");
            put("JNBANK", "CN0076");
            put("TCRCB", "CN0077");
            put("NJCB", "CN0078");
            put("ZZBANK", "CN0079");
            put("LYBANK", "CN0080");
            put("DYCB", "CN0081");
            put("ZBCB", "CN0082");
            put("FSCB", "CN0083");
            put("SCRCU", "CN0084");
            put("HBRCU", "CN0085");
            put("LSCCB", "CN0086");
            put("LSBANK", "CN0087");
            put("CBKF", "CN0088");
            put("YDRCB", "CN0089");
            put("HNRCU", "CN0090");
            put("YNRCC", "CN0091");
            put("H3CB", "CN0092");
            put("YXCCB", "CN0093");
            put("FDB", "CN0094");
            put("JSRCU", "CN0095");
            put("XYBANK", "CN0096");
            put("HANABANK", "CN0097");
            put("SZSBK", "CN0098");
            put("JINCHB", "CN0099");
            put("FXCB", "CN0100");
            put("WHRCB", "CN0101");
            put("HBYCBANK", "CN0102");
            put("TZCB", "CN0103");
            put("JXRCU", "CN0104");
            put("ZRCBANK", "CN0105");
            put("JSB", "CN0106");
            put("JZBANK", "CN0107");
            put("FJHXBC", "CN0108");
            put("XCYH", "CN0109");
            put("NXBANK", "CN0110");
            put("NHB", "CN0111");
            put("XXBANK", "CN0112");
            put("HSBANK", "CN0113");
            put("JJBANK", "CN0114");
            put("NHQS", "CN0115");
            put("JRCB", "CN0116");
            put("HZCCB", "CN0117");
            put("MTBANK", "CN0118");
            put("LANGFB", "CN0119");
            put("ASCB", "CN0120");
            put("SXRCCU", "CN0121");
            put("CCQTGB", "CN0122");
            put("DLB", "CN0123");
            put("DRCBCL", "CN0124");
            put("NBBANK", "CN0125");
            put("XABANK", "CN0126");
            put("KLB", "CN0127");
            put("CRCBANK", "CN0128");
            put("BOYK", "CN0129");
            put("KSRB", "CN0130");
            put("HRXJB", "CN0131");
            put("GLBANK", "CN0132");
            put("ARCU", "CN0133");
            put("BOQH", "CN0134");
            put("CDRCB", "CN0135");
            put("QDCCB", "CN0136");
            put("HKBEA", "CN0137");
            put("GSRCU", "CN0138");
            put("ZJNX", "CN0139");
            put("HBHSBANK", "CN0140");
            put("WZCB", "CN0141");
            put("TRCB", "CN0142");
            put("URMQCCB", "CN0143");
            put("XLBANK", "CN0144");
            put("CSCB", "CN0145");
            put("WJRCB", "CN0146");
            put("QLBANK", "CN0147");
            put("YBCCB", "CN0148");
            put("ZJTLCB", "CN0149");
            put("JHBANK", "CN0150");
            put("BHB", "CN0151");
            put("GZB", "CN0152");
            put("BZMD", "CN0153");
            put("NBYZ", "CN0154");
            put("LSBC", "CN0155");
            put("GYCB", "CN0156");
            put("CQBANK", "CN0157");
            put("BOCD", "CN0158");
            put("BJRCB", "CN0159");
            put("NCB", "CN0160");
            put("DAQINGB", "CN0161");
            put("TCCB", "CN0162");
            put("CGNB", "CN0163");
            put("CBBQS", "CN0164");
            put("XTB", "CN0165");
            put("XMBANK", "CN0166");
            put("FJNX", "CN0167");
            put("XIB", "CN0168");
            put("HSBC", "CN0169");
            put("CCAB", "CN0170");
            put("YZB", "CN0171");
            put("CRB", "CN0172");
            put("LZCCB", "CN0173");
            put("MYB", "CN0174");
            put("HRBB", "CN0175");
            put("JXB", "CN0176");
            put("ZYB", "CN0177");
            put("CCHRCB", "CN0178");
            put("LJYY", "CN0179");
            put("RTCB", "CN0180");
            put("CCRFCB", "CN0181");
            put("TMDYCZYH", "CN0182");
            put("YJHRVB", "CN0183");
            put("BEEB", "CN0184");
            put("HNRCB", "CN0185");
            put("HNB", "CN0186");
            put("YACCB", "CN0187");
            put("QZCCB", "CN0188");
            put("SRCU", "CN0189");
            put("HLJRCC", "CN0190");
            put("NMGNXS", "CN0191");
            put("FUBONCHINA", "CN0192");
            put("BGZ", "CN0193");
            put("DYLS", "CN0194");
            put("ZJRC", "CN0195");
            put("CORBANK", "CN0196");
            put("URCB", "CN0197");
            put("GHBANK", "CN0198");
            put("CSXBANK", "CN0199");
            put("ZXRC", "CN0200");
            put("SLBEEP", "CN0201");
            put("RIZHAO", "CN0202");
            put("RHMY", "CN0203");
            put("TJBHB", "CN0204");
            put("WEBANK", "CN0205");
            put("INRCC", "CN0206");
            put("YCDVB", "CN0207");
            put("SCBANK", "CN0208");
            put("XJRCCB", "CN0209");
            put("TSBANK", "CN0210");
            put("SNCCB", "CN0211");
            put("TFBANK", "CN0212");
            put("CZCCB", "CN0213");
            put("BDBANK", "CN0214");
            put("HNNXS", "CN0215");
            put("GZYZ", "CN0216");
            put("ZYCB", "CN0217");
        }
    };

    @Override
    public Result withdraw(Withdraw wit) {
        log.info("【进入FourPWitPay手动代付代付】");
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
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + "/fourPWitPayNotify",
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
            String amount = pay_amount;
            String channel = channelInfo.getChannelType();
            String merchantId = channelInfo.getChannelAppId();
            String url = channelInfo.getDealurl();

            Map<String,Object> map = new HashMap<String,Object>();
            map.put("merchantOrderId",pay_order_id);
            map.put("merchantCode", merchantId);
            map.put("channelTypeId", new Integer(channel));
            map.put("amount",  amount);
            map.put("notifyUrl", notify);
            map.put("payerName", pay_account_name);
//            map.put("accountName", wit.getBankName());
            map.put("payeeBankId", bankMap.get(wit.getBankcode()));
            map.put("payeeAccount", wit.getBankNo());
            String createParam = YiFuUtil.createParam(map)+ ""+channelInfo.getChannelPassword();
            System.out.println("【fourp 签名前参数："+createParam+"】");
            String md5 = YiFuUtil.md5(createParam );
            String sign = md5.toLowerCase();
            map.put("sign", sign);

            String reqUrl = url;
            log.info("reqUrl：" + reqUrl);
            log.info("param：" + JSONUtil.toJsonStr(map));
            String s = HttpUtil.post(url, JSONUtil.toJsonStr(map));
            log.info(s);
            JSONObject jsonObject = JSONUtil.parseObj(s);
            String code = jsonObject.getStr("code");
            if ("0000".equals(code)) {
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

    public static void main(String[] args) throws Exception {
        Withdraw dealOrderApp;
        String notifyUrl = "http://34.92.251.112:9010/notfiy-api-pay/fourPPayNotify";
        BigDecimal orderAmount;
        String orderId;
        ChannelInfo channelInfo;
        dealOrderApp = new Withdraw();
        dealOrderApp.setOrderId("test"+ RandomUtil.randomNumbers(8));
        dealOrderApp.setBankcode("CN0001");
        dealOrderApp.setBankNo(RandomUtil.randomNumbers(16));
        dealOrderApp.setAccname("王小二");
//        dealOrderApp.setBankName("工商银行");
        dealOrderApp.setAmount(new BigDecimal(100));
        orderAmount = new BigDecimal(1000);
        orderId = System.currentTimeMillis() + "";
        channelInfo = new ChannelInfo();
        channelInfo.setChannelPassword("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb2RlIjoiem4wMDQiLCJpYXQiOjE3MDEyNDcwMDgsImV4cCI6NDg1NDg0NzAwOCwiYXVkIjoiZm91ci1wYXJ0eTphdXRoIiwiaXNzIjoiZm91ci1wYXJ0eTphdXRoIiwic3ViIjoiMjIiLCJqdGkiOiJmb3VyLXBhcnR5LWp3dGlkIn0.Wj_RDQdDAHVku3SP7M-Dcr1hQ8uGGro4-DmcQVKdSZrb8-JC9l_Lb33nSZ_Eqx9U5BI8MMClnhBUXCcSBUk9e4E7Qcv0Sjh_TlYf4LdnGZr66MAlDXpXOpjAignE5YPhu-M-jGGwENeM56VxdM6eHLfRmU9advgVOor4yakf_s3htZj55SlFZNYK2-_MrIcje8qcB0SaqqGR_eR3YiwPwo744C-MxVLg-oWPpqTDGbSZh2QmC9R9QHRHpofnyue083po5EjJtRdkncWvcKqsh9TvVK7t4uDHwKK0wqVPp7A0YEkBt0Gc0gBBiczAjj648kQltr7eR2rUVu4JofM59rJflgvLuYvyv9HNChzh7kUGfI7DxSOvhqfcuJdltC4Im1Hb8tEkcZQ_VgLSZ8zqv6CMVTYlRby03h7K-SBPfFNK1NF-i_O2qpgKto86i7tvERY10iABmAfJ4I_pX7OBkJzXL5ZYM1_sytlE425SNLLcB-_8zb7S3Y3eCluWZASWBl41KL2FpURcNGjqVVuDd7jh4FZZpWs_KzXim4sLmGgDsI8c-yi6myTKnqZRQ0GKc8ti1BLZctqm5AYNp7EW5jN6ndfXK8UX3mze0v6_ODUeDpEqyiJF-kuH-UoqieKtwaQh3jSB3vj1xByKFl3MhFzcIy3JndqW26BS_Zs2U_I");
        channelInfo.setChannelAppId("zn004");
        channelInfo.setChannelType("2");
        channelInfo.setDealurl("https://api.65258723.com/v1/b2b/payout-orders/place-order");
        FourPWitPay pay = new FourPWitPay();
        String order = pay.createDpay(notifyUrl, dealOrderApp,channelInfo);

        System.out.printf(order);
    }

}
