package alipay.manage.mapper;

import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.DealOrderAppExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface DealOrderAppMapper {
    int countByExample(DealOrderAppExample example);

    int deleteByExample(DealOrderAppExample example);

    int deleteByPrimaryKey(@Param("id") Integer id, @Param("orderId") String orderId);

    int insert(DealOrderApp record);

    int insertSelective(DealOrderApp record);

    List<DealOrderApp> selectByExampleWithBLOBs(DealOrderAppExample example);

    List<DealOrderApp> selectByExample(DealOrderAppExample example);

    DealOrderApp selectByPrimaryKey(@Param("id") Integer id, @Param("orderId") String orderId);

    int updateByExampleSelective(@Param("record") DealOrderApp record, @Param("example") DealOrderAppExample example);

    int updateByExampleWithBLOBs(@Param("record") DealOrderApp record, @Param("example") DealOrderAppExample example);

    int updateByExample(@Param("record") DealOrderApp record, @Param("example") DealOrderAppExample example);

    int updateByPrimaryKeySelective(DealOrderApp record);

    int updateByPrimaryKeyWithBLOBs(DealOrderApp record);

    int updateByPrimaryKey(DealOrderApp record);
    @Select("select * from alipay_deal_order_app where orderId = #{orderId}")
	DealOrderApp findOrderByOrderId(@Param("orderId")String orderId);
}