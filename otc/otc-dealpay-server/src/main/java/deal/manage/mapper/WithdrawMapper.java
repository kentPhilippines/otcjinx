package deal.manage.mapper;

import deal.manage.bean.Withdraw;
import deal.manage.bean.WithdrawExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
@Mapper
public interface WithdrawMapper {
    int countByExample(WithdrawExample example);

    int deleteByExample(WithdrawExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Withdraw record);

    int insertSelective(Withdraw record);

    List<Withdraw> selectByExample(WithdrawExample example);

    Withdraw selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Withdraw record, @Param("example") WithdrawExample example);

    int updateByExample(@Param("record") Withdraw record, @Param("example") WithdrawExample example);

    int updateByPrimaryKeySelective(Withdraw record);

    int updateByPrimaryKey(Withdraw record);
    
    @Select("select * from dealpay_withdraw where orderId = #{orderId}")
	Withdraw findOrderId(@Param("orderId") String orderId);

    @Update("update dealpay_withdraw set orderStatus = 3 , dealDescribe = #{message} where orderId = #{orderId}")
	int updateStatusEr(@Param("orderId") String orderId, @Param("message") String message);
    @Update("update dealpay_withdraw set orderStatus = 2 , dealDescribe = #{message} where orderId = #{orderId}")
	int updateStatusSu(@Param("orderId") String orderId,@Param("message") String msg);
}