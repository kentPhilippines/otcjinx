package test.number;


import alipay.manage.api.channel.deal.jiabao.RSAUtil;
import alipay.manage.bean.util.ResultDeal;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import otc.result.Result;
import otc.util.RSAUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class rsa {
    private static final String UTF_8 = "utf-8";
    private static final String ENCODE_TYPE = "md5";

    public static void main(String[] args) throws IOException {
        String payInfo1 = "";

        try {
            Map map = new HashMap();
            Map<Object, Object> hmget = new HashMap<>();
            System.out.println(hmget.toString());
            if(ObjectUtil.isNotNull(hmget)){
                Object bank_name = hmget.get("bank_name");
                Object card_no = hmget.get("card_no");
                Object card_user = hmget.get("card_user");
                Object money_order = hmget.get("money_order");
                Object address = hmget.get("address");
                map.put("amount",money_order);
                map.put("bankCard",card_no);
                map.put("bankName",bank_name);
                map.put("name",card_user);
                map.put("bankBranch",address);
                JSONObject jsonObject = JSONUtil.parseFromMap(map);
                payInfo1 = jsonObject.toString();
            }
        } catch (Throwable e ){
            // log.error(e);
            // log.info("详细数据解析异常，当前订单号：" + dealOrderApp.getAppOrderId());
            System.out.println( Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl("result.getResult())")).toString());
        }


    }


    public static String md5(String a) {
        String c = "";
        MessageDigest md5;
        String result = "";
        try {
            md5 = MessageDigest.getInstance(ENCODE_TYPE);
            md5.update(a.getBytes(UTF_8));
            byte[] temp;
            temp = md5.digest(c.getBytes(UTF_8));
            for (int i = 0; i < temp.length; i++)
                result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
        return result;
    }

}
