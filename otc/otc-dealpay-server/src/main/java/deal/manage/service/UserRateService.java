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
}
