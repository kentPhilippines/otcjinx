package alipay.manage.service;

import alipay.manage.bean.BankList;

import java.util.List;

public interface BankListService {

	/**
	 * <p>查询自己的银行卡</p>
	 *
	 * @param userId
	 * @return
	 */
	List<BankList> findBankCardByQr(String userId);

	/**
	 * <p>添加一张银行卡</p>
	 *
	 * @param bank
	 * @return
	 */
	boolean addBankcard(BankList bank);

	BankList findBankByNo(String bankNo);

	/**
	 * 根据系统账户id查询绑定卡号
	 *
	 * @param usdtPay
	 * @return
	 */
	List<BankList> findBankByAccount(String usdtPay);
}
