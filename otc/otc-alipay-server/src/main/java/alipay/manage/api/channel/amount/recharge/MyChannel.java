package alipay.manage.api.channel.amount.recharge;

import org.springframework.stereotype.Component;

import alipay.manage.api.channel.amount.AmountObject;
import otc.bean.dealpay.Recharge;
import otc.result.Result;

/**
 * <p>自己的充值渠道</p>
 * @author K
 *
 */
@Component("MyChannelRecharge")
public class MyChannel extends AmountObject{
		private static final String userId = "当前充值渠道的商户号";
	
	@Override
	public Result recharge(Recharge rechaege) {
		//TODO 填充码商充值渠道        以下为充值接口
		/**		字段			数据类型 		是否必传			字段解释
		 * 		appkey	 	[int]				是					商户id	 
				kyc	 		[int]				是					实名认证	 
				username 	[string]			是					用户姓名	 
				area_code	[string]								国际区号(不传默认86，香港852/台湾886/澳门853)	 
				phone	 	[string]			是					手机号	 
				email	 	[string]								邮箱	 
				id_card_type [int]									证件类型(1.身份证 2.护照 3.其他)	 
				id_card_num	 [string]								证件号码 
				company_order_num	 [string]	是					商户订单号	 
				coin_sign	 [string]			是					数字货币标识(USDT)	 
				coin_sum	 [string]			是					交易总额CNY与coin_amount 二选一	 
				coin_amount	 [string]			是					实际成交数量 与coin_sum 二选一	 
				order_amount	 [string]							客户展示数量（最多接收四位小数）	 
				goods_name	 [string]								商品名称	 
				order_time	 [string]			是					订单时间戳(北京时间)	 
				sync_url	 [string]			是					同步回调地址	 
				async_url	 [string]			是					异步回调地址	 
				sign	 	 [string]			是					签名
		 */
		
		return super.recharge(rechaege);
	}

	
	

}
