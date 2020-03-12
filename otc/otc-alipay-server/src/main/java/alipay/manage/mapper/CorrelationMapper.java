package alipay.manage.mapper;

import alipay.manage.bean.Correlation;
import alipay.manage.bean.CorrelationExample;
import alipay.manage.bean.util.UserCountBean;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
@Mapper
public interface CorrelationMapper {
	static final String CORR = "CORRUSER:DATA";
    int countByExample(CorrelationExample example);
	@CacheEvict(value=CORR, allEntries=true)
    int deleteByExample(CorrelationExample example);
	@CacheEvict(value=CORR, allEntries=true)
    int deleteByPrimaryKey(Integer id);
	@CacheEvict(value=CORR, allEntries=true)
    int insert(Correlation record);
	@CacheEvict(value=CORR, allEntries=true)
    int insertSelective(Correlation record);
    @Cacheable(cacheNames= {CORR} ,  unless="#result == null")
    List<Correlation> selectByExampleWithBLOBs(CorrelationExample example);
    @Cacheable(cacheNames= {CORR} ,  unless="#result == null")
    List<Correlation> selectByExample(CorrelationExample example);
    @Cacheable(cacheNames= {CORR} ,  unless="#result == null")
    Correlation selectByPrimaryKey(Integer id);
	@CacheEvict(value=CORR, allEntries=true)
    int updateByExampleSelective(@Param("record") Correlation record, @Param("example") CorrelationExample example);
	@CacheEvict(value=CORR, allEntries=true)
    int updateByExampleWithBLOBs(@Param("record") Correlation record, @Param("example") CorrelationExample example);
	@CacheEvict(value=CORR, allEntries=true)
    int updateByExample(@Param("record") Correlation record, @Param("example") CorrelationExample example);
	@CacheEvict(value=CORR, allEntries=true)
    int updateByPrimaryKeySelective(Correlation record);
	@CacheEvict(value=CORR, allEntries=true)
    int updateByPrimaryKeyWithBLOBs(Correlation record);
	@CacheEvict(value=CORR, allEntries=true)
    int updateByPrimaryKey(Correlation record);
    @Cacheable(cacheNames= {CORR} ,  unless="#result == null")
    @Select("select id,parentId,parentName,parentType,childrenId,childrenName,childrenType,distance,status,createTime,submitTime,medium from product_correlation where childrenName = #{accountId} and distance > 0 ")
    List<Correlation> selectAllParentRelList(@Param("accountId") String accountId);
    @Cacheable(cacheNames= {CORR} ,  unless="#result == null")
    @Select("select id,parentId,parentName,parentType,childrenId,childrenName,childrenType,distance,status,createTime,submitTime,medium from product_correlation where parentName = #{accountId} and childrenName = #{accountId} and distance =0")
    Correlation findEntityByParentId(@Param("accountId") String accountId);
    /**
     * <p>批量插入</p>
     * @param findCorrelation
     * @return
     */
	@CacheEvict(value=CORR, allEntries=true)
	int insertCorrlationList(List<Correlation> findCorrelation);
	/**
	 * <p>新增支付宝账号到数据关系表</p>
	 * @param findCorrelation
	 * @return
	 */
	@CacheEvict(value=CORR, allEntries=true)
	int addAccountMedium(@Param("list")List<Correlation> list);
	@CacheEvict(value=CORR, allEntries=true)
	@Update("update product_correlation " + 
			"    set childrenType = 1 " + 
			"    where  " + 
			"    childrenId = #{childrenId}")
	int updateChildren(@Param("childrenId")String childrenId); 
	@CacheEvict(value=CORR, allEntries=true)
	@Update("  update product_correlation " + 
			"    set parentType = 1 " + 
			"    where  " + 
			"    Id = #{id}")
	int updateParentId(@Param("id")int id);
	@Cacheable(cacheNames= {CORR} ,  unless="#result == null")
	@Select(" SELECT " + 
			" COUNT(1) AS userAgentCount , " + 													//总下线人数
			" COUNT(CASE WHEN  childrenType = 1 AND distance >1 THEN 1 END) AS  moreAgent,  " + //多级代理
			" COUNT(CASE WHEN  distance = 1 THEN 1 END)   AS agent,  " + 						//直接下级代理
			" COUNT(CASE WHEN  distance >1 THEN 1 END) AS agentCount,   " + 					//多级总下线
			" COUNT(CASE WHEN  distance = 1 AND childrenType = 2 THEN 1 END) AS userAgent,   " +  //直接会员
			" COUNT(CASE WHEN  childrenType = 2 AND distance >1 THEN 1 END) AS userCount   " + //多级会员
			" FROM product_correlation  WHERE parentId  = #{id} AND distance > 0 ")			
	UserCountBean findMyDateAgen(@Param("id")Integer id);
	@Cacheable(cacheNames= {CORR} ,  unless="#result == null")
	@Select("  SELECT COUNT(1) FROM "
			+ "product_correlation WHERE"
			+ " parentId  = #{id} AND distance > 0  " + 
			"  ")
	int findMyUserCount(@Param("id")Integer id);
}