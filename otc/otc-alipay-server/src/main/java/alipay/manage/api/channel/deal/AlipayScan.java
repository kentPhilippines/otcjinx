package alipay.manage.api.channel.deal;

import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import org.springframework.stereotype.Component;
import otc.api.alipay.Common;
import otc.result.Result;

/**
 * <p>支付宝扫码支付处理类</p>
 * @author kent
 *
 */
@Component("TEST_" + Common.Deal.PRODUCT_ALIPAY_SCAN)
public class AlipayScan extends PayOrderService{
	@Override
	public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
		return super.deal(dealOrderApp, channel);
	}

	@Override
	public Result dealAlipayScan(DealOrderApp dealOrderApp) {
		// TODO Auto-generated method stub
		return super.dealAlipayScan(dealOrderApp);
	}

}
