package test.number.channal;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class shenLaiFu {
    public static void main(String[] args) {
        test();
    }
  /*  商户账号	            memberId	    是	是	30	商户在支付平台的唯一标识
    商户订单号	        orderId	        是	是	32	商户系统产生的唯一订单号
    订单金额	            amount	        是	是	30	以“元”为单位,不允许小数。
    交易日期        	    applyDate	    是	是	14	商户系统生成的订单日期 格式：YYYYMMDDHHMMSS
    交易渠道	            channelCode	    是	是	20	请见 交易渠道编码
    用户ID	            userId	        是	是	100	充值用户的个人ID
    银行编号	            bankCode	    否	否	15	银行编号
    支付结果通知地址	    notifyUrl	    是	是	128	页面通知和服务器通知地址二合一, 商户系统需要能同时处理这两种支付结果的通知，详见支付结果通知接口：
            1.页面跳转同步通知页面路径，支付平台处理完请求后，当前页面自动跳转到商户网站里指定页面的http/https路径。
            2.服务器异步通知商户接口路径，支付平台主动调商户接口通知订单支付结果。
    真实姓名	            realName	    是	否	30	注意:必传,不加入签名
    扩展字段	            reserved	    否	否	240	英文或中文字符串,支付完成后，按照原样返回给商户
    产品名称	            productName	    否	否	120	一般填写商户的商品名称，英文或中文字符串
    产品数量	            productNum	    否	否	2	一般填写商户的商品数量
    移动端	            isMobile	    否	否	5	若是移动端请填写true字符串预设是false字符串
    签名档	            md5Sign	        是	否	1024	必输，商户对交易数据的签名，（最后转出大写）签名方式参照签名档例子。
 */
    private static final SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDDHHMMSS");
    private static void test() {
        Map map = new HashMap();
        String key = "ab90c5506ec5c5aa37e1e16c42f09c1c";
        String memberId = "GP233554";
        String orderId = "3f3629dcb968wwwwwdf3c";
        String amount = "1000";
        String applyDate = sdf.format(new Date());
        String channelCode = "IMA";
        String userId = "GP233554";
        String notifyUrl = "www.baidu.com";
        String sgin = "memberId^"+memberId+"&orderId^"+orderId+"&amount^"+amount+"&applyDate^"+applyDate
                +"&channelCode^"+channelCode+"&userId^"+userId+"&notifyUrl^"+notifyUrl+"&key="+key;
        System.out.println(sgin);
        String md5Sign = md5(sgin).toUpperCase();
        System.out.println(md5Sign);
        map.put("memberId",memberId);
        map.put("orderId",orderId);
        map.put("amount",amount);
        map.put("applyDate",applyDate);
        map.put("channelCode",channelCode);
        map.put("userId",userId);
        map.put("realName","jinxin");
        map.put("notifyUrl",notifyUrl);
        map.put("md5Sign",md5Sign);
        String s = HttpUtil.toParams(map);
        System.out.println(s);
        String url = "https://www.godpay.shop/load/pay";
        url += ("?"+ s);
        System.out.println(url);
//memberId^HP0001&orderId^40288184626d555601626d5556f60001&amount^100&applyDate^20180328235421&channelCode^B&userId^tianming&notifyUrl^http://localhost/notice&key=345677565t765sasa
//memberId^GP233554&orderId^c882ac04-7b88-4fd5-94de-da035614c9c3&amount^1000&applyDate^2020061791006384&channelCode^IMA&userId^GP233554&notifyUrl^http://www.baidu.com&key=6a3ad1025aa57fd53f3629dcb968df3c
    //    String post = HttpUtil.get("https://www.godpay.shop/load/pay", map);
  //      System.out.println(post);


    }
    public static String md5(String a) {
        String c = "";
        MessageDigest md5;
        String result="";
        try {
            md5 = MessageDigest.getInstance("md5");
            md5.update(a.getBytes("utf-8"));
            byte[] temp;
            temp=md5.digest(c.getBytes("utf-8"));
            for (int i=0; i<temp.length; i++)
                result+=Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
        return result;
    }


}
