package alipay.manage.api.channel.deal.linghang;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
public class linhangNotify extends NotfiyChannel {

    @RequestMapping(value = "/linghangNotify")
    @ResponseBody
    public String notify(HttpServletRequest request, @RequestBody Map<String, String> params) {
        String clientIP = HttpUtil.getClientIP(request);
        log.info("linhang支付回调:{}", JSONUtil.toJsonStr(params));
        /**
         * mchId	            商户号	        是	            string	M1623984572	商户号
         * tradeNo	            支付订单号	    是	            string	P202109052329398641190	返回支付系统订单号
         * outTradeNo	        商户订单号	    是	            string	20210905000702675466	返回商户传入的订单号
         * amount	            订单金额        (单位: 分)	是	long	10000	订单金额 (单位: 分)，例如: 10000 即为 100.00 元
         * subject	            商品标题	        是	            string	商品标题测试	商品标题
         * state	            订单状态	        是	            int	1	订单状态：0=待支付，1=支付成功，2=支付失败
         * notifyTime	        通知时间	        是	            long	1622016572190	通知时间，13位时间戳
         * sign	                签名	            是	            string	694da7a446ab4b1d9ceea7e5614694f4	签名值，详见 签名算法
         */
        String tradeNo = params.get("outTradeNo");
        String channelKey = getChannelKey(tradeNo);
/**
 * mchId	        商户号	        是	string	M1623984572	商户号
 * outTradeNo	    商户订单号	    是	string	20210905000702675466	返回商户传入的订单号
 * reqTime	        请求时间	        是	long	1622016572190	请求接口时间，13位时间戳
 * sign	            签名	            是	string	694da7a446ab4b1d9ceea7e5614694f4	签名值，详见 签名算法
 */

        Map map = new HashMap();
        map.put("mchId", params.get("mchId"));
        map.put("outTradeNo", tradeNo);
        map.put("reqTime", System.currentTimeMillis());
        map.put("sign", PayUtil.md5(createParam(map) + "&key=" + channelKey).toUpperCase());
        String post = HttpUtil.post("https://dahengpay84dvf0d.zzbbm.xyz/api/pay/query", JSONUtil.parseObj(map).toString());
        JSONObject jsonObject = JSONUtil.parseObj(post);
        if ("0".equals(jsonObject.getStr("code"))) {
            JSONObject data = JSONUtil.parseObj(jsonObject.getStr("data"));
            String state = data.getStr("state");
            if ("1".equals(state)) {
                Result result = dealpayNotfiy(tradeNo, clientIP);
                if (result.isSuccess()) {
                    return "SUCCESS";
                }
            }

        }

        /**订单状态：0=待支付，1=支付成功，2=支付失败
         * {"code":0,"message":"ok","data":{"mchId":"M1685337144","wayCode":1063,"tradeNo":"P1663057943442649088","outTradeNo":"86212d7e-fe12-4f69-98fc-90b0d1dc7ba9","originTradeNo":"1","amount":"20000","subject":"支付宝","body":null,"extParam":null,"notifyUrl":"www.baosss.com","payUrl":"https://cdn1.onoz.xyz/api/payPage.html?id=5481553","expiredTime":"1685339186","successTime":"1685338887","createTime":"1685338887","state":2,"notifyState":0},"sign":"8580c378ad37393bc9ef2403ab9ae829"}
         */


        return "error";
    }

    public static String createParam(Map<String, Object> map) {
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
