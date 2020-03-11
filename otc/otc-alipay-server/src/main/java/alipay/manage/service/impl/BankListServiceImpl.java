package alipay.manage.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import alipay.manage.bean.BankList;
import alipay.manage.service.BankListService;
@Component
public class BankListServiceImpl implements BankListService{
	@Override
	public List<BankList> findBankCardByQr(String userId) {
		return null;
	}
	@Override
	public boolean addBankcard(BankList bank) {
		return false;
	}
}
