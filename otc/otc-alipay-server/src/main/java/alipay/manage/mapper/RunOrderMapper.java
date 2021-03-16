package alipay.manage.mapper;

import alipay.manage.bean.RunOrder;
import alipay.manage.bean.RunOrderExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
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


    /**
     * 查询关联订单
     *
     * @param associatedId
     * @return
     */
    @Select("select orderId , associatedId  from alipay_run_order  where associatedId = #{associatedId}")
    List<RunOrder> findAssOrder(@Param("associatedId") String associatedId);
}