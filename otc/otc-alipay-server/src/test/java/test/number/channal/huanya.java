package test.number.channal;

import alipay.manage.api.channel.deal.huanya.HuanYaUtil;
import cn.hutool.http.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class huanya {
    public static void main(String[] args) {
        Map<String, Object> mapp = new HashMap<>();
        String mchId = "20000054";
        String appId = "4386f690516a4fe384898cb3b5dc6074";
        String productId = "8020";
        String mchOrderNo = System.currentTimeMillis() + "";
        String currency = "cny";
        Integer amount = 20000;
        //   String returnUrl = dealOrderApp.getBack();
        String notifyUrl = "http://api.yanhua.net.cn/api/pay";
        String subject = "huanya";
        String body = "huanya";
        String extra = "s";
        mapp.put("mchId", mchId);
        mapp.put("appId", appId);
        mapp.put("productId", productId);
        mapp.put("mchOrderNo", mchOrderNo);
        mapp.put("currency", currency);
        mapp.put("amount", amount);
        mapp.put("notifyUrl", notifyUrl);
        mapp.put("subject", subject);
        mapp.put("body", body);
        mapp.put("extra", extra);
        String param = HuanYaUtil.createParam(mapp);
        param = param + "&key=" + "ED8HFA5T3B56ED34IE1VLSPIG855K65BQT3GRV1XHTTAV0GBOWAXQTGSTCPSVEP9NWVDRFBPFLRDI74KVIEXKZVWZ3FTGXIISRHJXVLLPBQFDHTTSVQTMVXLEWTGDKJW";
        System.out.println("【环亚加密前参数：" + param + "】");
        String sign = HuanYaUtil.md5(param);
        mapp.put("sign", sign);
        System.out.println("【环亚请求参数：" + param + "】");
        String post = HttpUtil.post("http://api.yanhua.net.cn/api/pay/create_order", mapp);
        System.out.println("【环亚响应参数：" + post + "】");
    }
}
