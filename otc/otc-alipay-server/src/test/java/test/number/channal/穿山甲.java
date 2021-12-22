package test.number.channal;

import alipay.manage.api.channel.util.xianyu.XianYuUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class 穿山甲 {
    public static void main(String[] args) {
        String fxnotifyurl = "https://www.qupay88.com/Pay";
        String fxbackurl = "https://www.qupay88.com/Pay";
        String fxattch = "test";
        String fxdesc = "desc";
        String fxfee = 500+"";
        String fxpay = "zfbwap2";
        String fxddh =  "J2021121413182ss1092249100";
        String fxid =  "100727";
        String key = "TVDOAtYWrafwhzbzZXVCIklmbmUnqPAd";
        //订单签名
        String fxsign = XianYuUtil.md5(fxid + fxddh + fxfee + fxnotifyurl + key);
        fxsign = fxsign.toLowerCase();
        Map<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("fxid", fxid);
        reqMap.put("fxddh", fxddh);
        reqMap.put("fxfee", fxfee);
        reqMap.put("fxpay", fxpay);
        reqMap.put("fxnotifyurl", fxnotifyurl);
        reqMap.put("fxbackurl", fxbackurl);
        reqMap.put("fxattch", fxattch);
        reqMap.put("fxnotifystyle", "1");
        reqMap.put("fxdesc", fxdesc);
        reqMap.put("fxip", "127.0.0.1");
        reqMap.put("fxsign", fxsign);
        System.out.println("【穿山甲请求参数：" + reqMap.toString() + "】");

        // 支付请求返回结果
        String result = null;
        result = HttpUtil.post( "https://www.qupay88.com/Pay", reqMap);
        System.out.println(result);
    }
}
