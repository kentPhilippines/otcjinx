package alipay.manage.mapper;

import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealOrderExample;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Mapper
public interface DealOrderMapper {
    static final String ORDER_INFO_CHANNEL = "ORDER:INFO:CHANNEL";

    int countByExample(DealOrderExample example);

    @CacheEvict(value = ORDER_INFO_CHANNEL, allEntries = true)
    int deleteByExample(DealOrderExample example);

    @CacheEvict(value = ORDER_INFO_CHANNEL, allEntries = true)
    int deleteByPrimaryKey(Integer id);
    @CacheEvict(value = ORDER_INFO_CHANNEL, allEntries = true)
    int insert(DealOrder record);

    @CacheEvict(value = ORDER_INFO_CHANNEL, allEntries = true)
    int insertSelective(DealOrder record);

    List<DealOrder> selectByExampleWithBLOBs(DealOrderExample example);

    List<DealOrder> selectByExample(DealOrderExample example);

    DealOrder selectByPrimaryKey(Integer id);

    @CacheEvict(value = ORDER_INFO_CHANNEL, allEntries = true)
    int updateByExampleSelective(@Param("record") DealOrder record, @Param("example") DealOrderExample example);

    @CacheEvict(value = ORDER_INFO_CHANNEL, allEntries = true)
    int updateByExampleWithBLOBs(@Param("record") DealOrder record, @Param("example") DealOrderExample example);

    @CacheEvict(value = ORDER_INFO_CHANNEL, allEntries = true)
    int updateByExample(@Param("record") DealOrder record, @Param("example") DealOrderExample example);

    @CacheEvict(value = ORDER_INFO_CHANNEL, allEntries = true)
    int updateByPrimaryKeySelective(DealOrder record);

    @CacheEvict(value = ORDER_INFO_CHANNEL, allEntries = true)
    int updateByPrimaryKeyWithBLOBs(DealOrder record);

    @CacheEvict(value = ORDER_INFO_CHANNEL, allEntries = true)
    int updateByPrimaryKey(DealOrder record);

    /**
     * <p>根据用户id 查询交易订单</p>
     *
     * @param id
     * @param userId
     * @param createTime
     * @return
     */
    List<DealOrder> selectByExampleByMyId(@Param("userId") String userId, @Param("createTime") String createTime, @Param("orderStatus") String orderStatus);

    /**
     * <p>根据用户id查询自己的交易订单号记录</p>
     * @param order
     * @return
     */
    List<DealOrder> findMyOrder(DealOrder order);
    @Cacheable(cacheNames = {ORDER_INFO_CHANNEL}, unless = "#result == null")
    @Select("select * from alipay_deal_order where orderId = #{orderId}")
	DealOrder findOrderByOrderId(@Param("orderId")String orderId);

    /**
     * 修改订单状态
     * @param orderId				订单号
     * @param status				订单状态
     * @param mag					修改备注
     * @return
     */
    @CacheEvict(value = ORDER_INFO_CHANNEL, allEntries = true)
    @Update("update alipay_deal_order set orderStatus  = #{status} , dealDescribe   = #{mag} ,submitTime = NOW()  , retain4  = 1   where  orderId = #{orderId}")
	int updateOrderStatus(@Param("orderId")String orderId, @Param("status")String status, @Param("mag")String mag);

    @Cacheable(cacheNames = {ORDER_INFO_CHANNEL}, unless = "#result == null")
    @Select("select * from alipay_deal_order where associatedId = #{associatedId}")
	DealOrder findOrderByAssociatedId(@Param("associatedId")String associatedId);

    @CacheEvict(value = ORDER_INFO_CHANNEL, allEntries = true)
    @Update("update alipay_deal_order set retain2  = #{id}  where  orderId = #{orderId}")
    void updataXianyYu(@Param("orderId") String orderId, @Param("id") String id);

    @Select("select retain2 , orderId from alipay_deal_order where createTime > DATE_ADD(NOW(),INTERVAL-3 HOUR)  and orderStatus = 1 and orderQrUser = 'XianYuZhifubao'")
    List<DealOrder> findXianYuOrder();

