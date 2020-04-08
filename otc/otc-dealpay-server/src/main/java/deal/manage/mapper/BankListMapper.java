package deal.manage.mapper;

import deal.manage.bean.BankList;
import deal.manage.bean.BankListExample;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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

    
    
    
    /**
     * 银行卡号查询
     * @param bankNo			卡号
     * @return
     */
    @Select("select * from dealpay_bank_list where bankcardAccount  = #{bankNo} and isDeal = 2")
	BankList findBankByNo(@Param("bankNo")String bankNo);
    @Delete("  delete from dealpay_bank_list     where id = #{id}"  )
	int deleteId(@Param("id")String id);

    
    @Select("select * from dealpay_bank_list where cardType = #{cardType} and isDeal = 2 ")
	List<BankList> findBankCardByType(@Param("cardType")Integer cardType);

    /**
     * <p>银行卡置为不可用</p>
     * @param id				银行卡id
     * @return
     */
    @Update("update dealpay_bank_list set status = 0 where id = #{id}")
	int updataQrStatusEr(@Param("id")String id);

    /**
     * <p>银行卡置为可用</p>
     * @param id
     * @return
     */
    @Update("update dealpay_bank_list set status = 1  where id = #{id}")
	int updataStatusSu(@Param("id")String id);

    @Select("select * from dealpay_bank_list where bankcardId = #{bankNo}")
	BankList findBankInfoNo(@Param("bankNo")String bankNo);
    @Select("select * from dealpay_bank_list where account = #{userId}")
	List<BankList> findBankCardByQr(@Param("userId")String userId);

    
    @Select("SELECT * FROM dealpay_bank_list  " + 
    		" WHERE account IN  ( SELECT userId FROM `dealpay_user_fund` WHERE " + 
    		"  accountBalance > #{amount}  AND userId IN " + 
    		"  (SELECT userId FROM `dealpay_user_info` WHERE  `switchs` = 1))" 
    		 )
	List<BankList> findDealBank(@Param("amount") BigDecimal amount);

    @Select("select * from dealpay_bank_list where cardType = 1 and status = 1 and isDeal = 2")
	List<BankList> findSystemBank();
}