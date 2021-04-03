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

	void updateOrderSu(String orderId,String orderStatus);

	/**
	 * <p>商户订单查询接口</p>
	 *
	 * @param appId      商户号
	 * @param appOrderId 订单号
	 * @return
	 */
	DealOrderApp findOrderByApp(String appId, String appOrderId);

	DealOrderApp findOrderByAppSum(String appId);
}
