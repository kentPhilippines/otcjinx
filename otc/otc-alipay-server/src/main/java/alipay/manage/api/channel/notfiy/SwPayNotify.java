package alipay.manage.api.channel.notfiy;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.channel.wit.SwPay;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.assertj.core.util.Lists;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 申付 代付回调处理类
 */
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class SwPayNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();

    @RequestMapping("/sw-noyfit")
    public String notify(HttpServletRequest req, HttpServletResponse res, @RequestBody Map<String, String> params) {
        log.info("【sw付代付回调数据为：" + JSONUtil.toJsonStr(params) + "】");
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为：" + clientIP + "】");
        List list = Lists.newArrayList("20.2.73.178,35.200.83.160,35.79.217.112".split(","));
        if (!list.contains(clientIP)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + list.toString() + "】");
            log.info("【当前回调ip不匹配】");
            return "ip is error";
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
        String orderId =params.get("OrderNo");
        String sign =params.get("Sign");
        params.remove("Sign");
        String channelKey = getChannelKey(orderId+"");

        String createParam = createParam(params);
        log.info("【sw代付签名前参数：" + createParam + "】");
        String md5 = SwPay.md5(createParam +"&SecretKey="+channelKey);
        if (sign.equalsIgnoreCase(md5)) {
            if ("4".equals(params.get("Status"))) {
                Result result = witNotfy(orderId, clientIP);
                if (result.isSuccess()) {
                    return "ok";
                }
            } else    {
                witNotSuccess(orderId);
            }
        } else {
            return "error";
        }
        return "end errer";
    }

    public static String createParam(Map<String, String> map) {
        try {
            if (map == null || map.isEmpty()) {
                return null;
            }
            Object[] key = map.keySet().toArray();
            Arrays.sort(key);
            StringBuffer res = new StringBuffer(128);
            for (int i = 0; i < key.length; i++) {
                if (ObjectUtil.isNotNull(map.get(key[i]))) {
                    res.append(key[i] + "=" + map.get(key[i]) + "&");
                }
            }
            String rStr = res.substring(0, res.length() - 1);
            return rStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
