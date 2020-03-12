package alipay.manage.mapper;
import alipay.manage.bean.CorrelationData;
import alipay.manage.bean.CorrelationDataExample;
import alipay.manage.bean.util.DataArray;
import alipay.manage.bean.util.UserCountBean;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
@Mapper
public interface CorrelationDataMapper {
	static final String CORR_DATA = "CORR:DATA";
    int countByExample(CorrelationDataExample example);
	@CacheEvict(value=CORR_DATA, allEntries=true)
    int deleteByExample(CorrelationDataExample example);
	@CacheEvict(value=CORR_DATA, allEntries=true)
    int deleteByPrimaryKey(@Param("id") Integer id, @Param("orderId") String orderId);
	@CacheEvict(value=CORR_DATA, allEntries=true)
    int insert(CorrelationData  record);
	@CacheEvict(value=CORR_DATA, allEntries=true)
    int insertSelective(CorrelationData record);
    @Cacheable(cacheNames= {CORR_DATA} ,  unless="#result == null")
    List<CorrelationData> selectByExample(CorrelationDataExample example);
    @Cacheable(cacheNames= {CORR_DATA} ,  unless="#result == null")
    CorrelationData selectByPrimaryKey(@Param("id") Integer id, @Param("orderId") String orderId);
    @CacheEvict(value=CORR_DATA, allEntries=true)
    int updateByExampleSelective(@Param("record") CorrelationData record, @Param("example") CorrelationDataExample example);
    @CacheEvict(value=CORR_DATA, allEntries=true)
    int updateByExample(@Param("record") CorrelationData record, @Param("example") CorrelationDataExample example);
    @CacheEvict(value=CORR_DATA, allEntries=true)
    int updateByPrimaryKeySelective(CorrelationData record);
    @CacheEvict(value=CORR_DATA, allEntries=true)
    int updateByPrimaryKey(CorrelationData record);
    @Cacheable(cacheNames= {CORR_DATA} ,  unless="#result == null")
    @Select("  SELECT " + 
    		"  SUM(CASE WHEN  orderStatus = 2  THEN amount END) AS moreAmountRun," + 
    		"  SUM(CASE WHEN  orderStatus = 2  THEN 1 END) AS moreDealCount" + 
    		"  FROM product_correlation_data WHERE userId IN (" + 
    		"  SELECT childrenName FROM product_correlation WHERE parentId  = #{id} )  AND  TO_DAYS(createTime) = TO_DAYS(now())" 
    		 )
	UserCountBean findDealDate(@Param("id") Integer id );
    @Cacheable(cacheNames= {CORR_DATA} ,  unless="#result == null")
    @Select("select  count(1) as  dataArray " + 
    		" from `product_correlation_data`"
    		+ " where  userId in "
    		+ "( select childrenName from "
    		+ "product_correlation WHERE parentId  = #{id}"
    		+ " AND distance > 0  ) and orderStatus = 2  AND  TO_DAYS(createTime) = TO_DAYS(NOW())"
    		+ "group by userId")
    List<DataArray> findOnline(@Param("id")Integer id);
}