package alipay.manage.api.channel.deal.rongsheng;

import alipay.manage.api.channel.util.qiangui.MD5;
import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.mapper.WithdrawMapper;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class RongshengWitNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();

    @Resource
    private WithdrawMapper withdrawDao;
    @RequestMapping("/rongsheng-wit-notify")
    public String notify(HttpServletRequest req, HttpServletResponse res , @RequestBody Map<String,Object> requestParams) {
        log.info("【Rongsheng回调：{}】", JSONUtil.toJsonStr(requestParams));
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为：" + clientIP + "】");
        Map<String,String> ipmap = new HashMap<>();
        ipmap.put("195.130.202.43","195.130.202.43");
        Object object = ipmap.get(clientIP);
        if (ObjectUtil.isNull(object)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + ipmap.toString() + "】");
            log.info("【当前回调ip不匹配】");
            return "ip errer";
        }
        String orderId = requestParams.get("osn").toString();
        String postMd5 = requestParams.get("sign").toString();

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

        String originalStr = createParam(requestParams,privateKey);
        String myMd5 = MD5.MD5Encode(originalStr.trim());
        log.info("【Rongsheng付代付签名前参数：{},my:{},{}】",originalStr,myMd5,postMd5);
        if (postMd5.equalsIgnoreCase(myMd5) ) {
            if ( "1".equals(requestParams.get("status")+"") ) {
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

    public static String sign(String secretKey, String data) {

        byte[] bytes = HmacUtils.hmacSha1(secretKey, data);
        return Base64.getEncoder().encodeToString(bytes);
    }
    public static String createParam(Map<String, Object> map,String key) {
        String temp ="mark_sell=%s&user=%s&money=%s&osn=%s&status=%s&key=%s";
        String mark_sell = map.get("mark_sell")+"";
        String user = map.get("user")+"";
        String money = map.get("money")+"";
        String osn = map.get("osn")+"";
        String status = map.get("status")+"";
        String source = String.format(temp, mark_sell,user,money,osn,status,key);
        return source;
    }


}
