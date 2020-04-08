package deal.manage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import deal.manage.bean.UserFund;
import deal.manage.bean.UserInfo;
import deal.manage.mapper.UserFundMapper;
import deal.manage.service.UserFundService;
@Component
public class UserFundServiceImpl implements UserFundService {
	@Autowired UserFundMapper userFundDao;
	@Override//查看接单记录
	public UserFund showTodayReceiveOrderSituation(String userId) {
		return null;
	}

	@Override
	public UserFund findUserFund(String userId) {
		return userFundDao.findUserFund(userId);
	}

	@Override
	public List<UserFund> findSunAccount(UserInfo user) {
		return userFundDao.findSunAccount(user);
	}

	@Override
	public boolean updateIsAgent(String userId) {
		int a = userFundDao.updateIsAgent(userId);
		return a > 0 & a < 2;
	}

	@Override
	public UserFund findUserFundMount(String userId) {
		return userFundDao.findUserFundMount(userId);
	}

}
