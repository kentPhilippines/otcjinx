package alipay.manage.api.channel.util;

import alipay.manage.api.channel.amount.BalanceInfo;
import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.ChannelInfo;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author water
 */
public class QueryBalanceTool {
    public static final Log log = LogFactory.get();
    private static SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");
    public static Map<String, ChannelInfo> channelInfoMap = new ConcurrentHashMap<>();


    /**
     * 余额查询接口
     * 参数	必须	说明
     * p1_merchantno		商户号: 请访问商户后台来获取您的商户号。
     * sign		MD5 签名: HEX 大写, 32 字节。
     */
    public static List<BalanceInfo> findBalance() {
        List<BalanceInfo> list = new ArrayList<>();
        try {
            if (CollectionUtil.isEmpty(channelInfoMap)) {
                log.info("查询余额无初始化数据");
                return list;
            }
            String amount = null;
            for (String s : channelInfoMap.keySet()) {
                ChannelInfo channelInfo = channelInfoMap.get(s);
                BalanceInfo balanceInfo = new BalanceInfo();
                if ("ShenfuHuafeiPay".equals(s)) {
                    amount = requestQueryBalance(channelInfo, s);
                } else if ("ShenFuSourcePay".equals(s)) {
                    amount = requestQueryBalance(channelInfo, s);
                }
                if (StringUtils.isNotBlank(amount)) {
                    balanceInfo.setChannel(channelInfo.getChannelAppId());
                    balanceInfo.setBalance(amount);
                    balanceInfo.setTime(d.format(new Date()));
                    list.add(balanceInfo);
                }
            }
        } catch (Exception e) {
            log.warn("数据不存在");
            return list;
        }
        return list;
    }


    /**
     * 查询余额方法
     *
     * @param channelInfo
     * @return
     */
    public static String requestQueryBalance(ChannelInfo channelInfo, String pay) {
        Map<String, Object> map = new HashMap<String, Object>();
        String amount = null;
        try {
            if ("ShenfuHuafeiPay".equals(pay)) {
                map.put("p1_merchantno", channelInfo.getChannelAppId());
                log.info("【申付话费查询余额数据：" + map + "】");
                String createParam = PayUtil.createParam(map);
                String md5 = PayUtil.md5(createParam + "&key=" + channelInfo.getChannelPassword());
                map.put("sign", md5.toUpperCase());
                log.info("【申付话费查询余额请求参数为：" + map.toString() + "】");
                String post = HttpUtil.post(channelInfo.getBalanceUrl(), map, 2000);
                log.info("【绅付查询余额响应参数为：" + post + "】");
                JSONObject parseObj = JSONUtil.parseObj(post);
                String rspcode = parseObj.getStr("rspcode");
                if ("A0".equals(rspcode)) {
                    amount = parseObj.getStr("t0money") + "," + parseObj.getStr("t1money");
                }
            } else if ("ShenFuSourcePay".equals(pay)) {
                map.put("oid_partner", channelInfo.getChannelAppId());
                map.put("sign_type", "MD5");
                String format = d.format(new Date());
                map.put("time_order", format);
                log.info("【话费代付订单查询数据：" + map + "】");
                String createParam = PayUtil.createParam(map);
                String md5 = PayUtil.md5(createParam + channelInfo.getChannelPassword());
                map.put("sign", md5.toUpperCase());
                log.info("【绅付查询余额请求参数为：" + map.toString() + "】");
                String post = HttpUtil.post(channelInfo.getBalanceUrl(), map, 2000);
                log.info("【绅付查询余额响应参数为：" + post + "】");
                JSONObject parseObj = JSONUtil.parseObj(post);
                String oid_partner = parseObj.getStr("oid_partner");
                if (channelInfo.getChannelAppId().equals(oid_partner)) {
                    amount = parseObj.getStr("money");
                }
            }
        } catch (Throwable throwable) {
            log.error("绅付查询余额异常", throwable);
            return null;
        }
        return amount;
    }
}
