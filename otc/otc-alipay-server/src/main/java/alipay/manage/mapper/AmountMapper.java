package alipay.manage.mapper;

import alipay.manage.bean.Amount;
import alipay.manage.bean.AmountExample;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AmountMapper {
    int countByExample(AmountExample example);

    int deleteByExample(AmountExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Amount record);

    int insertSelective(Amount record);

    List<Amount> selectByExampleWithBLOBs(AmountExample example);

    List<Amount> selectByExample(AmountExample example);

    Amount selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Amount record, @Param("example") AmountExample example);

    int updateByExampleWithBLOBs(@Param("record") Amount record, @Param("example") AmountExample example);

    int updateByExample(@Param("record") Amount record, @Param("example") AmountExample example);

    int updateByPrimaryKeySelective(Amount record);

    int updateByPrimaryKeyWithBLOBs(Amount record);

    int updateByPrimaryKey(Amount record);

    @Select("select * from alipay_amount where orderId = #{orderId}")
    Amount findOrder(@Param("orderId") String orderId);

    @Update("update alipay_amount set orderStatus  = #{orderStatus}, approval = #{approval}, comment = #{comment}, submitTime= sysdate() where orderId = #{orderId}")
    int updataOrder(@Param("orderId") String orderId, @Param("orderStatus") String orderStatus, @Param("approval") String approval, @Param("comment") String comment);
}