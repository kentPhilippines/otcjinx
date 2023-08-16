package alipay.manage.service.impl;

import alipay.manage.bean.UserFund;
import alipay.manage.mapper.UserFundMapper;
import alipay.manage.service.UserFundService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

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

	@Override
	public List<UserFund> findBankUserId() {

		return userFundDao.findBankUserId();

	}

	@Override
	public void updateAmount(BigDecimal amount, String userId) {
		  userFundDao.updateAmount(amount,userId);
	}

}
