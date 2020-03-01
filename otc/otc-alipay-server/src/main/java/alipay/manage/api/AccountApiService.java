package alipay.manage.api;

import alipay.manage.bean.UserInfo;
import otc.result.Result;

public interface AccountApiService {

	/**
	 * <p>添加一个用户</p>
	 * <strong>添加一个用户，未强制规定该用户是否为代理商</strong>
	 * @param user
	 * @return
	 */
	Result addAccount(UserInfo user);
	
	Result  login(UserInfo user);

	/**
	 * <p>修改密码【登录密码】</p>
	 * @param user
	 * @return
	 */
	Result updateLoginPassword(UserInfo user);

	
	Result updatePayPassword(UserInfo user);

	/**
	 * <p>根据账号查询自己详细信息</p>
	 * @param userId
	 * @return
	 */
	UserInfo findUserInfo(String userId);

	
	/**
	 * <p>将会员修改为代理商</p>
	 * @param accountId
	 * @return
	 */
	boolean updateIsAgent(String accountId);

	
	
	
	
	

}
