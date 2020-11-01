package alipay.manage.api.channel.deal.shenfu;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ShenfuHuafeiNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @RequestMapping("/shenfu-huafei-notify")
    public String notify(HttpServletRequest req, HttpServletResponse res) throws IOException {
        log.info("【收到申付话费支付成功回调】");

        /**
         *
         参数	说明
         p1_merchantno	商户号: 请访问商户后台来获取您的商户号。
         p2_amount	支付金额: 以元为单位，精确到小数点后 2 位。如 15.00 及 15 都是合法的参数值。
         p3_orderno	订单号: 唯一标识您的支付平台的一笔订单。
         p4_status	订单状态:
         1: 未支付
         2: 已支付
         3: 支付失败
         p5_producttype	支付产品类型编码: 请参阅支付产品类型编码表。
         p6_requesttime	支付发起时间: 14 字节长的数字串，格式布局如 yyyyMMddHHmmss. (如 20060102150405 表示 2006年1月2日下午3点04分05秒).
         p7_goodsname	商品名称。
         p8_tradetime	交易时间:14 字节长的数字串，格式布局如 yyyyMMddHHmmss. (如 20060102150405 表示 2006年1月2日下午3点04分05秒).
         p9_porderno	平台交易流水号: 绅付2支付平台为您的订单生成的全局唯一的交易流水号。
         sign	MD5 签名: HEX 大写, 32 字节。
         */
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为：" + clientIP + "】");
        Map map = PayUtil.ipMap;
        Object object = map.get(clientIP);
        if (ObjectUtil.isNull(object)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + map.toString() + "】");
            log.info("【当前回调ip不匹配】");
            return "ip is error";
        }
        String p1_merchantno = req.getParameter("p1_merchantno");
        String p2_amount = req.getParameter("p2_amount");
        String p3_orderno = req.getParameter("p3_orderno");
        String p4_status = req.getParameter("p4_status");
        String p5_producttype = req.getParameter("p5_producttype");
        String p6_requesttime = req.getParameter("p6_requesttime");
        String p7_goodsname = req.getParameter("p7_goodsname");
        String p8_tradetime = req.getParameter("p8_tradetime");
        String p9_porderno = req.getParameter("p9_porderno");
        String sign = req.getParameter("sign");
        Map mapp = new HashMap();

        mapp.put("p1_merchantno", p1_merchantno);
        mapp.put("p2_amount", p2_amount);
        mapp.put("p3_orderno", p3_orderno);
        mapp.put("p4_status", p4_status);
        mapp.put("p5_producttype", p5_producttype);
        mapp.put("p6_requesttime", p6_requesttime);
        mapp.put("p7_goodsname", p7_goodsname);
        mapp.put("p8_tradetime", p8_tradetime);
        mapp.put("p9_porderno", p9_porderno);
        String channelKey = super.getChannelKey(p3_orderno);
        log.info("【绅付话费加签参数：" + mapp + "】");
        String createParam = PayUtil.createParam(mapp);
        String md5 = PayUtil.md5(createParam + "&key=" + channelKey);
        if (md5.toUpperCase().equals(sign)) {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名成功】");
        } else {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名失败】");
            return "sign is error";
        }
        if (p4_status.equals("2")) {
            Result dealpayNotfiy = dealpayNotfiy(p3_orderno, clientIP, "申付话费回调成功");
            if (dealpayNotfiy.isSuccess()) {
                return "SUCCESS";
            }
        }
        return "";
    }
}
