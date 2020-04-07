package deal.manage.service;

import java.util.List;

import otc.bean.dealpay.Recharge;

public interface RechargeService {
	/**
	 * <p>根据当前订单查询充值订单号</p>
	 * @param orderId
	 * @return
	 */
	Recharge findOrderId(String orderId);

	
	/**
	 * <p>新建充值订单</p>
	 * @param order
	 * @return
	 */
	boolean addOrder(Recharge order);


	
	List<Recharge> findRechargeOrder(Recharge bean);


	/**
	 * <p>订单置为失败，说明原因</p>
	 * @param orderId			订单号
	 * @param message			原因
	 * @return
	 */
	boolean updateStatusEr(String orderId, String message);

}
