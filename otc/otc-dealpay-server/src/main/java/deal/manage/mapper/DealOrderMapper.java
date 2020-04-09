package deal.manage.mapper;

import deal.manage.bean.DealOrder;
import deal.manage.bean.DealOrderExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
    @Update("update dealpay_deal_order set orderStatus = #{orderStatus} , submitTime = NOW() where orderId = #{orderId} ")
	int updateOrder(@Param("orderId")String orderId,@Param("orderStatus") String orderStatus);
    /**
     * <p>人工修改交易订单状态</p>
     * @param orderId			订单号
     * @param orderStatus		订单状态
     * @param msg				记录消息
     * @return
     */
    @Update("update dealpay_deal_order set orderStatus = #{orderStatus} , dealDescribe = #{msg} , submitTime = NOW() where orderId = #{orderId} ")
	int updateOrderStatus(@Param("orderId")String orderId,@Param("orderStatus") String orderStatus,@Param("msg")  String msg);
    
    /**
     * <p>查询交易订单</P>
     * @param orderId				订单号
     * @return
     */
    @Select("select * from dealpay_deal_order where orderId = #{orderId}")
	DealOrder findOrderByOrderId(@Param("orderId") String orderId);

	
	@Select(" select r.depositor AS dealDescribe ," + 
			"	    d.*" + 
			"      from " + 
			"      	dealpay_deal_order d" + 
			"      	left join dealpay_recharge r on r.orderId = d.associatedId" + 
			"      	left join dealpay_order_status p on d.orderId = p.orderId" + 
			"      where" + 
			"      1=1" + 
			"        and d.createTime between #{startTime} and #{endTime}" + 
			"        and d.orderType = #{orderType}	" + 
			"		and d.orderQrUser = #{userId}" + 
			"		order by d.createTime desc")
	List<DealOrder> findOrderByUser(
			@Param("userId")String userId, @Param("orderType")String orderType,
			@Param("startTime") String startTime,@Param("endTime") String endTime);

	/**
	 * 根据关联订单号查询顶大
	 * @param orderId
	 * @return
	 */
	@Select("select * from dealpay_deal_order where associatedId = #{orderId}")
	DealOrder findOrderByAssociatedId(@Param("orderId") String orderId);
}