package alipay.manage.service;

import alipay.manage.bean.DealOrderApp;

public interface OrderAppService {

	boolean add(DealOrderApp dealApp);

	/**
	 * <p>根据订单号，查询商户订单</p>
	 * @param orderId				商户订单号
	 * @return
	 */
	DealOrderApp findOrderByOrderId(String orderId);

	
	/**
	 * <p>通过关联订单号查询数据</p>
	 * @param orderId
	 * @return
	 */
	DealOrderApp findAssOrder(String orderId);

	/**
	 * <p>修改商户预订单状态</p>
	 * @param orderId
	 * @param msg
	 */
	void updateOrderEr(String orderId, String msg);

}
