package deal.manage.service;

import java.util.List;

import deal.manage.bean.UserFund;
import deal.manage.bean.UserInfo;

public interface UserFundService {
    UserFund showTodayReceiveOrderSituation(String userId);

    /**
     * 通过userId查询用户账户
     * @param userId
     * @return
     */
    UserFund findUserFund(String userId);

    
    /**
     * <p>分页查询卡商资金数据【包含费率】</p>
     * @param user
     * @return
     */
	List<UserFund> findSunAccount(UserInfo user);
}
