package deal.manage.service;

import java.util.List;

import deal.manage.bean.Withdraw;

public interface WithdrawService {
	/**
	 * <p>代付订单生成</p>
	 * @param witb
	 * @return
	 */
	boolean addOrder(Withdraw witb);

	
	/**
	 * <p>根据代付订单号查询代付订单</p>
	 * @param orderId
	 * @return
	 */
	Withdraw findOrderId(String orderId);


	List<Withdraw> findWithdrawOrder(Withdraw bean);

}
