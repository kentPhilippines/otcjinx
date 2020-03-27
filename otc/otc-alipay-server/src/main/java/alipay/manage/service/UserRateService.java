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
}
