package alipay.manage.api.channel.deal.maida;

import alipay.manage.api.channel.deal.maida.ctrl.MaiDaPayServer;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
public class MaiDaNotify extends NotfiyChannel {
    @RequestMapping(value = "/maida-notify")
    @ResponseBody
    public String chaofanPayNotify(HttpServletRequest request ) throws IOException {
        String clientIP = HttpUtil.getClientIP(request);
        String mchOrderNo = request.getParameter("mchOrderNo");
        String status = request.getParameter("status");
        String channelKey = getChannelKey(mchOrderNo);
        String notify = MaiDaPayServer.notify(request,channelKey);
        if("success".equals(notify) && "2".equals(status)){
            Result result = dealpayNotfiy(mchOrderNo, clientIP, "三方回调成功");
            if(result.isSuccess()){
                return notify;
            }
        }
        return notify;
    }
}
