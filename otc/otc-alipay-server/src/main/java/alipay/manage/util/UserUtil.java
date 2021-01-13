package alipay.manage.util;

import alipay.manage.bean.UserInfo;
import alipay.manage.service.CorrelationService;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.api.alipay.Common;
import otc.result.Result;

@Component
public class UserUtil {
	Logger log = LoggerFactory.getLogger(UserUtil.class);
	@Autowired UserInfoService userInfoServiceImpl;
    @Autowired CorrelationService correlationServiceImpl;
    private static final Integer AGENT = 1;
    private static final Integer MEMBER = 2;

	 public Result  openAccountCorrlation(String userId) { //开户统计  顶戴开户
		 Integer myId = null;
		 Integer myType = null;
		 UserInfo user4 = null;
		 String parentId = null;
		 String parentName = null;
		 UserInfo user3 = userInfoServiceImpl.findUserInfoByUserId(userId);
		 if (StrUtil.isNotBlank(user3.getAgent())) {
			 user4 = userInfoServiceImpl.findUserInfoByUserId(user3.getUserId());
		 }
		 String isAgentAgent = Common.User.USER_IS_AGENT;
		 if (user3.getIsAgent().equals(isAgentAgent.toString())) {
			 myType = AGENT;
		 } else {
			 myType = MEMBER;
		 }
		 if (ObjectUtil.isNull(user4)) {
			 parentId = user3.getUserId();
			 parentName = user3.getUserId();
		 } else {
			 parentId = user4.getUserId();
			 parentName = user4.getUserName();
		 }
		 ;
		 boolean openAccount = correlationServiceImpl.openAccount(user3.getUserId(), user3.getUserId(), myType, parentId, parentName);
		 if (openAccount) {
			 return Result.buildSuccessResult();
		 }
		 return Result.buildFail();
	 }
}
