package deal.manage.mapper;

import deal.manage.bean.Invitecode;
import deal.manage.bean.InvitecodeExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
@Mapper
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

    /**
     * <p>跟新邀请码状态</p>
     * @param inviteCode			邀请码
     * @param userId				使用人
     * @return
     */
    @Update("update dealpay_invitecode set `use` = #{userId} , `status` = 0 ,`count` = `count`+1 ,submitTime = now() where inviteCode = #{inviteCode} ")
	int updataInviteCode(@Param("inviteCode") String inviteCode, @Param("userId") String userId);

    /**
     * <p>根据邀请码查询邀请码</p>
     * @param inviteCode
     * @return
     */
    @Select("select * from dealpay_invitecode where   inviteCode = #{inviteCode} ")
	Invitecode findInviteCode(@Param("inviteCode") String inviteCode);
}