package alipay.manage.api.channel.deal.ePay;

import alipay.manage.api.channel.util.yifu.YiFuUtil;
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

@Component("YiFuWangBankPay")
public class YiFuWangBankPay extends PayOrderService {
   //private static final Log log = LogFactory.get();
    @Autowired ConfigServiceClient configServiceClientImpl;
    @Autowired OrderService orderServiceImpl;
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channelId) {
            log.info("【进入易付卡转卡】");
            String orderId = create(dealOrderApp, channelId);
            if(StrUtil.isNotBlank(orderId)) {
                log.info("【本地订单创建成功，开始请求远程三方支付】");
                Result config = configServiceClientImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.SERVER_IP);
                log.info("【回调地址ip为："+config.toString()+"】" );
                log.info("【本地订单创建成功，开始请求远程三方支付】");
                String url = createOrder(dealOrderApp,config.getResult()+PayApiConstant.Notfiy.NOTFIY_API_WAI+"/YiFu-notfiy", dealOrderApp.getOrderAmount(),orderId);
                if (StrUtil.isBlank(url))
                    return Result.buildFailMessage("支付失败");
                else
                    return Result.buildSuccessResultCode("支付处理中", url, 1);
                }
            return Result.buildFailMessage("支付失败");
        }

    private String createOrder(DealOrderApp dealOrderApp,String notify, BigDecimal orderAmount, String orderId) {
            String callback_url = notify;
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("app_id", YiFuUtil.APPID);
            map.put("channel", YiFuUtil.CHANNEL_BANK);
            map.put("out_trade_no", orderId);
            map.put("money", orderAmount.intValue()+".00");
            map.put("callback_url", callback_url);
            String createParam = YiFuUtil.createParam(map);
            log.info("【易付签名前参数："+createParam+"】");
            String md5 = YiFuUtil.md5(createParam + "key="+YiFuUtil.KEY);
            String sign = md5.toUpperCase();
            map.put("sign", sign);
            log.info("【请求Yifu参数："+map.toString()+"】");
            //{"code":200,"msg":"ok","data":{"pay_url":"http://kpay8494.168yuju.cn/pay/gateway/order?c=22&o=2020070411330189354","money":"100"}}
            //{"code":419,"msg":"签名不正确","data":[]}
            String post = HttpUtil.post(YiFuUtil.URL, map);
        JSONObject jsonObject = JSONUtil.parseObj(post);
        String code = jsonObject.getStr("code");
        if(StrUtil.isNotBlank(code)){
            if(!"200".equals(code.toString())){
                orderEr(dealOrderApp,jsonObject.getStr("msg").toString());
                return "";
            }
            if("200".equals(code.toString())){
                String data = jsonObject.getStr("data");
                JSONObject dataJson = JSONUtil.parseObj(data);
                log.info("【获取易付返回参数,解析data结果："+data+"】");
                String pay_url = dataJson.getStr("pay_url");
                return pay_url;
            }
        }
        return "";
    }
}
