package alipay.manage.api.channel.notfiy;

import alipay.manage.api.channel.util.baG.BaGPayUtil;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.PostMapping;
import otc.result.Result;
import otc.util.MapUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class BaGPayNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();
    @PostMapping("/baG-notfiy")
    public String notify(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为："+clientIP+"】");
        Map map = new HashMap();
        map.put("47.56.151.163", "47.56.151.163");
        map.put("47.244.104.142", "47.244.104.142");
        Object object = map.get(clientIP);
        if(ObjectUtil.isNull(object)) {
            log.info("【当前回调ip为："+clientIP+"，固定IP登记为："+"47.56.151.163"+"，47.244.192.185"+"】");
            log.info("【当前回调ip不匹配】");
            return "ip is error";
        }
        map = null;
        /**
         * 商户号	merchant_id	    是	    10000098	String(32)	分配唯一的商户标识
         * 订单号	order_id	    是	    201911111234567890	String(28)	商户订单号
         * 支付状态	pay_status	    是	    2	String	1 未支付 2 已支付 3 订单超时 只会通知2已支付状态
         * 实付金额	paid_money	    是	    200.00	String	实付金额
         * 支付时间	pay_time	    是	    2020123123123	String	支付时间
         * 签名值	sign	        是	    xxxxxxxxxxxxxx	String	RSA2签名值
         */
        String merchant_id = req.getParameter("merchant_id");
        String order_id = req.getParameter("order_id");
        String pay_status = req.getParameter("pay_status");
        String paid_money = req.getParameter("paid_money");
        String pay_time = req.getParameter("pay_time");
        String sign = req.getParameter("sign");
        String PRIVATE_KEY= BaGPayUtil.PRIVATE_KEY;
        Map map1 = new HashMap();
        map1.put("merchant_id",merchant_id);
        map1.put("order_id",order_id);
        map1.put("pay_status",pay_status);
        map1.put("paid_money",paid_money);
        map1.put("pay_time",pay_time);
        log.info("【8Gpay回调参数为："+map1.toString()+"】");
        String param = MapUtil.createParam(map1);
        String sign1 =  BaGPayUtil.sign(param.getBytes("UTF-8"),PRIVATE_KEY);
        if(sign.equals(sign1)) {
            log.info("【验签成功】");
        }else{
            log.info("【验签失败，我方系统签名未："+sign1+"，对方系统签名为："+sign+"】");
            return "sign is error";
        }
        if(pay_status.equals("2")) {
            Result dealpayNotfiy = dealpayNotfiy(order_id, clientIP,"豪富回调成功");
            if(dealpayNotfiy.isSuccess()) {
                return "success";
            }
        }
        return "end errer";
    }
}
