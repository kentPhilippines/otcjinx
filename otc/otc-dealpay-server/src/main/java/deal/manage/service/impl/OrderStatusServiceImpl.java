package deal.manage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;
import deal.manage.bean.OrderStatus;
import deal.manage.bean.OrderStatusExample;
import deal.manage.bean.OrderStatusExample.Criteria;
import deal.manage.mapper.OrderStatusMapper;
import deal.manage.service.OrderStatusService;
@Component
public class OrderStatusServiceImpl implements OrderStatusService {
	@Autowired OrderStatusMapper orderStatusDao;
	@Override
	public OrderStatus queryByOrderId(String orderId) {
		return orderStatusDao.queryByOrderId(orderId);
	}

	@Override
	public boolean insertOrderstatus(OrderStatus orderS) {
		int selective = orderStatusDao.insertSelective(orderS);
		return selective > 0 && selective < 2;
	}

	@Override
	public boolean updateOrderStatus(OrderStatus orderS) {
		if(StrUtil.isBlank(orderS.getOrderId()))
			return false;
		OrderStatusExample example = new OrderStatusExample();
		Criteria criteria = example.createCriteria();
		criteria.andOrderIdEqualTo(orderS.getOrderId());
		int updateByExampleSelective = orderStatusDao.updateByExampleSelective(orderS, example);
		return updateByExampleSelective > 0 && updateByExampleSelective < 2;
		
	}

}
