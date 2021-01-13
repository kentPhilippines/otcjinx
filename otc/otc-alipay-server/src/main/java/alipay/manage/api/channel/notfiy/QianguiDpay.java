package alipay.manage.api.channel.notfiy;

import alipay.manage.api.channel.util.qiangui.Util;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class QianguiDpay extends NotfiyChannel {
    private static final Log log = LogFactory.get();

    @PostMapping("/qiankuiDpay-notfiy")
    public String notify(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String clientIP = HttpUtil.getClientIP(req);
        String qianguiip = "47.75.223.103";
        if (!qianguiip.equals(clientIP)) {
            return "接受回调服务器ip错误或者ip未识别";
        }
        res.setHeader("Access-Control-Allow-Origin", "*");
        String key = "YSYKC5XSF8QCDIZX";//商户对应key
        String appId = req.getParameter("appId");
        String orderNo = req.getParameter("orderNo");
        String appOrderNo = req.getParameter("appOrderNo");
        String orderAmt = req.getParameter("orderAmt");
        String orderTime = req.getParameter("orderTime");
        String orderStatus = req.getParameter("orderStatus");
	    String sign=req.getParameter("sign");
	    HashMap map = new HashMap<>();
	    map.put("appId",appId);
	    map.put("orderNo",orderNo);
	    map.put("appOrderNo",appOrderNo);
	    map.put("orderAmt",orderAmt);
	    map.put("orderTime",orderTime);
	    map.put("orderStatus",orderStatus);
	    String csign = Util.creatSign(map, key);//计算签名
	    if (csign.equals(sign)) {//我方给下游回调  这里要写一个回调的抽象类  全部继承然后同意记录
            //判断签名是否正确
            Result witNotfy = witNotfy(appOrderNo, clientIP);
            if (witNotfy.isSuccess()) {
                log.info("【代付通知成功】");
            }
            //TODO 任意事情
            return "SUCCESS";//订单没有任何问题 返回SUCCESS
        }
	    return "Error";
}


}
