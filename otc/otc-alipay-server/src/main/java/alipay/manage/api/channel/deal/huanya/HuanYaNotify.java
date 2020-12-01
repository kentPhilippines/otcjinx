package alipay.manage.api.channel.deal.huanya;

import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class HuanYaNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();

    @RequestMapping(HuanYaUtil.NOTIFY)
    public String notify(HttpServletRequest req, HttpServletResponse res,
                         @RequestBody JSONObject jsonObject) {
        String clientIP = HttpUtil.getClientIP(req);
        Map mapip = new HashMap();
        mapip.put("202.46.44.121", "202.46.44.121");
        mapip.put("202.46.44.135", "202.46.44.135");
        Object object = mapip.get(clientIP);
        if (ObjectUtil.isNull(object)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + mapip.toString() + "】");
            log.info("【当前回调ip不匹配】");
            return "ip is error";
        }
        /**
         支付订单号	payOrderId	是	String(30)	P20160427210604000490	支付中心生成的订单号
         商户ID	mchId	是	String(30)	20001222	支付中心分配的商户号
         应用ID	appId	是	String(32)	0ae8be35ff634e2abe94f5f32f6d5c4f	该商户创建的应用对应的ID
         支付产品ID	productId	是	int	8001	支付产品ID
         商户订单号	mchOrderNo	是	String(30)	20160427210604000490	商户生成的订单号
         支付金额	amount	是	int	100	支付金额,单位分
         状态	status	是	int	1	支付状态,0-订单生成,1-支付中,2-支付成功,3-业务处理完成
         支付成功时间	paySuccTime	是	long		精确到毫秒
         通知类型	backType	是	int	1	通知类型，1-前台通知，2-后台通知
         签名	sign	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	签名值，详见签名算法
         */

        String payOrderId = req.getParameter("payOrderId");
        String mchId = req.getParameter("mchId");
        String appId = req.getParameter("appId");
        String productId = req.getParameter("productId");
        String mchOrderNo = req.getParameter("mchOrderNo");
        String amount = req.getParameter("amount");
        String status = req.getParameter("status");
        String paySuccTime = req.getParameter("paySuccTime");
        String backType = req.getParameter("backType");
        String sign = req.getParameter("sign");
        Map<String, Object> map = new HashMap();
        map.put("payOrderId", payOrderId);
        map.put("mchId", mchId);
        map.put("appId", appId);
        map.put("productId", productId);
        map.put("mchOrderNo", mchOrderNo);
        map.put("amount", amount);
        map.put("status", status);
        map.put("paySuccTime", paySuccTime);
        map.put("backType", backType);
        String channelKey = getChannelKey(mchOrderNo);
        String param = HuanYaUtil.createParam(map);
        param = param + "&key=" + channelKey;
        log.info("【环亚加密前参数：" + param + "】");
        String md5 = HuanYaUtil.md5(param);
        if (md5.equals(sign)) {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名成功】");
        } else {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名失败】");
            return "sign is error";
        }
        Map<String, Object> finfMap = new HashMap<>();
        finfMap.put("mchId", mchId);
        finfMap.put("appId", appId);
        finfMap.put("payOrderId", payOrderId);
        finfMap.put("mchOrderNo", mchOrderNo);
        String findp = HuanYaUtil.createParam(finfMap);
        findp = findp + "&key=" + channelKey;
        log.info("【环亚订单查询加密前参数：" + findp + "】");
        String md5find = HuanYaUtil.md5(findp);
        finfMap.put("sign", md5find);
        String post = HttpUtil.post("http://api.yanhua.net.cn/api/pay/query_order", finfMap);
        JSONObject jsonObject1 = JSONUtil.parseObj(post);
        String mchOrderNo1 = jsonObject1.getStr("mchOrderNo");
        String status1 = jsonObject1.getStr("status");
        if (!mchOrderNo.equals(mchOrderNo1)) {
            return "mchOrderNo is not ture";
        }
        if (!status.equals(status1)) {
            return "status is not ture";
        }
        if ("2".equals(status)) {
            Result result = dealpayNotfiy(mchOrderNo1, clientIP, "环亚回调成功");
            if (result.isSuccess()) {
                return "success";
            }
        }
        return "";
    }
}