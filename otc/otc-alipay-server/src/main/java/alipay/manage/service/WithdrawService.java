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
	 *
	 * @param orderId
	 * @return
	 */
	Withdraw findOrderId(String orderId);

	Withdraw findOrderByApp(String appId, String appOrderId);

	/**
	 * 代付订单单纯修改为失败，兼容账户扣减失败
	 *
	 * @param orderId
	 */
	void updateWitError(String orderId);
}
