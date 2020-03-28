package alipay.manage.api.channel.amount;

import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.Recharge;
import alipay.manage.bean.Withdraw;
import otc.result.Result;

public interface AmountChannel {
	/**
	 * <p>充值交易请求【对接上游充值渠道】</p>
	 * @param  rechaege			充值订单
	 * @return
	 */
	Result recharge( Recharge rechaege ) ;
	/**
	 * <p>代付</p>
	 * @param wit				代付订单
	 * @return
	 */
	Result withdraw(Withdraw wit);
	
	

}
