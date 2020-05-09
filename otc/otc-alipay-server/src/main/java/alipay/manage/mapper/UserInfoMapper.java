package alipay.manage.mapper;

import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.UserInfoExample;

import java.util.List;

import alipay.manage.bean.UserRate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserInfoMapper {
    int countByExample(UserInfoExample example);

    int deleteByExample(UserInfoExample example);

    int deleteByPrimaryKey(@Param("id") Integer id, @Param("userId") String userId);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    List<UserInfo> selectByExample(UserInfoExample example);

    UserInfo selectByPrimaryKey(@Param("id") Integer id, @Param("userId") String userId);

    int updateByExampleSelective(@Param("record") UserInfo record, @Param("example") UserInfoExample example);

    int updateByExample(@Param("record") UserInfo record, @Param("example") UserInfoExample example);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    
    
    
    
    
    
    
    
    
    
    @Select("select * from alipay_user_info where userId = #{userId}")
    UserInfo selectByUserId(@Param("userId")String userId);

    UserInfo selectByUserName(String username);

    @Select("select * from alipay_user_info where userId = #{userId} or userName = #{userName}")
    UserInfo findUserId(@Param("userId") String userId, @Param("userName") String userName);

    @Select("select queryChildAgents(#{accountId})")
    List<String> selectChildAgentListById(@Param("accountId") String accountId);

    /**
     * <p>根据用户名id【唯一】 ，查询用户详细信息</p>
     *
     * @param userId
     * @return
     */
    @Select("select * from alipay_user_info where userId = #{userId}")
    UserInfo findUserByUserId(@Param("userId") String userId);

    @Select("select * from alipay_user_rate where userId = #{userId} and payTypr = #{passCode} and switchs = 1")
    UserRate selectUserRateByUserId(@Param("userId") String userId, @Param("passCode") String passCode);

    List<UserInfo> getLoginAccountInfo(String userId);

    @Select("select  id, userId, userName, cashBalance, rechargeNumber, freezeBalance, accountBalance, " +
            " sumDealAmount, sumRechargeAmount, sumProfit, sumAgentProfit, sumOrderCount, todayDealAmount, " +
            " todayProfit, todayOrderCount, todayAgentProfit, userType, agent, isAgent, createTime, " +
            " submitTime, status, version from alipay_user_fund where userId = #{userId}")
    UserFund selectUsrFundByUserId(@Param("userId") String userId);

    @Update("update alipay_user_info set ${paramKey} = #{paramValue} where userId = #{userId} ")
    int updateMerchantStatusByUserId(@Param("userId") String userId, @Param("paramKey") String paramKey, @Param("paramValue") String paramValue);

    @Update("update alipay_user_info set receiveOrderState = #{status}, remitOrderState = #{status} where userId = #{userId}")
    void stopAllStatusByUserId(@Param("userId") String userId, @Param("status") Integer status);

    @Update("update alipay_user_rate set switchs = #{status} where userId = #{userId} and userType = 1 ")
    void closeMerchantRateChannel(@Param("userId") String userId, @Param("status") Integer status);

    @Update("update alipay_user_info set switchs = 0 where userId = #{userId}")
	int updataStatusEr(@Param("userId")String userId);
    /**
     * 更新用户代理
     * @param user
     * @return
     */
    int updateproxyByUser(UserInfo user);

    /**
     * <p>修改登录密码</p>
     * @param userId
     * @param newPassword
     * @return
     */
    @Update("update alipay_user_info set password = #{newPassword} where userId = #{userId}")
	int updataPassword(@Param("userId")String userId, @Param("newPassword")String newPassword);

    
    @Update("update alipay_user_info set payPasword = #{newPassword} where userId = #{userId}")
	int updataPayPassword(String userId, String newPayPassword);

    
    @Update("update alipay_user_info set  todayDealAmount = 0 ,todayProfit = 0,todayOrderCount = 0 , todayAgentProfit = 0 ")
	void updateUserTime();
}