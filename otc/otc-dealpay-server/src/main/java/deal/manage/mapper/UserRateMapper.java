package deal.manage.mapper;

import deal.manage.bean.UserRate;
import deal.manage.bean.UserRateExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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

	UserRate findUserRateWitByUserId(String userId);
	/**
	 * <p>查询卡商出款费率</p>
	 * @param userId
	 * @return
	 */
	@Select("select * from  dealpay_user_rate where feeType = 2 and userId =  #{userId}")
	UserRate findUserRateC(@Param("userId") String userId);

	/**
	 * 查询卡商入款费率
	 * @param account
	 * @return
	 */
	@Select("select * from  dealpay_user_rate where feeType = 1 and userId =  #{userId}")
	UserRate findUserRateR(@Param("userId") String userId);
}