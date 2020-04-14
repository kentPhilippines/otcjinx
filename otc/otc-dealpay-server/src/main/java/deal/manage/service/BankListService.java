package deal.manage.service;

import java.math.BigDecimal;
import java.util.List;

import deal.manage.bean.BankList;


public interface BankListService {

	/**
	 * <p>查询自己的银行卡</p>
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

	/**
	 * <p>通过银行卡号查询当前银行卡信息</p>
	 * @param bankNo
	 * @return
	 */
	BankList findBankByNo(String bankNo);

	/**
	 * <p>查询与自己相关的银行卡信息</p>
	 * @param bank
	 * @return
	 */
	List<BankList> findBankInfoAccount(BankList bank);
    /**
     * <p>查询与自己所有相关的银行卡信息</p>
     * @param bank
     * @return
     */
	List<BankList> findAllBankInfoAccount(BankList bank);
	/**
	 * <p>删除一个银行卡根据数据id</p>
	 * @param id
	 * @return
	 */
	boolean deleteBankById(String id);

	/**
	 * <p>根据银行卡卡类型获取银行卡</p>
	 * @param bankApp
	 * @return
	 */
	List<BankList> findBankCardByType(Integer bankApp);

	/**
	 * <p>添加银行卡</p>
	 * @param bank
	 * @return
	 */
	boolean addBankCard(BankList bank);
	/**
	 * <p>编辑银行卡</p>
	 * @param bank
	 * @return
	 */
	boolean editBankCard(BankList bank);
	/**
	 * <p>修改当前银行卡为不可接单</p>
	 * @param id
	 * @return
	 */
	boolean updataQrStatusEr(String id);

	/**
	 * <p>修改当前银行卡为可接单</p>
	 * @param id
	 * @return
	 */
	boolean updataStatusSu(String id);

	/**
	 * <p>根据银行卡系统编号查询银行卡具体信息</p>
	 * @param cardbank
	 * @return
	 */
	BankList findBankInfoNo(String cardbank);

	List<BankList> findDealBank(BigDecimal amount);
     /**
      * 通过银行卡Id查询关联的用户
      * @param bank
      * @return
      */
	List<BankList> findBankCardById(BankList bank);


	
	
	
	/**
	 * <p>获取系统的银行卡</p>
	 * @return
	 */
	List<BankList> findSystemBank();





}
