package alipay.manage.mapper;

import alipay.manage.api.channel.amount.recharge.usdt.USDTOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface USDTMapper {


    int insertOrder(@Param("order") USDTOrder order);


}
