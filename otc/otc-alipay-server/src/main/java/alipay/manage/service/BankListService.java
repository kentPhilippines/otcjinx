package alipay.manage.service;

import java.util.List;

import alipay.manage.bean.BankList;

public interface BankListService {

	/**
	 * <p>查询自己的订单</p>
	 * @param userId
	 * @return
	 */
	List<BankList> findBankCardByQr(String userId);

	/**
	 * <p>添加一张银行卡</p>
	 * @param bank
	 * @return
	 */
	boolean addBankcard(BankList bank);

}
