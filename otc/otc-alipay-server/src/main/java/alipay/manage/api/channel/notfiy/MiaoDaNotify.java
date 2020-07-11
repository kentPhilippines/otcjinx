package alipay.manage.api.channel.notfiy;

import alipay.manage.api.channel.util.miaoda.MiaoDaUtil;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class MiaoDaNotify extends NotfiyChannel {
    @PostMapping("/miaoda-notfiy")
    public String notify(HttpServletRequest req, HttpServletResponse res) {
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为："+clientIP+"】");
        Map map = new HashMap();
        map.put("47.244.192.185", "47.244.192.185");
        map.put("47.91.198.215", "47.91.198.215");
        Object object = map.get(clientIP);
        if(ObjectUtil.isNull(object)) {
            log.info("【当前回调ip为："+clientIP+"，固定IP登记为："+"47.91.198.215"+"，47.244.192.185"+"】");
            log.info("【当前回调ip不匹配】");
            return "ip is error";
        }
        map = null;
        /**
         * 【秒达支付 参数：
         * {
         * psn=null,
         * type=ywsell,
         * body=null,
         * mno=10334,
         * trade_sn=10334070610353079572,
         * money=200.00,
         * out_order_sn=C1594002928872906919,
         * accid=13336,
         * ctime=1594002930,
         * time=1594002929,
         * attach=C1594002928872906919,
         * ptime=1594003097,
         * order_sn=C1594002928872906919,
         * status=2,
         * notice=0,
         * timestamp=1594003165
         * }
         * 】
         */
        String mno = req.getParameter("mno");
        String type = req.getParameter("type");
        String accid = req.getParameter("accid");
        String time = req.getParameter("time");
        String status = req.getParameter("status");
        String ctime = req.getParameter("ctime");
        String ptime = req.getParameter("ptime");
        String trade_sn = req.getParameter("trade_sn");
        String order_sn = req.getParameter("order_sn");
        String out_order_sn = req.getParameter("out_order_sn");
        String money = req.getParameter("money");
        String remark = req.getParameter("remark");
        String attach = req.getParameter("attach");
        String notice = req.getParameter("notice");
        String timestamp = req.getParameter("timestamp");
        String sign = req.getParameter("sign");
        Map<String,Object> mapParam = new HashMap<String,Object>();
        mapParam.put("type",type);
        mapParam.put("accid",accid);
        mapParam.put("time",time);
        mapParam.put("status",status);
        mapParam.put("ctime",ctime);
        mapParam.put("ptime",ptime);
        mapParam.put("trade_sn",trade_sn);
        mapParam.put("order_sn",order_sn);
        mapParam.put("out_order_sn",out_order_sn);
        mapParam.put("money",money);
        mapParam.put("attach",attach);
        mapParam.put("notice",notice);
        mapParam.put("timestamp",timestamp);
        log.info("【秒达支付 参数："+mapParam.toString()+"】");
        String param = MiaoDaUtil.createParam(mapParam);
        log.info("【秒达支付排序后参数："+param+"】");
        String s = MiaoDaUtil.APPID + "&" + param + MiaoDaUtil.KEY;
        log.info("【秒达支付签名前参数："+s+"】");
        String s1 = MiaoDaUtil.md5(s);
        log.info("【秒达支付签名："+s1+"】");
        if (sign.equals(s1)){
            log.info("【验签成功】");
        }else{
            log.info("【验签失败，我方系统签名未："+s1+"，对方系统签名为："+sign+"】");
            return "sign is error";
        }
        if("2".equals(status)){
            Result dealpayNotfiy = dealpayNotfiy(order_sn, clientIP,"秒达支付回调");
            if(dealpayNotfiy.isSuccess())
                return "success";
        }
        return "";
    }

    /**
     * 	时间戳	int	用于增加签名的随机性，也可与本地时间进行合法校验。
     * 	签名	string	数据签名，用于验证数据有合法性。
     */
}
