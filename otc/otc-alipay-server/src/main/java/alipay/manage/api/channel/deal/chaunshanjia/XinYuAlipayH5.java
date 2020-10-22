package alipay.manage.api.channel.deal.chaunshanjia;

import alipay.manage.api.config.PayOrderService;
import alipay.manage.api.feign.ConfigServiceClient;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.OrderService;
import cn.hutool.core.lang.Snowflake;
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
import otc.bean.config.ConfigFile;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
@Component("XianYuH5")
public class XinYuAlipayH5 extends PayOrderService{
	private static final Log log = LogFactory.get();
	@Autowired ConfigServiceClient configServiceClientImpl;
	@Autowired OrderService orderServiceImpl;
	@Override
	public Result deal(DealOrderApp dealOrderApp, String payType) {
		String channelId = "XianYuZhifubao";
		log.info("【进入咸鱼支付宝H5】");
		String create = create(dealOrderApp, channelId);
		if(StrUtil.isNotBlank(create)) {
			log.info("【本地订单创建成功，开始请求远程三方支付】");
			Result config = configServiceClientImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.SERVER_IP);
			XianYu xianyu = createOrder(config.getResult()+PayApiConstant.Notfiy.NOTFIY_API_WAI+"/xianyu-notfiy", dealOrderApp.getOrderAmount(),create);
			if(ObjectUtil.isNull(xianyu)) {
				boolean orderEr = orderEr(dealOrderApp);
				if(orderEr)
					return Result.buildFailMessage("支付失败");
			}else {
				if(xianyu.getStatus().equals("1")) {
                    return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(xianyu.getPayurl()));
                }else {
					orderEr(dealOrderApp);
					return Result.buildFailMessage(xianyu.getPayurl());
				}
			}
		}
		return Result.buildFailMessage("支付失败");
	}
	private XianYu createOrder(String notfiy, BigDecimal orderAmount, String orderId) {
        DealOrder order = orderServiceImpl.findOrderByOrderId(orderId);
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        String id = snowflake.nextId() + "";
        orderServiceImpl.updataXianyYu(order.getOrderId(), id);
        String fxnotifyurl = notfiy;
        String fxbackurl = order.getBack();
        String fxattch = "test";
        String fxdesc = "desc";
        String fxfee = orderAmount.toString();
        String fxpay = "zfbh5";
        String fxddh = id; //订单号
        String fxid = "2020177";
        String key = "AHFuoYCUgZcOdpectBxYiPElWMVGljbc";
        //订单签名
        String fxsign = md5(fxid + fxddh + fxfee + fxnotifyurl + key);
        fxsign = fxsign.toLowerCase();
        Map<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("fxid", fxid);
        reqMap.put("fxddh", fxddh);
        reqMap.put("fxfee", fxfee);
        reqMap.put("fxpay", fxpay);
        reqMap.put("fxnotifyurl", fxnotifyurl);
        reqMap.put("fxbackurl", fxbackurl);
        reqMap.put("fxattch", fxattch);
        reqMap.put("fxnotifystyle", "1");
        reqMap.put("fxdesc", fxdesc);
        reqMap.put("fxip", "127.0.0.1");
        reqMap.put("fxsign", fxsign);
        log.info("【咸鱼H5请求参数：" + reqMap.toString() + "】");
        // 支付请求返回结果
        String result = null;
        result = HttpUtil.post("https://csj.fenvun.com/Pay", reqMap);
        JSONObject parseObj = JSONUtil.parseObj(result);
        log.info("【咸鱼H5返回：" + parseObj.toString() + "】");
        Object object = parseObj.get("status");
	    XianYu bean = new XianYu();
        if (object.toString().equals("1")) {
            bean = JSONUtil.toBean(parseObj, XianYu.class);


        } else {
            bean.setPayurl(parseObj.get("error").toString());
            bean.setStatus("0");
        }
	    //{"status":1,"payurl":"trade_no=2020051404200399991055074076&biz_sub_type=peerpay_trade&presessionid=&app=tb&channel=&type2=gulupay&bizcontext={\"biz_type\":\"share_pp_pay\",\"type\":\"qogirpay\"}"}
		return bean;
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
class XianYu{
	private String status;
	private String payurl;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPayurl() {
		return payurl;
	}
	public void setPayurl(String payurl) {
		this.payurl = payurl;
	}
}
