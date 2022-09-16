package alipay.manage.api.channel.wit;

import alipay.manage.api.config.PayOrderService;
import org.springframework.stereotype.Component;
import otc.api.alipay.Common;
import otc.bean.dealpay.Withdraw;
import otc.result.Result;

@Component(Common.Deal.WITHDRAW_MY)
public class WithdrawMy  extends PayOrderService{
	
	@Override
	public Result withdraw(Withdraw wit) {

		log.info(wit.getBankNo() + " ==== " + wit.toString());


		return Result.buildSuccessMessage("该笔代付已提交财务人员处理");


	}
}
