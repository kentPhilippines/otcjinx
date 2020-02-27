package alipay.manage.mapper;

import alipay.manage.bean.Medium;
import alipay.manage.bean.MediumExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MediumMapper {
    int countByExample(MediumExample example);

    int deleteByExample(MediumExample example);

    int deleteByPrimaryKey(@Param("id") Integer id, @Param("mediumNumber") String mediumNumber, @Param("mediumId") String mediumId);

    int insert(Medium record);

    int insertSelective(Medium record);

    List<Medium> selectByExampleWithBLOBs(MediumExample example);

    List<Medium> selectByExample(MediumExample example);

    Medium selectByPrimaryKey(@Param("id") Integer id, @Param("mediumNumber") String mediumNumber, @Param("mediumId") String mediumId);

    int updateByExampleSelective(@Param("record") Medium record, @Param("example") MediumExample example);

    int updateByExampleWithBLOBs(@Param("record") Medium record, @Param("example") MediumExample example);

    int updateByExample(@Param("record") Medium record, @Param("example") MediumExample example);

    int updateByPrimaryKeySelective(Medium record);

    int updateByPrimaryKeyWithBLOBs(Medium record);

    int updateByPrimaryKey(Medium record);
}