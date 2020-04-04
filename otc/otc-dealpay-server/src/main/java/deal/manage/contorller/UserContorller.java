package deal.manage.contorller;


import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import deal.manage.api.AccountApiService;
import deal.manage.bean.BankList;
import deal.manage.bean.Invitecode;
import deal.manage.bean.UserInfo;
import deal.manage.service.BankListService;
import deal.manage.service.InviteCodeService;
import deal.manage.service.UserInfoService;
import deal.manage.util.CardBankOrderUtil;
import deal.manage.util.SessionUtil;
import otc.result.Result;

@Controller
@RequestMapping("/userAccount")
public class UserContorller {
	Logger log = LoggerFactory.getLogger(UserContorller.class);
	@Autowired UserInfoService userInfoServiceImpl;
	@Autowired SessionUtil sessionUtil;
	@Autowired BankListService bankCardServiceImpl;
	@Autowired InviteCodeService inviteCodeServiceImpl;
	@Autowired CardBankOrderUtil bankUtil;
	@Autowired AccountApiService accountApiServiceImpl;
	/**
	 * <p>获取账号登录情况</p>
	 * @return
	 */
	@GetMapping("/getUserAccountInfo")
	@ResponseBody
	public Result getUserAccountInfo(HttpServletRequest request) {
		UserInfo user = sessionUtil.getUser(request);
		if(ObjectUtil.isNull(user)) 
			return Result.buildFailMessage("当前用户未登录");
		UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(user.getUserId());
		return Result.buildSuccessResult("数据获取成功",userInfo);
	}
	/**
	 * <p>修改当前用户的接单状态</p>
	 * @param receiveOrderState        
	 * @return
	 */
	@PostMapping("/updateReceiveOrderState")
	@ResponseBody
	public Result updateReceiveOrderState(HttpServletRequest request,String receiveOrderState,String accountId) {
		UserInfo user = sessionUtil.getUser(request);
		if(ObjectUtil.isNull(user)) 
			return Result.buildFailMessage("当前用户未登录");
		if(StrUtil.isNotBlank(accountId)) 
			user.setUserId(accountId);
		boolean flag = userInfoServiceImpl.updateReceiveOrderState(user.getUserId(),Integer.valueOf(receiveOrderState));
		if(flag) 
			return Result.buildSuccessResult();
		return Result.buildFailMessage("修改失败");
	}
	
