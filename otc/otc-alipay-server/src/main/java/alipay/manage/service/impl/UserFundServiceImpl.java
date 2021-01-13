package alipay.manage.service.impl;

import alipay.manage.bean.UserFund;
import alipay.manage.mapper.UserFundMapper;
import alipay.manage.service.UserFundService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserFundServiceImpl implements UserFundService {
	@Resource
	UserFundMapper userFundDao;

	@Override
	public UserFund showTodayReceiveOrderSituation(String userId) {
		return userFundDao.findUserFundByUserId(userId);
	}

	@Override
	public UserFund findUserInfoByUserId(String userId) {
		return userFundDao.findUserFundByUserId(userId);
	}

}
