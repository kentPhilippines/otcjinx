package alipay.manage.api.config;

import alipay.manage.bean.DealOrderApp;
import otc.bean.dealpay.Withdraw;
import otc.result.Result;

import java.util.Map;

public interface PayServiceV2 {
	/**
	 * <p>交易请求【支付宝扫码】</p>
	 * @return
	 */
	Result deal(DealOrderApp dealOrderApp,String payType)  ;
	/**
	 * <p>代付</p>
	 * @param wit
	 * @return
	 */
	Result withdraw(Withdraw wit,String payType);


	/**
	 * 余额查询
	 * @return
	 */
	Result findBalance(String channelId , String payType);

	String dealNotify(Map<String,Object> map);

	String witNotify(Map<String,Object> map);






}
