package alipay.manage.contorller;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import alipay.manage.bean.UserFund;
import alipay.manage.service.UserFundService;
import alipay.manage.util.Md5Util;
import alipay.manage.util.QrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import alipay.manage.api.AccountApiService;
import alipay.manage.bean.BankList;
import alipay.manage.bean.InviteCode;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.BankListService;
import alipay.manage.service.InviteCodeService;
import alipay.manage.service.UserInfoService;
import alipay.manage.util.SessionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import otc.exception.user.UserException;
import otc.result.Result;

@Controller
@RequestMapping("/userAccount")
public class UserContorller {
	Logger log = LoggerFactory.getLogger(UserContorller.class);
	@Autowired UserInfoService  userInfoServiceImpl;
	@Autowired	UserFundService userFundService;
	@Autowired SessionUtil sessionUtil;
	@Autowired BankListService bankListServiceImpl;
	@Autowired InviteCodeService inviteCodeServiceImpl;
	@Autowired AccountApiService accountApiServiceImpl;
	@Autowired QrUtil qrUtil;
	/**
	 * <p>获取账号登录情况</p>
	 * @return
	 */
	@GetMapping("/getUserAccountInfo")
	@ResponseBody
	public Result getUserAccountInfo(HttpServletRequest request) {
		UserInfo user = sessionUtil.getUser(request);
		if (ObjectUtil.isNull(user)) {
	        log.info("当前用户未登陆");
	        return Result.buildFailMessage("当前用户未登陆");
	    }
		UserInfo user2 = userInfoServiceImpl.findUserInfoByUserId(user.getUserId());
		return Result.buildSuccessResult(user2);
	}

	/**
	 * 用户获取资金账户
	 * @param request
	 * @return
	 */
	@GetMapping("/getUserFundInfo")
    @ResponseBody
	public Result getUserFundInfo(HttpServletRequest request){
		UserInfo user = sessionUtil.getUser(request);
		if (ObjectUtil.isNull(user)) {
			log.info("当前用户未登陆");
			return Result.buildFailMessage("当前用户未登陆");
		}
		UserFund byUserId = userFundService.findUserInfoByUserId(user.getUserId());
		return Result.buildSuccessResult(byUserId);
	}
	
	/**
	 * <p>获取用户绑定的银行卡接口</p>
	 * @return
	 */
	@GetMapping("/getBankInfo")
	@ResponseBody
	public Result getBankInfo(HttpServletRequest request) {
		UserInfo user = sessionUtil.getUser(request);
		if (ObjectUtil.isNull(user)) {
	        log.info("当前用户未登陆");
	        return Result.buildFailMessage("当前用户未登陆");
	    }
		List<BankList> bank = bankListServiceImpl.findBankCardByQr(user.getUserId());
		return Result.buildSuccessResult(bank);
	}
	/**
	 * <p>添加银行卡</p>
	 * @param bank
	 * @return
	 */
	@PostMapping("/bindBankInfo")
	@ResponseBody
	public Result bindBankInfo(BankList bank , HttpServletRequest request) {
		UserInfo user = sessionUtil.getUser(request);
		if (ObjectUtil.isNull(user)) {
	        log.info("当前用户未登陆");
	        return Result.buildFailMessage("当前用户未登陆");
	    }
		log.info("【当前添加银行卡用户："+user.getUserId()+"】");
		bank.setAccount(user.getUserId());
		boolean flag  = bankListServiceImpl.addBankcard(bank);
		if(flag)
			return Result.buildSuccess();
		return Result.buildFail();
	}
	/**
	 * <p>用户通过邀请码注册</p>
	 * @param request
	 * @return
	 */
	@PostMapping("/register")
	@ResponseBody
	public Result register(UserInfo user ,HttpServletRequest request) {
		user.setPayPasword("zsqaz1234");
		if(StrUtil.isBlank(user.getUserId())|| StrUtil.isBlank(user.getPassword())
				|| StrUtil.isBlank(user.getPayPasword())|| StrUtil.isBlank(user.getUserName())
				|| StrUtil.isBlank(user.getInviteCode()))
			return Result.buildFailResult("必填参数为空");
		InviteCode code = inviteCodeServiceImpl.findInviteCode(user.getInviteCode());
		if(code.getStatus()==0)
			return Result.buildFailResult("当前邀请码不可使用");
		user.setAgent(code.getBelongUser());//邀请码生成人
		Result openAgentAccount = accountApiServiceImpl.addAccount(user);
		boolean flag = false;
		if(openAgentAccount.isSuccess())
			flag = inviteCodeServiceImpl.updataInviteCode(user.getInviteCode(),user.getUserId());
		if(openAgentAccount.isSuccess() && flag)
			return Result.buildSuccessResult();
		return Result.buildFailMessage("注册失败");
	}
	/**
	 * <p>修改密码</p>
	 * @param newLoginPwd
	 * @return
	 */
	@PostMapping("/modifyLoginPwd")
	@ResponseBody
	public Result modifyLoginPwd(String oldLoginPwd , String newLoginPwd,HttpServletRequest request) {
		if(StrUtil.isBlank(oldLoginPwd)|| StrUtil.isBlank(newLoginPwd))
			return Result.buildFailResult("必填参数为空");
		UserInfo user = sessionUtil.getUser(request);
		if (ObjectUtil.isNull(user)) {
	        log.info("当前用户未登陆");
	        return Result.buildFailMessage("当前用户未登陆");
	    }
		user.setPassword(oldLoginPwd);
		user.setNewPassword(newLoginPwd);
		Result updateAccountPassword = accountApiServiceImpl.updateLoginPassword(user);
		if(updateAccountPassword.isSuccess())
			return Result.buildSuccessResult();
		return updateAccountPassword;
	}
	/**
	 * <p>修改支付密码</p>
	 * @param newMoneyPwd
	 * @return
	 */
	@PostMapping("/modifyMoneyPwd")
	@ResponseBody
	public Result modifyMoneyPwd(String oldMoneyPwd , String newMoneyPwd,HttpServletRequest request) {
		if(StrUtil.isBlank(oldMoneyPwd)|| StrUtil.isBlank(newMoneyPwd))
			return Result.buildFailResult("必填参数为空");
		UserInfo user = sessionUtil.getUser(request);
		if (ObjectUtil.isNull(user)) {
	        log.info("当前用户未登陆");
	        return Result.buildFailMessage("当前用户未登陆");
	    }
		newMoneyPwd = Md5Util.md5(newMoneyPwd);
		oldMoneyPwd = Md5Util.md5(oldMoneyPwd);
		user.setNewPayPassword(newMoneyPwd);
		user.setPayPasword(oldMoneyPwd); 
		Result updateAccountPassword = accountApiServiceImpl.updatePayPassword(user);
		if(updateAccountPassword.isSuccess())
			return Result.buildSuccessResult();
		return updateAccountPassword;
	}