	/**
	 * <p>获取用户绑定的银行卡接口</p>
	 * @param receiveOrderState
	 * @return
	 */
	@GetMapping("/getBankInfo")
	@ResponseBody
	public Result getBankInfo(String receiveOrderState ,HttpServletRequest request) {
		UserInfo user = sessionUtil.getUser(request);
		if(ObjectUtil.isNull(user)) 
			return Result.buildFailMessage("当前用户未登录");
		List<BankList> bank = bankCardServiceImpl.findBankCardByQr(user.getUserId());
		return Result.buildSuccessResult(bank);
	}
	/**
	 * <p>添加银行卡</p>
	 * @param receiveOrderState
	 * @return
	 */
	@PostMapping("/bindBankInfo")
	@ResponseBody
	public Result bindBankInfo(@RequestBody BankList bank ,HttpServletRequest request) {
		UserInfo user = sessionUtil.getUser(request);
		if(ObjectUtil.isNull(user)) 
			return Result.buildFailMessage("当前用户未登录");
		bank.setAccount(user.getUserId());
		System.out.println(bank.toString());
		boolean flag  = bankCardServiceImpl.addBankCard(bank);
		if(flag)
			return Result.buildSuccessResult();
		return Result.buildFailMessage("添加银行卡失败");
	}
	/**
	 * <p>用户通过邀请码注册</p>
	 * @param receiveOrderState
	 * @return
	 */
	@PostMapping("/register")
	@ResponseBody
	public Result register(  UserInfo user ,HttpServletRequest request) {
		if(StrUtil.isBlank(user.getUserId())|| StrUtil.isBlank(user.getPassword()) 
				|| StrUtil.isBlank(user.getPassword())
				|| StrUtil.isBlank(user.getPayPasword())
				|| StrUtil.isBlank(user.getUserName())
				|| StrUtil.isBlank(user.getInviteCode()))
			return Result.buildFailMessage("必传参数为空");
/*		Invitecode code = inviteCodeServiceImpl.findInviteCode(user.getInviteCode());
		if(code.getStatus()==0) {
			return Result.buildFailMessage("当前邀请码不可用");
		}
		user.setFee(new BigDecimal(code.getFee()));//邀请码费率
		user.setAgent(code.getBelongUser());//邀请码生成人
		user.setIsAgent(code.getUserType().equals("agent")?"1":"2");
		user.setCardFee(new BigDecimal(code.getCardFee()));
		user.setUserType(Common.USER_TYPE_CARD);
		JsonResult openAgentAccount = agentApi.openAgentAccount(user);
		boolean flag = false;
		if(openAgentAccount.isSuccess())
			flag = inviteCodeServiceImpl.updataInviteCode(user.getInviteCode(),user.getAccountId());
		if(openAgentAccount.isSuccess() && flag)
			return JsonResult.buildSuccessResult();
			*/
		return Result.buildFailMessage("注册失败");
	}
	/**
	 * <p>修改密码</p>
	 * @param receiveOrderState
	 * @return
	 */
	@PostMapping("/modifyLoginPwd")
	@ResponseBody
	public Result modifyLoginPwd(String oldLoginPwd , String newLoginPwd,HttpServletRequest request) {
		if(StrUtil.isBlank(oldLoginPwd)|| StrUtil.isBlank(newLoginPwd))
			return Result.buildFailMessage("必传参数为空");
		UserInfo user = sessionUtil.getUser(request);
		if(ObjectUtil.isNull(user)) 
			return Result.buildFailMessage("当前用户未登录");
		user.setPassword(oldLoginPwd);
		user.setNewPassword(newLoginPwd);
		Result updateAccountPassword = accountApiServiceImpl.updateLoginPassword(user);
		if(updateAccountPassword.isSuccess())
			return Result.buildSuccessResult();
		return updateAccountPassword;
	}
	/**
	 * <p>修改密码</p>
	 * @param receiveOrderState
	 * @return
	 */
	@PostMapping("/modifyMoneyPwd")
	@ResponseBody
	public Result modifyMoneyPwd(String oldMoneyPwd , String newMoneyPwd,HttpServletRequest request) {
		if(StrUtil.isBlank(oldMoneyPwd)|| StrUtil.isBlank(newMoneyPwd))
			return Result.buildFailMessage("必传参数为空");
		UserInfo user = sessionUtil.getUser(request);
		if(ObjectUtil.isNull(user)) 
			return Result.buildFailMessage("当前用户未登录");
		user.setNewPayPassword(newMoneyPwd);
		user.setPayPasword(oldMoneyPwd);
		Result updateAccountPassword = accountApiServiceImpl.updateLoginPassword(user);
		if(updateAccountPassword.isSuccess())
			return Result.buildSuccessResult();
		return updateAccountPassword;
	}
	
