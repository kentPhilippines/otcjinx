package alipay.manage.api.config;

import java.util.Map;

import com.google.common.collect.Maps;

import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.Withdraw;
import otc.api.alipay.Common;
import otc.common.SystemConstants;
import otc.result.Result;
import otc.util.RSAUtils;

/**
 * <p>请求交易抽象【交易】【代付】</p>
 * @author kent
 */
public abstract class PayOrderService implements PayService{
	private static final String ORDER = "orderid";
	
	
	
	
	
	//TODO 未确认 是否加入四方交易逻辑
	@Override
	public Result deal(DealOrderApp dealOrderApp,String payType) {
		if(Common.Deal.PRODUCT_ALIPAY_SCAN.equals(payType))
			return dealAlipayScan(dealOrderApp);
		else if(Common.Deal.PRODUCT_ALIPAY_H5.equals(payType)) 
			return dealAlipayH5(dealOrderApp);
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
		Map<String, Object> param = Maps.newHashMap();
		param.put(ORDER, dealOrderApp.getOrderId());
		String encryptPublicKey = RSAUtils.getEncryptPublicKey(param, SystemConstants.INNER_PLATFORM_PUBLIC_KEY);
		return Result.buildSuccessResult("127.0.0.1:9010/pay/alipayScan/"+encryptPublicKey);
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
