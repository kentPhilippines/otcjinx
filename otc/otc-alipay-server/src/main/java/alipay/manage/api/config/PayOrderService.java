package alipay.manage.api.config;

import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.Withdraw;
import otc.api.alipay.Common;
import otc.result.Result;

/**
 * <p>请求交易抽象【交易】【代付】</p>
 * @author kent
 */
public abstract class PayOrderService implements PayService{
	//TODO 未确认 是否加入四方交易逻辑
	@Override
	public Result deal(DealOrderApp dealOrderApp,String payType) {
		if(Common.Deal.PRODUCT_ALIPAY_SCAN.equals(payType))
			return dealAlipayScan(dealOrderApp);
		return null;
	}
	/**
	 * <p>支付宝扫码支付实体</p>
	 */
	public Result dealAlipayScan(DealOrderApp dealOrderApp) {
		/**
		 * #############################
		 * 生成预订单病返回支付连接
		 */
		return null;
	}
	/**
	 * <p>支付宝H5</p>
	 */
	public Result dealAlipayH5(DealOrderApp dealOrderApp) {
		/**
		 * ############################
		 * 生成预订单并返回支付连接
		 */
		return null;
	}
	
	/**
	 * <p>代付</p>
	 */
	@Override
	public Result withdraw(Withdraw wit) {
		/**
		 * #####################################
		 * 生成代付订单并做 扣款操作
		 */
		return null;
	}
	
	
}
