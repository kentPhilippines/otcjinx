package alipay.manage.api.channel.deal;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alipay.manage.api.config.PayOrderService;
import alipay.manage.api.feign.ConfigServiceClient;
import alipay.manage.bean.DealOrderApp;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import otc.api.alipay.Common;
import otc.bean.config.ConfigFile;
import otc.common.PayApiConstant;
import otc.result.Result;
@Component("YouSuAlipayScan")
public class YouShuAlipayScan extends PayOrderService{
	private static final Log log = LogFactory.get();
	@Autowired ConfigServiceClient configServiceClientImpl;
	@Override
	public Result deal(DealOrderApp dealOrderApp, String payType) {
		log.info("【进入优树支付宝个码支付】");
		String channelId = "YOUSHUALIPAYSCAN";//配置的渠道账号
		String create = create(dealOrderApp, channelId);
		if(StrUtil.isNotBlank(create)) {
			log.info("【本地订单创建成功，开始请求远程三方支付】");
			Result config = configServiceClientImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.SERVER_IP);
			log.info("【回调地址ip为："+config.toString()+"】" );
			bean createOrder = createOrder(config.getResult()+PayApiConstant.Notfiy.NOTFIY_API_WAI+"/youshu-notfiy", dealOrderApp.getOrderAmount(),create);
			if(ObjectUtil.isNull(createOrder)) {
				boolean orderEr = orderEr(dealOrderApp);
				if(orderEr)
					return Result.buildFailMessage("支付失败");
			}else {
				return Result.buildSuccessResultCode("支付处理中", createOrder.getPay_url(),1);
			}
		}
		return  Result.buildFailMessage("支付错误");
	}
	bean    createOrder(String notfiy, BigDecimal amount, String orderId){ 
		log.info("【进入优树支付宝扫码  】" );
		String apiurl = "http://www.6278pk.com/api/orders/index.html"; // API下单地址
		String key = "zfZ2BTd6PHKvwCxU"; // 商户密钥
		String bankco = "alipay";
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("mch_id", "3362"); // 商户号
		parameterMap.put("type", bankco ); // 支付类型
		parameterMap.put("price",  amount.toString() ); // 金额
		parameterMap.put("out_order_id", orderId); // 商户号
		parameterMap.put("notifyurl", notfiy); // 异步通知地址
		parameterMap.put("returnurl","www.baidu.com"); // 同步通知地址
		parameterMap.put("extend", "312|xxx"); // 附加数据
		String stringSignTemp = "extend="+parameterMap.get("extend")+"&mch_id="+parameterMap.get("mch_id")+"&notifyurl="+parameterMap.get("notifyurl")+"&out_order_id="+parameterMap.get("out_order_id")+"&price="+parameterMap.get("price")+"&returnurl="+parameterMap.get("returnurl")+"&type="+parameterMap.get("type")+"&key="+key;
		parameterMap.put("sign", md5(stringSignTemp).toUpperCase()); // 附加数据
		log.info("【组合优树参数为："+parameterMap.toString()+"】" );
		String jsonString = HttpUtil.post(apiurl, parameterMap);
		log.info(jsonString);
		//{"code":1,"msg":"success","time":"1589364033","data":{"pay_url":"http:\/\/www.qsy123.cn\/index\/pay\/index\/order_id\/caf9da3d-03d2-4d4d-b2be-de442c2d8abe.html"}}
		YouShuBean bean = JSONUtil.toBean(jsonString, YouShuBean.class);
		log.info(jsonString);
		if(ObjectUtil.isNotNull(bean)) {
			alipay.manage.api.channel.deal.bean bean2 = JSONUtil.toBean(bean.getData(), bean.class);
			if(bean.getCode().equals("1") ) {
				return bean2;
			}
		}
		return null;
		}
	 public static String md5(String a) {
	    	String c = "";
	    	MessageDigest md5;
		   	String result="";
			try {
				md5 = MessageDigest.getInstance("md5");
				md5.update(a.getBytes("utf-8"));
				byte[] temp;
				temp=md5.digest(c.getBytes("utf-8"));
				for (int i=0; i<temp.length; i++)
					result+=Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			}
			return result;
	    }

}
class YouShuBean{
	private String code;
	private String success;
	private String time;
	private String data;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}
class bean{
	private String pay_url;
	public void setPay_url(String pay_url) {
		this.pay_url=pay_url;
	};
	public String getPay_url() {
		return this.pay_url;
	}
	
}
