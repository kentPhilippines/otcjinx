package alipay.manage.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import alipay.manage.bean.Amount;
import alipay.manage.bean.UserFund;
import alipay.manage.mapper.AmountMapper;
import alipay.manage.service.MediumService;
import alipay.manage.service.UserInfoService;
import alipay.manage.util.AmountRunUtil;
import alipay.manage.util.AmountUtil;
import alipay.manage.util.LogUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import otc.api.alipay.Common;
import otc.bean.alipay.Medium;
import otc.common.PayApiConstant;
import otc.result.Result;

@RestController
public class Api {
	@Autowired AmountMapper amountDao;
	@Autowired MediumService mediumServiceImpl;
	@Autowired LogUtil logUtil;
	@Autowired AmountUtil amountUtil;
	@Autowired UserInfoService userInfoServiceImpl;
	@Autowired AmountRunUtil amountRunUtil;
	Logger log = LoggerFactory.getLogger(Api.class);
	@PostMapping(PayApiConstant.Alipay.MEDIUM_API+PayApiConstant.Alipay.FIND_MEDIUM_IS_DEAL)
	public List<Medium> findIsDealMedium(String mediumType, String code) {
		/**
		 * ##############################################
		 * 1，根据code值获取对应的所有支付媒介  code值即为自己的顶级代理账号
		 * 2，如果code值为空 则获取所有的支付媒介
		 */
		log.info("【远程调用获取初始化数据，传递参数为："+mediumType+"，code  = "+code+"】");
		if(StrUtil.isBlank(code)) {
			//获取所有的支付媒介
			List<Medium> mediumList = mediumServiceImpl.findMediumByType(mediumType);
			return mediumList;
		}else {
			List<Medium> mediumList = mediumServiceImpl.findMediumByType(mediumType,code);
			return mediumList;
		}
	}
	
	/**
	 * <p>人工加扣款接口</p>
	 * @param amount					加扣款订单
	 * @param request					请求
	 * @return
	 */
	@PostMapping(PayApiConstant.Alipay.ACCOUNT_API+PayApiConstant.Alipay.AMOUNT)
	public Result addAmount(Amount amount,HttpServletRequest request) {
		if(ObjectUtil.isNull(amount))
			return Result.buildFailMessage("当前订单不存在");
		if(StrUtil.isBlank(amount.getOrderId()))
			return Result.buildFailMessage("订单号不存在");
		log.info("【当前调用人工资金处理接口，当前订单号："+amount.getOrderId()+"】");
		String clientIP = HttpUtil.getClientIP(request);
		if(StrUtil.isBlank(clientIP))
			return Result.buildFailMessage("当前使用代理服务器 或是操作ip识别出错，不允许操作");
		Amount addmount =  amountDao.findOrder(amount.getOrderId());
		if(addmount.getAmountType().equals(Common.Deal.AMOUNT_ORDER_ADD.toString())) {//加款
			Result addAmount = amountRunUtil.addAmount(amount, clientIP);
			if(addAmount.isSuccess()) {
				UserFund userFund = userInfoServiceImpl.findUserFundByAccount(addmount.getUserId());
				Result addAmountAdd = amountUtil.addAmountAdd(userFund, addmount.getAmount());
				if(addAmountAdd.isSuccess()) {
					logUtil.addLog(request, "当前发起加钱操作，加款订单号："+addmount.getOrderId()+"，加款成功，加款用户："+addmount.getUserId()+"，操作人："+addmount.getAccname()+"", addmount.getAccname());
					return Result.buildSuccessMessage("操作成功");
				}
			}
		}else if(addmount.getAmountType().equals(Common.Deal.AMOUNT_ORDER_DELETE.toString())){
			Result deleteAmount = amountRunUtil.deleteAmount(amount, clientIP);
			if(deleteAmount.isSuccess()) {
				UserFund userFund = userInfoServiceImpl.findUserFundByAccount(addmount.getUserId());
				Result deleteAmount2 = amountUtil.deleteAmount(userFund, addmount.getAmount());
				if(deleteAmount2.isSuccess()) {
					logUtil.addLog(request, "当前发起扣款操作，扣款订单号："+addmount.getOrderId()+"，扣款成功，扣款用户："+addmount.getUserId()+"，操作人："+addmount.getAccname()+"", addmount.getAccname());
					return Result.buildSuccessMessage("操作成功");
				}
			}
		}
		return Result.buildFailMessage("人工加款失败");
	}
	
	
	
	
	
}
