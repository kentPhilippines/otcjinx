package alipay.manage.service;

import alipay.manage.bean.RunOrder;

import java.util.List;

public interface RunOrderService {

	boolean addOrder(RunOrder run);

	List<RunOrder> findAssOrder(String associatedId);

}
