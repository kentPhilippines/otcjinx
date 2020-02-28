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

}
