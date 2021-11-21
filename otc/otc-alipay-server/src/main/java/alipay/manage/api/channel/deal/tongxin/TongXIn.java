package alipay.manage.api.channel.deal.tongxin;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



@Component("TongXIn")
public class TongXIn extends PayOrderService {
    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;

    private static String getNowDateStr() {
        return DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMAT);
    }
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel)  {
        log.info("【进入刚盾支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
        String orderId = create(dealOrderApp, channel);
        UserInfo userInfo = userInfoServiceImpl.findDealUrl(dealOrderApp.getOrderAccount());
        if (StrUtil.isBlank(userInfo.getDealUrl())) {
            orderEr(dealOrderApp, "当前商户交易url未设置");
            return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
        }
        String payInfo = "";
        if(dealOrderApp.getDealDescribe().contains("付款人")){
            payInfo = dealOrderApp.getDealDescribe();
        }
        Result result = createOrder(
                userInfo.getDealUrl() +
                        PayApiConstant.Notfiy.NOTFIY_API_WAI + "/tongxing-huafei-notify",
                dealOrderApp.getOrderAmount(),
                orderId,
                getChannelInfo(channel, dealOrderApp.getRetain1()),dealOrderApp,payInfo
        );
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            orderEr(dealOrderApp, "错误消息：" + result.getMessage());
            return result;
        }
    }

    private Result createOrder(String s, BigDecimal orderAmount, String orderId,
                               ChannelInfo channelInfo, DealOrderApp dealOrderApp,
                               String payInfo) {

        String nodnotifyurl = s;
        /**
         *  机构号	            agtorg	            是	            String(30)	            机构号（平台提供）
         *  商户号	            mercid	            是	            String(30)	            商户号（平台提供）
         *  订单号	            ordernumber	        是	            String(30)	            机构号+不重复订单号
         *  后台回调地址	        nodnotifyurl	    是	            String(128)
         * 订单说明	            body	            是	            String(50)	订单说明
         * 业务类型	            busytyp 	        是	            String(12)	PHONEPAY
         * 交易金额	            amount	            是	            String(12)	单位:分 不包含小数点
         * 签名	                sign	            是	            String(32)	MD5签名如下
         */
        String agtorg = "JX199";
        String mercid = channelInfo.getChannelAppId();
        String key = channelInfo.getChannelPassword();
        String ordernumber = orderId;
        String body = "body";
        String busytyp = channelInfo.getChannelType();
        int v = orderAmount.intValue();
        v*=100;
        String amount =v+"";
        String param = "agtorg="+agtorg+"&amount="+amount+"&body="+body+"&busytyp="+busytyp+"&mercid="+mercid+"&nodnotifyurl="+nodnotifyurl+"&ordernumber="+ orderId +"&key="+key+"";
        log.info(param);

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
        log.info("请求参数："+map.toString());
        String post = HttpUtil.post(channelInfo.getDealurl(), map);
        log.info("返回参数："+post);
        JSONObject jsonObject = JSONUtil.parseObj(post);
        Object code = jsonObject.get("RSPCOD");
//{"RSPCOD":"000000","RSPMSG":"获取成功","DATA":"http://49.235.197.37/sakura/userpays/jump10.html?orderno=20211119095849866110&amount=100&p=1"}
        if ("000000".equals(code)) {
            return Result.buildSuccessResult(jsonObject.getStr("DATA"));
        } else {
            return Result.buildFailMessage(jsonObject.getStr("msg"));
        }
    }






    }

