package alipay.manage.api.channel.deal.shenfu;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 申付 代付回调处理类
 */
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class HuiTongFuDpayNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();

    @RequestMapping("/huitongfuwit-noyfit")
    public String notify(HttpServletRequest req, HttpServletResponse res, @RequestBody String json) {
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为：" + clientIP + "】");
        Map mapIp = PayUtil.ipMap;
        Object object = mapIp.get(clientIP);
        if (ObjectUtil.isNull(object)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + mapIp.toString() + "】");
            log.info("【当前回调ip不匹配】");
            return "ip errer";
        }
        /**
         * 		oid_partner		String(18)	√	商家号 商户签约时，分配给商家的唯一身份标识 例如：201411171645530813
         * 		money_order		Number(13,2)	√	参数名称：客户实际支付金额与币种对应
         * 		no_order		String(32)	√	商家订单号 商家网站生成的订单号，由商户保证其唯一性，由字母、数字组成。
         * 		oid_paybill		String(32)	√	平台支付交易号
         * 		sign			String	√	参数名称：签名数据 该字段不参与签名，值如何获取，请参考提供的示例代码。
         * 		time_order		Date	√	请求时间，时间格式：YYYYMMDDH24MISS 14 位数 字，精确到秒
         * 		result_pay		String(32)	√	SUCCESS 付款成功 PROCESSING 付款处理中 CANCEL 退款 FAILURE 失败
         * 		sign_type		String(10)	√	参数名称：签名方式  2.取值为：MD5
         */
        log.info("【汇通付代付回调数据为：" + json + "】");
        JSONObject parseObj = JSONUtil.parseObj(json);
        String oid_partner = parseObj.getStr("oid_partner");
        String money_order = parseObj.getStr("money_order");
        String no_order = parseObj.getStr("no_order");
        String oid_paybill = parseObj.getStr("oid_paybill");
        String sign = parseObj.getStr("sign");
        String time_order = parseObj.getStr("time_order");
        String result_pay = parseObj.getStr("result_pay");
        String sign_type = parseObj.getStr("sign_type");
        Map<String, Object> map = new HashMap<>();
        map.put("oid_partner", oid_partner);
        map.put("money_order", money_order);
        map.put("no_order", no_order);
        map.put("oid_paybill", oid_paybill);
        map.put("time_order", time_order);
        map.put("result_pay", result_pay);
        map.put("sign_type", sign_type);
        String createParam = PayUtil.createParam(map);
        log.info("【汇通付代付签名前参数：" + createParam + "】");
        String dpAyChannelKey = getDPAyChannelKey(no_order);
        String md5 = PayUtil.md5(createParam + dpAyChannelKey);
        if (sign.equals(md5)) {
            if ("SUCCESS".equals(result_pay)) {
                Result result = witNotfy(no_order, clientIP);
                if (result.isSuccess()) {
                    return "success";
                }
            } else if ("FAILURE".equals(result_pay)) {
                witNotfyEr(no_order, clientIP, "代付失败");
                return "success";
            }
        } else {
            return "error";
        }
        return "end errer";
    }
}
