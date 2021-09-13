package alipay.manage.api.config;

import alipay.manage.bean.DealOrderApp;
import otc.bean.dealpay.Withdraw;
import otc.result.Result;

public interface PayService {
	/**
	 * <p>交易请求【支付宝扫码】</p>
	 * @return
	 */
	Result deal(DealOrderApp dealOrderApp,String payType) throws Exception;
	/**
	 * <p>代付</p>
	 * @param wit
	 * @return
	 */
	Result withdraw(Withdraw wit);




	boolean witCheckBank(String bankCode);



}
