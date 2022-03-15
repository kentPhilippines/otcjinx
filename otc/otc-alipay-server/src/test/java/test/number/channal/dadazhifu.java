package test.number.channal;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.text.SimpleDateFormat;
import java.util.*;

public class dadazhifu {

    public static void main(String[] args) {
       test();
    // test1();


    }

    /**
     * 商户ID         mchId       是       long        20001222        分配的商户号
     * 应用ID         appId       是       String(32)  72211da10c65477ba9c261d9f035f154        该商户创建的应用对应的ID
     * 支付产品ID      productId  是         int         8019        支付产品ID
     * 商户订单号      mchOrderNo  是       String(30)      20160427210604000490        商户生成的订单号
     * 币种           currency    是       String(3)       cny         三位货币代码,人民币:cny
     * 支付金额         amount      是       int             100         支付金额,单位分
     * 客户端IP        clientIp    否       String(32)      210.73.10.148       客户端IP地址
     * 设备           device      否       String(64)      ios10.3.1       客户端设备
     * 支付结果后台回调URL  notifyUrl   是   String(128)     http://shop.yeepay.org/notify.htm   支付结果回调URL
     * 商品主题     subject     是   String(64)          yeepay测试商品1 商品主题
     * 商品描述信息   body        是   String(256)     yeepay测试商品描述    商品描述信息
     * 扩展参数1    param1         否    String(64)  支付中心回调时会原样返回
     * 扩展参数2    param2      否       String(64)      支付中心回调时会原样返回
     * 附加参数     extra       否       String(512)
     * 签名       sign        是       String(32)      C380BEC2BFD727A4B6845133519F3AD6
     * 签名值，详见签名算法
     */
    static void test(){
        String key  = "RDBECCGUN2OQ6NMS8WUDOZGAXME9UYPA0RJPTR89COWMISQ2X2JWHRE4YHX2FMCUGOPVF5KGOIO7ZKWXOCNDIFKIWPDK9LGI4OWJCOTDWFTWU1KVZ3KLTDONCD364FC3";
        String amount  =  "1000";
        String appId  =  "72211da10c65477ba9c261d9f035f154";
        String mchId  =  "1804";
        String productId  =  "8019";
        String notifyUrl  =  "www.dasdhsaihdhdas.com";
        String subject  =  "今晚打老虎";
        String body  =  "干就完了";
        String url = "http://42.157.129.22:3020";
        String orderId = "123sdasds2222as4513126";
        Map<String,Object> map   = new HashMap<>();
        map.put("mchId",mchId);
        map.put("appId", appId);
        map.put("mchOrderNo",orderId);
        map.put("currency","cny");
        map.put("amount",amount);
        map.put("notifyUrl",notifyUrl);
        map.put("subject",subject);
        map.put("body",body);
        String createParam = PayUtil.createParam(map);
        System.out.println(createParam);
        String md5 = PayUtil.md5(createParam + "&key="+key).toUpperCase(Locale.ROOT);
        System.out.println(md5);
        map.put("sign",md5);
        String payUrl = url+"/api/cashier/h5_recharge?"+createParam+"&sign="+md5;
        System.out.println(payUrl);
    }


    /**
     * 商户ID  mchId 是 long 20001222 分配的商户号
     * 商户代付单号 mchOrderNo 是 String(30) 20160427210604000490 商户代付订单号
     * 代付金额 amount 是 int 8000 代付金额,单位分
     * 账户属性 accountAttr 否 Byte 0 0-对私,1-对公,默认对私
     * 收款人账户名 accountName 是 String(64) manager 收款人账户名
     * 收款人账户号 accountNo 是 Number 6222020200098541458 收款人账户号
     * 开户行所在省份 province 否 String(32) 北京 开户行所在省份
     * 开户行所在市 city 否 String(32) 北京 开户行所在市
     * 开户行名称 bankName 否  String(128) 北京上地支行 开户行名称
     * 联行号 bankNumber 否 String(64) 11473707055 联行号
     * 代付结果回调URL notifyUrl 否 String(128)  http://www.baidu.com 代付结果回调URL
     * 备注 remark 是 String(128) 代付1000元 备注
     * 扩展域 extra 否 String(128) 扩展域
     * 请求时间 reqTime 是 String(20) 20181009171032 请求发起时间,时间格式:yyyyMMddHHmmss
     * 签名 sign 是 String(32) C380BEC2BFD727A4B6845133519F3AD6 签名值，详见签名算法
     * 银行卡号：622908343055178618
     * 开户人：曾黎
     * 开户行：兴业银行 金额：500
     */
  static   SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");
    static void test1(){
        String key  = "RDBECCGUN2OQ6NMS8WUDOZGAXME9UYPA0RJPTR89COWMISQ2X2JWHRE4YHX2FMCUGOPVF5KGOIO7ZKWXOCNDIFKIWPDK9LGI4OWJCOTDWFTWU1KVZ3KLTDONCD364FC3";
        String amount  =  "1000";
        String appId  =  "72211da10c65477ba9c261d9f035f154";
        String mchId  =  "1804";
        String accountName  =  "吴东飞";
        String orderId = "W20220218183222644719160502";
        String accountNo = "6228481089392408573";
        String notifyUrl = "http://47.243.33.146:17628/notfiy-api-pay/dadaWit-noyfit";
        String remark = "代付";
        Map<String,Object> map   = new HashMap<>();
        map.put("mchId",mchId);
        map.put("mchOrderNo",orderId);
        map.put("amount",amount);
        map.put("accountName",accountName);
        map.put("accountNo",accountNo);
        map.put("notifyUrl",notifyUrl);
        map.put("remark",remark);
        map.put("reqTime",d.format(new Date()));
        String createParam = PayUtil.createParam(map);
        System.out.println(createParam);
        String md5 = PayUtil.md5(createParam + "&key="+key).toUpperCase(Locale.ROOT);
        System.out.println(md5);
        map.put("sign",md5);
        System.out.println(map.toString());
        String post = HttpUtil.post("http://42.157.129.22:3020/api/agentpay/apply", map);
        System.out.println(post);

        //{"fee":216,"sign":"4B8ECDF51F8D84DBC9713BBEF1550A5F","resCode":"SUCCESS","retCode":"SUCCESS","agentpayOrderId":"G01202202172105416190012","retMsg":"","status":0}
    }
}
