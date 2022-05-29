package alipay.manage.api.channel.deal.youqing;

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
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class YouQingWitNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();
    @Resource
    private WithdrawMapper withdrawDao;

    public YouQingWitNotify(WithdrawMapper withdrawDao) {
        this.withdrawDao = withdrawDao;
    }

    @RequestMapping("/youqingWit-notify")
    public String notify(HttpServletRequest req, HttpServletResponse res ,@RequestParam Map<String,Object> requestParams) {
        log.info("【youqing付代：{}】",JSONUtil.toJsonStr(requestParams));
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为：" + clientIP + "】");
        Map<String,String> ipmap = new HashMap<>();
        ipmap.put("206.119.102.28","206.119.102.28");
        ipmap.put("206.119.102.124","206.119.102.124");

        Object object = ipmap.get(clientIP);
        if (ObjectUtil.isNull(object)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + ipmap.toString() + "】");
            log.info("【当前回调ip不匹配】");
            return "ip errer";
        }
        String orderId = req.getParameter("MerchantUniqueOrderId");
        String sign = req.getParameter("Sign");
        String status = req.getParameter("Status");
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
       String postMd5 =requestParams.get("Sign")+"";
        requestParams.remove("Sign");
        String paramJson = PayUtil.createParam(requestParams);
        String myMd5 = PayUtil.md5(paramJson + privateKey);
        log.info("【youqing付代付签名前参数：{},my:{},{}】",paramJson,myMd5,postMd5);
        if (postMd5.equalsIgnoreCase(myMd5)) {
            if ("0".equals(status) ) {
                Result result = witNotfy(wit.getOrderId(), clientIP);
                if (result.isSuccess()) {
                    return "SUCCESS";
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
