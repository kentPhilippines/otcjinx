package alipay.manage.api.channel.deal.qingtian;

import alipay.manage.api.channel.util.qiangui.MD5;
import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.ChannelLocalUtil;
import alipay.manage.api.config.ChannelUtil;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealWit;
import alipay.manage.util.CheckUtils;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;
import otc.result.Result;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class QingTianUtil extends ChannelUtil implements ChannelLocalUtil {
    private static SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");


    /**
     *  mch_id	            string	否	申请的商户账号，不是id
     *  ptype	            int	    否
     *  order_sn	        string	否	32个字符以内
     *  money	            float	否	金额（元最多小数点后两位），可以增加一定随机金额
     *  format	            string	否	url
     *  notify_url	        string	否	用户支付成功后回调
     *  time	            int	    否	系统当前时间戳 UTC 秒
     *  sign	            string	否	见签名算法
     * @param order
     * @param channelInfo
     * @return
     */
    @Override
    public Result channelDealPush(DealOrder order, ChannelInfo channelInfo) {
        String mch_id = channelInfo.getChannelAppId();
        String order_sn = order.getOrderId();
        String notify_url = order.getNotify();
        String money = order.getDealAmount()+"";
        String ptype = channelInfo.getChannelType();
        String time = System.currentTimeMillis()/1000 + "";
        String format =  "url";
        Map map = new HashMap();
        map.put("mch_id", mch_id);
        map.put("ptype", ptype);
        map.put("order_sn", order_sn);
        map.put("money", money);
        map.put("format", format);
        map.put("time", time);
        map.put("notify_url", notify_url);
        String param = createParam(map);
        log.info(" 签名数据为：" + param +"&key="+ channelInfo.getChannelPassword());
        String md5 = MD5.MD5Encode(param +"&key="+ channelInfo.getChannelPassword());
        String sign = md5;
        map.put("sign", sign);
        log.info("请求前参数为: " + JSONUtil.parse(map).toString());
        logRequestDeal(this.getClass().getName(), JSONUtil.parse(map).toString(), order.getOrderId(), order.getExternalOrderId());
        try {
            String post = HttpUtil.post(channelInfo.getDealurl() , map );
            log.info("响应数据为：" + post);
            logResponseDeal(this.getClass().getName(), post, order.getOrderId(), order.getExternalOrderId());
            //：{"success":true,"message":"支付处理中","result":{"sussess":true,"cod":1,"openType":1,"returnUrl":"http://20.188.122.189:8122/api/Pay/Cashier/8111c0c8479d4d418d9062293d82dae3","payInfo":"","payInfo2":""},"code":1}
            JSONObject jsonObject = JSONUtil.parseObj(post);
            String str = jsonObject.getStr("success");
            if ("true".equals(str)) {
                JSONObject result = jsonObject.getJSONObject("result");
                String str1 = result.getStr("returnUrl");
                return Result.buildSuccessResult(str1);
            } else {
                orderEr(order, post);
            }
        } catch (Throwable e) {
           log.error(" v2qs 渠道异常 ",e);
        }
        return Result.buildFail();
    }

    @Override
    public Result channelWitPush(DealWit order, ChannelInfo channelInfo) {












        return null;
    }


    public Result witNotify(Map<String, Object> map) {






        return Result.buildFail();
    }

    public Result dealNotify(Map map) {
        log.info("【收到V2qsNotify手动支付成功回调】");
        String sign = map.get("sign")+"";
        String apporderid = map.get("apporderid")+"";
        map.remove("sign");
        map.put("statusdesc", "成功");
        String channelKey = super.getChannelKey(apporderid);
        String md5 = CheckUtils.getSign(map, channelKey);
        if (md5.equals(sign)) {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名成功】");
        } else {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名失败】");
          return Result.buildFailMessage("签名失败");
        }
        Result dealpayNotfiy =              dealNotfiy(apporderid, "三方系统回调成功");

        if (dealpayNotfiy.isSuccess()) {
            return dealpayNotfiy;
        }
        return Result.buildFail();
    }
    public void findBalance(String channelId, ChannelInfo channelInfo) {
        log.info("【调用查询渠道余额方法，当前渠道信息为："+channelInfo.toString()+"】");

    }


    public static void main(String[] args) {
        DealOrder order; ChannelInfo channelInfo;
        order = new DealOrder();
        order.setDealAmount(new BigDecimal(301));
        order.setOrderId(System.currentTimeMillis()+"");
        order.setNotify("http://www.ba222idu.com");
        channelInfo = new ChannelInfo();
        channelInfo.setChannelType("3");
        channelInfo.setChannelPassword("17e293b28de3c3ec1982823fc02af55bb5fa210a");
        channelInfo.setDealurl("https://vip2.asfd.xyz?c=Pay&");
        channelInfo.setChannelAppId("tianxuan");
        QingTianUtil util = new QingTianUtil();
        util.channelDealPush(order,channelInfo);
    }
}
