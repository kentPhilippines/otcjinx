package deal.manage.service;

import deal.manage.bean.Withdraw;

public interface WithdrawService {
	/**
	 * <p>代付订单生成</p>
	 * @param witb
	 * @return
	 */
	boolean addOrder(Withdraw witb);

}
