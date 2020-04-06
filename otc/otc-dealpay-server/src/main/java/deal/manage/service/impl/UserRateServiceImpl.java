package deal.manage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import deal.manage.bean.UserRate;
import deal.manage.mapper.UserRateMapper;
import deal.manage.service.UserRateService;
@Component
public class UserRateServiceImpl implements UserRateService {
	@Autowired UserRateMapper userRateDao;
	@Override
	public UserRate findProductFeeBy(String userId, String productCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserRate findUserRateInfoByUserId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserRate findUserRateC(String userId) {
		UserRate rate = userRateDao.findUserRateC(userId);
		return rate;
	}

	@Override
	public UserRate findUserRateR(String account) {
		UserRate rate = userRateDao.findUserRateR(account);
		return rate;
	}

}
