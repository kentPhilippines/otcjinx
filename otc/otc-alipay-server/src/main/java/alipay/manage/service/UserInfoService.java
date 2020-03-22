package alipay.manage.service;
import java.util.List;

import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.UserRate;
import otc.result.Result;

public interface UserInfoService {
	/**
	 * <p>查询所有的下级</p>
	 * @param user
	 * @return
	 */
	List<UserInfo> findSunAccount(UserInfo user);

	List<UserInfo> findSumAgentUserByAccount(String userId);

	/**
	 * <p>根据用户账号查询码商账号</p>
	 *
	 * @param username
	 * @return
	 */
	UserInfo getUser(String username);

	/**
	 * <p>根据账户id查询账户【查询资金账户】</p>
	 * @param userId
	 * @return
	 */
	//UserInfo findUserByAccount(String userId);

	/**
	 *	<p>根据自己的id查询所有自己子账户的id</p>
	 * @param userId
	 * @return
	 */
	List<String> findSunAccountByUserId(String userId);

	/**
	 * <p>修改会员为代理商</p>
	 * @param accountId
	 * @return
	 */
	boolean updateIsAgent(String accountId);

	/**
	 * <p>将用户总状态关闭【该状态关闭后该用户不产生任何资金变动】</p>
	 * @param userId				用户状态
	 * @return
	 */
	Boolean updataStatusEr(String userId);
	/**
	 * <p>根据账户id查询账户【查询详情账户】</p>
	 * @param userId
	 * @return
	 */
	UserInfo findUserInfoByUserId(String userId);
	/**
	 * <p>更新账户余额</p>
	 * @param userFund
	 * @return
	 */
	Boolean updataAmount(UserFund userFund);

	/**
	 * <p>根据费率id 查询费率具体信息</p>
	 * @param feeId
	 * @return
	 */
	UserRate findUserRateById(Integer feeId);
	/**
	 * <p>根据accountId查询所有下级的ID</p>
	 * @param accountId
	 * @return
	 */
	List<String> findSubLevelMembers(String accountId);

	/**
	 * <p>根据二维码编号，金额，媒介编号，生成一条二维码数据</p>
	 * @param qrcodeId
	 * @param mediumId
	 * @param amount
	 * @return
	 */
	Result addQrByMedium(String qrcodeId, String mediumId, String amount, String userId, String flag);

	UserFund findUserFundByAccount(String userId);

	/**
	 * 修改登录密码
	 * @param user
	 * @return
	 */
	boolean updataAccountPassword(UserInfo user);

	/**
	 * <p>根据用户明查询用户的详细信息</p>
	 *
	 * @param userId 用户名
	 * @return
	 */
	List<UserInfo> getLoginAccountInfo(String userId);
}
