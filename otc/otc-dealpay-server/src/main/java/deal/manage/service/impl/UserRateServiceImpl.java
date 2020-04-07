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

	@Override
	public boolean add(UserRate rate) {
		int insertSelective = userRateDao.insertSelective(rate);
		return insertSelective > 0 && insertSelective < 2;
	}

	@Override
	public boolean updateRateR(String userId, String fee) {
		int a = userRateDao.updateRateR(userId,fee);
		return a > 0 && a < 2;
	}

	@Override
	public boolean updateRateC(String userId, String fee) {
		int updateRateC = userRateDao.updateRateC(userId, fee);
		return updateRateC > 0 && updateRateC < 2;
	}

}
