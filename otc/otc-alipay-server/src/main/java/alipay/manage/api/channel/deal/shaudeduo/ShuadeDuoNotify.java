package alipay.manage.api.channel.deal.shaudeduo;

import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
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

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class ShuadeDuoNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();

    @PostMapping("/shuadeduo-notify")
    public String notify(HttpServletRequest req, HttpServletResponse res, @RequestBody String json) throws IOException {
        String clientIP = HttpUtil.getClientIP(req);
        if (!"13.114.179.98".equals(clientIP)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + "13.114.179.98" + "】");
            log.info("【当前回调ip不匹配】");
            return "ip is error";
        }
        if (StrUtil.isBlank(json)) {
            log.info("【参数获取错误】");
            return "param is error";
        }
        JSONObject jsonObject = JSONUtil.parseObj(json);
        String trade_no = jsonObject.getStr("trade_no");
        String status = jsonObject.getStr("status");
        String url = "https://15634145.com/api/transaction/" + trade_no;
        String header = "Bearer ";
        String channelKey = getChannelKey(trade_no);
        String result2 = HttpRequest.get(url)
                .header(Header.CONTENT_TYPE, "application/x-www-form-urlencoded")//头信息，多个头信息多次调用此方法即可
                .header("Authorization", header + channelKey)//头信息，多个头信息多次调用此方法即可
                .timeout(20000)//超时，毫秒
                .execute().body();
        log.info("【刷的多订单查询返回数据：" + result2 + "】");

        JSONObject result = JSONUtil.parseObj(result2);
        String status1 = result.getStr("status");
        if (status.equals(status1) && "success".equals(status)) {
            Result result1 = dealpayNotfiy(trade_no, "刷的多正常回调", clientIP);
            if (result1.isSuccess()) {
                return "success";
            }
        }
        return "";
    }


}
