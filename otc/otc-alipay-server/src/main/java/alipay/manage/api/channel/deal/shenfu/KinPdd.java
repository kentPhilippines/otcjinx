package alipay.manage.api.channel.deal.shenfu;

import alipay.manage.api.channel.util.kinpay.PayUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.api.feign.ConfigServiceClient;
import alipay.manage.bean.DealOrderApp;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.api.alipay.Common;
import otc.bean.config.ConfigFile;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component(Common.Deal.PDD_303)
public class KinPdd extends PayOrderService {
    private static final Log log = LogFactory.get();
    @Autowired
    ConfigServiceClient configServiceClientImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String payType) {
        log.info("【进入金星拼多多支付】");
        String channelId = "PDD303";//配置的渠道账号
        String create = create(dealOrderApp, channelId);
        if (StrUtil.isNotBlank(create)) {
            log.info("【本地订单创建成功，开始请求远程三方支付】");
            Result config = configServiceClientImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.SERVER_IP);
            PddBean createOrder = createOrder(config.getResult() + PayApiConstant.Notfiy.NOTFIY_API_WAI + "/kinPdd-notfiy", dealOrderApp.getOrderAmount(), create);
            if (ObjectUtil.isNull(createOrder)) {
                boolean orderEr = orderEr(dealOrderApp);
                if (orderEr) {
                    return Result.buildFailMessage("支付失败");
                }
            } else {
                return Result.buildSuccessResultCode("支付处理中", createOrder.getRedirect_url(), 1);
			}
		}
		return  Result.buildFailMessage("支付错误");
	}
	static SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");
	PddBean createOrder(String notfiy,BigDecimal bigDecimal, String orderId){
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
        map.put("oid_partner", PayUtil.appid);
        map.put("notify_url", notfiy);
        map.put("sign_type", "MD5");
        map.put("user_id", IdUtil.objectId());
        map.put("no_order", orderId);
        map.put("time_order", d.format(new Date()));
        map.put("money_order", bigDecimal);
        map.put("name_goods", "pdd");
        map.put("pay_type", "47");//PDD PDD 插件通道
        map.put("info_order", "info_order");
	String createParam = PayUtil.createParam(map);
	log.info("【金星拼多多插件请求参数："+createParam+"】");
	String md5 = PayUtil.md5(createParam+PayUtil.key);
	map.put("sign", md5);
	String post = HttpUtil.post(PayUtil.url, map);
	log.info("【金星拼多多返回数据："+post+"】");
	log.info(post);
	PddBean bean = JSONUtil.toBean(post, PddBean.class);
	if(ObjectUtil.isNotNull(bean)) {
        if ("0000".equals(bean.getRet_code())) {
            return bean;
        }
    }
	return null;
	}

}
class PddBean{
	private String money_order;
	private String no_order;
	private String oid_partner;
	private String redirect_url;
	private String ret_code;
	private String ret_msg;
	private String sign;
	public String getMoney_order() {
		return money_order;
	}
	public void setMoney_order(String money_order) {
		this.money_order = money_order;
	}
	public String getNo_order() {
		return no_order;
	}
	public void setNo_order(String no_order) {
		this.no_order = no_order;
	}
	public String getOid_partner() {
		return oid_partner;
	}
	public void setOid_partner(String oid_partner) {
		this.oid_partner = oid_partner;
	}
	public String getRedirect_url() {
		return redirect_url;
	}
	public void setRedirect_url(String redirect_url) {
		this.redirect_url = redirect_url;
	}
	public String getRet_code() {
		return ret_code;
	}
	public void setRet_code(String ret_code) {
		this.ret_code = ret_code;
	}
	public String getRet_msg() {
		return ret_msg;
	}
	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	@Override
	public String toString() {
		return "PddBean [money_order=" + money_order + ", no_order=" + no_order + ", oid_partner=" + oid_partner
				+ ", redirect_url=" + redirect_url + ", ret_code=" + ret_code + ", ret_msg=" + ret_msg + ", sign="
				+ sign + "]";
	}

}
