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
import deal.manage.bean.UserRate;
import deal.manage.bean.util.PageResult;
import deal.manage.service.InviteCodeService;
import deal.manage.service.UserInfoService;
import deal.manage.service.UserRateService;
import deal.manage.util.SessionUtil;
import otc.api.dealpay.Common;
import otc.result.Result;

@Controller
@RequestMapping("/agent")
public class AgentContorller {
	Logger log = LoggerFactory.getLogger(AgentContorller.class);
	@Autowired SessionUtil sessionUtil;
	@Autowired InviteCodeService inviteCodeServiceImpl;
	@Autowired UserInfoService userinfoServiceImpl;
	@Autowired AccountApiService accountApiServiceImpl;
	@Autowired UserRateService userRateServiceImpl;
	/**
	 * <p>代理商开户</p>
	 *	 手机端专用
	 * @return
	 */
	@PostMapping("/agentOpenAnAccount")
	@ResponseBody
	@Transactional
	public Result agentOpenAnAccount(@RequestBody UserInfo user,HttpServletRequest request) {
		UserInfo user2 = sessionUtil.getUser(request);
		if(ObjectUtil.isNull(user2))
			return Result.buildFailMessage("当前用户未登录");
		user.setAgent(user2.getUserId());
		user.setUserType(Common.User.USER_TYPE_CARD);
		user.setIsAgent(Common.User.USER_IS_MEMBER);
		UserRate rateR = userRateServiceImpl.findUserRateR(user2.getUserId());
		UserRate rateC = userRateServiceImpl.findUserRateC(user2.getUserId());
		BigDecimal feeR = rateR.getFee();
		BigDecimal feeC = rateC.getFee();
		String cardFee = user.getCardFee();//出款
		String fee = user.getFee();//入款
		if(StrUtil.isBlank(fee)||StrUtil.isBlank(cardFee))
			return Result.buildFailMessage("请设置会员费率");
		if(!(feeR.compareTo(new BigDecimal(fee)) > -1)) 
			return Result.buildFailMessage("入款费率设置违规");
		if(!(feeC.compareTo(new BigDecimal(cardFee)) > -1)) 
			return Result.buildFailMessage("出款费率设置违规");
		Result addAccount = accountApiServiceImpl.addAccount(user);
		if(addAccount.isSuccess()) {
			UserRate rate = new UserRate();
			rate.setUserId(user.getUserId());
			rate.setFee(new BigDecimal(user.getFee()));
			rate.setFeeType(Common.User.DEAL_FEE);
			rate.setUserType(Common.User.USER_TYPE_CARD);
			boolean add = userRateServiceImpl.add(rate);
			rate.setFeeType(Common.User.CAED_FEE);
			rate.setFee(new BigDecimal(user.getCardFee()));
			boolean a = userRateServiceImpl.add(rate);
			if(add&&a) 
				return Result.buildSuccessMessage("开户成功");
		}
		return Result.buildFailMessage("开户失败");
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
		if(ObjectUtil.isNull(bean.getCustFee())||StrUtil.isBlank(bean.getUserType())||ObjectUtil.isNull(user)||ObjectUtil.isNull(bean.getFee()))
			return Result.buildFailMessage("必传参数为空");
		UserRate rateR = userRateServiceImpl.findUserRateR(user.getUserId());
		UserRate rateC = userRateServiceImpl.findUserRateC(user.getUserId());
		BigDecimal feeR = rateR.getFee();
		BigDecimal feeC = rateC.getFee();
		BigDecimal cardFee = bean.getCustFee();//出款
		BigDecimal fee = bean.getFee();//入款
		if(!(feeR.compareTo(fee) > -1)) 
			return Result.buildFailMessage("入款费率设置违规");
		if(!(feeC.compareTo(cardFee) > -1)) 
			return Result.buildFailMessage("出款费率设置违规");
		String createinviteCode = createinviteCode();
		bean.setBelongUser(user.getUserId());
		bean.setCount(0);
		bean.setInviteCode(createinviteCode);
		bean.setIsDeal(Common.isOk);
		bean.setFee(fee);
		bean.setCustFee(cardFee);
		boolean flag = inviteCodeServiceImpl.addinviteCode(bean);
		if(flag)
			return Result.buildSuccessResult("操作成功","127.0.0.1:7010/register?inviteCode="+createinviteCode);
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
