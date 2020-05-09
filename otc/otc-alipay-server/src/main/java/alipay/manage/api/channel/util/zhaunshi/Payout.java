package alipay.manage.api.channel.util.zhaunshi;


import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class Payout {

    private static final String URL = "/gateway/api/v2/payouts";
    private static void create() throws Exception {
        Map<String, Object> data = new TreeMap<String, Object>();
        long request_time = new Date().getTime() / 1000;
        data.put("bank_id", "BK0001");//BankEnum.ABC.getBankId()
        data.put("platform_channel_id", "PFC00000009");
        data.put("amount", 48000);
        data.put("number", "123456789012");
        data.put("name", "小哈");
        data.put("platform_id", Config.PLATFORM_ID);
        data.put("payout_cl_id", String.valueOf(request_time));
        data.put("request_time", request_time);
        data.put("service_id", Config.PAYOUT_SERVICE_ID);
        System.out.println(data);
        String generatedSign = Md5Util.md5(StringUtil.convertToHashMapToQueryString(data), Config.KEY);
        System.out.println(generatedSign);
        data.put("sign", generatedSign);
        String jsonString = JSON.toJSONString(data);
        System.out.println(jsonString);
        String post = HttpUtil.post(Config.DOMAIN + URL, jsonString);
        System.out.println(post);
    }


    public static void get() throws Exception {
        Map<String, Object> data = new TreeMap<String, Object>();
        data.put("payout_cl_id", "POT00261611");
        System.out.println(data);

        String jsonString = JSON.toJSONString(data);
        System.out.println(jsonString);
       String post = HttpUtil.post(   Config.DOMAIN + URL + "?"+jsonString,"");
       System.out.println(post);
        
        
    }

    public static void main(String[] args) throws Exception {
        create();
     //   get();
    }

}
