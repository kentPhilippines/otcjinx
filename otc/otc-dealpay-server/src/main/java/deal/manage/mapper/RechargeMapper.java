package deal.manage.mapper;

import deal.manage.bean.RechargeExample;
import otc.bean.dealpay.Recharge;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface RechargeMapper {
    int countByExample(RechargeExample example);

    int deleteByExample(RechargeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Recharge record);

    int insertSelective(Recharge record);

    List<Recharge> selectByExampleWithBLOBs(RechargeExample example);

    List<Recharge> selectByExample(RechargeExample example);

    Recharge selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Recharge record, @Param("example") RechargeExample example);

    int updateByExampleWithBLOBs(@Param("record") Recharge record, @Param("example") RechargeExample example);

    int updateByExample(@Param("record") Recharge record, @Param("example") RechargeExample example);

    int updateByPrimaryKeySelective(Recharge record);

    int updateByPrimaryKeyWithBLOBs(Recharge record);

    int updateByPrimaryKey(Recharge record);

    @Select("select * from dealpay_recharge where orderId = #{orderId}")
	Recharge findOrderId(@Param("orderId") String orderId);
}