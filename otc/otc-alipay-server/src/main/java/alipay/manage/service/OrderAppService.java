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

}
