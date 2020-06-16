package alipay.manage.api.channel.deal;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Struct;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alipay.manage.api.channel.util.shanfu.ShanFuUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.api.feign.ConfigServiceClient;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.service.OrderService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import otc.bean.config.ConfigFile;
import otc.common.PayApiConstant;
import otc.result.Result;
import otc.util.number.GenerateOrderNo;
@Component("ShanFuAlipayH5")
public class ShanFuAlipayH5 extends PayOrderService{
	private static final Log log = LogFactory.get();
	@Autowired ConfigServiceClient configServiceClientImpl;
    @Autowired OrderService orderServiceImpl;
    private  final static String SU = "1";
    private  final static String ER = "0";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static String getNowDateStr() {
	   return sdf.format(new Date());
	}
	@Override
	public Result deal(DealOrderApp dealOrderApp, String channelId) {
		log.info("【进入善付支付宝H5支付】");
		String create = create(dealOrderApp, channelId);
		if(StrUtil.isBlank(dealOrderApp.getBack())) {
			log.info("【同步跳转地址未传】");
			boolean orderEr = orderEr(dealOrderApp,"同步跳转地址未传");
			return 	  Result.buildFailMessage("同步跳转地址未传");
		}
		log.info("【本地订单创建成功，开始请求远程三方支付】");
		Result config = configServiceClientImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.SERVER_IP);
		log.info("【回调地址ip为："+config.toString()+"】" );
		Map<String,String> map = createOrder(create,config.getResult()+PayApiConstant.Notfiy.NOTFIY_API_WAI+"/shanfu-notfiy");
		String url = map.get(SU);
		if(StrUtil.isBlank(url)) {
			log.info("【请求失败】");
			String msg = map.get(ER);
			log.info("【请求失败，失败信息："+msg+"】");
			orderEr(dealOrderApp,msg);
			return Result.buildFailMessage(msg);
		}
		return Result.buildSuccessResultCode("支付处理中", url,1);
	}
	Map<String,String>  createOrder(String orderId,String notfiy){
		Map  mapR = new HashMap();
		DealOrder order = orderServiceImpl.findOrderByOrderId(orderId);
		String AuthorizationURL = ShanFuUtil.URL;
		String merchantId = ShanFuUtil.APPID;
		String keyValue = ShanFuUtil.KEY;
		String pay_bankcode=ShanFuUtil.BANKCODE;
		String pay_memberid = merchantId;//商户id
		String pay_orderid= orderId ;	//20位订单号 时间戳+6位随机字符串组成
		String pay_applydate=getNowDateStr();//yyyy-MM-dd HH:mm:ss
		String pay_amount =order.getDealAmount().intValue()+"";
		String pay_notifyurl = notfiy;
		String pay_callbackurl = order.getBack();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pay_memberid" , pay_memberid) ;
		map.put("pay_applydate" , pay_applydate) ;
		map.put("pay_orderid" , pay_orderid) ;
		map.put("pay_bankcode" , pay_bankcode) ;
		map.put("pay_notifyurl" , pay_notifyurl) ;
		map.put("pay_callbackurl" , pay_callbackurl) ;
		map.put("pay_amount" , pay_amount) ;
		String createParam = createParam(map);
		log.info("善付签名前加密串："+createParam);
		String pay_md5sign =  md5(createParam+"&key="+keyValue).toUpperCase();
		map.put("pay_productname" , "测试") ;
		map.put("pay_md5sign" , pay_md5sign) ;
		log.info("请求闪付参数为："+map.toString());
		String post = HttpUtil.post(AuthorizationURL, map);
		log.info("善付返回参数："+post);
		map = null;
		String unWrap = StrUtil.unWrap(post, "<a href='", "'>点此付费</a>");
		try {
			JSONObject parseObj = JSONUtil.parseObj(unWrap);
			Object object = parseObj.get("status");
			if(ObjectUtil.isNotNull(object)) {
				log.info("支付失败");
				mapR.put(ER, parseObj.get("msg"));
			}
		} catch (Exception e) {
			log.info("支付链接为转换后的字符为："+unWrap);
			mapR.put(SU, unWrap);
			return mapR;
		}
		return mapR;
	}
	public static String createParam(Map<String, Object> map) {
		try {
			if (map == null || map.isEmpty())
				return null;
			Object[] key = map.keySet().toArray();
			Arrays.sort(key);
			StringBuffer res = new StringBuffer(128);
			for (int i = 0; i < key.length; i++) 
				if(ObjectUtil.isNotNull(map.get(key[i])))
					res.append(key[i] + "=" + map.get(key[i]) + "&");
			String rStr = res.substring(0, res.length() - 1);
			return rStr;
		} catch (Exception e) {
			e.printStackTrace();
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