	/**
	 * <p>修改当前二维码为不可接单</p>
	 * @return
	 */
	@GetMapping("/updataQrStatusEr")
	@ResponseBody
	public Result updataQrStatusEr(String id ,HttpServletRequest request) {
		UserInfo user = sessionUtil.getUser(request);
		if(ObjectUtil.isNull(user)) 
			return Result.buildFailMessage("当前用户未登录");
		boolean flag = bankCardServiceImpl.updataQrStatusEr(id);
		if(flag)
			return Result.buildSuccessResult();
		return Result.buildFailMessage("修改失败");
	}
	/**
	 * <p>修改当前二维码为可接单</p>
	 * @return
	 */
	@GetMapping("/updataQrStatusSu")
	@ResponseBody
	public Result updataQrStatusSu(String id ,HttpServletRequest request) {
		UserInfo user = sessionUtil.getUser(request);
		if(ObjectUtil.isNull(user)) 
			return Result.buildFailMessage("当前用户未登录");
		boolean flag = bankCardServiceImpl.updataStatusSu(id);
		if(flag)
			return Result.buildSuccessResult();
		return Result.buildFailMessage("修改失败");
	}
	/**
	 * <p>修改会员性质为代理商</p>
	 * @param request
	 * @param accountId
	 * @return
	 * @throws ParseException
	 */
	@PostMapping("/updateIsAgent")
	@ResponseBody
	public Result updateIsAgent(HttpServletRequest request,String accountId ) throws ParseException {
		UserInfo user = sessionUtil.getUser(request);
		if(ObjectUtil.isNull(user)) 
			return Result.buildFailMessage("当前用户未登录");
		boolean flag = userInfoServiceImpl.updateIsAgent(accountId);
		if(flag)
			return	Result.buildSuccessResult();
		return Result.buildFailMessage("修改为代理商失败");
	}
	@PostMapping("/findUserByAccountId")
	@ResponseBody
	public Result findUserByAccountId(HttpServletRequest request,String accountId ) throws ParseException {
		UserInfo user = sessionUtil.getUser(request);
		if(ObjectUtil.isNull(user)) 
			return Result.buildFailMessage("当前用户未登录");
		UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(accountId);
		return Result.buildSuccessResult(userInfo);
	}
	@GetMapping("/updataAccountFee")
	public String updataAccountFee(HttpServletRequest request,String accountId ) throws ParseException {
		return "updataAccountFee";
	}
	/**
	 * <p>修改下级代理费率</p>
	 * @param request
	 * @param accountId			下级账户 id
	 * @param fee				下级费率
	 * @return
	 */
	@PostMapping("/updataAgentFee")
	@ResponseBody
	public Result updataAgentFee(HttpServletRequest request,String accountId,String fee,String feeType )  {
		UserInfo user = sessionUtil.getUser(request);
/*		if(ObjectUtil.isNull(user)) 
			return Result.buildFailMessage("当前用户未登录");
		UserInfo qrUser =  userInfoServiceImpl.findUserInfoByUserId(accountId);
		boolean flag = false;
		String userFee = "0";
		String crUserFee = "0";
		if(feeType.equals("fee")) {
			userFee = user.getFee().toString();
			crUserFee = qrUser.getFee().toString();
		}else {
			userFee = user.getCardFee().toString();
			crUserFee = qrUser.getCardFee().toString();
		}
		boolean fee1 = Double.valueOf(userFee) >  Double.valueOf(fee);
		boolean fee2 = Double.valueOf(crUserFee) <  Double.valueOf(fee);
		if(fee2 && fee1) {
			 flag = accountServiceImpl.updataAgentFee(accountId,fee,qrUser.getVersion(),feeType);
		} else {
			return Result.buildFailResult("数据不合规，当前费率有误");
		}
		if(flag)
			return Result.buildSuccessResult();
			*/
		return Result.buildFailResult("修改失败");
	}
	
	/**
	 * <p>接单入款开启状态</p>
	 * @param request
	 * @return
	 */
	@GetMapping("/updataReceiveOrderStateNO")
	@ResponseBody
	public Result updataReceiveOrderStateNO(HttpServletRequest request) {
		UserInfo user = sessionUtil.getUser(request);
		if(ObjectUtil.isNull(user)) 
			return Result.buildFailMessage("当前用户未登录");
		boolean flag = userInfoServiceImpl.updataReceiveOrderStateNO(user.getUserId());
		if(flag)
			return Result.buildSuccessResult();
		return Result.buildFailResult("状态修改失败");
	}
	/**
	 * <p>接单入款开启状态</p>
	 * @param request
	 * @return
	 */
	@GetMapping("/updataReceiveOrderStateOFF")
	@ResponseBody
	public Result updataReceiveOrderStateOFF(HttpServletRequest request) {
		UserInfo user = sessionUtil.getUser(request);
		if(ObjectUtil.isNull(user)) 
			return Result.buildFailMessage("当前用户未登录");
		boolean flag = userInfoServiceImpl.updataReceiveOrderStateOFF(user.getUserId());
		if(flag)
			return Result.buildSuccessResult();
		return Result.buildFailResult("状态修改失败");
	}
	/**
	 * <p>收款接单开启</p>
	 * @param request
	 * @return
	 */
	@GetMapping("/updataRemitOrderStateNO")
	@ResponseBody
	public Result updataRemitOrderStateNO(HttpServletRequest request) {
		UserInfo user = sessionUtil.getUser(request);
		if(ObjectUtil.isNull(user)) 
			return Result.buildFailMessage("当前用户未登录");
		boolean flag = userInfoServiceImpl.updataRemitOrderStateNO(user.getUserId());
		if(flag)
			return Result.buildSuccessResult();
		return Result.buildFailResult("状态修改失败");
	}
	/**
	 * <p>收款接单关闭</p>
	 * @param request
	 * @return
	 */
	@GetMapping("/updataRemitOrderStateOFF")
	@ResponseBody
	public Result updataRemitOrderStateOFF(HttpServletRequest request) {
		UserInfo user = sessionUtil.getUser(request);
		if(ObjectUtil.isNull(user)) 
			return Result.buildFailMessage("当前用户未登录");
		boolean flag = userInfoServiceImpl.updataRemitOrderStateOFF(user.getUserId());
		if(flag)
			return Result.buildSuccessResult();
		return Result.buildFailResult("状态修改失败");
	}
}
