package alipay.manage.api.channel.notfiy;

import alipay.manage.api.channel.util.longteng.LongTentUtil;
import alipay.manage.api.config.NotfiyChannel;
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
public class LongTengNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();

    @RequestMapping("/longteng-notfiy")
    public void notify(HttpServletRequest req, HttpServletResponse res) throws IOException {
        log.info("【收到龙腾支付回调请求】");
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为：" + clientIP + "】");
        if (!"34.92.37.87".equals(clientIP)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + "34.92.37.87" + "】");
            log.info("【当前回调ip不匹配】");
            return;
        }
        /**
         商户号				sid						M				商户ID
         商户订单号				out_trade_no			M				商户订单号
         平台单号				trade_no				M				龙腾支付统一单号
			订单交易金额			amount					M				保留2位小数，比如：500.00
			客户实付金额			pay_amount				M				保留2位小数，比如：500.00，实付请以此金额为准
			交易币种				currency				M				三位 ISO 货币代码	中国：CNY			越南：VND
			请求结果编码			code					M				1000表示支付成功，其余编码表示支付失败
			请求结果描述			msg						C				请求结果描述
			商户保留字段			attach					M				原样返回给通知接口
			签名信息				sign					M				签名信息
		 */
		String sid = req.getParameter("sid");
		String out_trade_no = req.getParameter("out_trade_no");
		String trade_no = req.getParameter("trade_no");
		String amount = req.getParameter("amount");
		String pay_amount = req.getParameter("pay_amount");
		String currency = req.getParameter("currency");
		String code = req.getParameter("code");
		String msg = req.getParameter("msg");
		String attach = req.getParameter("attach");
		String sign = req.getParameter("sign");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("sid", sid);
		map.put("out_trade_no", out_trade_no);
		map.put("trade_no", trade_no);
		map.put("amount", amount);
		map.put("pay_amount", pay_amount);
		map.put("currency", currency);
		map.put("code", code);
		map.put("msg", msg);
		map.put("attach", attach);
        String createParam = LongTentUtil.createParam(map);
        String upperCase = LongTentUtil.md5(createParam + LongTentUtil.KEY).toUpperCase();
        if (sign.equals(upperCase)) {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + upperCase + "】");
            log.info("【签名成功】");
        } else {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + upperCase + "】");
            log.info("【签名失败】");
            return;
        }
        if ("1000".equals(code)) {
            Result dealpayNotfiy = dealpayNotfiy(out_trade_no, clientIP, "龙腾回调订单成功");
            if (dealpayNotfiy.isSuccess()) {
                log.info("【订单回调修改成功，订单号为 ：" + out_trade_no + " 】");
                res.getWriter().write("success");
            }
        }
    }
}
