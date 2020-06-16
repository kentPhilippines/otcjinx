package alipay.manage.api.channel.wit;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Struct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alipay.manage.api.channel.util.haofu.HaoFuUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.api.feign.ConfigServiceClient;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import otc.api.alipay.Common;
import otc.bean.config.ConfigFile;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

@Component("HaoFuDpay")
public class HaoFuDpay extends PayOrderService{
	private static final Log log = LogFactory.get();
	@Autowired ConfigServiceClient configServiceClientImpl;
	@Override
	public Result withdraw(Withdraw wit) {
		Result withdraw = super.withdraw(wit);
		if(withdraw.isSuccess()) {
			log.info("【进入豪富代付】");
				try {
					log.info("【本地订单创建成功，开始请求远程三方代付接口】");
					Result config = configServiceClientImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.SERVER_IP);
					log.info("【回调地址ip为："+config.toString()+"】" );
					String createDpay = createDpay(config.getResult().toString()+PayApiConstant.Notfiy.NOTFIY_API_WAI+"/HaoFuDpay-noyfit", wit.getOrderId(), wit.getAmount().doubleValue()+"",  wit.getBankcode(), wit.getAccname(), wit.getBankNo());
					//create(wit.getBankcode(), intValue*100+"", wit.getOrderId(), wit.getBankNo(), wit.getAccname(),PayApiConstant.Notfiy.NOTFIY_API_WAI+"/zuanshiDpay-notfiy");
				if(StrUtil.isNotBlank(createDpay) && createDpay.equals("SUCCESS") )
					return  Result.buildSuccessMessage("代付成功等待处理");
				else
					return Result.buildFailMessage(createDpay);
				} catch (Exception e) {
					log.error("【错误信息打印】"+e.getMessage());
					return  Result.buildFailMessage("代付失败");
					}
		}
		return  Result.buildSuccessMessage("代付成功等待处理");
	}
	
	String createDpay(String notify ,String orderId , String money,String bankcode ,String accname,String bankNo) {
		Map<String,Object> map = new HashMap<String,Object>();
		/**
		 * 		partner				32			是	是			商户合作号，由平台注册提供
				notify_url			256			是	是			回调地址
				request_time		20			是	是			时间戳,精确到秒
				trade_no			256			是	是			订单号
				amount				20			是	是			金额(单位:元,支持两位小数)
				bank_sn				20			是	是			银行编码
				bank_site_name		1024		是	是			银行所在地
				bank_account_name	32			是	是			账户姓名
				bank_province		10			是	是			省
				bank_city			3			是	是			城市
				bank_account_no		256			是	是			账户账号
				remark				20			否	是	
				bus_type			20			否	是			交易类型:固定值0
				bank_mobile_no		13			是	是			手机号
				sign				64			是	否			签名字符串
		 */
		String partner = HaoFuUtil.APPID;
		String notify_url = notify;
		String request_time = System.currentTimeMillis()/1000 +"";
		String trade_no = orderId;
		String amount = money;
		String bank_sn = bankcode;
		String bank_site_name = "北京市";
		String bank_province = "朝阳区";
		String bank_account_name = accname;
		String bank_city = "北京";
		String bank_account_no = bankNo;
		String remark = "";
		String bus_type = "";
		String bank_mobile_no = "13888888888";
		String sign = "";
		map.put("partner", partner);
		map.put("notify_url", notify_url);
		map.put("request_time", request_time);
		map.put("trade_no", trade_no);
		map.put("amount", amount);
		map.put("bank_site_name", bank_site_name);
		map.put("bank_sn", bank_sn);
		map.put("bank_province",bank_province);
		map.put("bank_account_name", bank_account_name);
		map.put("bank_city", bank_city);
		map.put("bank_account_no", bank_account_no);
		map.put("remark", remark);
		map.put("bus_type", bus_type);
		map.put("bank_mobile_no", bank_mobile_no);
		String createParam = createParam(map);
		String md5 = md5(createParam+"&"+HaoFuUtil.KEY);
		map.put("sign", md5);
		log.info("【当前豪富代付请求参数为："+map.toString()+"】");
		String post = HttpUtil.post(HaoFuUtil.URL+"/payCenter/agentPay", map);
		log.info("【豪富代付响应参数为："+post+"】");
		JSONObject parseObj = JSONUtil.parseObj(post);
		Object object = parseObj.get("is_success");
		if(ObjectUtil.isNotNull(object)) {
			log.info("当前豪富的订单为："+object+"");
			if(object.equals("T")) {
					return "SUCCESS";
			}else {
				Object object2 = parseObj.get("fail_msg");
				if(ObjectUtil.isNotNull(object2)) {
					log.info("【代付错误数据为："+object2+"】");
					return object2.toString();
				}
			}
		}
		return "";
	}
	public static String createParam(Map<String, Object> map) {
		try {
			if (map == null || map.isEmpty())
				return null;
			Object[] key = map.keySet().toArray();
			Arrays.sort(key);
			StringBuffer res = new StringBuffer(128);
			for (int i = 0; i < key.length; i++) 
				if(ObjectUtil.isNotNull(map.get(key[i])) && map.get(key[i]) != "")
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
