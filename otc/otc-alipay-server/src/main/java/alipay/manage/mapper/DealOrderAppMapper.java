package alipay.manage.mapper;

import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.DealOrderAppExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Mapper
public interface DealOrderAppMapper {
    static final String ORDER_APP_INFO = "ORDER:APP:INFO";

    int countByExample(DealOrderAppExample example);

    @CacheEvict(value = ORDER_APP_INFO, allEntries = true)
    int deleteByExample(DealOrderAppExample example);

    @CacheEvict(value = ORDER_APP_INFO, allEntries = true)
    int deleteByPrimaryKey(@Param("id") Integer id, @Param("orderId") String orderId);

    int insert(DealOrderApp record);

    int insertSelective(DealOrderApp record);

    List<DealOrderApp> selectByExampleWithBLOBs(DealOrderAppExample example);

    List<DealOrderApp> selectByExample(DealOrderAppExample example);

    DealOrderApp selectByPrimaryKey(@Param("id") Integer id, @Param("orderId") String orderId);

    @CacheEvict(value = ORDER_APP_INFO, allEntries = true)
    int updateByExampleSelective(@Param("record") DealOrderApp record, @Param("example") DealOrderAppExample example);

    @CacheEvict(value = ORDER_APP_INFO, allEntries = true)
    int updateByExampleWithBLOBs(@Param("record") DealOrderApp record, @Param("example") DealOrderAppExample example);

    @CacheEvict(value = ORDER_APP_INFO, allEntries = true)
    int updateByExample(@Param("record") DealOrderApp record, @Param("example") DealOrderAppExample example);

    @CacheEvict(value = ORDER_APP_INFO, allEntries = true)
    int updateByPrimaryKeySelective(DealOrderApp record);

    @CacheEvict(value = ORDER_APP_INFO, allEntries = true)
    int updateByPrimaryKeyWithBLOBs(DealOrderApp record);

    @CacheEvict(value = ORDER_APP_INFO, allEntries = true)
    int updateByPrimaryKey(DealOrderApp record);

    @Cacheable(cacheNames = {ORDER_APP_INFO}, unless = "#result == null")
    @Select("select * from alipay_deal_order_app where orderId = #{orderId}")
    DealOrderApp findOrderByOrderId(@Param("orderId")String orderId);

    @CacheEvict(value = ORDER_APP_INFO, allEntries = true)
    @Update("update alipay_deal_order_app set orderStatus = #{orderStatusSu} ,submitTime = NOW() where orderId = #{orderId}")
	boolean updateOrderSu(@Param("orderId")String orderId, @Param("orderStatusSu")String orderStatusSu);

    @CacheEvict(value = ORDER_APP_INFO, allEntries = true)
    @Update("update alipay_deal_order_app set orderStatus = 4 ,submitTime = NOW() , dealDescribe = #{msg} where orderId = #{orderId}")
	void updateOrderEr(@Param("orderId") String orderId, @Param("msg") String msg);

    /**
     * 代理商结算标记
     * @param orderId
     * @return
     */
    @CacheEvict(value = ORDER_APP_INFO, allEntries = true)
    @Update("update alipay_deal_order_app set retain2 = #{yse} ,submitTime = NOW()   where orderId = #{orderId}")
    boolean updateOrderIsAgent(@Param("orderId") String orderId, @Param("yse") String yse);

    @Cacheable(cacheNames = {ORDER_APP_INFO}, unless = "#result == null")
    @Select("select *  from alipay_deal_order_app where appOrderId = #{appOrderId} and  orderAccount = #{appId}")
    DealOrderApp findOrderByApp(@Param("appId") String appId, @Param("appOrderId") String appOrderId);


    @Update("update alipay_deal_order_app  set txhash =  #{hash} where orderId = #{orderId} ")
    int updateUsdtTxHash(@Param("orderId") String orderId, @Param("hash") String hash);


    @Select(
            " select  sum(orderAmount) as orderAmount " +
                    " from alipay_deal_order_app where orderAccount = #{appId} and TO_DAYS(createTime) =  TO_DAYS(now()) and orderStatus = 2  group by orderStatus; "
    )
    DealOrderApp findOrderByAppSum(@Param("appId") String appId);
}
