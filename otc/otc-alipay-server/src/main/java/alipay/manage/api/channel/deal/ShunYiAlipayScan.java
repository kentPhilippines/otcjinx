package alipay.manage.api.channel.deal;

import alipay.manage.api.channel.util.kinpay.PayUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("shunyiAlipayScan")
public class ShunYiAlipayScan extends PayOrderService{
	private static final String KEY = "ASDASFQ4FRQEGRGQewfewrevrtboscdnoodmvoMmoeviVIVH9ERUERVURH9UHUBHBUHURHTB9RTBH9RHBTGHHGIRHFIjejiji";
    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;
	@Override
	public Result deal(DealOrderApp dealOrderApp, String payType) {
		log.info("【进入顺易支付宝支付支付宝扫码】");
        String create = create(dealOrderApp, payType);
        if (StrUtil.isNotBlank(create)) {
            log.info("【本地订单创建成功，开始请求远程三方支付】");
            UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
            if (StrUtil.isBlank(userInfo.getDealUrl())) {
                orderEr(dealOrderApp, "当前商户交易url未设置");
                return Result.buildFailMessage("请联系运营为您的商户号设置交易url");
            }
            log.info("【回调地址ip为：" + userInfo.getDealUrl() + "】");
            JSONObject createOrder = createOrder(dealOrderApp, userInfo.getDealUrl() + PayApiConstant.Notfiy.NOTFIY_API_WAI + "/shunyi-notfiy",
                    dealOrderApp.getOrderAmount(), create);
            if (ObjectUtil.isNull(createOrder)) {
            } else {
                return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(createOrder.getStr("redirect_url")));
            }
        }
        return Result.buildFailMessage("支付错误");
    }

    static SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");

    JSONObject createOrder(DealOrderApp dealOrderApp, String notfiy, BigDecimal bigDecimal, String orderId) {
        /**
         oid_partner			String(18)			√				参数名称：商家号 商户签约时，分配给商家的唯一身份标识 例如：201411171645530813
         notify_url			String(128)			√				参数名称：服务器异步通知地址 支付成功后，系统会主动发送通知给商户，商户必须指定此通知地址
         user_id				String(32)			√				该用户在商户系统中的唯一编号，要求是该 编号在商户系统中唯一标识该用户
         sign_type			String(10)			√				参数名称：签名方式 1.取值为：MD5
         sign				String				√				参数名称：签名数据 该字段不参与签名，值如何获取，请参考提供的示例代码。
         no_order			String(32)			√				参数名称：商家订单号 商家网站生成的订单号，由商户保证其唯一性，由字母、数字组成。
         time_order			Date				√				参数名称：商家订单时间 时间格式：YYYYMMDDH24MISS 14 位数 字，精确到秒
         money_order			Number(13,2)		√				参数名称：客户实际支付金额与币种对应
         name_goods			String(40)			√				参数名称：商品名称
         info_order			String(255)			×				参数名称：商品描述
         pay_type			String(5)			√				参数名称：支付类型
         */

        Map<String, Object> map = new HashMap();
        map.put("oid_partner", "202007301750003675");
        map.put("notify_url", notfiy);
        map.put("sign_type", "MD5");
        map.put("user_id", IdUtil.objectId());
        map.put("no_order", orderId);
        map.put("time_order", d.format(new Date()));
        map.put("money_order", bigDecimal);
        map.put("name_goods", "alipay");
        map.put("pay_type", "58");
        map.put("info_order", "info_order");
        String createParam = PayUtil.createParam(map);
        log.info("【顺易支付宝扫码请求参数：" + createParam + "】");
        String md5 = PayUtil.md5(createParam + KEY);
        map.put("sign", md5);
        String post = HttpUtil.post("http://api.zdjs1688.cn/gateway/bankgateway/getpayurl", map);
        log.info("【顺易支付扫码返回数据：" + post + "】");
        log.info(post);
        JSONObject jsonObject = JSONUtil.parseObj(post);

        if (ObjectUtil.isNotNull(jsonObject)) {
            if (jsonObject.getStr("ret_code").equals("0000")) {
                return jsonObject;
            } else {
                //	{"ret_code":"4008","ret_msg":"服务未开通"}
                orderEr(dealOrderApp, "顺易返回：" + jsonObject.getStr("ret_msg"));
            }
        }
        return null;
    }
}
