package alipay.manage.api.channel.deal.qihang;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class QiHangWitNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();

    @Resource
    private WithdrawMapper withdrawDao;
    @RequestMapping("/qihang-wit-notify")
    public String notify(HttpServletRequest req, HttpServletResponse res , @RequestBody Map<String,Object> requestParams) {
        log.info("【起航回调：{}】", JSONUtil.toJsonStr(requestParams));
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为：" + clientIP + "】");
        Map<String,String> ipmap = new HashMap<>();
        ipmap.put("16.162.90.42","16.162.90.42");
        Object object = ipmap.get(clientIP);
        if (ObjectUtil.isNull(object)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + ipmap.toString() + "】");
            log.info("【当前回调ip不匹配】");
            return "ip errer";
        }
        String orderId = requestParams.get("storeOrderNo").toString();
        String postMd5 = requestParams.get("sign").toString();
        requestParams.remove("sign");
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

        String originalStr = createParam(requestParams)+"&key="+privateKey;
        String myMd5 = PayUtil.md5(originalStr);
        log.info("【newXiangyunWit付代付签名前参数：{},my:{},{}】",originalStr,myMd5,postMd5);
        if (postMd5.equalsIgnoreCase(myMd5) ) {
            if ( "success".equals(requestParams.get("payResult")+"") ) {
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
    private   String createParam(Map<String, Object> map) {
        try {
            if (map == null || map.isEmpty()) {
                return null;
            }
            Object[] key = map.keySet().toArray();
            Arrays.sort(key);
            StringBuffer res = new StringBuffer(128);
            for (int i = 0; i < key.length; i++) {
                if (ObjectUtil.isNotNull(map.get(key[i]))) {
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
