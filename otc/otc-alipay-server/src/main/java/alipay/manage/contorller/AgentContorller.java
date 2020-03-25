package alipay.manage.contorller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import alipay.config.exception.OtherErrors;
import alipay.manage.util.AgentApi;
import alipay.manage.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import alipay.manage.bean.InviteCode;
import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.PageResult;
import alipay.manage.bean.util.UserCountBean;
import alipay.manage.service.CorrelationService;
import alipay.manage.service.InviteCodeService;
import alipay.manage.service.UserInfoService;
import alipay.manage.util.SessionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import otc.api.alipay.Common;
import otc.result.Result;

@Controller
@RequestMapping("/agent")
public class AgentContorller {
	    Logger log = LoggerFactory.getLogger(AgentContorller.class);
	    @Autowired SessionUtil sessionUtil;
	    @Autowired InviteCodeService inviteCodeServiceImpl;
	    @Autowired UserInfoService userInfoServiceImpl;
	    @Autowired CorrelationService correlationServiceImpl;
	    @Autowired AgentApi agentApi;
	    @Autowired UserUtil userUtil;
	    /**
	     * <p>代理商开户</p>
	     *	 手机端专用
	     * @return
	     */
	    @GetMapping("/agentOpenAnAccount")
	    @ResponseBody
	    public Result agentOpenAnAccount(UserInfo user, HttpServletRequest request) {
	    	UserInfo user2 = sessionUtil.getUser(request);
			if (ObjectUtil.isNull(user))
				return Result.buildFailMessage("未获取到登录用户");
	        user.setAgent(user2.getUserId());
	        user.setIsAgent(Common.User.USER_IS_AGENT);
			Result result = agentApi.openAgentAccount(user);
			if (result.isSuccess())
				userUtil.openAccountCorrlation(user.getUserId());
	        return Result.buildFailMessage("开户失败");
	    }
	    /**
	     * <p>密码修改</p>
	     * 	手机端专用
	     * @return
	     */
	    @PostMapping("/updateAccountPasswpord")
	    @ResponseBody
	    public Result updateAccountPasswpord(@RequestBody UserInfo user, HttpServletRequest request) {
	    	UserInfo user2 = sessionUtil.getUser(request);
	        user.setUserId(user2.getUserId());
	        log.info("【登录密码修改方法】");
	        return Result.buildFail();
	    }

	    /**
	     * <p>资金密码修改</p>
	     * 	手机端专用
	     * @return
	     */
	    @PostMapping("/updateSecurityPassword")
	    @ResponseBody
	    public Result updateSecurityPassword(@RequestBody UserInfo user, HttpServletRequest request) {
	    	UserInfo user2 = sessionUtil.getUser(request);
	    	user.setUserId(user2.getUserId());
	    	  log.info("【登录资金密码修改方法】");
	    	return Result.buildFail();
	    }
	    /**
	     * <p>资金密码修改</p>
	     * 	手机端专用
	     * @return
	     */
	    @PostMapping("/generateInviteCodeAndGetInviteRegisterLink")
	    @ResponseBody
	    public Result generateInviteCodeAndGetInviteRegisterLink(
	            @RequestBody InviteCode bean,
	            HttpServletRequest request
	    ) {
	    	UserInfo user = sessionUtil.getUser(request);
	    	log.info("【生成邀请码的方法，将邀请吗返回给前端】");
	    	String createinviteCode = createinviteCode();
	        boolean flag = inviteCodeServiceImpl.addinviteCode(bean);
	        if (flag)
	            return Result.buildSuccessResult("操作成功","从配置中获取的服务器路径：" + createinviteCode);
	        return Result.buildFail();
	    }
	    /**
	     * <p>产生随机邀请码</p>
	     * @return
	     */
	    String createinviteCode() {
	        String randomString = RandomUtil.randomString(10);
	        boolean flag = inviteCodeServiceImpl.findinviteCode(randomString);
	        if (!flag)
	            return randomString;
	        return createinviteCode();
	    }
	    /**
	     * <p>查询自己的子账户</p>
	     * 	手机端专用
	     * @return
	     */
	    @GetMapping("/findLowerLevelAccountDetailsInfoByPage")
	    @ResponseBody
	    public Result findLowerLevelAccountDetailsInfoByPage(String pageSize,String pageNum,String userName,HttpServletRequest request) {
	    	UserInfo user2 = sessionUtil.getUser(request);
	    	log.info("获取用户 " + user2);
	        if (StrUtil.isBlank(user2.getUserId()))
	            return Result.buildFail();
	        UserInfo user = new UserInfo();
	        if (StrUtil.isNotBlank(userName))
	            user.setUserId(userName);
	        user.setAgent(user2.getUserId());
	        PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
	        List<UserInfo> userList = userInfoServiceImpl.findSunAccount(user);
	        log.info("获取用户信息" + userList);
	        for (UserInfo qrUser : userList)
	            findOnline(qrUser);
	        PageInfo<UserInfo> pageInfo = new PageInfo<UserInfo>(userList);
	        PageResult<UserInfo> pageR = new PageResult<UserInfo>();
	        pageR.setContent(pageInfo.getList());
	        pageR.setPageNum(pageInfo.getPageNum());
	        pageR.setTotal(pageInfo.getTotal());
	        pageR.setTotalPage(pageInfo.getPages());
	        return Result.buildSuccessResult(pageR);
	    }
	    @GetMapping("/findAgentCounts")
	    @ResponseBody
	    public Result findAgentCount(HttpServletRequest request) {
	    	UserInfo user2 = sessionUtil.getUser(request);
	    	log.info("user2.getUserId"+user2.getUserId());
			if (ObjectUtil.isNull(user2))
				throw new OtherErrors("当前用户未登录");
	        UserFund findUserByAccount = userInfoServiceImpl.findUserFundByAccount(user2.getUserId());
	        log.info("通过id获取资金账户表::"+ findUserByAccount);
	        UserCountBean findMoreCount = findMyDate(findUserByAccount.getId());
	        findMoreCount.setMoreDealProfit(findUserByAccount.getTodayAgentProfit().toString());
	        return Result.buildSuccessResult(findMoreCount);
	    }
	    /**
	     * <p>根据我的账户id，查询我的个人数据情况</p>
	     * @param id
	     */
	    private UserCountBean findMyDate(@NotNull Integer id) {
	    	log.info("获取id "+ id);
	    	UserCountBean bean = correlationServiceImpl.findMyDateAgen(id);
	    	UserCountBean bean1 = correlationServiceImpl.findDealDate(id);
	    	if(ObjectUtil.isNotNull(bean1)) {
	    		bean.setMoreAmountRun(ObjectUtil.isNull(bean1.getMoreAmountRun())?new BigDecimal("0") :  bean1.getMoreAmountRun() );
	    		bean.setMoreDealCount(ObjectUtil.isNull(bean1.getMoreDealCount())? 0 :  bean1.getMoreDealCount());
	    	} else {
	    		bean.setMoreAmountRun(new BigDecimal("0"));
	    		bean.setMoreDealCount( 0 );
	    	}
			return bean;
		}
	    UserInfo findOnline(UserInfo user) {
	        if (Common.User.USER_IS_AGENT.toString().equals(user.getIsAgent())) {
	           int[][] dataArray = correlationServiceImpl.findOnline(user.getId());
	           user.setOnline(dataArray[1][0]+"");
	           user.setToDayOrderCount(dataArray[0][0]);
	           user.setAgentCount(dataArray[2][0]+"");
	        }
	        return user;
	    }
	}
