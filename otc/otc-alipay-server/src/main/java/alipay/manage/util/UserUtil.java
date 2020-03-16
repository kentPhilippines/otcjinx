package alipay.manage.util;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alipay.manage.bean.UserFund;
import alipay.manage.service.CorrelationService;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import otc.api.alipay.Common;
import otc.result.Result;

@Component
public class UserUtil {
	Logger log = LoggerFactory.getLogger(UserUtil.class);
	@Autowired UserInfoService userInfoServiceImpl;
    @Autowired CorrelationService correlationServiceImpl;
    private static final Integer AGENT = 1;
    private static final Integer MEMBER = 2;
	 public Result  openAccountCorrlation(String userId){
	    	Integer myId = null;
	    	Integer myType = null;
	    	UserFund user4 = null;
	    	String parentId = null;
	    	String parentName = null;
	    	UserFund user3 = userInfoServiceImpl.findUserByAccount(userId);
	    	if(StrUtil.isNotBlank(user3.getAgent())) 
	    		user4 = userInfoServiceImpl.findUserByAccount(userId);
	    	String isAgentAgent = Common.User.USER_IS_AGENT;
	    	if(user3.getIsAgent().equals(isAgentAgent.toString())) 
	    		myType = AGENT;
	    	 else 
	    		myType = MEMBER;
	    	if(ObjectUtil.isNull(user4)) {
	    		parentId = user3.getUserId();
	    		parentName = user3.getUserName();
	    	}else {
	    		parentId = user4.getUserId();
	    		parentName = user4.getUserName();
	    	};
	    	boolean openAccount = correlationServiceImpl.openAccount(user3.getUserId(), user3.getUserName(), myType, parentId , parentName);
	    	if(openAccount)
	    		return Result.buildSuccessResult();
	    	return Result.buildFail();
	   }
}
