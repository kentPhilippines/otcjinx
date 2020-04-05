package deal.manage.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import deal.manage.bean.DealOrder;
import deal.manage.bean.Runorder;
import deal.manage.bean.Withdraw;
import deal.manage.service.OrderService;
import otc.bean.dealpay.Recharge;
@Component
public class OrderServiceImpl implements OrderService {

	@Override
	public List<DealOrder> findOrderByUser(String userId, String createTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DealOrder findOrderByAssociatedId(String orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Runorder> findOrderRunByPage(Runorder order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DealOrder> findOrderByPage(DealOrder order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Recharge> findRechargeOrder(Recharge bean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Withdraw> findWithdrawOrder(Withdraw bean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DealOrder findOrderByOrderId(String orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateOrderStatus(String orderId, String status, String mag) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<DealOrder> findMyOrder(DealOrder order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addOrder(DealOrder orderApp) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updataOrderStatusByOrderId(String orderId, String s) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updataOrderisNotifyByOrderId(String orderId, String isNotify) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addRechargeOrder(Recharge order) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updataOrderStatusByOrderId(String orderId, String string, boolean b) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<DealOrder> findOrderByUser(String userId, String orderType, String formatDateTime,
			String formatDateTime2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateOrderStatus(String orderId, String orderStatusSu) {
		// TODO Auto-generated method stub
		return false;
	}

}
