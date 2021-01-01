package test.number.channal;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class hengtongpa {
    public static void main(String[] args) {
        /**
         * merchant	M	商户号。
         * amount	M	存款金额。单位：元，可保留2位小数
         * paymentType	M	支付类型。 取值:[
         * username	M	用户名。网站用户唯一标识，本平台不会检测用户名的有效性，
         * depositRealname	C	付款人姓名。对于银行卡转银行卡，如果不传付款人姓名，本平台的自带收银台页面，会要求网站玩家输入付款人真实姓名。
         * callback	C	服务端回调地址。只有成功的订单会回调。收到回调后，请返回 "success" 字符串。不填写则不会回调。
         * remark	O	备注。
         * paymentReference	M	商户订单号。必须保证唯一
         * clientIp	O	用户真实IP，可为空
         * sign	M	签名，签名方式参考“签名说明”
         */
        String key = "RZTYFOMWY289NWI6ZP04QSUAUXE5WDTA";
        String merchant = "M-HT-JINXING-016";
        String amount = "100";
        String paymentType = "6";
        String username = "";
        String depositRealname = "";
        String callback = "www.baidu.com";
        String remark = "";
        String paymentReference = IdUtil.simpleUUID();
        String clientIp = "";
        String sign = "";
        Map<String, Object> map = new HashMap<>();
        map.put("merchant", merchant);
        map.put("paymentType", paymentType);
        map.put("amount", amount);
        map.put("username", username);
        map.put("depositRealname", depositRealname);
        map.put("callback", callback);
        map.put("remark", remark);
        map.put("paymentReference", paymentReference);
        map.put("clientIp", clientIp);
        String param = createParam(map);
        String s = md5(param + "&key=" + key);
        sign = s.toUpperCase();
        map.put("sign", sign);
        String post = HttpUtil.post("https://api.gogo9845.com/api/deposit/page", map);
        System.out.println(post);
        //  {"whether":"YES","code":1,"message":"","data":{"paymentReference":"bb9bd22795174e49ab9a18c57ae0b8ad","price":100,"qrCodeType":"BANK_BANK","drecType":6,"depositRealname":"","reference":"20210101163813667fxgbq","createTime":1609490293667,"redirect":"https://app.qeboqbl.cn//static/dp/bankbank2_1.html?reference=20210101163813667fxgbq","statusSt":0,"interfaceType":0,"revisedPrice":100,"recAccount":null,"recRealname":null,"recBank":null,"recBankName":null,"recBankBranch":null,"qrCodeContent":""},"success":true}
        //{"whether":"NO","code":65568,"message":"sign不存在","data":null,"success":false}

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

    public static String md5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

}