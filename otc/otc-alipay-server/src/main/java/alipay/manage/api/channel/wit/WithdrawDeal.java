package alipay.manage.api.channel.wit;

import org.bouncycastle.pqc.jcajce.provider.rainbow.SignatureSpi.withSha224;
import org.springframework.stereotype.Component;

import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.Withdraw;
import otc.api.alipay.Common;
import otc.result.Result;
/**
 * <p>对接上游代付渠道</p>
 * @author K
 */
@Component(Common.Deal.WITHDRAW_DEAL)
public class WithdrawDeal extends PayOrderService{
	@Override
	public Result withdraw(Withdraw wit) {
		Result withdraw = super.withdraw(wit);
		if(withdraw.isSuccess())
			return Result.buildSuccessMessage("代付处理中，等待财务人员出款");
		return Result.buildFailMessage("代付失败，联系运营人员手动失败") ;
	}
}
