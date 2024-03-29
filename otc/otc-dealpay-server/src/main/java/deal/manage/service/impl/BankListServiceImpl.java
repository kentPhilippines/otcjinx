package deal.manage.service.impl;

import cn.hutool.core.util.StrUtil;
import deal.manage.bean.BankList;
import deal.manage.bean.BankListExample;
import deal.manage.bean.BankListExample.Criteria;
import deal.manage.mapper.BankListMapper;
import deal.manage.service.BankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.util.number.Number;

import java.math.BigDecimal;
import java.util.List;
@Component
public class BankListServiceImpl implements BankListService {
	@Autowired BankListMapper bankListDao;
	@Override
	public List<BankList> findBankCardByQr(String userId) {
		return bankListDao.findBankCardByQr(userId);
	}

	@Override
	public boolean addBankcard(BankList bank) {
		int insertSelective = bankListDao.insertSelective(bank);
		return insertSelective > 0 && insertSelective < 2;
	}

	@Override
	public BankList findBankByNo(String bankNo) {
		return bankListDao.findBankByNo(bankNo);
	}

	@Override
	public List<BankList> findBankInfoAccount(BankList bank) {
		BankListExample example = new BankListExample();
		Criteria criteria = example.createCriteria();
		if (StrUtil.isNotBlank(bank.getAccount())) {
			criteria.andAccountEqualTo(bank.getAccount());
		}
		if (StrUtil.isNotBlank(bank.getAccountHolder())) {
			criteria.andAccountHolderEqualTo(bank.getAccountHolder());
		}
		if (StrUtil.isNotBlank(bank.getBankcardId())) {
			criteria.andBankcardAccountEqualTo(bank.getBankcardId());
		}
		if (StrUtil.isNotBlank(bank.getBankType())) {
			criteria.andBankTypeEqualTo(bank.getBankType());
		}
		if (StrUtil.isNotBlank(bank.getOpenAccountBank())) {
			criteria.andOpenAccountBankEqualTo(bank.getOpenAccountBank());
		}
		if (StrUtil.isNotBlank(bank.getBankcardAccount())) {
			criteria.andBankcardAccountEqualTo(bank.getBankcardAccount());
		}
		if (StrUtil.isNotBlank(bank.getBankcode())) {
			criteria.andBankcodeEqualTo(bank.getBankcode());
		}
		if (StrUtil.isNotBlank(String.valueOf(bank.getStatus()))) {
			criteria.andBankStatusEqualTo(String.valueOf(bank.getStatus()));
		}

		List<BankList> selectByExample = bankListDao.selectByExample(example);
		return selectByExample;
	}
	
	@Override
	public List<BankList> findAllBankInfoAccount(BankList bank) {
		// 查询与自己所有相关的银行卡信息
		BankListExample example = new BankListExample();
		Criteria criteria = example.createCriteria();
		if (StrUtil.isNotBlank(bank.getAccount())) {
			criteria.andAccountEqualTo(bank.getAccount());
		}
		List<BankList> selectByExample = bankListDao.selectByExample(example);
		return selectByExample;
	}
	
	@Override
	public boolean deleteBankById(String id) {
		int a = bankListDao.deleteId(id);
		return a > 0 && a  < 2;
	}

	@Override
	public List<BankList> findBankCardByType(Integer bankApp) {
		return bankListDao.findBankCardByType(bankApp);
	}

	@Override
	public boolean addBankCard(BankList bank) {
		bank.setBankcardId(Number.getBank());
		int insertSelective = bankListDao.insertSelective(bank);
		return insertSelective > 0 && insertSelective < 2;
	}
	@Override
	public boolean editBankCard(BankList bank) {
		// 编辑银行卡
		int updateSelective=bankListDao.updateByPrimaryKey(bank);
		return updateSelective>0 && updateSelective<2;
	}
	@Override
	public boolean updataQrStatusEr(String id) {
		int a = bankListDao.updataQrStatusEr(id);
		return a > 0 && a < 2;
	}

	@Override
	public boolean updataStatusSu(String id) {
		int a = bankListDao.updataStatusSu(id);
		return a > 0 && a < 2;
	}

	@Override
	public BankList findBankInfoNo(String cardbank) {
		return bankListDao.findBankInfoNo(cardbank);
	}
	
	@Override
	public List<BankList> findBankCardById(BankList bank) {
		// 通过银行卡Id查询关联的用户
		BankListExample example = new BankListExample();
		Criteria criteria = example.createCriteria();
		if (StrUtil.isNotBlank(bank.getAccount())) {
			criteria.andAccountEqualTo(bank.getAccount());
		}
		if (StrUtil.isNotBlank(String.valueOf(bank.getId()))) {
			criteria.andIdEqualTo(bank.getId());
		}
		return bankListDao.selectByExample(example);
	}

	@Override
	public List<BankList> findDealBank(BigDecimal amount) {
		return bankListDao.findDealBank(amount);
	}

	@Override
	public List<BankList> findSystemBank() {
		return bankListDao.findSystemBank();
	}






}
