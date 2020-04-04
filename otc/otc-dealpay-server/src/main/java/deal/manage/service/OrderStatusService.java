package deal.manage.service;

import deal.manage.bean.OrderStatus;

public interface OrderStatusService {

	/**
	 * <p>查看订单状态</p>
	 * @param orderId
	 * @return
	 */
	OrderStatus queryByOrderId(String orderId);

	/**
	 * <p>增加订单状态</p>
	 * @param orderS
	 * @return
	 */
	boolean insertOrderstatus(OrderStatus orderS);

	/**
	 * <p>修改订单状态</p>
	 * @param orderS
	 * @return
	 */
	boolean updateOrderStatus(OrderStatus orderS);

}