	@GetMapping("/virtualAmount")
	@ResponseBody
	public Result virtualAmount(HttpServletRequest request ){
		UserInfo user = sessionUtil.getUser(request);
		if(ObjectUtil.isNull(user))
			throw new UserException("当前用户未登录",null);
		BigDecimal userAmount = qrUtil.getUserAmount(user.getUserId());
		return Result.buildSuccessResult(userAmount);
	}

	/**
	 * <p>修改会员性质为代理商</p>
	 * @param request
	 * @param accountId
	 * @return
	 */
	@PostMapping("/updateIsAgent")
	@ResponseBody
	public Result updateIsAgent(HttpServletRequest request,String accountId)   {
		UserInfo user = sessionUtil.getUser(request);
		if (ObjectUtil.isNull(user)) {
	        log.info("当前用户未登陆");
	        return Result.buildFailMessage("当前用户未登陆");
	    }
		boolean flag = accountApiServiceImpl.updateIsAgent(accountId);
		if(flag)
			return	Result.buildSuccessResult();
		return Result.buildFail();
	}
	@PostMapping("/findUserByAccountId")
	@ResponseBody
	public Result findUserByAccountId(HttpServletRequest request,String accountId ){
		UserInfo user = sessionUtil.getUser(request);
		if (ObjectUtil.isNull(user)) {
	        log.info("当前用户未登陆");
	        return Result.buildFailMessage("当前用户未登陆");
	    }
		UserInfo qrUser =  accountApiServiceImpl.findUserInfo(accountId);
		return Result.buildSuccessResult(qrUser);
	}
	@GetMapping("/updataAccountFee")
	public String updataAccountFee(HttpServletRequest request,String accountId )  {
		return "updataAccountFee";
	}
	/**
	 * <p>修改下级代理费率</p>
	 * @param request
	 * @param accountId			下级账户 id
	 * @param fee				下级费率
	 * @return
	
	@PostMapping("/updataAgentFee")
	@ResponseBody
	public JsonResult updataAgentFee(HttpServletRequest request,String accountId,String fee )  {
		QrcodeUser user = sessionUtil.getUser(request);
		if(ObjectUtil.isNull(user))
			throw new ParamException("当前用户未登录");
		QrcodeUser qrUser =  accountServiceImpl.findUserById(accountId);
		boolean flag = false;
		boolean fee1 = Double.valueOf(user.getFee().toString()) >  Double.valueOf(fee);
		boolean fee2 = Double.valueOf(qrUser.getFee().toString()) <  Double.valueOf(fee);
		if(fee2 && fee1) {
			 flag = accountServiceImpl.updataAgentFee(accountId,fee,qrUser.getVersion());
		} else {
			return JsonResult.buildFailResult("数据不合规，当前费率有误");
		}
		if(flag)
			return JsonResult.buildSuccessResult();
		return JsonResult.buildFailResult("修改失败");
	}
 */
}
