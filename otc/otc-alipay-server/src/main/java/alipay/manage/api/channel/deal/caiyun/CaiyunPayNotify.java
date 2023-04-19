package alipay.manage.api.channel.deal.caiyun;

import alipay.manage.api.channel.util.qiangui.MD5;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
public class CaiyunPayNotify extends NotfiyChannel {

    /**
     * 测试报文：{
     * "storeCode": "平台分配的商户号",
     * "orderNo": "本平台的订单号",
     * "storeOrderNo": "商户订单号",
     * "payResult": "订单状态： pending=支付处理中； success=支付成功； fail=支付失败； fail_overtime=超时失败",
     * "totalAmt": "金额（单位为 元，转字符串） ",
     * "payStartTime": "订单开始时间，格式： yyyyMMddHHmmss",
     * "payEndTime": "订单支付完成时间，可能为空，格式： yyyyMMddHHmmss",
     * "sign": "参考【Get Start】页面里面的签名规则",
     * "random": "一个随机串，没有任何业务意义，仅用来参与签名，增加 sign 的变化"
     * }
     * @param request
     * @param params
     * @return
     */
    @RequestMapping(value = "/caiyunNotify",method = RequestMethod.POST)
    @ResponseBody
    public String chaofanPayNotify( HttpServletRequest request,@RequestParam Map<String,String> params) {
        log.info("caiyunNotify:{}", JSONUtil.toJsonStr(params));
        String clientIP = HttpUtil.getClientIP(request);
        Map<String,String> ipmap = new HashMap<>();
        ipmap.put("154.23.180.214","154.23.180.214");
        ipmap.put("154.38.113.85","154.38.113.85");
        String s = ipmap.get(clientIP);
        if(StrUtil.isEmpty(s)){
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + ipmap.toString()+ "】");
            log.info("【当前回调ip不匹配】");
            return "ip is error";
        }
        log.info("caiyunNotify:{}", JSONUtil.toJsonStr(params));
        String postSign = params.get("sign").toString();
        params.remove("sign");
        params.remove("platform_no");
        String password = "eac29f43410e8cecc210605ce52fb994";
        String originalStr = createParam(params)+"&key="+password;
        log.info("originalStr:{}", originalStr);
        String sign = MD5.MD5Encode(originalStr);
        if(postSign.equalsIgnoreCase(sign) && "6".equals(params.get("status")+"")) {
            Result result = dealpayNotfiy(params.get("order_no")+"", clientIP+"", "caiyun回调成功");
            return "success";
        }
        return "error";
    }

    private   String createParam(Map<String, String> map) {
        try {
            if (map == null || map.isEmpty()) {
                return null;
            }
            Object[] key = map.keySet().toArray();
            Arrays.sort(key);
            StringBuffer res = new StringBuffer(128);
            for (int i = 0; i < key.length; i++) {
                if (ObjectUtil.isNotNull(map.get(key[i])) && StringUtils.isNotEmpty(map.get(key[i])+"")) {
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
