package alipay.manage.service.impl;

import alipay.manage.bean.UserRate;
import alipay.manage.mapper.UserRateMapper;
import alipay.manage.service.UserRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRateServiceImpl implements UserRateService {
    @Autowired
    UserRateMapper userRateMapper;
	@Override
	public UserRate findUserRateInfoByUserId(String userId) {
		// 查询当前用户的费率值
		UserRate userRate=userRateMapper.findUserRateInfoByUserId(userId);
		return userRate;
	}
	@Override
	public UserRate findUserRateR(String userId) {
		// TODO Auto-generated method stub
		return userRateMapper.findUserRateR(userId);
	}
	@Override
	public boolean add(UserRate rate) {
		// TODO Auto-generated method stub
		int insertSelective = userRateMapper.insertSelective(rate);
		return insertSelective > 0 && insertSelective < 2;
	}
	@Override
	public boolean updateRateR(String userId, String fee,String payTypr) {
		//修改一个码商的入款费率
		int a = userRateMapper.updateRateR(userId,fee,payTypr);
		return a > 0 && a < 2;
	}
	@Override
	public  UserRate findRateFee(Integer feeId) {
		return userRateMapper.selectByPrimaryKey(feeId);
	}
}
