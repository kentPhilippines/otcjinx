package alipay.manage.api.channel.notfiy;

import alipay.manage.api.channel.util.shenlaifu.ShenlaifuUtil;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
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
public class ShenLaiFuNotify  extends NotfiyChannel {
    private static final Log log = LogFactory.get();

    /**
     * 商户账号	            memberId	        30	        商户在支付平台的唯一标识
     * 商户订单号	        orderId	            32	        商户系统产生的唯一订单号
     * 订单金额	            amount	            2	        以“元”为单位，仅允许两位小数，必须大于零
     * 交易日期	            dateTime	        14	        商户系统生成的订单日期格式：YYYYMMDDHHMMSS
     * 交易结果	            returnCode	        2	        2：表示交易成功
     * 扩展字段	            reserved	        240	        英文或中文字符串,支付完成后，按照原样返回给商户
     * 签名档	            sign	            1024	    必输，商户对交易数据的签名，（最后转出大写）签名方式参照签名档例子
     * @param req
     * @param res
     * @return
     * @throws IOException
     */
    @RequestMapping("/shenlaifu-notfiy")
    public String notify(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为："+clientIP+"】");
        Map map = new HashMap();
        map.put("35.229.152.179", "35.229.152.179");
        map.put("35.229.223.126", "35.229.223.126");
        Object object = map.get(clientIP);
        if(ObjectUtil.isNull(object)) {
            log.info("【当前回调ip为："+clientIP+"，固定IP登记为："+"35.229.152.179"+"，35.229.223.126】");
            log.info("【当前回调ip不匹配】");
            return "ip is error";
        }
        String memberId = req.getParameter("memberId");
        String orderId = req.getParameter("orderId");
        String amount = req.getParameter("amount");
        String dateTime =req.getParameter("dateTime");
        String returnCode = req.getParameter("returnCode");
        String reserved = req.getParameter("reserved");
        String sign = req.getParameter("sign");
        log.info("【神来付参数情况：memberId="+memberId+"，orderId="+orderId+"，amount="+amount+"，dateTime="+dateTime+"，returnCode="+returnCode+"】");
        if(StrUtil.isBlank(memberId)
                ||StrUtil.isBlank(orderId) ||StrUtil.isBlank(sign)
                ||StrUtil.isBlank(amount) ||StrUtil.isBlank(dateTime) ||StrUtil.isBlank(returnCode)) {
            log.info("【神来付必传参数为空】");
            return "param is null";
        }
        String sgin = "amount^"+amount+"&dateTime^"+dateTime+"&memberId^"+memberId+"&orderId^"+orderId
                +"&returnCode^"+returnCode+"&key="+ ShenlaifuUtil.KEY;
        log.info("【神来付加签参数："+sgin+"】");
        String md5Sign = ShenlaifuUtil.md5(sgin).toUpperCase();
        if(md5Sign.equals(sign)) {
            log.info("【当前支付成功回调签名参数："+sign+"，当前我方验证签名结果："+md5Sign+"】");
            log.info("【签名成功】");
        } else {
            log.info("【当前支付成功回调签名参数："+sign+"，当前我方验证签名结果："+md5Sign+"】");
            log.info("【签名失败】");
            return  "sgin is error";
        }
        Result dealpayNotfiy = dealpayNotfiy(orderId, clientIP);
        if(dealpayNotfiy.isSuccess()) {
            return "SUCCESS";
        }
        return "error";
    }
}
