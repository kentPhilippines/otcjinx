package alipay.manage.api.config;

import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.Withdraw;
import otc.result.Result;

public interface PayService {
	/**
	 * <p>交易请求【支付宝扫码】</p>
	 * @param dealOrderApp商户交易预订单
	 * @return
	 */
	Result deal(DealOrderApp dealOrderApp,String payType) ;
	/**
	 * <p>代付</p>
	 * @param wit
	 * @return
	 */
	Result withdraw(Withdraw wit);
	
	
	
}
