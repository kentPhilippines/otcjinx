package alipay.manage.mapper;

import alipay.manage.bean.FileListExample;

import java.math.BigDecimal;
import java.util.List;

import alipay.manage.bean.UserInfo;
import alipay.manage.bean.UserInfoExample;
import otc.bean.alipay.FileList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheEvict;

@Mapper
public interface FileListMapper {
    static final String USERINFO_FUND = "USERQR:FUND";
    int countByExample(FileListExample example);

    int deleteByExample(FileListExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FileList record);

    int insertSelective(FileList record);

    List<FileList> selectByExampleWithBLOBs(FileListExample example);

    List<FileList> selectByExample(FileListExample example);

    FileList selectByPrimaryKey(Integer id);

    @CacheEvict(value=USERINFO_FUND, allEntries=true)
    int updateByExampleSelective(@Param("record") FileList record, @Param("example") FileListExample example);

    int updateByExampleWithBLOBs(@Param("record") FileList record, @Param("example") FileListExample example);
    int updateByExample(@Param("record") FileList record, @Param("example") FileListExample example);

    int updateByPrimaryKeySelective(FileList record);

    int updateByPrimaryKeyWithBLOBs(FileList record);

    int updateByPrimaryKey(FileList record);

    @Select("select * from alipay_file_list where concealId = #{concealId} and isDeal='2' ")
    FileList findConcealId(@Param("concealId")String mediumId);

    List<String> selectQrAmountList(String concealId);
    @Select(" select " + 
    		"		 l.id AS id,	" + 
    		"		 l.`fileId` AS fileId ," + 
    		"		 m.`mediumNumber` AS mediumNumber," + 
    		"		 m.`mediumPhone` AS phone," + 
    		"		 l.code AS `code`," + 
    		"		 l.`fixationAmount` AS fixationAmount,"
    		+ " l.fileholder as fileholder ," + 
    		"		 l.`createTime` ," + 
    		"		 l.`concealId`  , m.attr as attr" + 
    		"      from alipay_file_list l" + 
    		"      left join alipay_medium m    on l.concealId = m.mediumId" + 
    		"      where " + 
    		"	m.`isDeal` = '2'" + 
    		"	and m.`status` = 1" + 
    		"	and l.`isDeal` = 2 " + 
    		"    and l.`fixationAmount` = #{amount}")
	List<FileList> findQrByAmount(@Param("amount")BigDecimal amount);
}