package deal.manage.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import deal.manage.bean.UserFund;
import deal.manage.bean.UserInfo;
import deal.manage.bean.UserRate;
import deal.manage.service.UserInfoService;
import otc.result.Result;
@Component
public class UserInfoServiceImpl implements UserInfoService {

	@Override
	public List<UserInfo> findSunAccount(UserInfo user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserInfo> findSumAgentUserByAccount(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserInfo getUser(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> findSunAccountByUserId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateIsAgent(String accountId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Boolean updataStatusEr(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserInfo findUserInfoByUserId(String userId) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updataReceiveOrderStateOFF(String userId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updataRemitOrderStateNO(String userId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updataRemitOrderStateOFF(String userId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<UserFund> findUserByWeight(String[] split) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserFund> findUserFund() {
		// TODO Auto-generated method stub
		return null;
	}

}
