package alipay.manage.api.channel.deal.shanhepay;

import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
public class ShanhePayNotify extends NotfiyChannel {


    @RequestMapping(value = "/shanhePayNotify",method = RequestMethod.POST)
    @ResponseBody
    public String shanhePayNotify(HttpServletRequest request, @RequestParam Map<String,String> params) {
        log.info("shanhePayNotify:{}", JSONUtil.toJsonStr(params));
        String clientIP = HttpUtil.getClientIP(request);
        Map<String,String> ipmap = new HashMap<>();
        ipmap.put("45.9.110.114","45.9.110.114");
        ipmap.put("45.9.110.115","45.9.110.115");
        ipmap.put("45.9.110.116","45.9.110.116");
        ipmap.put("45.9.110.117","45.9.110.117");
        ipmap.put("45.9.110.118","45.9.110.118");
        String s1 = ipmap.get(clientIP);
        if(StrUtil.isEmpty(s1)){
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + ipmap.toString()+ "】");
            log.info("【当前回调ip不匹配】");
            return "ip is error";
        }
        JSONObject r=new JSONObject();
        try {
            String mchId = request.getParameter("mchId") ;
            String outTradeNo = request.getParameter("outTradeNo") ;
            String payAmount = request.getParameter("payAmount") ;
            String transactionId = request.getParameter("transactionId") ;
            String nonceStr = request.getParameter("nonceStr") ;
            String success = request.getParameter("success") ;
            String sign = request.getParameter("sign") ;
            StringBuffer s = new StringBuffer();
            s.append("mchId=" + mchId + "&");
            s.append("outTradeNo=" + outTradeNo+ "&");
            s.append("payAmount=" + payAmount + "&");
            s.append("transactionId=" + transactionId + "&");
            s.append("nonceStr=" + nonceStr+ "&");
            s.append("success=" + success);
//			s.append("key=" + member.getApikey());
            String publicKey=getChannel(outTradeNo).getWitip().split(",")[1];//平台公钥
            if (RSASignature.doCheck(s.toString(), sign, publicKey) && success.equals("true")) {
                //业务处理
                Result result = dealpayNotfiy(outTradeNo+"", s1, "shanhePayNotify回调成功");
                return "OK";
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return "OK";
    }

}
