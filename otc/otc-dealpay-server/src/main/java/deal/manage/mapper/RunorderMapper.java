package deal.manage.mapper;

import deal.manage.bean.Runorder;
import deal.manage.bean.RunorderExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface RunorderMapper {
    int countByExample(RunorderExample example);

    int deleteByExample(RunorderExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Runorder record);

    int insertSelective(Runorder record);

    List<Runorder> selectByExampleWithBLOBs(RunorderExample example);

    List<Runorder> selectByExample(RunorderExample example);

    Runorder selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Runorder record, @Param("example") RunorderExample example);

    int updateByExampleWithBLOBs(@Param("record") Runorder record, @Param("example") RunorderExample example);

    int updateByExample(@Param("record") Runorder record, @Param("example") RunorderExample example);

    int updateByPrimaryKeySelective(Runorder record);

    int updateByPrimaryKeyWithBLOBs(Runorder record);

    int updateByPrimaryKey(Runorder record);
}