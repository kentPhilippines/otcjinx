package alipay.manage.api.channel.deal.ePay;

import alipay.manage.api.channel.util.yifu.YiFuUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component("YiFuKuaiJiePay")
public class YiFuKuaiJiePay extends PayOrderService {
    @Autowired
    OrderService orderServiceImpl;
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channelId) {
        log.info("【进入易付快捷支付】");
        String orderId = create(dealOrderApp, channelId);
        if (StrUtil.isNotBlank(orderId)) {
            UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
            if (StrUtil.isBlank(userInfo.getDealUrl())) {
                orderEr(dealOrderApp, "当前商户交易url未设置");
                return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
            }
            log.info("【回调地址ip为：" + userInfo.getDealUrl() + "】");
            log.info("【本地订单创建成功，开始请求远程三方支付】");
            String url = createOrder(dealOrderApp, userInfo.getDealUrl() + PayApiConstant.Notfiy.NOTFIY_API_WAI + "/YiFu-notfiy", dealOrderApp.getOrderAmount(), orderId);
            if (StrUtil.isBlank(url)) {
                return Result.buildFailMessage("支付失败");
            } else {
                return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(url));
            }
        }
        return Result.buildFailMessage("支付失败");
    }

    private String createOrder(DealOrderApp dealOrderApp, String notify, BigDecimal orderAmount, String orderId) {
        int i = RandomUtil.randomInt(1000000000);
        String out_trade_no = i + "";
        String money = "100";
        String callback_url = notify;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("app_id", YiFuUtil.APPID);
        map.put("channel", YiFuUtil.BANK_TO_BANK);
        map.put("out_trade_no", orderId);
        map.put("money", orderAmount.intValue() + ".00");
        map.put("callback_url", callback_url);
        String createParam = YiFuUtil.createParam(map);
        log.info("【易付签名前参数：" + createParam + "】");
        String md5 = YiFuUtil.md5(createParam + "key=" + YiFuUtil.KEY);
        String sign = md5.toUpperCase();
        map.put("sign", sign);
        log.info("【请求Yifu参数：" + map.toString() + "】");
        //{"code":200,"msg":"ok","data":{"pay_url":"http://kpay8494.168yuju.cn/pay/gateway/order?c=22&o=2020070411330189354","money":"100"}}
        //{"code":419,"msg":"签名不正确","data":[]}
        String post = HttpUtil.post(YiFuUtil.URL, map);
        JSONObject jsonObject = JSONUtil.parseObj(post);
        String code = jsonObject.getStr("code");
        if (StrUtil.isNotBlank(code)) {
            if (!"200".equals(code.toString())) {
                orderEr(dealOrderApp, jsonObject.getStr("msg").toString());
                return "";
            }
            if ("200".equals(code.toString())) {
                String data = jsonObject.getStr("data");
                JSONObject dataJson = JSONUtil.parseObj(data);
                log.info("【获取易付返回参数,解析data结果：" + data + "】");
                String pay_url = dataJson.getStr("pay_url");
                return pay_url;
            }
        }
        return "";
    }
}
