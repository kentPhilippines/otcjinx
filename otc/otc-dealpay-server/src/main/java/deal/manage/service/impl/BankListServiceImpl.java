package deal.manage.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import deal.manage.bean.BankList;
import deal.manage.service.BankListService;
@Component
public class BankListServiceImpl implements BankListService {

	@Override
	public List<BankList> findBankCardByQr(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addBankcard(BankList bank) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BankList findBankByNo(String bankNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BankList> findBankInfoAccount(BankList bank) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteBankById(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<BankList> findBankCardByType(Integer bankApp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addBankCard(BankList bank) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updataQrStatusEr(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updataStatusSu(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BankList findBankInfoNo(String cardbank) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BankList> findDealBank(BigDecimal amount) {
		// TODO Auto-generated method stub
		return null;
	}

}
