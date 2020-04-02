package deal.manage.service;

import deal.manage.bean.UserFund;

public interface UserFundService {
    UserFund showTodayReceiveOrderSituation(String userId);

    /**
     * 通过userId查询用户账户
     * @param userId
     * @return
     */
    UserFund findUserInfoByUserId(String userId);
}
