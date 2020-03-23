package alipay.manage.api.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;

import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserFund;
import alipay.manage.bean.Withdraw;
import alipay.manage.service.UserInfoService;
import alipay.manage.util.AmountRunUtil;
import alipay.manage.util.AmountUtil;
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
	@Autowired AmountUtil amountUtil;
	@Autowired AmountRunUtil amountRunUtil;
    @Autowired UserInfoService userInfoServiceImpl;
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
		 * 代付扣款操作
		 */
		UserFund userFund = userInfoServiceImpl.findUserFundByAccount(wit.getUserId());
		Result deleteWithdraw = amountUtil.deleteWithdraw(userFund, wit.getActualAmount());
		if(!deleteWithdraw.isSuccess())
			return Result.buildFailMessage("账户扣减失败,请联系技术人员处理");
		Result deleteAmount = amountRunUtil.deleteAmount(wit, wit.getRetain2(), true);
		if(!deleteAmount.isSuccess())
			return Result.buildFailMessage("账户扣减失败,请联系技术人员处理");
		Result deleteWithdraw2 = amountUtil.deleteWithdraw(userFund, wit.getFee());
		if(!deleteWithdraw2.isSuccess())
			return Result.buildFailMessage("账户扣减失败,请联系技术人员处理");
		Result deleteAmountFee = amountRunUtil.deleteAmountFee(wit, wit.getRetain2(), true);
		if(!deleteAmountFee.isSuccess())
			return  Result.buildFailMessage("账户扣减失败,请联系技术人员处理");
		return Result.buildSuccess();
	}
}
