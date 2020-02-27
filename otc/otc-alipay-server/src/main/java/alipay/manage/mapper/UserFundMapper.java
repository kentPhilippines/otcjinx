package alipay.manage.mapper;

import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserFundExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserFundMapper {
    int countByExample(UserFundExample example);

    int deleteByExample(UserFundExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserFund record);

    int insertSelective(UserFund record);

    List<UserFund> selectByExample(UserFundExample example);

    UserFund selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserFund record, @Param("example") UserFundExample example);

    int updateByExample(@Param("record") UserFund record, @Param("example") UserFundExample example);

    int updateByPrimaryKeySelective(UserFund record);

    int updateByPrimaryKey(UserFund record);
}