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
    int countByExample(CorrelationDataExample example);
    int deleteByExample(CorrelationDataExample example);
    int deleteByPrimaryKey(@Param("id") Integer id, @Param("orderId") String orderId);
    int insert(CorrelationData  record);
    int insertSelective(CorrelationData record);
    List<CorrelationData> selectByExample(CorrelationDataExample example);
    CorrelationData selectByPrimaryKey(@Param("id") Integer id, @Param("orderId") String orderId);
    int updateByExampleSelective(@Param("record") CorrelationData record, @Param("example") CorrelationDataExample example);
    int updateByExample(@Param("record") CorrelationData record, @Param("example") CorrelationDataExample example);
    int updateByPrimaryKeySelective(CorrelationData record);
    int updateByPrimaryKey(CorrelationData record);
    @Select("  SELECT " + 
    		"  SUM(CASE WHEN  orderStatus = 2  THEN amount END) AS moreAmountRun," + 
    		"  SUM(CASE WHEN  orderStatus = 2  THEN 1 END) AS moreDealCount" + 
    		"  FROM alipay_correlation_data WHERE userId IN (" + 
    		"  SELECT childrenName FROM alipay_correlation WHERE parentName  = #{userId} )  AND  TO_DAYS(createTime) = TO_DAYS(now())"
    		 )
	UserCountBean findDealDate(@Param("userId") String userId );
    @Select("select  count(1) as  dataArray " + 
    		" from `alipay_correlation_data`"
    		+ " where  userId in "
    		+ "( select childrenName from "
    		+ "alipay_correlation WHERE parentName  = #{userId}"
    		+ " AND distance > 0  ) and orderStatus = 2  AND  TO_DAYS(createTime) = TO_DAYS(NOW())"
    		+ "group by userId")
    List<DataArray> findOnline(@Param("userId")String userId);
}