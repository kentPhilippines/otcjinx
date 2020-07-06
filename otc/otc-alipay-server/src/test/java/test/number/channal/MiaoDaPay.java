package test.number.channal;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.collections.map.HashedMap;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MiaoDaPay {
    public static void main(String[] args) {
        test();
    }

    /**
     * type	                支付方式	        string	        使用的支付渠道,如:weixin, alipay等,新增mdjhpay[秒达聚合]模式。 建议使用mdjhpay模式，需要用哪种支付方式用type_add来控制，当mdjhpay模式时，如果type_add为空，则会调用同时支持weixin,alipay支付的通道。	Y
     * type_add	            支付附加方式	    string	        当支付方式type为:wxpay时可以指定为h5/qrcode/jsapi 当使用微信商家收款时，建议识别用户的浏览器，当为使用微信环境时使用jsapi; 当使用手机浏览器时可以使用h5,当为PC时使用qrcode,这样用户支付会更加方便。当支付方式type为:mdjhpay时type_add可选参数为:weixin[微信],alipay[支付宝],bank[云闪付]	N
     * order_sn	            业务订单号	    string	        您平台的订单编号，可重复	Y
     * out_order_sn	        外部订单号	    string	        与本平台的唯一订单编号，不可重复。	Y
     * money	            订单金额	        float	        订单金额须保持2位小数 如2.00，防止签名失败	Y
     * ouser	            用户帐号	        string(32)	    提交业务的会员帐户号，在您平台为唯一标识。 建议请勿使用IP来标识，移动网通常会是网关IP，支付系统会依一定时间内同一用户支付成功率进行限制下单，如担心隐私问题，为了防止误限，您可以把你平台用户唯一标识进行md5加密进行提交。
     * uip	                用户IP	        string	        提交业务的客户IP。请真实提交。	Y
     * time	                订单时间	        number	        您的订单产生时间，转化成秒级时间戳	Y
     * body	                商品描述	        string	        购买的订单描述，会出现支付说明里，请勿填写敏感字符。	N
     * attach	            附加数据	        string	        附加数据，原样返回，如：订单ID，返回验证时作为处理目标	N
     * notify_url	        异步地址	        string	        支付成功后的异步通知地址。	Y
     * back_url	            同步地址	        string	        订单支付成功，同步通知地址	N
     * qrtype	            同步生成码	    int	            是否同步返回支付码，如果要实时生码可能会返回慢
     */
    private static void test() {
        String mno = "10334";
        String key = "XPowxrRE4hKWEo2g";
        String url ="http://pay.miaodapay.com/orders/post";
        String type = "mdjhpay";
        String type_add = "alipay";
        String order_sn = RandomUtil.randomString(10);
        String out_order_sn = order_sn;
        String money = "500.00";
        String ouser = "userid";
        String uip = "154.197.25.171";
        String time = System.currentTimeMillis()/1000+"";
        String body = "";
        String attach = order_sn;
        String notify_url = "www.baodu.com";
        String back_url = "";
        String qrtype = "";
        Map map = new HashMap();
        map.put("type",type);
        map.put("order_sn",order_sn);
        map.put("money",money);
        map.put("uip",uip);
        map.put("time",time);
        map.put("type_add",type_add);
        map.put("ouser",ouser);
        map.put("out_order_sn",out_order_sn);
        map.put("attach",attach);
        map.put("notify_url",notify_url);
        String param = createParam(map);
        System.out.println(param);
        String s = mno + "&" + param + key;
        System.out.println(s);
        String s1 = md5(s);
        System.out.println(s1);
        JSONObject jsonObject = JSONUtil.parseFromMap(map);
        String s2 = jsonObject.toString();
        System.out.println(s2);
        url = url+"?mno="+mno+"&type=json&sign="+s1+"&data="+s2;
        System.out.println("完整URL:"+url);
        String s3 = HttpUtil.get(url);
        System.out.println(s3);
        JSONObject jsonObject1 = JSONUtil.parseObj(s3);
        if("100".equals(jsonObject1.getStr("code"))){
            JSONObject data = JSONUtil.parseObj(jsonObject1.getStr("data"));
            System.out.println(data);
            String jump_uri = data.getStr("jump_uri");
            System.out.println(jump_uri);
        }
    }
    public static String createParam(Map<String, Object> map) {
        try {
            if (map == null || map.isEmpty())
                return null;
            Object[] key = map.keySet().toArray();
            Arrays.sort(key);
            StringBuffer res = new StringBuffer(128);
            for (int i = 0; i < key.length; i++)
                if(ObjectUtil.isNotNull(map.get(key[i])))
                    res.append(key[i] + "=" + map.get(key[i]) + "&");
            return res.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
