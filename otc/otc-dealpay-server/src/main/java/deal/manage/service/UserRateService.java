package deal.manage.service;

import deal.manage.bean.UserRate;

public interface UserRateService {
    /**
     * 查询用户产品
     * @param userId
     * @param productCode
     * @return
     */
    UserRate findProductFeeBy(String userId, String productCode);
     /**
      * 查询当前用户费率值
      * @param userId
      * @return
      */
	UserRate findUserRateInfoByUserId(String userId);
	
	
	/**
	 * <p>查询当前用户的出款费率</p>
	 * @param userId
	 * @return
	 */
	UserRate findUserRateC(String userId);
	/**
	 * <p>卡商入款费率</p>
	 * @param account
	 * @return
	 */
	UserRate findUserRateR(String account);
	/**
	 * <p>添加一个费率</p>
	 * @param rate
	 */
	boolean  add(UserRate rate);
	/**
	 * <p>修改一个卡商的入款费率</p>
	 * @param userId			商户id
	 * @param fee				费率
	 * @return
	 */
	boolean updateRateR(String userId, String fee);
	/**
	 * <p>修改卡商的出款费率</p>
	 * @param userId
	 * @param fee
	 * @return
	 */
	boolean updateRateC(String userId, String fee);
}
