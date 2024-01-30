package alipay.manage.api.channel.deal.haiwang;

import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 海王转卡
 */
@Component("haiwangTocard")
public class ToCard extends PayOrderService {
    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入海王转银行卡】");
        String create = create(dealOrderApp, channel);
        if (StrUtil.isNotBlank(create)) {
            log.info("【本地订单创建成功，开始请求远程三方支付】");
            UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
            if (StrUtil.isBlank(userInfo.getDealUrl())) {
                orderEr(dealOrderApp, "当前商户交易url未设置");
                return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
            }
            log.info("【回调地址ip为：" + userInfo.getDealUrl() + "】");
            String url = createOrder(dealOrderApp, userInfo.getDealUrl() +
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + Util.NOTIFY,
                    dealOrderApp.getOrderAmount(), create,
                    getChannelInfo(channel, dealOrderApp.getRetain1())
            );
            if (StrUtil.isBlank(url)) {
                log.info("【海王转卡支付失败，订单号为：" + create + "】");
            } else {
                return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(url));
            }
        }
        return Result.buildFailMessage("支付失败");

    }

    private String createOrder(DealOrderApp dealOrderApp, String notifys,
                               BigDecimal orderAmount, String orderId,
                               ChannelInfo channelInfo) {
        String pid = channelInfo.getChannelAppId();
        int i = orderAmount.intValue();
        String money = i + ".00";
        String sn = orderId;
        String pay_type_group = channelInfo.getChannelType();
        String notify_url = notifys;
        String key = channelInfo.getChannelPassword();
        String s = "pid=" + pid + "&money=" + money + "&sn=" + sn + "&pay_type_group=" +
                pay_type_group + "&notify_url=" + notify_url + "&key=" + key;
        log.info("海王签名参数：" + s.toString());
        String sign = Util.md5(s).toUpperCase();
        Map<String, Object> map = new HashMap<>();
        map.put("pid", pid);
        map.put("money", money);
        map.put("sn", sn);
        map.put("pay_type_group", pay_type_group);
        map.put("notify_url", notify_url);
        map.put("sign", sign);
        log.info("海王请求参数：" + map.toString());
        String post = HttpUtil.post(channelInfo.getDealurl(), map);
        log.info(post);
        // {"code":0,"msg":"商户订单号重复","data":""}
        //{"code":1,"msg":"订单生成成功",
        // "data":{"sn":"894327","amount":100.09,"pay_type":"5",
        // "code_url":"http:\/\/api.lalarfb.cn\/pay\/imlayuvubbbm82o.html"}}
        String code = JSONUtil.parseObj(post).getStr("code");
        if ("0".equals(code)) {
            orderEr(dealOrderApp, JSONUtil.parseObj(post).getStr("msg"));
            return "";
        }
        if ("1".equals(code)) {
            JSONObject jsonObject = JSONUtil.parseObj(post);
            String data = jsonObject.getStr("data");
            JSONObject jsonObject1 = JSONUtil.parseObj(data);
            String code_url = jsonObject1.getStr("code_url");
            return code_url;
        }
        return "";
    }
}
