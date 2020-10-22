package alipay.manage.api.channel.wit;

import alipay.manage.api.config.PayOrderService;
import alipay.manage.api.feign.DealpayServiceClien;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.api.alipay.Common;
import otc.bean.dealpay.Withdraw;
import otc.result.Result;
/**
 * <p>对接上游代付渠道</p>
 * @author K
 */
@Component(Common.Deal.WITHDRAW_DEAL)
public class WithdrawDeal extends PayOrderService{
	@Autowired DealpayServiceClien dealpayServiceClienImpl;
	@Override
	public Result withdraw(Withdraw wit) {
			Result result = dealpayServiceClienImpl.wit(wit);
			if(result.isSuccess())
				return result;
			return Result.buildFailMessage("代付失败，联系运营人员手动失败") ;
	}
}
