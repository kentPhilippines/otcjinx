package alipay.manage.contorller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import alipay.manage.util.AgentApi;
import alipay.manage.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import alipay.config.redis.RedisUtil;
import alipay.manage.bean.InviteCode;
import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.PageResult;
import alipay.manage.bean.util.UserCountBean;
import alipay.manage.service.InviteCodeService;
import alipay.manage.service.UserInfoService;
import alipay.manage.util.SessionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import otc.api.alipay.Common;
import otc.result.Result;

@Controller
public class AccountContorller {
    Logger log = LoggerFactory.getLogger(AccountContorller.class);
    @Autowired
    SessionUtil sessionUtil;
    @Autowired
    InviteCodeService inviteCodeServiceImpl;
    @Autowired
    UserInfoService accountServiceImpl;
    @Autowired
    AgentApi agentApi;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserUtil userUtil;
    /**
     * <p>代理商开户</p>
     * 手机端专用
     * @return
     */
    @PostMapping("/agentOpenAnAccount")
    @ResponseBody
    public Result agentOpenAnAccount(@RequestBody UserInfo user, HttpServletRequest request) {
        UserInfo user2 = sessionUtil.getUser(request);
        if (ObjectUtil.isNull(user))
            return Result.buildFailMessage("未获取到登录用户");
        user.setIsAgent("1");
        user.setAgent(user2.getAgent());
        Result result = agentApi.openAgentAccount(user);
        if (result.isSuccess())
            userUtil.openAccountCorrlation(user.getUserId());
        return Result.buildFail();
    }
    /**
     * <p>密码修改</p>
     * 手机端专用
     *
     * @return
     */
    @PostMapping("updateAccountPasswpord")
    @ResponseBody
    public Result updateAccountPasswpord(@RequestBody UserInfo user, HttpServletRequest request) {
    	UserInfo user2 = sessionUtil.getUser(request);
        return Result.buildFail();
    }
    /**
     * <p>资金密码修改</p>
     * 手机端专用
     *
     * @return
     */
    @PostMapping("/updateSecurityPassword")
    @ResponseBody
    public Result updateSecurityPassword(@RequestBody UserInfo user, HttpServletRequest request) {
    	UserInfo user2 = sessionUtil.getUser(request);
        return Result.buildFail();
    }
    /**
     * <p>生成邀请码</p>
     * 手机端专用
     * @return
     */
    @PostMapping("/generateInviteCodeAndGetInviteRegisterLink")
    @ResponseBody
    public Result generateInviteCodeAndGetInviteRegisterLink(
            @RequestBody InviteCode bean,
            HttpServletRequest request
    ) {
    	UserInfo user = sessionUtil.getUser(request);
        if (ObjectUtil.isNull(user)) {
        	log.info("当前用户未登陆");
        	return Result.buildFailMessage("当前用户未登陆");
        }
        String createinviteCode = createinviteCode();
        bean.setInviteCode(createinviteCode);
        boolean flag = inviteCodeServiceImpl.addinviteCode(bean);
        if (flag)
            return Result.buildSuccessResult("操作成功", "网站域名" + createinviteCode);
        return Result.buildFail();
    }
    /**
     * <p>产生随机邀请码</p>
     *
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
     * 手机端专用
     * @return
     */
    @Transactional
    @GetMapping("findLowerLevelAccountDetailsInfoByPage")
    @ResponseBody
    public Result findLowerLevelAccountDetailsInfoByPage(
            String pageSize,
            String pageNum,
            String userName,
            HttpServletRequest request) {
    	UserInfo user2 = sessionUtil.getUser(request);
        if (ObjectUtil.isNull(user2)) {
        	log.info("当前用户未登陆");
        	return Result.buildFailMessage("当前用户未登陆");
        }
        UserInfo user = new UserInfo();
        log.info(userName);
        if (StrUtil.isNotBlank(userName))
            user.setUserId(userName); 
        PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
        List<UserInfo> userList = accountServiceImpl.findSunAccount(user);
        PageInfo<UserInfo> pageInfo = new PageInfo<UserInfo>(userList);
        PageResult<UserInfo> pageR = new PageResult<UserInfo>();
        pageR.setContent(pageInfo.getList());
        pageR.setPageNum(pageInfo.getPageNum());
        pageR.setTotal(pageInfo.getTotal());
        pageR.setTotalPage(pageInfo.getPages());
        return Result.buildSuccessResult(pageR);
    }
    /**
     * <p>账户统计数据</p>
     * @param request
     * @return
     */
    @GetMapping("/findAgentCount")
    @ResponseBody
    public Result findAgentCount(HttpServletRequest request) {
    	UserInfo user2 = sessionUtil.getUser(request);
        if (ObjectUtil.isNull(user2)) {
        	log.info("当前用户未登陆");
        	return Result.buildFailMessage("当前用户未登陆");
        }
        List<UserInfo> sumUserList = accountServiceImpl.findSumAgentUserByAccount(user2.getUserId());
        UserFund findUserById = accountServiceImpl.findUserFundByAccount(user2.getUserId());

        Integer userCount = 0;
        Integer userAgentCount = 0;
        for (UserInfo user : sumUserList)
            if (Common.User.USER_IS_AGENT.toString().equals(user.getIsAgent()))
                userAgentCount++;
            else
                userCount++;
        UserCountBean findMoreCount = findMoreCount(user2.getUserId());
        findMoreCount.setMoreDealProfit(findUserById.getTodayAgentProfit().toString());
        findMoreCount.setAgent(userAgentCount.toString());
        findMoreCount.setUserAgent(userCount.toString());
        log.info(findMoreCount.toString());
        return Result.buildSuccessResult(findMoreCount);
    }
    /**
     * <p>关于自己下线的统计递归方法</p>
     * @param userId
     * @return
     */
	private UserCountBean findMoreCount(String userId) {
		return null;
	}
}

