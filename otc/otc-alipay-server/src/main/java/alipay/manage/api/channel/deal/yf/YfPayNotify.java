package alipay.manage.api.channel.deal.yf;

import alipay.manage.api.channel.util.qiangui.MD5;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
public class YfPayNotify extends NotfiyChannel {


    @RequestMapping(value = "/yfNotify",method = RequestMethod.POST)
    @ResponseBody
    public String chaofanPayNotify( HttpServletRequest request,@RequestBody Map<String,String> params) {
        log.info("yfNotify:{}", JSONUtil.toJsonStr(params));
        String clientIP = HttpUtil.getClientIP(request);
        Map<String,String> ipmap = new HashMap<>();
        ipmap.put("35.220.226.100","35.220.226.100");
        String s = ipmap.get(clientIP);
        if(StrUtil.isEmpty(s)){
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + ipmap.toString()+ "】");
            log.info("【当前回调ip不匹配】");
            return "ip is error";
        }
        log.info("yfNotify:{}", JSONUtil.toJsonStr(params));
        String postSign = params.get("sign").toString();
        params.remove("sign");
        String password = "x9V51wel7n55Q2N16V461C1LE84aU9I5";
        String originalStr = createParam(params)+"&key="+password;
        log.info("originalStr:{}", originalStr);
        String sign = MD5.MD5Encode(originalStr);
        if(postSign.equalsIgnoreCase(sign) && "0".equals(params.get("status")+"")) {
            Result result = dealpayNotfiy(params.get("customerNo")+"", clientIP+"", "yifu回调成功");
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
