package deal.manage.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import deal.manage.bean.Amount;

@Mapper
public interface AmountMapper {

	@Select("select * from dealpay_amount where orderId = #{orderId}")
	Amount findOrder(String string);

    @Update("update alipay_amount set orderStatus  = #{orderStatus}, approval = #{approval}, comment = #{comment}, submitTime= sysdate() where orderId = #{orderId}")
    int updataOrder(@Param("orderId") String orderId, @Param("orderStatus") String orderStatus, @Param("approval") String approval, @Param("comment") String comment);
	/**
	 * <p>新增加减款订单</p>
	 * @param alipayAmount
	 * @return
	 */
    @Insert("insert into dealpay_amount\r\n" + 
    		"    <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >\r\n" + 
    		"      <if test=\"orderId != null\" >\r\n" + 
    		"        orderId,\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"userId != null\" >\r\n" + 
    		"        userId,\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"amountType != null\" >\r\n" + 
    		"        amountType,\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"accname != null\" >\r\n" + 
    		"        accname,\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"orderStatus != null\" >\r\n" + 
    		"        orderStatus,\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"amount != null\" >\r\n" + 
    		"        amount,\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"fee != null\" >\r\n" + 
    		"        fee,\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"actualAmount != null\" >\r\n" + 
    		"        actualAmount,\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"createTime != null\" >\r\n" + 
    		"        createTime,\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"submitTime != null\" >\r\n" + 
    		"        submitTime,\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"status != null\" >\r\n" + 
    		"        status,\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"retain1 != null\" >\r\n" + 
    		"        retain1,\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"retain2 != null\" >\r\n" + 
    		"        retain2,\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"dealDescribe != null\" >\r\n" + 
    		"        dealDescribe,\r\n" + 
    		"      </if>\r\n" + 
    		"    </trim>\r\n" + 
    		"    <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >\r\n" + 
    		"      <if test=\"orderId != null\" >\r\n" + 
    		"        #{orderId,jdbcType=CHAR},\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"userId != null\" >\r\n" + 
    		"        #{userId,jdbcType=VARCHAR},\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"amountType != null\" >\r\n" + 
    		"        #{amountType,jdbcType=CHAR},\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"accname != null\" >\r\n" + 
    		"        #{accname,jdbcType=VARCHAR},\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"orderStatus != null\" >\r\n" + 
    		"        #{orderStatus,jdbcType=CHAR},\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"amount != null\" >\r\n" + 
    		"        #{amount,jdbcType=DECIMAL},\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"fee != null\" >\r\n" + 
    		"        #{fee,jdbcType=DECIMAL},\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"actualAmount != null\" >\r\n" + 
    		"        #{actualAmount,jdbcType=DECIMAL},\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"createTime != null\" >\r\n" + 
    		"        #{createTime,jdbcType=TIMESTAMP},\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"submitTime != null\" >\r\n" + 
    		"        #{submitTime,jdbcType=TIMESTAMP},\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"status != null\" >\r\n" + 
    		"        #{status,jdbcType=INTEGER},\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"retain1 != null\" >\r\n" + 
    		"        #{retain1,jdbcType=CHAR},\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"retain2 != null\" >\r\n" + 
    		"        #{retain2,jdbcType=CHAR},\r\n" + 
    		"      </if>\r\n" + 
    		"      <if test=\"dealDescribe != null\" >\r\n" + 
    		"        #{dealDescribe,jdbcType=LONGVARCHAR},\r\n" + 
    		"      </if>\r\n" + 
    		"    </trim>")
	int insertAmountEntitys(Amount alipayAmount);

}
