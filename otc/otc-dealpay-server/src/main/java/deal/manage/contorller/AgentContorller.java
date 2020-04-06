package deal.manage.contorller;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import deal.manage.api.AccountApiService;
import deal.manage.bean.Invitecode;
import deal.manage.bean.UserInfo;
import deal.manage.bean.util.PageResult;
import deal.manage.service.InviteCodeService;
import deal.manage.service.UserInfoService;
import deal.manage.util.SessionUtil;
import otc.result.Result;

@Controller
@RequestMapping("/agent")
public class AgentContorller {
	Logger log = LoggerFactory.getLogger(AgentContorller.class);
	@Autowired SessionUtil sessionUtil;
	@Autowired InviteCodeService inviteCodeServiceImpl;
	@Autowired UserInfoService userinfoServiceImpl;
	@Autowired AccountApiService accountApiServiceImpl;
	/**
	 * <p>代理商开户</p>
	 *	 手机端专用
	 * @return
	 */
	@PostMapping("/agentOpenAnAccount")
	@ResponseBody
	public Result agentOpenAnAccount(@RequestBody UserInfo user,HttpServletRequest request) {
		
		
		
		
		
		
		return Result.buildFail();
	}
	/**
	 * <p>密码修改</p>
	 * 	手机端专用
	 * @return
	 */
	@PostMapping("/updateAccountPasswpord")
	@ResponseBody
	public Result updateAccountPasswpord(@RequestBody UserInfo user,HttpServletRequest request) {
		UserInfo user2 = sessionUtil.getUser(request);
		user.setUserId(user2.getUserId()); 
		Result result = accountApiServiceImpl.updateLoginPassword(user);
		return result;
	}
	/**
	 * <p>资金密码修改</p>
	 * 	手机端专用
	 * @return
	 */
	@PostMapping("updateSecurityPassword")
	@ResponseBody
	public Result updateSecurityPassword(@RequestBody UserInfo user,HttpServletRequest request) {
		UserInfo user2 = sessionUtil.getUser(request);
		user.setUserId(user2.getUserId());
		Result result = accountApiServiceImpl.updatePayPassword(user);
		return result;
	}
	/**
	 * <p>卡商开展下线</p>
	 * @return
	 */
	@PostMapping("/generateInviteCodeAndGetInviteRegisterLink")
	@ResponseBody
	public Result generateInviteCodeAndGetInviteRegisterLink( @RequestBody Invitecode bean , HttpServletRequest request ) {
		/**
		 * ###########
		 * <p>卡商开户逻辑</p>
		 * <li>出款费率不能大于自己的费率</li>
		 * <li>入款费率不能大于自己的费率</li>
		 */
		UserInfo user = sessionUtil.getUser(request);
		if(ObjectUtil.isNull(bean.getCustFee())||StrUtil.isBlank(bean.getUserType())||ObjectUtil.isNull(user))
			return Result.buildFailMessage("必传参数为空");
	/*
		
		
		UserInfo user2 = accountServiceImpl.getUser(user2.getUserId());
		BigDecimal fee = user2.getFee();
		BigDecimal rebateNew = new BigDecimal(bean.getRebate());
		if(fee.compareTo(rebateNew) == -1 ) {
			return JsonResult.buildFailResult("利率设置异常，无法设置高于自己账号入款利率，当前我的利率为："+fee);
		}
		BigDecimal cardFee = user2.getCardFee();
		String cardFee2 = bean.getCardFee();
		BigDecimal cardFee3 = new BigDecimal(cardFee2);
		if(cardFee.compareTo(cardFee3) == -1 ) {
			return JsonResult.buildFailResult("利率设置异常，无法设置高于自己账号出款利率，当前我的利率为："+fee);
		}
		bean.setBelongUser(user.getAccountId());
		bean.setCount(0);
		String createinviteCode = createinviteCode();
		bean.setInviteCode(createinviteCode);
		bean.setIsDeal(Common.isOk);
		boolean flag = inviteCodeServiceImpl.addinviteCode(bean);
		if(flag)
			return JsonResult.buildSuccessResult("操作成功",settingFile.getName(settingFile.OPEN_ACCOUNT_CODE)+createinviteCode);
		return JsonResult.buildFailResult();
		*/
		return Result.buildFailMessage("开户失败");
	}
	/**
	 * <p>产生随机邀请码</p>
	 * @return
	 */
	String createinviteCode(){
		String randomString = RandomUtil.randomString(10);
		boolean flag = inviteCodeServiceImpl.findinviteCode(randomString);
		if(!flag)
			return randomString;
		return createinviteCode();
	}
	/**
	 * <p>查询自己的子账户</p>
	 * 	手机端专用
	 * @return
	 */
	@GetMapping("findLowerLevelAccountDetailsInfoByPage")
	@ResponseBody
	public Result findLowerLevelAccountDetailsInfoByPage(
			String	pageSize,
			String	pageNum,
			String	userName, 
			HttpServletRequest request) {
		UserInfo user2 = sessionUtil.getUser(request);
		if(StrUtil.isBlank(user2.getUserId()))
			return Result.buildFailMessage("必传参数为空");
		UserInfo user =  new UserInfo();
		if(StrUtil.isNotBlank(userName))
			user.setUserId(userName); 
		user.setAgent(user2.getUserId());
		PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
		List<UserInfo> userList = userinfoServiceImpl.findSunAccount(user);
		PageInfo<UserInfo> pageInfo = new PageInfo<UserInfo>(userList);
		PageResult<UserInfo> pageR = new PageResult<UserInfo>();
		pageR.setContent(pageInfo.getList());
		pageR.setPageNum(pageInfo.getPageNum());
		pageR.setTotal(pageInfo.getTotal());
		pageR.setTotalPage(pageInfo.getPages());
		return Result.buildSuccessResult(pageR);
	}
}
