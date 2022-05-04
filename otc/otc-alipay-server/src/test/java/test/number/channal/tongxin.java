package test.number.channal;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import cn.hutool.http.HttpUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class tongxin {
    public static void main(String[] args) {
        String url = "http://mng.yuegepay.com/709015.tran9";
        String nodnotifyurl = "http://www.sdahasdas.sd.com";
        String agtorg = "JX199";
        String mercid ="484584045119529";
        String key ="F3B47EAE8E9C44B1";
        String ordernumber = UUID.randomUUID().toString();
        String body = "body";
        String busytyp ="ALPAY";
        int v = 2000;
        v*=100;
        String amount =v+"";
        String param = "agtorg="+agtorg+"&amount="+amount+"&body="+body+"&busytyp="+busytyp+"&mercid="+mercid+"&nodnotifyurl="+nodnotifyurl+"&ordernumber="+ ordernumber +"&key="+key+"";

        String md5 = PayUtil.md5(param).toUpperCase();
        Map<String,Object> map = new HashMap<>();
        map.put("agtorg",agtorg);
        map.put("mercid",mercid);
        map.put("ordernumber",ordernumber);
        map.put("body",body);
        map.put("busytyp",busytyp);
        map.put("amount",amount);
        map.put("nodnotifyurl",nodnotifyurl);
        map.put("sign",md5);
        System.out.println("请求参数："+map.toString());
        String post = HttpUtil.post(url, map);
        System.out.println("返回参数："+post);

    }
}
