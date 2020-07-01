package alipay.manage.api.channel.wit;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import alipay.manage.api.channel.util.zhaunshi.Config;
import alipay.manage.api.channel.util.zhaunshi.Md5Util;
import alipay.manage.api.channel.util.zhaunshi.StringUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.api.feign.ConfigServiceClient;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import otc.api.alipay.Common;
import otc.bean.config.ConfigFile;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

//@Component(Common.Deal.WITHDRAW_ZAHUNSHI_ALIPAY)
public class ZuanShiDpay extends PayOrderService{
	private static final Log log = LogFactory.get();
	private   final String URL = "/gateway/api/v2/payouts";//钻石代付接口
	@Autowired ConfigServiceClient configServiceClientImpl;
	@Override
	public Result withdraw(Withdraw wit) {
		Result withdraw = super.withdraw(wit);
		if(withdraw.isSuccess()) {
			log.info("【进入钻石银行卡代付】");
				try {
					Double doubleValue = wit.getAmount().doubleValue();
					int intValue = doubleValue.intValue();
					create(wit.getBankcode(), intValue*100+"", wit.getOrderId(), wit.getBankNo(), wit.getAccname(),PayApiConstant.Notfiy.NOTFIY_API_WAI+"/zuanshiDpay-notfiy");
				} catch (Exception e) {
					log.error("【错误信息打印】"+e.getMessage());
					return  Result.buildFailMessage("代付失败");
					}
		}
		return  Result.buildSuccessMessage("代付成功等待处理");
	}
	
	/**
	 * <p>钻石银行卡代付</p>
	 * @param bankId			银行卡id
	 * @param amount			金额【分】
	 * @param orderId			订单号
	 * @param bankAcc			银行卡账号
	 * @param accName			银行卡开户人
	 * @throws Exception 
	 */
	  private   void create(String bankId , String amount ,
							String orderId , String bankAcc ,
							String accName,String notfiy) throws   Exception {/*
		  Result config = configServiceClientImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.SERVER_IP);
		  String ip = config.getResult().toString();
		  log.info("【拿到回调值为："+ip+"】");
	        Map<String, Object> data = new TreeMap<String, Object>();
	        long request_time = new Date().getTime() / 1000;
	        data.put("bank_id", bankId);//BankEnum.ABC.getBankId()
	        data.put("platform_channel_id", "PFC00000010");//充值代付钱包
	        data.put("amount", amount );//代付金额
	        data.put("number", bankAcc);//收款人银行账号
	        data.put("name", accName);//收款人账户名
	        data.put("platform_id", Config.PLATFORM_ID);//我方商户号
	        data.put("payout_cl_id", orderId);//商户订单号
	        data.put("request_time", request_time);//请求时间【秒】
	        data.put("notify_url", ip+notfiy);//代付回调地址
	        data.put("service_id", Config.PAYOUT_SERVICE_ID);//服务Id请填入SVC0004(银行卡代付)
	        log.info("【加密前参数】"+data.toString());
	        String generatedSign = Md5Util.md5(StringUtil.convertToHashMapToQueryString(data), Config.KEY);
	        log.info(generatedSign);
	        data.put("sign", generatedSign);
	        String jsonString = JSON.toJSONString(data);
	        log.info("【请求前参数："+jsonString+"】");
	        SSLContext ctx = SSLContext.getInstance("TLSv1.2");
            ctx.init(null, null, null);
            SSLContext.setDefault(ctx);  
            System.setProperty("https.protocols", "TLSv1.2");
	        String responseBody = new OkHttpClient().newCall(new Request.Builder().url(Config.DOMAIN + URL)
	                .post(RequestBody.create(Config.JSON, jsonString)).build()).execute().body().string();
         log.info("响应参数："+responseBody);*/
	    }

}
