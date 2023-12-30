

package alipay.manage.api.channel.deal.xdzpay;

import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class HengliNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();


    /**
     * memberid	        商户编号	    是	平台分配商户号
     * orderid	        平台订单号	是	订单号唯一, 字符长度20
     * amount	        订单金额	    是	订单金额 单位：元
     * transaction_id	交易流水号	是	交易流水号
     * datetime	        交易成功时间	是	时间格式：2018-12-28 18:18:18
     * returncode	    交易状态	    是	“00” 为成功
     * attach	        扩展返回	    否	商户附加数据返回
     * sign	            签名	        否	请看MD5签名处理
     * @param req
     * @param res
     * @return
     * @throws IOException
     */
    @PostMapping("/xdzpay-notify")
    public String notify(HttpServletRequest req, HttpServletResponse res,@RequestBody Map<String, String> params) throws IOException {

        log.info("【henglipay付代付回调数据为：" + JSONUtil.toJsonStr(params) + "】");
        String clientIP = HttpUtil.getClientIP(req);
        String orderId =params.get("OrderNo");
        String sign =params.get("Sign");
        params.remove("Sign");
        String channelKey = getChannelKey(orderId+"");

        String createParam = createParam(params);
        log.info("【sw代付签名前参数：" + createParam + "】");
        String md5 = HengliPay.md5(createParam +"&SecretKey="+channelKey);


        if (!sign.equalsIgnoreCase(md5.toString())) {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名失败】");
            return "sign is error";
        }
        if ("4".equals(params.get("Status"))) {
            Result dealpayNotfiy = dealpayNotfiy(orderId.toString(), clientIP, "hengli success");
            if (dealpayNotfiy.isSuccess()) {
                return "ok";
            }
        }
        return "fail";
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
