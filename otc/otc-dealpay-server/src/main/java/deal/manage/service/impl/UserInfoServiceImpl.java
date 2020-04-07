package deal.manage.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;
import deal.manage.bean.UserFund;
import deal.manage.bean.UserInfo;
import deal.manage.bean.UserInfoExample;
import deal.manage.bean.UserInfoExample.Criteria;
import deal.manage.bean.UserRate;
import deal.manage.mapper.UserFundMapper;
import deal.manage.mapper.UserInfoMapper;
import deal.manage.service.UserInfoService;
import otc.result.Result;
@Component
public class UserInfoServiceImpl implements UserInfoService {
	@Autowired UserInfoMapper userInfoDao;
	@Autowired UserFundMapper userFundDao;
	@Override
	public List<UserInfo> findSunAccount(UserInfo user) {
		UserInfoExample example = new UserInfoExample();
		Criteria createCriteria = example.createCriteria();
		if(StrUtil.isNotBlank(user.getUserId()))
			createCriteria.andUserIdEqualTo(user.getUserId());
		if(StrUtil.isNotBlank(user.getAgent()))
			createCriteria.andAgentEqualTo(user.getAgent());
		List<UserInfo> selectByExample = userInfoDao.selectByExample(example);
		return selectByExample;
	}

	@Override
	public List<UserInfo> findSumAgentUserByAccount(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserInfo getUser(String username) {
		return null;
	}

	@Override
	public List<String> findSunAccountByUserId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateIsAgent(String userId) {
		int a = userInfoDao.updateIsAgent(userId);
		return a > 0 && a < 2;
	}

	@Override
	public Boolean updataStatusEr(String userId) {
		int a = userInfoDao.updataStatusEr(userId);
		return a > 0 && a < 2;
	}

	@Override
	public UserInfo findUserInfoByUserId(String userId) {
		return userInfoDao.findUserByUserId(userId);
	}

	@Override
	public Boolean updataAmount(UserFund userFund) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserRate findUserRateById(Integer feeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> findSubLevelMembers(String accountId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result addQrByMedium(String qrcodeId, String mediumId, String amount, String userId, String flag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserFund findUserFundByAccount(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updataAccountPassword(UserInfo user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<UserInfo> getLoginAccountInfo(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserFund> findUserByAmount(BigDecimal amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserRate findUserRate(String userId, String productAlipayScan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserInfo getQrCodeUser(UserInfo qruser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addQrcodeUserInfo(UserInfo entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addQrcodeUser(UserInfo user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateproxyByUser(UserInfo user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int updateBalanceById(Integer id, BigDecimal deduct, Integer version) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean updateReceiveOrderState(String userId, Integer valueOf) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updataReceiveOrderStateNO(String userId) {
		int a = userInfoDao.updataReceiveOrderStateNO(userId);
		return a  > 0 && a  < 2;
	}

	@Override
	public boolean updataReceiveOrderStateOFF(String userId) {
		int a = userInfoDao.updataReceiveOrderStateOFF(userId);
		return a  > 0 && a  < 2;
	}

	@Override
	public boolean updataRemitOrderStateNO(String userId) {
		int a = userInfoDao.updataRemitOrderStateNO(userId);
		return a  > 0 && a  < 2;
	}

	@Override
	public boolean updataRemitOrderStateOFF(String userId) {
		int a = userInfoDao.updataRemitOrderStateOFF(userId);
		return a  > 0 && a  < 2;
	}

	@Override
	public List<UserFund> findUserByWeight(String[] split) {
		//1,根据顶代账号查询所有下线账号   账号总开关开启， 出款接单开启
		
		
		
		
		return null;
	}

	@Override
	public List<UserFund> findUserFund() {
		return userInfoDao.findUserFund();
	}

	@Override
	public UserInfo findUserFundKeyId(String userId) {
		return userInfoDao.findUserFundKeyId(userId);
	}

}
