package alipay.manage.mapper;

import alipay.manage.bean.UserRate;
import alipay.manage.bean.UserRateExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.MappedJdbcTypes;
@Mapper
public interface UserRateMapper {
    int countByExample(UserRateExample example);

    int deleteByExample(UserRateExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserRate record);

    int insertSelective(UserRate record);

    List<UserRate> selectByExample(UserRateExample example);

    UserRate selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserRate record, @Param("example") UserRateExample example);

    int updateByExample(@Param("record") UserRate record, @Param("example") UserRateExample example);

    int updateByPrimaryKeySelective(UserRate record);

    int updateByPrimaryKey(UserRate record);

    /**
     * <p>查询当前用户唯一可用的代付费率</p>
     * @param userId
     * @return
     */
    @Select("select * from alipay_user_rate where feeType = 2 and userId = #{userId}")
	UserRate findUserRateWitByUserId(@Param("userId") String userId);

    @Select("select * from alipay_user_rate where feeType = 1 and userId = #{userId} and switchs = 1 and payTypr = #{productAlipayScan} and status  = 1")
	UserRate findUserRate(@Param("userId")String userId,@Param("productAlipayScan") String productAlipayScan);
}