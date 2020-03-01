package alipay.manage.service;

import java.util.List;

import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserInfo;

public interface UserInfoService {

	/**
	 * <p>查询所有的下级</p>
	 * @param user
	 * @return
	 */
	List<UserInfo> findSunAccount(UserInfo user);

	List<UserInfo> findSumAgentUserByAccount(String userId);

	/**
	 * <p>根据账户id查询账户【查询资金账户】</p>
	 * @param userId
	 * @return
	 */
	UserFund findUserByAccount(String userId);

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

}
