package alipay.manage.api.channel.deal;

import alipay.manage.api.channel.util.baG.BaGPayUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.api.feign.ConfigServiceClient;
import alipay.manage.bean.DealOrderApp;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.bean.config.ConfigFile;
import otc.common.PayApiConstant;
import otc.result.Result;
import otc.util.MapUtil;
import cn.hutool.http.HttpUtil;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Struct;
import java.util.LinkedHashMap;
import java.util.Map;

@Component("BaGPayToBank")
public class BaGPay extends PayOrderService {
    private static final Log log = LogFactory.get();
    @Autowired
    ConfigServiceClient configServiceClientImpl;
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channelId) throws Exception {
        log.info("【进入8Gpay支付】");
        String create = create(dealOrderApp, channelId);
        if(StrUtil.isNotBlank(create)) {
            log.info("【本地订单创建成功，开始请求远程三方支付】");
            Result config = configServiceClientImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.SERVER_IP);
            log.info("【回调地址ip为："+config.toString()+"】" );
            String url = createOrder(dealOrderApp,config.getResult()+ PayApiConstant.Notfiy.NOTFIY_API_WAI+"/baG-notfiy", dealOrderApp.getOrderAmount(),create);
            if(StrUtil.isBlank(url))
                return  Result.buildFailMessage("支付错误");
            else
                return Result.buildSuccessResultCode("支付处理中", url,1);
        }
        return  Result.buildFailMessage("支付错误");
    }

    private String createOrder(DealOrderApp dealOrderApp, String notify, BigDecimal orderAmount, String orderId) throws Exception {
        String appid = BaGPayUtil.APPID;
        String orderI = orderId;
        String amount = orderAmount.toString();
        String paytype = BaGPayUtil.PAY_TYPE;
        String PRIVATE_KEY= BaGPayUtil.PRIVATE_KEY;
        Map<String, Object> paramsMap = new LinkedHashMap<String, Object>();
        paramsMap.put("merchant_id",appid);
        paramsMap.put("order_no", orderI);
        paramsMap.put("amount", amount);
        paramsMap.put("method", paytype);
        paramsMap.put("notify_url", HttpUtil.encode(notify, "UTF-8"));
        String back = "";
        if(StrUtil.isBlank(dealOrderApp.getBack()))
            back = notify;
        else
            back = dealOrderApp.getBack();
        paramsMap.put("return_url",  HttpUtil.encode(back, "UTF-8"));
        paramsMap.put("version", "2.0");
        String param = MapUtil.createParam(paramsMap);
        //   String paramsString = getURL(paramsMap);
        log.info("【8G付加密验签参数为：" +param+"】");
        String sign =  BaGPayUtil.sign(param.getBytes("UTF-8"),PRIVATE_KEY);
        log.info("【8G付签名数据为sign：" +sign+"】");
        JSONObject jsobObject = new JSONObject();
        jsobObject.append("merchant_id", appid);
        jsobObject.append("order_no", orderI);
        jsobObject.append("amount",amount);
        jsobObject.append("method", paytype);
        jsobObject.append("notify_url", HttpUtil.encode(notify, "UTF-8"));
        jsobObject.append("return_url", HttpUtil.encode(back, "UTF-8"));
        jsobObject.append("version", "2.0");
        jsobObject.append("sign", sign);
        String result2 = HttpUtil.post("https://www.yfyfyfypay.com/gateway/build",jsobObject.toString());
     //   {"errorCode":200,"message":"操作成功","data":{"payurl":"https:\/\/www.yfyfyfypay.com\/eGrPTxYn"}}
        log.info("【8G付支付返回结果为：" +result2+"】");
        JSONObject jsonObject = JSONUtil.parseObj(result2);
        String errorCode = jsonObject.getStr("errorCode");
        if("200".equals(errorCode)){
            String data = jsonObject.getStr("data");
            JSONObject jsonObject1 = JSONUtil.parseObj(data);
            String payurl = jsonObject1.getStr("payurl");
            log.info("【8G付支付连接：" +payurl+"】");
            return payurl;
        }else{
            //{"errorCode":400007,"message":"SIGN验签失败"}
            String message = jsonObject.getStr("message");
            orderEr(dealOrderApp,message);
        }
        return "";
    }
}
