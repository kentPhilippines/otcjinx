package alipay.manage.service.impl;

import java.util.List;
import alipay.manage.util.Number;
import alipay.manage.bean.BankListExample;
import alipay.manage.mapper.BankListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alipay.manage.bean.BankList;
import alipay.manage.service.BankListService;
import otc.api.alipay.Common;

@Component
public class BankListServiceImpl implements BankListService{
	@Autowired
	BankListMapper bankListMapper;

	@Override
	public List<BankList> findBankCardByQr(String userId) {
		List<BankList> selectByExample = bankListMapper.selectBankCardByQ(userId);
		System.out.println("获取selectByExample " + selectByExample);
		return selectByExample;
	}



	@Override
	public boolean addBankcard(BankList bank) {
		BankListExample example = new BankListExample();
		BankListExample.Criteria criteria = example.createCriteria();
		bank.setBankcardId(Number.getbankNo());
		BankList bank1 = new BankList();
		bank1.setAccount(bank.getAccount());
		bank1.setIsDeal(1); //数据逻辑删除
		criteria.andAccountEqualTo(bank.getAccount());
		bank1.setStatus(0);//数据无效
		bankListMapper.updateByExampleSelective (bank1, example);
		bank.setIsDeal(2); //数据逻辑可用
		bank.setCardType(1);//码商上传的银行卡；
		int insertSelective = bankListMapper.insertSelective(bank);
		return insertSelective > 0 && insertSelective < 2;
	}
}
