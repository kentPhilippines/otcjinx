package alipay.manage.api.channel.deal;

import alipay.manage.api.channel.util.miaoda.MiaoDaUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.api.feign.ConfigServiceClient;
import alipay.manage.bean.DealOrderApp;
import cn.hutool.core.util.ObjectUtil;
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

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component("MiaoDaAlipayH5Pay")
public class MiaoDaAlipayH5Pay extends PayOrderService {
    @Autowired ConfigServiceClient configServiceClientImpl;
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channelId) {
        log.info("【进入秒达支付宝H5支付】");
        String orderId = create(dealOrderApp, channelId);
        if(StrUtil.isNotBlank(orderId)) {
            log.info("【本地订单创建成功，开始请求远程三方支付】");
            Result config = configServiceClientImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.SERVER_IP);
            log.info("【回调地址ip为："+config.toString()+"】" );
            String url = createOrder(dealOrderApp,config.getResult()+ PayApiConstant.Notfiy.NOTFIY_API_WAI+"/miaoda-notfiy", dealOrderApp.getOrderAmount(),orderId);
            if(StrUtil.isBlank(url)) {
                boolean orderEr = orderEr(dealOrderApp,"暂无支付宝H5通道");
                if(orderEr)
                    return Result.buildFailMessage("支付失败");
            }else {
                return Result.buildSuccessResultCode("支付处理中", url,1);
            }
        }
        return Result.buildFailMessage("暂无通道");
    }

    private String createOrder(DealOrderApp dealOrderApp, String notify, BigDecimal orderAmount, String orderId) {
        String mno = MiaoDaUtil.APPID;
        String key = MiaoDaUtil.KEY;
        String url = MiaoDaUtil.URL;
        String type = "mdjhpay";
        String type_add = "alipay";
        String order_sn = orderId;
        String out_order_sn = order_sn;
        String money = orderAmount.intValue()+".00";
        String ouser = dealOrderApp.getOrderAccount();
        String uip = dealOrderApp.getOrderIp();
        String time = System.currentTimeMillis()/1000+"";
        String body = "";
        String attach = order_sn;
        String notify_url = notify;
        String back_url = "";
        String qrtype = "";
        Map map = new HashMap();
        map.put("type",type);
        map.put("order_sn",order_sn);
        map.put("money",money);
        map.put("uip",uip);
        map.put("time",time);
        map.put("type_add",type_add);
        map.put("ouser",ouser);
        map.put("out_order_sn",out_order_sn);
        map.put("attach",attach);
        map.put("notify_url",notify_url);
        String param = MiaoDaUtil.createParam(map);
        log.info("【秒达支付排序后参数："+param+"】");
        String s = mno + "&" + param + key;
        log.info("【秒达支付签名前参数："+s+"】");
        String s1 = MiaoDaUtil.md5(s);
        log.info("【秒达支付签名："+s1+"】");
        JSONObject jsonObject = JSONUtil.parseFromMap(map);
        String s2 = jsonObject.toString();
        url = url+"?mno="+mno+"&type=json&sign="+s1+"&data="+s2;
        log.info("【秒达支付完整URL:"+url+"】");
        String s3 = HttpUtil.get(url);
        log.info("【秒达支付请求返回："+s3+"】");
        JSONObject jsonObject1 = JSONUtil.parseObj(s3);
        if("100".equals(jsonObject1.getStr("code"))){
            JSONObject data = JSONUtil.parseObj(jsonObject1.getStr("data"));
            log.info("【秒达支付返回data数据转换："+data+"】");
            String jump_uri = data.getStr("jump_uri");
            log.info("【秒达支付支付请求url："+jump_uri+"】");
            return jump_uri;
        }
        return "";
    }
}
