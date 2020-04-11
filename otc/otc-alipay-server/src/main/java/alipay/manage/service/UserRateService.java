package alipay.manage.service;

import alipay.manage.bean.UserRate;

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
	 * <p>码商入款费率</p>
	 * @param account
	 * @return
	 */
	UserRate findUserRateR(String userId);
	/**
	 * <p>查询当前用户的出款费率</p>
	 * @param userId
	 * @return
	 */
	UserRate findUserRateC(String userId);
	/**
	 * <p>添加一个费率</p>
	 * @param rate
	 */
	boolean  add(UserRate rate);
	/**
	 * <p>修改一个码商的入款费率</p>
	 * @param userId			商户id
	 * @param fee				费率
	 * @param payTypr           产品类型
	 * @return
	 */
	boolean updateRateR(String userId, String fee,String payTypr);
}
