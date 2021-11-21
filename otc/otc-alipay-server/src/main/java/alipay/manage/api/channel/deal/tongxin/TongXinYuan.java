package alipay.manage.api.channel.deal.tongxin;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.service.UserInfoService;
import alipay.manage.util.CheckUtils;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class TongXinYuan extends NotfiyChannel {

    /**
     * 签名	        sign	        是	    String(32)	    签名
     * 信息码	    RSPCOD	        是	    String(30)	    000000:回调成功
     * 信息描述	    RSPMSG	        是	    String(100)	    回调信息
     * 回调信息	    resultmsg	    是	    String(50)	    成功或失败或其它
     * 时间戳	    timestamp	    是	    String(16)	    时间戳
     * 订单号	    orderno	        是	    String(50)
     * 交易金额	    txnamt	        是	    Sting(12)
     * 交易结果	    orderresult	    是	    String(1)	    2 成功0 失败 1受理中
     * 平台订单号	    porderno	    是	    String(40)	    平台订单号
     * 附加数据	    extraData	    否	    String(64)	    有特殊情况才会返回
     */

    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @RequestMapping("/tongxing-huafei-notify")
    public String notify(HttpServletRequest req, HttpServletResponse res,@RequestBody String json) throws IOException {
        log.info("【收到同心支付成功回调】");
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为：" + clientIP + "】");
        Map mapIp = PayUtil.ipMap;
        Object object = mapIp.get(clientIP);
        if (ObjectUtil.isNull(object)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + mapIp.toString() + "】");
            log.info("【当前回调ip不匹配】");
            return "ip errer";
        }
        log.info("【同心话费回调数据为：" + json.toString() + "】");
        HashMap<String, String> stringStringHashMap = HttpUtil.decodeParamMap(json, Charset.defaultCharset().toString());
        String sign = "";
        String RSPCOD = "";
        String RSPMSG = "";
        String resultmsg = "";
        String timestamp = "";
        String orderno ="";
        String txnamt = "";
        String orderresult = "";
        String porderno = "";
        String extraData = "";
        if(null != stringStringHashMap){
            log.info("同心话费回调参数："+stringStringHashMap.toString());
        }
        try {

            JSONObject parseObj = JSONUtil.parseObj(json);
             sign = parseObj.getStr("sign");
             RSPCOD = parseObj.getStr("RSPCOD");
             RSPMSG = parseObj.getStr("RSPMSG");
             resultmsg = parseObj.getStr("resultmsg");
             timestamp = parseObj.getStr("timestamp");
             orderno = parseObj.getStr("orderno");
             txnamt = parseObj.getStr("txnamt");
             orderresult = parseObj.getStr("orderresult");
             porderno = parseObj.getStr("porderno");
             extraData = parseObj.getStr("extraData");
        }catch (Exception c ){
            sign = stringStringHashMap.get("sign");
            RSPCOD = stringStringHashMap.get("RSPCOD");
            RSPMSG = stringStringHashMap.get("RSPMSG");
            resultmsg = stringStringHashMap.get("resultmsg");
            timestamp = stringStringHashMap.get("timestamp");
            orderno = stringStringHashMap.get("orderno");
            txnamt = stringStringHashMap.get("txnamt");
            orderresult = stringStringHashMap.get("orderresult");
            porderno = stringStringHashMap.get("porderno");
            extraData = stringStringHashMap.get("extraData");
        }
        String dpAyChannelKey = getDPAyChannelKey(orderno);
        Map<String,Object> query = new HashMap<>();
        String orderId = orderno;
        query.put("agtorg","JX199");
        query.put("mercid","484584045119506");
        query.put("ordernumber",orderId);
        String sg = "agtorg=JX199&mercid=484584045119506&ordernumber="+orderId+"&key="+dpAyChannelKey;
        query.put("sign",PayUtil.md5(sg).toUpperCase());
        String post = HttpUtil.post( "http://mng.yuegepay.com/709105.tran9",query);
        System.out.println(post);
        JSONObject jsonObject = JSONUtil.parseObj(post);
        String ordernumber = jsonObject.getStr("ORDERNUMBER");
        String paystatus = jsonObject.getStr("PAYSTATUS");
        if(ordernumber.equals(orderId) && "2".equals(paystatus)){
           log.info("支付成功");
            Result dealpayNotfiy = dealpayNotfiy(orderno, clientIP);
            if (dealpayNotfiy.isSuccess()) {
                return  "success";
            }
        }
        return "error";
    }

}
