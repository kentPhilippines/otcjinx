package alipay.manage.mapper;

import alipay.manage.bean.UserInfo;
import alipay.manage.bean.UserInfoExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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

    
    @Select("select * from alipay_user_info where userId = #{userId} or userName = #{userName}")
	UserInfo findUserId( @Param("userId")String userId, @Param("userName")String userName);

    
    /**
     * <p>根据用户名id【唯一】 ，查询用户详细信息</p>
     * @param userId
     * @return
     */
    @Select("select * from alipay_user_info where userId = #{userId}")
    UserInfo findUserByUserId( @Param("userId")String userId);
}