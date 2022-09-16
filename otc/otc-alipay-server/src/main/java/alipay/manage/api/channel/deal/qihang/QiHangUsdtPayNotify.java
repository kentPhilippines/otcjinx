package alipay.manage.api.channel.deal.qihang;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.mapper.WithdrawMapper;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class QiHangUsdtPayNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();

    @Resource
    private WithdrawMapper withdrawDao;
    @RequestMapping("/qihang-pay-notify")
    public String notify(HttpServletRequest req, HttpServletResponse res , @RequestBody Map<String,Object> requestParams) {
        log.info("【起航回调：{}】", JSONUtil.toJsonStr(requestParams));
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为：" + clientIP + "】");
        Map<String,String> ipmap = new HashMap<>();
        ipmap.put("16.162.90.42","16.162.90.42");
        Object object = ipmap.get(clientIP);
        /*if (ObjectUtil.isNull(object)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + ipmap.toString() + "】");
            log.info("【当前回调ip不匹配】");
            return "ip errer";
        }*/
        Map<String,Object> data = (Map<String, Object>) requestParams.get("data");
        String orderId = data.get("order_no").toString();
        String postMd5 = data.get("sign").toString();
        data.remove("sign");
        Withdraw wit =      withdrawDao.findWitOrder(orderId);
        String channel = "";
        if (StrUtil.isNotBlank(wit.getChennelId())) {//支持运营手动推送出款
            channel = wit.getChennelId();
        } else {
            channel = wit.getWitChannel();
        }
        ChannelInfo channelInfo = getChannelInfo(channel, wit.getWitType());
        String mchCode = channelInfo.getChannelAppId();
        String privateKey = channelInfo.getBalanceUrl();

        String originalStr = createParam(data);
        String myMd5 = sign(privateKey,originalStr.trim());
        log.info("【qihangpay付代付签名前参数：{},my:{},{}】",originalStr,myMd5,postMd5);
        if (postMd5.equalsIgnoreCase(myMd5) ) {
            if ( "succeeded".equals(data.get("status")+"") ) {
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
    public static String createParam(Map<String, Object> map) {
        try {
            if (map == null || map.isEmpty())
                return null;
            Object[] key = map.keySet().toArray();
            Arrays.sort(key);
            StringBuffer res = new StringBuffer(128);
            for (int i = 0; i < key.length; i++)
            {
                if (ObjectUtil.isNotNull(map.get(key[i])) && StringUtils.isNotEmpty(map.get(key[i])+""))
                {res.append(key[i] + "=" + map.get(key[i]) + "&");}
            }


            String rStr = res.substring(0, res.length() - 1);
            return rStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
