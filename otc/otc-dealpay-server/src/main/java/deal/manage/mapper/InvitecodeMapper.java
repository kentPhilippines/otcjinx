package deal.manage.mapper;

import deal.manage.bean.Invitecode;
import deal.manage.bean.InvitecodeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface InvitecodeMapper {
    int countByExample(InvitecodeExample example);

    int deleteByExample(InvitecodeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Invitecode record);

    int insertSelective(Invitecode record);

    List<Invitecode> selectByExample(InvitecodeExample example);

    Invitecode selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Invitecode record, @Param("example") InvitecodeExample example);

    int updateByExample(@Param("record") Invitecode record, @Param("example") InvitecodeExample example);

    int updateByPrimaryKeySelective(Invitecode record);

    int updateByPrimaryKey(Invitecode record);
}