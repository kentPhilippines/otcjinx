package alipay.manage.mapper;

import alipay.manage.bean.ExceptionOrder;
import alipay.manage.bean.ExceptionOrderExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExceptionOrderMapper {
    int countByExample(ExceptionOrderExample example);

    int deleteByExample(ExceptionOrderExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ExceptionOrder record);

    int insertSelective(ExceptionOrder record);

    List<ExceptionOrder> selectByExampleWithBLOBs(ExceptionOrderExample example);

    List<ExceptionOrder> selectByExample(ExceptionOrderExample example);

    ExceptionOrder selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ExceptionOrder record, @Param("example") ExceptionOrderExample example);

    int updateByExampleWithBLOBs(@Param("record") ExceptionOrder record, @Param("example") ExceptionOrderExample example);

    int updateByExample(@Param("record") ExceptionOrder record, @Param("example") ExceptionOrderExample example);

    int updateByPrimaryKeySelective(ExceptionOrder record);

    int updateByPrimaryKeyWithBLOBs(ExceptionOrder record);

    int updateByPrimaryKey(ExceptionOrder record);
}
