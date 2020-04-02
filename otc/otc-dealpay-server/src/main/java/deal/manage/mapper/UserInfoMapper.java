package deal.manage.mapper;

import deal.manage.bean.UserFund;
import deal.manage.bean.UserInfo;
import deal.manage.bean.UserInfoExample;
import deal.manage.bean.UserRate;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

	UserInfo findUserId(String userId, String userName);

	UserInfo findUserByUserId(String userId);

	UserRate selectUserRateByUserId(String userId, String passCode);

	UserFund selectUsrFundByUserId(String userId);

	int updateMerchantStatusByUserId(String userId, String paramKey, String paramValue);

	void closeMerchantRateChannel(String userId, int code);

	void stopAllStatusByUserId(String userId, int code);
}