package alipay.manage.mapper;

import alipay.manage.bean.DealWit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AlipayDealWitMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DealWit record);

    int insertSelective(DealWit record);

    DealWit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DealWit record);

    int updateByPrimaryKeyWithBLOBs(DealWit record);

    int updateByPrimaryKey(DealWit record);






    @Update(" update alipay_deal_wit set request = #{request}   where orderId = #{orderId}")
    void updateOrderRequest(@Param("orderId") String orderId,@Param("request")   String request);
    @Update(" update alipay_deal_wit set response = #{response}   where orderId = #{orderId}")
    void updateOrderResponse(@Param("orderId") String orderId, @Param("response")  String response);
    @Update(" update alipay_deal_wit set orderStatus = #{orderStatus}   where orderId = #{orderId}")
    int updateOrderStatus(@Param("orderId")String orderId,@Param("orderStatus")  String orderStatus);
    @Select(" select * from   alipay_deal_wit   where orderId = #{orderId}")
    DealWit findOrderByOrderId(@Param("orderId")String orderId);
    @Update(" update alipay_deal_wit set orderStatus = '5' , msg = #{msg}  where orderId = #{orderId}")
    void updateWitEr(@Param("orderId")String orderId,@Param("msg") String msg);
    @Update(" update alipay_deal_wit set orderStatus = '4' , msg = #{msg}  where orderId = #{orderId}")
    void witOrdererSu(@Param("orderId")String orderId,@Param("msg") String msg);
}
