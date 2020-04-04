package deal.manage.service.impl;

import org.springframework.stereotype.Component;

import deal.manage.bean.OrderStatus;
import deal.manage.service.OrderStatusService;
@Component
public class OrderStatusServiceImpl implements OrderStatusService {

	@Override
	public OrderStatus queryByOrderId(String orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertOrderstatus(OrderStatus orderS) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateOrderStatus(OrderStatus orderS) {
		// TODO Auto-generated method stub
		return false;
	}

}
