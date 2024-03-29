package deal.manage.service.impl;

import cn.hutool.core.util.StrUtil;
import deal.manage.bean.*;
import deal.manage.bean.UserInfoExample.Criteria;
import deal.manage.mapper.UserFundMapper;
import deal.manage.mapper.UserInfoMapper;
import deal.manage.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.*;
@Component
public class UserInfoServiceImpl implements UserInfoService {
	@Autowired UserInfoMapper userInfoDao;
	@Autowired UserFundMapper userFundDao;
	@Override
	public List<UserInfo> findSunAccount(UserInfo user) {
		UserInfoExample example = new UserInfoExample();
		Criteria createCriteria = example.createCriteria();
		if (StrUtil.isNotBlank(user.getUserId())) {
			createCriteria.andUserIdEqualTo(user.getUserId());
		}
		if (StrUtil.isNotBlank(user.getAgent())) {
			createCriteria.andAgentEqualTo(user.getAgent());
		}
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
		UserInfoExample example = new UserInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andAgentEqualTo(userId);
		List<UserInfo> selectByExample = userInfoDao.selectByExample(example);
		List<String> list = new ArrayList();
		for (UserInfo user : selectByExample) {
			list.add(user.getUserId());
		}
		return list;
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
		UserFundExample example = new UserFundExample();
		UserFundExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userFund.getUserId());
		criteria.andVersionEqualTo(userFund.getVersion());
		userFund.setVersion(null);
		int updateByExampleSelective = userFundDao.updateByExampleSelective(userFund , example );
		return updateByExampleSelective > 0 && updateByExampleSelective   < 2;
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
		return userFundDao.findUserFund(userId);
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
		List<String> asList = null;
		List<List<String>> list = new ArrayList();
		for(String userId : split) {
			String userIdList = userInfoDao.queryChildAgents(userId);
			String[] split2 = userIdList.split(",");
			asList = Arrays.asList(split2);
			list.add(asList);
		}
		Set<String> set =new  HashSet<String>();
		for(List<String> userIdList :list) {
			for(String userId:userIdList) {
				set.add(userId);
			}
		}
		List<Object> asList2 = Arrays.asList(set.toArray());
		return userInfoDao.findUserByWeight(asList2);
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
