package alipay.manage.api.channel.deal.huachenghui;

import alipay.manage.api.channel.util.qiangui.MD5;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
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
public class HuachenghuiPayNotify extends NotfiyChannel {


    @RequestMapping(value = "/huachenghuiPayNotify",method = RequestMethod.POST)
    @ResponseBody
    public String shanhePayNotify(HttpServletRequest request, @RequestParam Map<String,String> params) {
        log.info("huachenghuiPayNotify:{}", JSONUtil.toJsonStr(params));
        String clientIP = HttpUtil.getClientIP(request);
        JSONObject r=new JSONObject();
        try {
            //{"income":"70100","payOrderId":"J20230129072328099820936","amount":"70100","mchId":"20000027","productId":"8008","mchOrderNo":"J20230129072328099820936","paySuccTime":"1674956178000","sign":"318503D4125A0D402948F3F43ADB4933","channelOrderNo":"","backType":"2","param1":"","param2":"","appId":"","status":"2"}
//            String mchId = request.getParameter("mchId") ;
//            String outTradeNo = request.getParameter("mchOrderNo") ;
//            String payAmount = request.getParameter("payAmount") ;
//            String transactionId = request.getParameter("mchOrderNo") ;
//            String nonceStr = request.getParameter("nonceStr") ;
//            String status = request.getParameter("status") ;
            String postSign = params.get("Sign");
            params.remove("Sign");
            String password = getChannelKey(params.get("MerchantUniqueOrderId"));
            String originalStr = createParam(params)+""+password;
            String sign = MD5.MD5Encode(originalStr).toLowerCase();
            if(postSign.equalsIgnoreCase(sign) && "100".equals(params.get("PayOrderStatus")+"")) {
                Result result = dealpayNotfiy(params.get("MerchantUniqueOrderId")+"", clientIP+"", "huachenghuiPay回调成功");
                return "SUCCESS";
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return "OK";
    }

    private static String createParam(Map<String, String> map) {
        try {
            if (map == null || map.isEmpty()) {
                return null;
            }
            Object[] key = map.keySet().toArray();
            Arrays.sort(key);
            StringBuffer res = new StringBuffer(128);
            for (int i = 0; i < key.length; i++) {
                if (StringUtils.isNotEmpty(map.get(key[i]))) {
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
