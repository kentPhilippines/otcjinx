package alipay.manage.mapper;

import alipay.manage.bean.MediumExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import otc.bean.alipay.Medium;

import java.math.BigDecimal;
import java.util.List;
@Mapper
public interface MediumMapper {
    int countByExample(MediumExample example);

    int deleteByExample(MediumExample example);

    int deleteByPrimaryKey(@Param("id") Integer id, @Param("mediumNumber") String mediumNumber, @Param("mediumId") String mediumId);

    int insert(Medium record);

    int insertSelective(Medium record);

    List<Medium> selectByExampleWithBLOBs(MediumExample example);

    List<Medium> selectByExample(MediumExample example);

    Medium selectByPrimaryKey(@Param("id") Integer id);

    int updateByExampleSelective(@Param("record") Medium record, @Param("example") MediumExample example);

    int updateByExampleWithBLOBs(@Param("record") Medium record, @Param("example") MediumExample example);

    int updateByExample(@Param("record") Medium record, @Param("example") MediumExample example);

    int updateByPrimaryKeySelective(Medium record);

    int updateByPrimaryKeyWithBLOBs(Medium record);

    int updateByPrimaryKey(Medium record);


    List<Medium> findIsMyMediumPage(String accountId);

    @Select("select  *  from alipay_medium where mediumId = #{mediumId}")
    Medium findMediumBy(@Param("mediumId") String mediumId);

    @Select("select  *  from alipay_medium where code = #{mediumType} and attr = #{code} and isDeal = 2 and status = 1")
    List<Medium> findMediumByType(@Param("mediumType") String mediumType, @Param("code") String code);

    @Select("select  *  from alipay_medium where code = #{mediumType}  and isDeal = 2 and status = 1")
    List<Medium> findMediumByType1(@Param("mediumType") String mediumType);


    /**
     * 无权重查询当前在线接单媒介
     *
     * @param amount
     * @return
     */
    @Select("select * from alipay_medium where status = 1 and  (( mountSystem + #{amount}) < mountLimit ) ")
    List<Medium> findBankByAmount(@Param("amount") BigDecimal amount);


    /**
     * 带权重查询当前在线接单媒介
     *
     * @param amount
     * @param code
     * @return
     */
    List<Medium> findBankByAmountAndAttr(@Param("code") String[] code);


    @Update("update alipay_medium set mountNow - #{dealAmount} where mediumNumber = #{bankAccount} ")
    void subMountNow(@Param("bankAccount") String bankAccount, @Param("dealAmount") BigDecimal dealAmount);

    @Update("update alipay_medium set  mountNow = mountNow + #{dealAmount} , mountSystem = mountSystem + #{dealAmount}   where mediumNumber = #{bankAccount} ")
    void addMountNow(String bankAccount, BigDecimal dealAmount);
}