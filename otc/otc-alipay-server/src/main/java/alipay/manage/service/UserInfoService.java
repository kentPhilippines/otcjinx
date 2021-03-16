package alipay.manage.service;

import alipay.manage.bean.Amount;
import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.UserRate;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.List;

public interface UserInfoService {
	/**
	 * <p>查询所有的下级</p>
	 *
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

	/**
	 * <p>查询【资金】账户详情</p>
	 * @param userId
	 * @return
	 */
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

	/**
	 * <p>根据交易金额【查询可用的交易账户】</p>
	 * @param amount				交易金额
	 * @param flag 					是否为顶代选码模式  true是  false 否
	 * @return
	 */
	List<UserFund> findUserByAmount(BigDecimal amount, boolean flag);

	/**
	 * <P>根据用户id和产品类型查询用户交易费率</P>
	 * @param userId
	 * @param productAlipayScan
	 * @return
	 */
	UserRate findUserRate(String userId, String productAlipayScan);

    UserInfo getQrCodeUser(UserInfo qruser);

	/**
	 * 添加代理用户
	 * @param entity
	 * @return
	 */
	boolean addQrcodeUserInfo(UserInfo entity);

	/**
	 * 新增码商代理商数据
	 * @param user
	 * @return
	 */
	boolean addQrcodeUser(UserInfo user);

	/**
	 * 更新代理商
	 * @param user
	 * @return
	 */
	boolean updateproxyByUser(UserInfo user);


	/**
	 * 根据主键ID 更新账户余额
	 *
	 * @param id
	 * @return
	 */
	int updateBalanceById(Integer id, BigDecimal deduct, Integer version);

	int insertAmountEntitys(Amount amount);

	/**
	 * 根据渠道标记商户号查找渠道密钥
	 *
	 * @param oid_partner
	 * @return
	 */
	UserInfo findChannelAppId(String oid_partner);


	UserFund fundUserFundAccounrBalace(String userId);


	/**
	 * 查询商户余额
	 *
	 * @param userId
	 * @return
     */
    UserFund findBalace(String userId);

    /**
     * 查询商户交易url
     *
     * @param orderAccount
     * @return
     */
    UserInfo findDealUrl(String orderAccount);

    /**
     * 代理商专用查询
     *
     * @param appId
     * @return
     */
    UserInfo findUserAgent(String appId);


    /**
     * 查询用户密码用来验证签名
     *
     * @param appId
     * @return
     */
    UserInfo findPassword(String appId);

    /**
     * 查询账户货币类型
     *
     * @param appid
     * @return
     */
    UserFund findCurrency(String appid);


    /**
     * 查询商户信息
     *
     * @param userId
     * @return
     */
    List<UserInfo> finauserAll(String userId);


	/**
	 * 更新商户密钥
	 *
	 * @param userId
	 * @param publickey
	 * @param privactkey
	 * @return
	 */
	boolean updateDealKey(String userId, String publickey, String privactkey, String key);

	/**
	 * 针对交易结算做查询
	 *
	 * @param orderQrUser
	 * @return
	 */
	UserInfo findUserByOrder(String orderQrUser);

	/**
	 * 查询交易状态
	 *
	 * @param userId
	 * @return
	 */
	UserInfo getSwitchs(String userId);


	/**
	 * 针对渠道数据专门做缓存
	 *
	 * @param channelId
	 * @return
	 */
	UserInfo findNotifyChannel(String channelId);
}
