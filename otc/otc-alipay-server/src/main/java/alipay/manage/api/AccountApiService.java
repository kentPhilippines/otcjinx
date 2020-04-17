package alipay.manage.api;

import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.UserRate;
import otc.result.Result;

public interface AccountApiService {
	public static final String EDIT_PASSWORD_ADMIN = "EDIT_PASSWORD_ADMIN";
	public static final String EDIT_PASSWORD_QR = "EDIT_PASSWORD_QR";
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
	boolean updateIsAgent(Integer id,String userId);
	/**
	 * <p>修改 用户详情</p>
	 * @param user
	 * @return
	 */
	Result editAccount(UserInfo user);
	/**
	 * <p>修改用户登录密码</p>
	 * @param user
	 * @return
	 */
	Result editAccountPassword(UserInfo user);
	Result addAmount(UserFund userFund);

	/**
	 * 查询商户的费率
	 * @param userId
	 * @param passCode
	 * @return
	 */
    UserRate findUserRateByUserId(String userId, String passCode);
    /**
     * <p>查询当前用户的代付费率</p>
     * @param userId
     *	<p>当前用户只可以查询到唯一一个 可以正常使用的代付费率  【本地代付处理】【代付通道代付处理】</p>
     * @return
     */
	UserRate findUserRateWitByUserId(String userId);

	/**
	 * 根据商户号查询资金账户
	 * @param userId
	 * @return
	 */
	UserFund findUserFundByUserId(String userId);

	/**
	 *	根据商户的userId修改状态
	 * @param userId        商户账号
	 * @param paramKey        字段名
	 * @param paramValue    状态值
	 * @return				返回结果
	 */
    Result auditMerchantStatusByUserId(String userId, String paramKey, String paramValue);
}