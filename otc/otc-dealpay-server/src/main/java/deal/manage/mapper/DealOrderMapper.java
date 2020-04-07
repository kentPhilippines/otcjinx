package deal.manage.mapper;

import deal.manage.bean.DealOrder;
import deal.manage.bean.DealOrderExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
@Mapper
public interface DealOrderMapper {
    int countByExample(DealOrderExample example);

    int deleteByExample(DealOrderExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DealOrder record);

    int insertSelective(DealOrder record);

    List<DealOrder> selectByExampleWithBLOBs(DealOrderExample example);

    List<DealOrder> selectByExample(DealOrderExample example);

    DealOrder selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DealOrder record, @Param("example") DealOrderExample example);

    int updateByExampleWithBLOBs(@Param("record") DealOrder record, @Param("example") DealOrderExample example);

    int updateByExample(@Param("record") DealOrder record, @Param("example") DealOrderExample example);

    int updateByPrimaryKeySelective(DealOrder record);

    int updateByPrimaryKeyWithBLOBs(DealOrder record);

    int updateByPrimaryKey(DealOrder record);

    
    /**
     * <p>修改订单状态</p>
     * @param orderId			订单号
     * @param orderStatus		订单状态
     * @return
     */
    @Update("update dealpay_deal_order set orderStatus = #{orderStatus} where orderId = #{orderId} ")
	int updateOrder(@Param("orderId")String orderId,@Param("orderStatus") String orderStatus);
    /**
     * <p>人工修改交易订单状态</p>
     * @param orderId			订单号
     * @param orderStatus		订单状态
     * @param msg				记录消息
     * @return
     */
    @Update("update dealpay_deal_order set orderStatus = #{orderStatus} ,dealDescribe = #{msg} where orderId = #{orderId} ")
	int updateOrderStatus(@Param("orderId")String orderId,@Param("orderStatus") String orderStatus,@Param("msg")  String msg);
    
    /**
     * <p>查询交易订单</P>
     * @param orderId				订单号
     * @return
     */
	DealOrder findOrderByOrderId(String orderId);
}