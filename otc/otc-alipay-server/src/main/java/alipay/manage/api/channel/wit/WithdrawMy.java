package alipay.manage.api.channel.wit;

import org.springframework.stereotype.Component;

import alipay.manage.api.config.PayOrderService;
import otc.api.alipay.Common;
import otc.bean.dealpay.Withdraw;
import otc.result.Result;

@Component(Common.Deal.WITHDRAW_MY)
public class WithdrawMy  extends PayOrderService{
	
	@Override
	public Result withdraw(Withdraw wit) {
		Result withdraw = super.withdraw(wit);
		if(withdraw.isSuccess())
			return Result.buildSuccessMessage("该笔代付已提交财务人员处理");
		return  Result.buildFailMessage("代付失败") ;
	}
}
