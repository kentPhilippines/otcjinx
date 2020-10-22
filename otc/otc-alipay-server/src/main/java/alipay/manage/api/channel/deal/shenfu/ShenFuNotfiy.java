package alipay.manage.api.channel.deal.shenfu;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class ShenFuNotfiy extends NotfiyChannel {
    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @PostMapping("/shenfu-source-notify")
    public KinpayNotfiyBean notify(HttpServletRequest req, HttpServletResponse res) throws IOException {
        log.info("【收到金星拼多多支付成功回调】");
        InputStream inputStream = req.getInputStream();
        String body;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
        body = stringBuilder.toString();
        JSON parse = JSONUtil.parse(body);
        JSONObject parseObj = JSONUtil.parseObj(parse);

        Set<String> keySet = parseObj.keySet();
        log.info("【收到绅付支付成功请求，当前请求参数为：" + parseObj + "】");
        Map<String, Object> decodeParamMap = new ConcurrentHashMap();
        for (String key : keySet)
            decodeParamMap.put(key, parseObj.getObj(key));
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为：" + clientIP + "】");
        Map map = PayUtil.ipMap;
        Object object = map.get(clientIP);
        if (ObjectUtil.isNull(object)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + map.toString() + "】");
            log.info("【当前回调ip不匹配】");
            return new KinpayNotfiyBean("9999", "ip is not here [这个回调ip我们不接受]");
        }
        log.info("【转换为map为：" + decodeParamMap.toString() + "】");
        String sign = (String) decodeParamMap.get("sign");
        String remove = (String) decodeParamMap.remove("sign");
        String createParam = PayUtil.createParam(decodeParamMap);
        String oid_partner = parseObj.getStr("no_order");//获取渠道商户Id
        String channelKey = super.getChannelKey(oid_partner);
        String md5 = PayUtil.md5(createParam + channelKey);
        if (md5.equals(sign)) {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名成功】");
        } else {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名失败】");
            return new KinpayNotfiyBean("9999", "sign is errer  [我方验签错误]");
        }

        Result dealpayNotfiy = dealpayNotfiy((String) decodeParamMap.get("no_order"), clientIP);
        if (dealpayNotfiy.isSuccess()) {
            return new KinpayNotfiyBean("0000", "交易成功");
        }
        return new KinpayNotfiyBean("9999", "errer");
    }


}

class KinpayNotfiyBean {
    private String ret_code;
    private String ret_msg;

    public KinpayNotfiyBean() {
        super();
    }

    public KinpayNotfiyBean(String ret_code, String ret_msg) {
        super();
        this.ret_code = ret_code;
        this.ret_msg = ret_msg;
    }

    public String getRet_code() {
        return ret_code;
    }

    public void setRet_code(String ret_code) {
        this.ret_code = ret_code;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }
}