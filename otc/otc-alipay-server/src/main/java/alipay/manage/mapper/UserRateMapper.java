package alipay.manage.mapper;

import alipay.manage.bean.UserRate;
import alipay.manage.bean.UserRateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

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
}