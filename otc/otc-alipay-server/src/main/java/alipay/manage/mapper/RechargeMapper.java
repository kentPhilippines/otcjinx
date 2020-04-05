package alipay.manage.mapper;

import alipay.manage.bean.RechargeExample;
import otc.bean.dealpay.Recharge;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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

    /**
     * <p>修改充值订单状态</p>
     * @param orderId						订单号
     * @param orderStatus					订单状态
     * @return
     */
    @Update("update alipay_recharge set orderStatus = #{orderStatus} where orderId = #{orderId}")
	int updateOrderStatus(@Param("orderId")String orderId, @Param("orderStatus")String orderStatus);

    @Select("select * from alipay_recharge where orderId = #{orderId}")
	Recharge findRechargeOrder(@Param("orderId")String orderId);
}