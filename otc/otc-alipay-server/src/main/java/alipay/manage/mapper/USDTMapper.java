package alipay.manage.mapper;

import alipay.manage.api.channel.amount.recharge.usdt.USDTOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface USDTMapper {


    @Insert("insert into alipay_usdt_order (blockNumber, timeStamp, hash, blockHash, from, contractAddress, to, value,tokenName,tokenSymbol) " +
            "values (#{order.blockNumber}, #{order.timeStamp},#{order.hash},#{order.blockHash},#{order.from}" +
            ",#{order.ontractAddress},#{order.to},#{order.value},#{order.tokenName},#{order.tokenSymbol})")
    int addOrder(USDTOrder order);


}
