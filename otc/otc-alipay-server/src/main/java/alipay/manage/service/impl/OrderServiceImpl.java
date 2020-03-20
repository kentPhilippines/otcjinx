package alipay.manage.service.impl;

import java.util.List;

import alipay.manage.mapper.DealOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alipay.manage.bean.DealOrder;
import alipay.manage.bean.Recharge;
import alipay.manage.bean.RunOrder;
import alipay.manage.bean.Withdraw;
import alipay.manage.service.OrderService;
@Component
public class OrderServiceImpl implements OrderService{
    @Autowired
	DealOrderMapper dealOrderMapper;
	@Override
	public List<DealOrder> findOrderByUser(String userId, String createTime) {
		List<DealOrder> selectByExample = dealOrderMapper.selectByExampleByMyId(userId,createTime);
		return selectByExample;
	}


	@Override
	public DealOrder getOrderByAssociatedId(String orderId) {
		return null;
	}
	@Override
	public List<RunOrder> findOrderRunByPage(RunOrder order) {
		return null;
	}
	@Override
	public List<DealOrder> findOrderByPage(DealOrder order) {
		return null;
	}
	@Override
	public List<Recharge> findRechargeOrder(Recharge bean) {
		return null;
	}

	@Override
	public List<Withdraw> findWithdrawOrder(Withdraw bean) {
		return null;
	}
	@Override
	public DealOrder findOrderByOrderId(String orderId) {
		return null;
	}
	@Override
	public boolean updateOrderStatus(String orderId, String status, String mag) {
		return false;
	}
}
