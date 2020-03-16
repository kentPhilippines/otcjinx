package alipay.manage.api;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserInfo;
import alipay.manage.contorller.AccountContorller;
import alipay.manage.util.LogUtil;
import alipay.manage.util.UserUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import otc.api.alipay.Common;
import otc.common.PayApiConstant;
import otc.result.Result;

@RequestMapping(PayApiConstant.Alipay.ACCOUNT_API)
@RestController
public class AccountApi {
	Logger log = LoggerFactory.getLogger(AccountApi.class);
	@Autowired
	AccountApiService accountApiServiceImpl;
	@Autowired LogUtil logUtil;
	@Autowired UserUtil userUtil;
	/**
	 * <p>开户接口</p>
	 * <P>当前开户接口只接受代理商开始，且该商户只能为码商</P>
	 * @param user
	 * @return
	 */
	@PostMapping(PayApiConstant.Alipay.ADD_ACCOUNT)
	@Transactional
	public Result addAccount(UserInfo user) {
		if(ObjectUtil.isNull(user))
			return Result.buildFailMessage("实体类为空，请检查传递方法是否正确");
		log.info("【远程调用开通顶级代理的方法】");
		if(StrUtil.isBlank(user.getUserId()) 
				|| StrUtil.isBlank(user.getUserName())
				||ObjectUtil.isNull(user.getUserType())
				|| StrUtil.isBlank(user.getIsAgent())
				|| StrUtil.isBlank(user.getEmail())
				) {
			return Result.buildFailMessage("必传参数为空");
		}
		if(!user.getUserType().toString().equals(Common.User.USER_TYPE_QR))
			return Result.buildFailMessage("开户账户类型不符合");
		user.setIsAgent(Common.User.USER_IS_AGENT);
		Result addAccount = accountApiServiceImpl.addAccount(user);
		if(addAccount.isSuccess())
			userUtil.openAccountCorrlation(user.getUserId());
		return addAccount;
	}
	
	@PostMapping(PayApiConstant.Alipay.EDIT_ACCOUNT)
	@Transactional
	public Result editAccount(UserInfo user) {
		log.info("【远程调用修改用户的方法】");
		if(ObjectUtil.isNull(user))
			return Result.buildFailMessage("实体类为空，请检查传递方法是否正确");
		if(StrUtil.isBlank(user.getUserId())) 
			return Result.buildFailMessage("必传参数为空");
		return accountApiServiceImpl.editAccount(user);
	}
	
	/**
	 * <p>增加充值点数</p>
	 * @param 	userFund			账户名
	 * @param	rechargeNumber		充值点数
	 * @param	user				用户名
	 * @param   note				加款理由
	 * @return
	 */
	@PostMapping(PayApiConstant.Alipay.ADD_AMOUNT)
	@Transactional
	public Result addAmount(HttpServletRequest request,UserFund userFund,String user,String note) {
		log.info("【远程调用修改用户的方法】");
		if(StrUtil.isBlank(user) )
			return Result.buildFailMessage("加款人不详");
		if(StrUtil.isBlank(note))
			return Result.buildFailMessage("请填写加款理由");
		logUtil.addLog(request, "【加钱】	当前账户人工处理为加钱，加款金额："+userFund.getRechargeNumber()+"，加款理由："+note+"，加款人："+user+"", user);
		if(ObjectUtil.isNull(userFund))
			return Result.buildFailMessage("实体类为空，请检查传递方法是否正确");
		if(StrUtil.isBlank(userFund.getUserId()) || StrUtil.isBlank(userFund.getRechargeNumber().toString())) 
			return Result.buildFailMessage("必传参数为空");
		return accountApiServiceImpl.addAmount(userFund);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
