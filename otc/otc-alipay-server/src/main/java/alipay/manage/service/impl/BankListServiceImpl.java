package alipay.manage.service.impl;

import alipay.manage.bean.BankList;
import alipay.manage.bean.BankListExample;
import alipay.manage.mapper.BankListMapper;
import alipay.manage.service.BankListService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;
import otc.api.alipay.Common;
import otc.util.number.Number;

import javax.annotation.Resource;
import java.util.List;

@Component
public class BankListServiceImpl implements BankListService {
	@Resource
	BankListMapper bankListMapper;

	@Override
	public List<BankList> findBankCardByQr(String userId) {
		BankListExample example = new BankListExample();
		BankListExample.Criteria criteria = example.createCriteria();
		if (StrUtil.isBlank(userId)) {
			criteria.andAccountEqualTo(userId);
		}
		List<BankList> selectByExample = bankListMapper.selectByExample(example);
		return selectByExample;
	}



	@Override
	public boolean addBankcard(BankList bank) {
		BankListExample example = new BankListExample();
		BankListExample.Criteria criteria = example.createCriteria();
		String bank2 = Number.getBank();
		bank.setBankcardId(bank2);
		BankList bank1 = new BankList();
		bank1.setAccount(bank.getAccount());
		bank1.setIsDeal(1); //数据逻辑删除
		criteria.andAccountEqualTo(bank.getAccount());
		bank1.setStatus(0);//数据无效
		bankListMapper.updateByExampleSelective (bank1, example);
		bank.setIsDeal(2); //数据逻辑可用
		bank.setCardType(Common.Bank.BANK_QR);//码商上传的银行卡；
		int insertSelective = bankListMapper.insertSelective(bank);
		return insertSelective > 0 && insertSelective < 2;
	}


	@Override
	public BankList findBankByNo(String bankNo) {
		return bankListMapper.selectBankCardByBankNo(bankNo);
	}
}
