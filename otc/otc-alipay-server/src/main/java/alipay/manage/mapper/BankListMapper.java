package alipay.manage.mapper;

import alipay.manage.bean.BankList;
import alipay.manage.bean.BankListExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface BankListMapper {
    int countByExample(BankListExample example);
    int deleteByExample(BankListExample example);
    int deleteByPrimaryKey(@Param("id") Integer id, @Param("bankcardId") String bankcardId);
    int insert(BankList record);
    int insertSelective(BankList record);
    List<BankList> selectByExampleWithBLOBs(BankListExample example);
    List<BankList> selectByExample(BankListExample example);
    BankList selectByPrimaryKey(@Param("id") Integer id, @Param("bankcardId") String bankcardId);
    int updateByExampleSelective(@Param("record") BankList record, @Param("example") BankListExample example);
    int updateByExampleWithBLOBs(@Param("record") BankList record, @Param("example") BankListExample example);
    int updateByExample(@Param("record") BankList record, @Param("example") BankListExample example);
    int updateByPrimaryKeySelective(BankList record);
    int updateByPrimaryKeyWithBLOBs(BankList record);
    int updateByPrimaryKey(BankList record);

    List<BankList> selectBankCardByQ(String userId);
}