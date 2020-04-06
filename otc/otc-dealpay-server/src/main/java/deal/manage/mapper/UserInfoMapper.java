package deal.manage.mapper;

import deal.manage.bean.UserFund;
import deal.manage.bean.UserInfo;
import deal.manage.bean.UserInfoExample;
import deal.manage.bean.UserRate;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
@Mapper
public interface UserInfoMapper {
    int countByExample(UserInfoExample example);

    int deleteByExample(UserInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    List<UserInfo> selectByExampleWithBLOBs(UserInfoExample example);

    List<UserInfo> selectByExample(UserInfoExample example);

    UserInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserInfo record, @Param("example") UserInfoExample example);

    int updateByExampleWithBLOBs(@Param("record") UserInfo record, @Param("example") UserInfoExample example);

    int updateByExample(@Param("record") UserInfo record, @Param("example") UserInfoExample example);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKeyWithBLOBs(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    
    
    
    
    
    
    @Select("select * from dealpay_user_info where userId = #{userId} or userName = #{userName}")
    UserInfo findUserId(@Param("userId") String userId, @Param("userName") String userName);
    @Select("select * from alipay_user_info where userId = #{userId}")
    UserInfo findUserByUserId(@Param("userId") String userId);

    @Select("select * from alipay_user_rate where userId = #{userId} and payTypr = #{passCode} and switchs = 1")
    UserRate selectUserRateByUserId(@Param("userId") String userId, @Param("passCode") String passCode);

    @Select("select  id, userId, userName, cashBalance, rechargeNumber, freezeBalance, accountBalance, " +
            " sumDealAmount, sumRechargeAmount, sumProfit, sumAgentProfit, sumOrderCount, todayDealAmount, " +
            " todayProfit, todayOrderCount, todayAgentProfit, userType, agent, isAgent, createTime, " +
            " submitTime, status, version from dealpay_user_fund where userId = #{userId}")
    UserFund selectUsrFundByUserId(@Param("userId") String userId);

    @Update("update dealpay_user_info set ${paramKey} = #{paramValue} where userId = #{userId} ")
    int updateMerchantStatusByUserId(@Param("userId") String userId, @Param("paramKey") String paramKey, @Param("paramValue") String paramValue);
    @Update("update dealpay_user_rate set switchs = #{status} where userId = #{userId} and userType = 1 ")
	void closeMerchantRateChannel(@Param("userId") String userId, @Param("status")  Integer code);
    @Update("update dealpay_user_info set receiveOrderState = #{status}, remitOrderState = #{status} where userId = #{userId}")
	void stopAllStatusByUserId(@Param("userId") String userId, @Param("status")  Integer code);

	
    @Update("update dealpay_user_info set password = #{newPassword} where userId = #{userId}")
	int updataPassword(@Param("userId")String userId, @Param("newPassword")String newPassword);

    
    @Update("update dealpay_user_info set payPasword = #{newPassword} where userId = #{userId}")
	int updataPayPassword(String userId, String newPayPassword);

}