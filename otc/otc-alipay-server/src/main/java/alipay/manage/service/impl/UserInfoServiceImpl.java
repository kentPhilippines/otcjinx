package alipay.manage.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.UserInfoService;
@Component
public class UserInfoServiceImpl implements UserInfoService{
	@Override
	public List<UserInfo> findSunAccount(UserInfo user) {
		return null;
	}

	@Override
	public List<UserInfo> findSumAgentUserByAccount(String userId) {
		return null;
	}

	@Override
	public UserFund findUserByAccount(String userId) {
		return null;
	}

	@Override
	public List<String> findSunAccountByUserId(String userId) {
		return null;
	}

	@Override
	public boolean updateIsAgent(String accountId) {
		return false;
	}

	@Override
	public Boolean updataStatusEr(String userId) {
		return null;
	}

	@Override
	public UserInfo findUserInfoByUserId(String userId) {
		return null;
	}

	@Override
	public Boolean updataAmount(UserFund userFund) {
		return null;
	}

}
