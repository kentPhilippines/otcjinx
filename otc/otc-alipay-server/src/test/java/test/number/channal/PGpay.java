package test.number.channal;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
public class PGpay {
    public static void main(String[] args) {


        deal();
        wit();

    }


    /**
     * appid                string      商户号，请向开户人员索取
     * apporderid           string      订单号，必须唯一<br>appid+orderid【验证唯一性】
     * ordertime            string      请求时间，时间格式：yyyyMMddHHmmss<br>时间格式错误会产生异常
     * amount               string      代付金额<br>以元为单位，可以2位小数
     * acctno               string      银行卡号
     * acctname             string      银行卡开户人姓名
     * bankcode             string      银行标识（银行名称）<br>如：ICBC 工商银行，如果贵公司 无法传递按照规则的 银行编号，该字段直接传递银行名称
     * notifyurl            string      回调地址<br>异步通知
     * sign                 string
     */
    private static void wit() {
        String key = "68FCD1FE21E24FB283843E24CC9A065D";
        String appid = "jackma";
        String acctno = "6552626271872817227";
        String apporderid = System.currentTimeMillis() + "";
        String notifyurl = "www.sss.aos";
        String acctname = "王发";
        String amount = "149.00";
        String bankcode = "ICBC";
        String ordertime = d.format(new Date());
        Map map = new HashMap();
        map.put("appid", appid);
        map.put("acctno", acctno);
        map.put("notifyurl", notifyurl);
        map.put("apporderid", apporderid);
        map.put("amount", amount);
        map.put("bankcode", bankcode);
        map.put("acctname", acctname);
        map.put("ordertime", ordertime);
        map.put("witType", "BANK_WIT_S");
        String createParam = createParam(map);
        System.out.println("签名前请求串：" + createParam);
        String md5 = getKeyedDigestUTF8(createParam + key);
        System.out.println("签名：" + md5);
        map.put("sign", md5);
        System.out.println(JSONUtil.parseObj(map).toString());
        String result2 = HttpRequest.post("http://45.207.58.145:5055/api-alipay/v2/deal/wit")
                .header(Header.USER_AGENT, "Hutool http")//头信息，多个头信息多次调用此方法即可
                .body(JSONUtil.parseObj(map))
                .timeout(20000)//超时，毫秒
                .execute().body();
        Console.log(result2);

    }

    static SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");

    private static void deal() {
        /**
         * appId        string      商户号，请向开户人员索取
         * openType     string
         * orderId      string      订单号，必须唯一
         * notifyUrl    string      回调地址<br>异步通知
         * pageUrl      string      同步返回地址<br>支付成功后跳转的地址
         * amount       string      代收金额<br>以元为单位，可以2位小数
         * passCode     string      通道编码：ALIPAY_SCAN【支付宝扫码】 ，请向开户人员索要
         * applyDate    string      请求时间，时间格式：yyyyMMddHHmmss<br>时间格式错误会产生异常
         * mcPayName    string      付款人姓名
         */
        String key = "68FCD1FE21E24FB283843E24CC9A065D";
        String appId = "jackma";
        String openType = "open_info";
        String orderId = System.currentTimeMillis() + "";
        String notifyUrl = "www.test.net";
        String pageUrl = "www.test.com";
        String amount = "309.01";
        String passCode = "BANK_R_S";
        String applyDate = d.format(new Date());
        String mcPayName = "王双双";
        String sign = "";
        Map map = new HashMap();
        map.put("appId", appId);
        map.put("openType", openType);
        map.put("orderId", orderId);
        map.put("notifyUrl", notifyUrl);
        map.put("pageUrl", pageUrl);
        map.put("amount", amount);
        map.put("passCode", passCode);
        map.put("applyDate", applyDate);
        map.put("mcPayName", mcPayName);

        String createParam = createParam(map);
        System.out.println("签名前请求串：" + createParam);
        String md5 = getKeyedDigestUTF8(createParam + key);
        System.out.println("签名：" + md5);
        map.put("sign", md5);
        System.out.println(JSONUtil.parseObj(map).toString());
        String result2 = HttpRequest.post("http://45.207.58.145:5055/api-alipay/v2/deal/pay")
                .header(Header.USER_AGENT, "Hutool http")//头信息，多个头信息多次调用此方法即可
                .body(JSONUtil.parseObj(map))
                .timeout(20000)//超时，毫秒
                .execute().body();
        Console.log(result2);
//{"success":false,"message":"当前无可用渠道","result":null,"code":null}
//{"success":true,"message":null,"result":{"success":true,"openType":"open_url_info","returnUrl":"http://206.119.167.19/pay?orderId=SD20230619202327941432931&type=203","payInfo":"PayInfo{amount='302.01', bankCard='9876543210123456789', bankName='中国工商银行', name='张三', bankBranch='', orderId='SD20230619202327941432931'}"},"code":null}
//{"success":false,"message":"当前无可用渠道","result":null,"code":null}
    }
//【请求 alipay.manage.api.channel.other.hongyuntong.HongYunTongUtil 上游渠道的参数为：{PayTypeIdFormat=null, MerchantUniqueOrderId=SD20230619191451208119606, PayTypeId=kzk, Amount=300.01, Ip=null, NotifyUrl=http://45.207.58.145:5055/api-alipayhttp://45.207.58.145:5055/api-alipay/notfiy-api-pay/HongYunTong/form-urlencoded/deal, ClientRealName=null, Sign=00a9a71c53515dfc540d35f4d0ccedb9, MerchantId=218783, Remark=SD20230619191451208119606}，当前订单我方订单号为：SD20230619191451208119606，当前商户订单号为：MC20230619191450842987159，请求时间：2023-06-19 19:14:51.274】


    public static String getKeyedDigestUTF8(String strSrc) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(strSrc.getBytes("UTF8"));
            String result = "";
            byte[] temp;
            temp = md5.digest("".getBytes("UTF8"));
            for (int i = 0; i < temp.length; i++)
                result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
}
