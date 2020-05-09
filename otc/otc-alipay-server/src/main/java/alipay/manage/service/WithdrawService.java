package alipay.manage.service;

import otc.bean.dealpay.Withdraw;

public interface WithdrawService {
	/**
	 * <p>代付订单生成</p>
	 * @param witb
	 * @return
	 */
	boolean addOrder(Withdraw witb);

	/**
	 * <p>根据代付订单号查询订单</p>
	 * @param orderId
	 * @return
	 */
	Withdraw findOrderId(String orderId);

}
