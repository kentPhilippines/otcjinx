package alipay.manage.api.channel.notfiy;

import alipay.manage.api.channel.util.yifu.YiFuUtil;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.http.HttpUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class YiFuPayNotify  extends NotfiyChannel {
    @RequestMapping("/YiFu-notfiy")
    public String notify(HttpServletRequest request) {
        log.info("【收到YiFu回调】");
        /**
         * trade_no	是	系统订单号
         * out_trade_no	是	外部订单号
         * money	是	支付金额
         * pay_at	是	支付时间
         * code	是	订单状态："success":成功,其他则失败
         * app_id	是	APP_ID
         * remark	否	充值备注，不参与加密！！！！
         * sign	否	签名,详见下方签名方式
         */
        log.info("【收到UzPay回调】");
        String clientIP = HttpUtil.getClientIP(request);
        if (!clientIP.equals("13.250.191.201")) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + "13.250.191.201" + "】");
            log.info("【当前回调ip不匹配】");
            return "ip errer";
        }
        String trade_no = request.getParameter("trade_no");
        String out_trade_no = request.getParameter("out_trade_no");
        String money = request.getParameter("money");
        String pay_at = request.getParameter("pay_at");
        String app_id = request.getParameter("app_id");
        String code = request.getParameter("code");
        String sign = request.getParameter("sign");
        Map map = new HashMap();
        map.put("trade_no", trade_no);
        map.put("out_trade_no", out_trade_no);
        map.put("money", money);
        map.put("pay_at", pay_at);
        map.put("app_id", app_id);
        map.put("code", code);
        String createParam = YiFuUtil.createParam(map);
        //app_id=-YbBmS2aFEfgBx4mnXI&money=1000&out_trade_no=C1593861094418016017&pay_at=2020-07-04 19:11:36&
        log.info("【易付签名前参数：" + createParam + "】");
        String md5 = YiFuUtil.md5(createParam + "key=" + YiFuUtil.KEY);
        md5 = md5.toUpperCase();
        if (md5.equals(sign)) {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名成功】");
        } else {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名失败】");
            return "sgin is error";
        }
        if (!"success".equals(code.toString())) {
            log.info("【易付回调状态出错，当前回调状态为：" + code + "，我方需要状态为：" + "success" + "】");
            return "错误 status is error";
        }
        Result dealpayNotfiy = dealpayNotfiy(out_trade_no, clientIP, "yifu回调订单成功");
        if (dealpayNotfiy.isSuccess()) {
            log.info("【订单回调修改成功，订单号为 ：" + out_trade_no + " 】");
            return "success";
        }
        return "错误 error";
    }
}
