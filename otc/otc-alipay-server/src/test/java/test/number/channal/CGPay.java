package test.number.channal;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CGPay {
    public static void main(String[] args) {
        wit();
    }




   void deal(){
       /**
        * pay_customer_id 是 是 平台分配商户编号
        * pay_apply_date 是 是 当下时间戳(timestamp)，格式1582539115，单位秒
        * pay_order_id 是 是 商户订单，字符⻓度最⼤32
        * pay_notify_url 是 是 服务端回调地址，字符⻓度最⼤255，不转义反斜杠)
        * pay_amount 是 是 提单⾦额(元)
        * pay_channel_id 是 是 通道编号，请向客服索取
        * pay_md5_sign 是 否 请参考签名算法
        * pay_product_name 否 是 商品名称，字符⻓度最⼤20
        * user_name 否 是
        * 玩家姓名，字符⻓度最⼤20
        * 如通道需实名制，为必填
        * bank_id 否 是
        * 收款指定银⾏，字符⻓度最⼤5
        * （仅部分通道⽀持）
        */
       String pay_customer_id = "52141";
       String pay_apply_date =System.currentTimeMillis()/1000 + "";
       String pay_order_id = System.currentTimeMillis() + "";
       String pay_notify_url = "http://www.baisss.com";
       String pay_amount = "3000";
       String pay_channel_id = "1302";
       String key = "f9b2d986e32e9cae1b3818e4b1190682102e9bddc8100b686eda8ef56a84732e";
       Map map  = new HashMap();
       map.put("pay_customer_id",pay_customer_id);
       map.put("pay_apply_date",pay_apply_date);
       map.put("pay_order_id",pay_order_id);
       map.put("pay_notify_url",pay_notify_url);
       map.put("pay_amount",pay_amount);
       map.put("pay_channel_id",pay_channel_id);

       String reqSign = PayDigestUtil.getSign(map, key);
       map.put("pay_md5_sign", reqSign.toUpperCase(Locale.ROOT));


       HttpResponse header = HttpRequest.post("https://etpay888.com/api/pay_order")
               .header("Header", "Content-Type: application/json")
               .body(JSONUtil.parseObj(map)).execute();
       String s = header.body().toString();
       System.out.println(s);
       /**
        * {"code":0,"message":"success","data":{"order_id":"1678954182461","transaction_id":"T03161609441094584461","view_url":"https:\/\/pay5.superpay.us\/gcash_ex\/gcash_ex.html?order_id=T03161609441094584461&money=3000.00&pay_channel=gcash_ex&merchant_name=gcp127","qr_url":"https:\/\/api.qrserver.com\/v1\/create-qr-code\/?data=https:\/\/pay5.superpay.us\/gcash_ex\/gcash_ex.html?order_id=T03161609441094584461&money=3000.00&pay_channel=gcash_ex&merchant_name=gcp127&size=200x200&ecc=M","expired":"2023-03-16 18:09:44","user_name":"","bill_price":3000,"real_price":3000,"bank_no":"09956413421","bank_name":"Gcash","bank_from":"","bank_owner":"09956413421","payment_code":null,"remark":"","qr_code":null,"phone":null,"tm_link":null}}
        */
       JSONObject jsonObject = JSONUtil.parseObj(s);
       String code = jsonObject.getStr("code");
       if("0".equals(code)){
           JSONObject data = JSONUtil.parseObj(jsonObject.getStr("data"));
           String view_url = data.getStr("view_url");
           System.out.println(view_url);
       }else {
           String message = jsonObject.getStr("message");
           System.out.println(message);
       }
    }




    static void  wit(){


        /**
         * pay_customer_id 是 是 平台分配商户ID
         * pay_apply_date 是 是 当下时间戳(timestamp)，格式1582539115，单位秒
         * pay_order_id 是 是 商户订单，字符⻓度最⼤32
         * pay_notify_url 是 是 服务端回调地址，字符⻓度最⼤255
         * pay_amount 是 是 下单⾦额，⽀持⼩数⾄2位
         * pay_md5_sign 是 否 请参考签名算法
         * pay_account_name 是 是 银⾏卡持有⼈姓名（USDT模式：玩家名称）
         * pay_card_no 是 是 银⾏卡号（USDT模式：钱包地址）
         * pay_bank_name 是 是 银⾏卡所属银⾏名称（USDT模式：USDT）
         */



        String pay_customer_id = "52141";
        String pay_card_no = "1254615312545";
        String pay_bank_name = "BANK OF THE PHIL. ISLANDS";
        String pay_account_name = "KKEN·USI";
        String pay_apply_date =System.currentTimeMillis()/1000 + "";
        String pay_order_id = System.currentTimeMillis() + "";
        String pay_notify_url = "http://www.baisss.com";
        String pay_amount = "10000";
        String pay_channel_id = "1302";
        String key = "f9b2d986e32e9cae1b3818e4b1190682102e9bddc8100b686eda8ef56a84732e";
        Map map  = new HashMap();
        map.put("pay_customer_id",pay_customer_id);
        map.put("pay_apply_date",pay_apply_date);
        map.put("pay_order_id",pay_order_id);
        map.put("pay_notify_url",pay_notify_url);
        map.put("pay_amount",pay_amount);

        map.put("pay_bank_name",pay_bank_name);
        map.put("pay_account_name",pay_account_name);
        map.put("pay_card_no",pay_card_no);

        String reqSign = PayDigestUtil.getSign(map, key);
        map.put("pay_md5_sign", reqSign.toUpperCase(Locale.ROOT));

        HttpResponse header = HttpRequest.post("https://etpay888.com/api/payments/pay_order")
                .header("Header", "Content-Type: application/json")
                .body(JSONUtil.parseObj(map)).execute();
        String s = header.body().toString();
        System.out.println(s);
        /**
         * {"code":0,"message":"success","data":{"order_id":"1678954182461","transaction_id":"T03161609441094584461","view_url":"https:\/\/pay5.superpay.us\/gcash_ex\/gcash_ex.html?order_id=T03161609441094584461&money=3000.00&pay_channel=gcash_ex&merchant_name=gcp127","qr_url":"https:\/\/api.qrserver.com\/v1\/create-qr-code\/?data=https:\/\/pay5.superpay.us\/gcash_ex\/gcash_ex.html?order_id=T03161609441094584461&money=3000.00&pay_channel=gcash_ex&merchant_name=gcp127&size=200x200&ecc=M","expired":"2023-03-16 18:09:44","user_name":"","bill_price":3000,"real_price":3000,"bank_no":"09956413421","bank_name":"Gcash","bank_from":"","bank_owner":"09956413421","payment_code":null,"remark":"","qr_code":null,"phone":null,"tm_link":null}}
         */
        JSONObject jsonObject = JSONUtil.parseObj(s);
        String code = jsonObject.getStr("code");
        if("0".equals(code)){
            //{"code":0,"message":"success","data":{"transaction_id":"P03162100327991454871","amount":"100.0000"}}
            JSONObject data = JSONUtil.parseObj(jsonObject.getStr("data"));
            String view_url = data.getStr("view_url");
            System.out.println(view_url);
        }else {
            String message = jsonObject.getStr("message");
            System.out.println(message);
        }




    }
}
