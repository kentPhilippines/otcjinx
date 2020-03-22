package alipay.manage.service.impl;

import java.util.List;

import alipay.manage.bean.BankListExample;
import alipay.manage.mapper.BankListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alipay.manage.bean.BankList;
import alipay.manage.service.BankListService;
import otc.api.alipay.Common;

import javax.validation.constraints.NotNull;

@Component
public class BankListServiceImpl implements BankListService{
	@Autowired
	BankListMapper bankListMapper;

	@Override
	public List<BankList> findBankCardByQr(@NotNull String accountId) {
		BankListExample example = new BankListExample();
		BankListExample.Criteria criteria = example.createCriteria();
//		criteria.andQrAccountEqualTo(accountId);
		criteria.andIsDealEqualTo(2); //数据逻辑可用
		List<BankList> selectByExample = bankListMapper.selectByExample(example);
		return selectByExample;
	}


	@Override
	public boolean addBankcard(BankList bank) {
		return false;
	}


	@Override
	public BankList findBankByNo(String bankNo) {
		return null;
	}
}
