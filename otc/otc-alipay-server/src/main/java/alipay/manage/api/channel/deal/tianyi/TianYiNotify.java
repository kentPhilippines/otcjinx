package alipay.manage.api.channel.deal.tianyi;

import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
public class TianYiNotify extends NotfiyChannel {
    static Map<String, String> mapId = new ConcurrentHashMap();
    static {
        mapId.put("caiyun88", "tainyiPay");
    }


    @RequestMapping(value = "/TianyiPayNotify")
    @ResponseBody
    public Map notify(HttpServletRequest request, @RequestBody Map<String, String> params) {
        Map map = new HashMap();
        map.put("code", 500);
        map.put("msg", "无");
        log.info("tianyi支付回调:{}", JSONUtil.toJsonStr(params));
        String encrypt = params.get("encrypt");
        String merchantId = params.get("merchantId");
        String o = mapId.get(merchantId);
        String key = getKey(o);
        String result = "";
        try {
            result = RSAMerchantUtils.decryptByPubKey(encrypt, key);
        } catch (Throwable e) {
            log.error("解密渠道回调数据失败：", e);
            map.put("code", 500);
            map.put("msg", "解密渠道回调数据失败");
            return map;
        }
        JSONObject jsonObject1 = JSONUtil.parseObj(result);
        log.info("tianyi支付回调:{}", jsonObject1);
        String receiveNo = jsonObject1.getStr("receiveNo");
        String state = jsonObject1.getStr("state");
        if ("2".equals(state)) {
            Result result1 = dealpayNotfiy(receiveNo, HttpUtil.getClientIP(request));
            if (result1.isSuccess()) {
                map.put("code", 200);
                map.put("msg", "接收成功描述");
                return map;
            }
        }
        return map;
    }
}
