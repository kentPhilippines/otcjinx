package deal.manage.service;

import java.util.List;

import deal.manage.bean.Runorder;

public interface RunOrderService {

	boolean addOrder(Runorder run);

	/**
	 * <p>分页查询流水</p>
	 * @param order
	 * @return
	 */
	List<Runorder> findOrderRunByPage(Runorder order);

}
