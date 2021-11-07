package alipay.manage.api.channel.deal.xinshen;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class XinShenNotify extends NotfiyChannel {
    /**
     * app_id	商户号	是	平台分配商户号
     * status	状态	是	1为成功，其他不成功
     * money	金额	是	实际支付的金额
     * order_no	商户单号	是	商户订单号
     * trade_no	平台单号	是	平台生成的订单号
     * notify_url	回调地址	否	商户回调地址
     * sign
     */
    private static final Log log = LogFactory.get();
    @PostMapping("/xinshen-huafei-notify")
    public void notify(HttpServletRequest req, HttpServletResponse res) throws IOException {
        log.info("【收到新盛回调，当前回调ip为："+HttpUtil.getClientIP(req)+"】");
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为：" + clientIP + "】");
        Map mapip = new HashMap();
        mapip.put("34.96.180.78","34.96.180.78");
        mapip.put("35.220.182.58","35.220.182.58");
        if(StrUtil.isEmpty(clientIP) || ObjectUtil.isNull(mapip.get(clientIP)) ){
            log.info("【当前回调ip有误】");
            return;
        }
        String app_id = req.getParameter("app_id");
        String status = req.getParameter("status");
        String money = req.getParameter("money");
        String order_no = req.getParameter("order_no");
        String trade_no = req.getParameter("trade_no");
        Map map = new HashMap();
        map.put("app_id",app_id);
        map.put("status",status);
        map.put("money",money);
        map.put("order_no",order_no);
        map.put("trade_no",trade_no);
        String channelKey = super.getChannelKey(order_no);
        String createParam = PayUtil.createParam(map);
        log.info("【新盛回调签名钱参数为："+createParam+"】");
        String md5 = PayUtil.md5(createParam + "&key=" + channelKey);
        String sign = req.getParameter("sign");
        if (md5.equals(sign)) {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名成功】");
        } else {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名失败】");
            return ;
        }
        //1为成功，其他不成功    status
        if ("1".equals(status)) {
            Result dealpayNotfiy = dealpayNotfiy(order_no, clientIP);
            if(dealpayNotfiy.isSuccess()){
                log.info("【 新盛回调成功，当前订单号："+order_no+"】");
                return;
            }
        }
    }
}
