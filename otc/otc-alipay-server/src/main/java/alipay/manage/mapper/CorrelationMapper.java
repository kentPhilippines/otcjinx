package alipay.manage.mapper;

import alipay.manage.bean.Correlation;
import alipay.manage.bean.CorrelationExample;
import alipay.manage.bean.util.UserCountBean;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
@Mapper
public interface CorrelationMapper {
    int countByExample(CorrelationExample example);
    int deleteByExample(CorrelationExample example);
    int deleteByPrimaryKey(Integer id);
    int insert(Correlation record);
    int insertSelective(Correlation record);
    List<Correlation> selectByExampleWithBLOBs(CorrelationExample example);

    List<Correlation> selectByExample(CorrelationExample example);

    Correlation selectByPrimaryKey(Integer id);
    int updateByExampleSelective(@Param("record") Correlation record, @Param("example") CorrelationExample example);
    int updateByExampleWithBLOBs(@Param("record") Correlation record, @Param("example") CorrelationExample example);
    int updateByExample(@Param("record") Correlation record, @Param("example") CorrelationExample example);
    int updateByPrimaryKeySelective(Correlation record);
    int updateByPrimaryKeyWithBLOBs(Correlation record);
    int updateByPrimaryKey(Correlation record);
    @Select("select id,parentId,parentName,parentType,childrenId,"
    		+ "childrenName,childrenType,distance,status,createTime,"
    		+ "submitTime,medium from alipay_correlation where "
    		+ "childrenName = #{accountId} and distance > 0 ")
    List<Correlation> selectAllParentRelList(@Param("accountId") String accountId);
    @Select("select id,parentId,parentName,parentType,childrenId,"
    		+ "childrenName,childrenType,distance,status,createTime,"
    		+ "submitTime,medium from alipay_correlation where "
    		+ "parentName = #{accountId} and childrenName = #{accountId} and distance =0")
    Correlation findEntityByParentId(@Param("accountId") String accountId);
    /**
     * <p>批量插入</p>
     * @param findCorrelation
     * @return
     */
	int insertCorrlationList(List<Correlation> findCorrelation);
	/**
	 * <p>新增支付宝账号到数据关系表</p>
	 * @param list
	 * @return
	 */
	int addAccountMedium(@Param("list")List<Correlation> list);

	@Update("update alipay_correlation " + 
			"    set childrenType = 1 " + 
			"    where  " + 
			"    childrenId = #{childrenId}")
	int updateChildren(@Param("childrenId")String childrenId); 
	@Update("  update alipay_correlation " + 
			"    set parentType = 1 " + 
			"    where  " + 
			"    Id = #{id}")
	int updateParentId(@Param("id")int id);
	@Select(" SELECT " + 
			" COUNT(1) AS userAgentCount , " + 													//总下线人数
			" COUNT(CASE WHEN  childrenType = 1 AND distance >1 THEN 1 END) AS  moreAgent,  " + //多级代理
			" COUNT(CASE WHEN  distance = 1 THEN 1 END)   AS agent,  " + 						//直接下级代理
			" COUNT(CASE WHEN  distance >1 THEN 1 END) AS agentCount,   " + 					//多级总下线
			" COUNT(CASE WHEN  distance = 1 AND childrenType = 2 THEN 1 END) AS userAgent,   " +  //直接会员
			" COUNT(CASE WHEN  childrenType = 2 AND distance >1 THEN 1 END) AS userCount   " + //多级会员
			" FROM alipay_correlation  WHERE parentName  = #{userId} AND distance > 0 ")
	UserCountBean findMyDateAgen(@Param("userId")String userId);
	@Select("  SELECT COUNT(1) FROM "
			+ "alipay_correlation WHERE"
			+ " parentName  = #{userId} AND distance > 0  " +
			"  ")
	int findMyUserCount(@Param("userId")String userId);
	@Select("select parentName from alipay_correlation WHERE childrenName = #{qrcodeId}  ORDER BY distance DESC LIMIT 1")
	String findAgent(@Param("qrcodeId")String qrcodeId);

}