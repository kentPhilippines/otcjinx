package alipay.manage.mapper;

import alipay.manage.bean.RunOrder;
import alipay.manage.bean.RunOrderExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface RunOrderMapper {
    int countByExample(RunOrderExample example);

    int deleteByExample(RunOrderExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RunOrder record);

    int insertSelective(RunOrder record);

    List<RunOrder> selectByExampleWithBLOBs(RunOrderExample example);

    List<RunOrder> selectByExample(RunOrderExample example);

    RunOrder selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RunOrder record, @Param("example") RunOrderExample example);

    int updateByExampleWithBLOBs(@Param("record") RunOrder record, @Param("example") RunOrderExample example);

    int updateByExample(@Param("record") RunOrder record, @Param("example") RunOrderExample example);

    int updateByPrimaryKeySelective(RunOrder record);

    int updateByPrimaryKeyWithBLOBs(RunOrder record);

    int updateByPrimaryKey(RunOrder record);
      
    @Select("select * from alipay_run_order where orderAccount=#{orderAccount} and status=1")
	List<RunOrder> selectAllRunOrderByorderAccount(@Param("orderAccount")String orderAccount);
}