    @Select("SELECT retain2 , orderId FROM alipay_deal_order WHERE createTime > DATE_ADD(NOW(),INTERVAL -20 MINUTE)  AND orderStatus = 1 AND orderQrUser = 'XYALIPAYSCAN' LIMIT 50")
    List<DealOrder> findXianYuOrder1();

    @Select("SELECT retain2 , orderId FROM alipay_deal_order WHERE createTime > DATE_ADD(NOW(),INTERVAL -20 MINUTE)  AND orderStatus = 1 AND orderQrUser = 'ChuanShanJia' LIMIT 100")
    List<DealOrder> findXianYuOrder2();

    @Update("update alipay_deal_order set orderQr = #{bank} where orderId = #{orderId}")
    int updateBankInfoByOrderId(String bank, String orderId);

    @Cacheable(cacheNames = {ORDER_INFO_CHANNEL}, unless = "#result == null")
    @Select("select id, orderId, associatedId, orderStatus, dealAmount ,orderAccount, orderQrUser,externalOrderId,  notify  , isNotify  FROM alipay_deal_order  where  orderId = #{orderId}")
    DealOrder findOrderNotify(@Param("orderId") String orderId);

    @Cacheable(cacheNames = {ORDER_INFO_CHANNEL}, unless = "#result == null")
    @Select("select id, orderId, associatedId, orderStatus, dealAmount ,orderAccount, orderQrUser,externalOrderId FROM alipay_deal_order  where  orderId = #{orderId}")
    DealOrder findOrderStatus(String orderId);

    /*  @Insert("insert into  alipay_usdt_order (blockNumber, timeStamp, hash, blockHash, fromAccount, contractAddress, toAccount, value,tokenName,tokenSymbol) " +
              "values (#{order.blockNumber}, #{order.timeStamp},#{order.hash},#{order.blockHash},#{order.from}" +
              ",#{order.ontractAddress},#{order.to}#{order.value},#{order.tokenName},#{order.tokenSymbol})")
      int addUsdtOrder(@Param("order")USDTOrder order);*/
    @Insert("insert into  alipay_usdt_order (blockNumber, timeStamp, hash, blockHash, fromAccount, contractAddress, toAccount, value,tokenName,tokenSymbol) " +
            "values (#{blockNumber}, #{timeStamp},#{hash},#{blockHash},#{from}" +
            ",#{ontractAddress},#{to},#{value},#{tokenName},#{tokenSymbol})")
    int addUsdtOrder(@Param("blockNumber") String blockNumber, @Param("timeStamp") String timeStamp,
                     @Param("hash") String hash, @Param("blockHash") String blockHash, @Param("from") String from,
                     @Param("contractAddress") String contractAddress, @Param("to") String to,
                     @Param("value") String value, @Param("tokenName") String tokenName, @Param("tokenSymbol") String tokenSymbol);


    @Update("update alipay_deal_order set txhash = #{hash} where orderId = #{orderId}")
    int updateUsdtTxHash(@Param("orderId") String orderId, @Param("hash") String hash);


    /*
        查询未结算账户的订单  成功   且   retain4  = 1    且   10秒内 结算最多15 笔
     */
    @Select("" + " " +
            " ( select * from alipay_deal_order where orderStatus = 2  and  retain4 = 1   and (orderType = 1 or orderType = 3 )      and    submitTime  between    CURRENT_TIMESTAMP - INTERVAL 50 MINUTE   " +
            "     and  now() order by id limit 25) " +
            " union all " +
            " ( select * from alipay_deal_order  where orderStatus = 2  and  retain4 = 1   and  orderType = 4  and    submitTime  between    CURRENT_TIMESTAMP - INTERVAL 50 MINUTE  " +
            "        and  CURRENT_TIMESTAMP - INTERVAL 20 MINUTE )  ")
    List<DealOrder> findSuccessAndNotAmount();


    /**
     * 修改订单为以结算
     *
     * @param orderId
     */
    @Update(" update alipay_deal_order set retain4 = 0  where orderId = #{orderId}")
    void updateSuccessAndAmount(@Param("orderId") String orderId);
}
