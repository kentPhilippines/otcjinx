package alipay.manage.mapper;

import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealOrderExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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

    int insert(DealOrder record);

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
     * @param createTime
     * @param userId
     * @return
     */
    @Cacheable(cacheNames = {ORDER_INFO_CHANNEL}, unless = "#result == null")
    List<DealOrder> selectByExampleByMyId(@Param("userId")String userId,@Param("createTime") String createTime);

    /**
     * <p>根据用户id查询自己的交易订单号记录</p>
     * @param order
     * @return
     */
    List<DealOrder> findMyOrder(DealOrder order);

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
    @Update("update alipay_deal_order set orderStatus  = #{status} , dealDescribe   = #{mag} ,submitTime = NOW()   where  orderId = #{orderId}")
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


    @Select("id, orderId, associatedId, orderStatus, dealAmount ,orderAccount, orderQrUser,externalOrderId,  notify  , isNotify  FROM alipay_deal_order  where  orderId = #{orderId}")
    DealOrder findOrderNotify(@Param("orderId") String orderId);


    @Select("id, orderId, associatedId, orderStatus, dealAmount ,orderAccount, orderQrUser,externalOrderId FROM alipay_deal_order  where  orderId = #{orderId}")
    DealOrder findOrderStatus(String orderId);
}
