package deal.manage.util;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import deal.manage.bean.BankList;
import deal.manage.service.BankListService;

@Component
public class BankUtil {
	@Autowired BankListService bankListServiceImpl;
	/**
	 * <p>根据交易金额选定交易银行卡</p>
	 * @param amount
	 * @return
	 */
	public List<BankList> findBank(BigDecimal amount) {
		/**
		 * 1,当前卡商总开关开启
		 * 2,当前卡商入款接单开启
		 * 3,当前卡商余额够
		 * 4,获取当前卡商开启的银行卡
		 * 5,组成银行卡集合
		 */
		return bankListServiceImpl.findDealBank(amount);
	}

}
