package alipay.manage.api.channel.deal.doudoupay;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.mapper.WithdrawMapper;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class DouDouWitNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();
    @Resource
    private WithdrawMapper withdrawDao;

    public DouDouWitNotify(WithdrawMapper withdrawDao) {
        this.withdrawDao = withdrawDao;
    }

    @RequestMapping("/doudouWit-notify")
    public String notify(HttpServletRequest req, HttpServletResponse res ,@RequestParam Map<String,String> requestParams) {
        log.info("【doudou付代：{}】",JSONUtil.toJsonStr(requestParams));
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为：" + clientIP + "】");
        Map<String,String> ipmap = new HashMap<>();
        ipmap.put("52.187.72.221","52.187.72.221");
        ipmap.put("104.208.76.107","104.208.76.107");

        Object object = ipmap.get(clientIP);
        if (ObjectUtil.isNull(object)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + ipmap.toString() + "】");
            log.info("【当前回调ip不匹配】");
            return "ip errer";
        }
        String orderId = req.getParameter("orderId");
        String amount = req.getParameter("amount");
        String payAmount = req.getParameter("payAmount");
        String sign = req.getParameter("sign");
        String status = req.getParameter("status");
        Withdraw wit =      withdrawDao.findWitOrder(orderId);
        String channel = "";
        if (StrUtil.isNotBlank(wit.getChennelId())) {//支持运营手动推送出款
            channel = wit.getChennelId();
        } else {
            channel = wit.getWitChannel();
        }
        ChannelInfo channelInfo = getChannelInfo(channel, wit.getWitType());
        String mchCode = channelInfo.getChannelAppId();
        String privateKey = channelInfo.getChannelPassword();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("mchCode",mchCode);
        map.put("orderId",orderId );
        map.put("amount", Integer.valueOf(amount));

        String myMd5 = PayUtil.md5(JSONUtil.toJsonStr(map) + privateKey);
        log.info("【doudou付代付签名前参数：{},my:{},{}】",JSONUtil.toJsonStr(map),myMd5,sign);
        if (sign.equals(myMd5)) {
            if ("0".equals(status) ) {
                Result result = witNotfy(wit.getOrderId(), clientIP);
                if (result.isSuccess()) {
                    return "{\"retcode\":0}";
                }
            }else {
                witNotSuccess(orderId);
            }
        } else {
            return "error";
        }
        return "end errer";
    }


}
