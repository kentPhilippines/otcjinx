package alipay.manage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alipay.manage.bean.UserFund;
import alipay.manage.mapper.UserFundMapper;
import alipay.manage.service.UserFundService;
@Component
public class UserFundServiceImpl  implements UserFundService{
	@Autowired UserFundMapper userFundDao;
	@Override
	public UserFund showTodayReceiveOrderSituation(String userId) {
		return userFundDao.findUserFundByUserId(userId);
	}

	@Override
	public UserFund findUserInfoByUserId(String userId) {
		return userFundDao.findUserFundByUserId(userId);
	}

}
