package alipay.manage.api.channel.deal.yifu;

import alipay.manage.api.channel.util.yifu.YiFu02Util;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.api.feign.ConfigServiceClient;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.service.OrderService;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.bean.config.ConfigFile;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component("YiFu02AlipayH5")
public class YiFu02AlipayH5 extends PayOrderService {
    //private static final Log log = LogFactory.get();
    @Autowired
    ConfigServiceClient configServiceClientImpl;
    @Autowired
    OrderService orderServiceImpl;
    static final String msg = "支付金额错误,当前金额只支持100,200,300,500,800,1000,1500,2000";
    static Map<String, String> amount = new ConcurrentHashMap<>();
    static {
        amount.put("200", "200");
        amount.put("100", "100");
        amount.put("300", "300");
        amount.put("500", "500");
        amount.put("800", "800");
        amount.put("1000", "1000");
        amount.put("1500", "1500");
        amount.put("2000", "2000");
    }
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel)   {
        String orderId = create(dealOrderApp, channel);
        Integer i = dealOrderApp.getOrderAmount().intValue();
        String s = amount.get(i.toString());
        if (StrUtil.isBlank(s)) {
            orderEr(dealOrderApp, msg);
            return Result.buildFailMessage(msg);
        }
        if (StrUtil.isNotBlank(orderId)){
            log.info("【本地订单创建成功，开始请求远程易付三方支付】");
            Result config = configServiceClientImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.SERVER_IP);
            log.info("【回调地址ip为："+config.toString()+"】" );
            log.info("【本地订单创建成功，开始请求远程易付三方支付】");
            String url = createOrder(dealOrderApp, config.getResult() + PayApiConstant.Notfiy.NOTFIY_API_WAI + "/YiFuH502-notfiy", dealOrderApp.getOrderAmount(), orderId);
            if (StrUtil.isBlank(url))
                return Result.buildFailMessage("支付失败");
            else
                return Result.buildSuccessResultCode("支付处理中", url, 1);
        }
        return  Result.buildFailMessage("支付失败");
    }

    private String createOrder(DealOrderApp dealOrderApp, String notify, BigDecimal orderAmount, String orderId) {
        String key = YiFu02Util.XUNFU_KEY;
        String merchant_id = YiFu02Util.XUNFU_APPID;
        String order_id = orderId;
        String amount = orderAmount.intValue() + "00.00";
        String pay_type = "alipayh5";
        String notify_url = notify;
        String user_id = RandomUtil.randomString(10).toUpperCase();
        String user_ip = dealOrderApp.getOrderIp();
        Map<String, Object> map = new HashMap();
        map.put("merchant_id", merchant_id);
        map.put("order_id", order_id);
        map.put("amount", amount);
        map.put("pay_type",pay_type);
        map.put("notify_url",notify_url);
        map.put("user_id",user_id);
        map.put("user_ip",user_ip);
        String param = YiFu02Util.createParam(map);
        log.info("【易付2号加签前的参数："+param+"】");
        String s = YiFu02Util.md5(param + "key=" + key);
        map.put("sign",s);
        log.info("【易付2号请求前的参数："+map.toString()+"】");
        String post = HttpUtil.post(YiFu02Util.XUNFU_URL, map);
        log.info(post);
        JSONObject jsonObject = JSONUtil.parseObj(post);
        String code = jsonObject.getStr("code");
        if("0".equals(code)){
            String data = jsonObject.getStr("data");
            JSONObject datajson = JSONUtil.parseObj(data);
            String pay_url = datajson.getStr("pay_url");
            log.info("【易付02支付URL:"+pay_url+"】");
            return  pay_url;
        } else {
             orderEr(dealOrderApp, "易付02号支付失败原因未知");
             return "";
        }
    }
}
