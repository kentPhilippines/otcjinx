package alipay.manage.mapper;

import alipay.manage.bean.UserRate;
import alipay.manage.bean.UserRateExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Mapper
public interface UserRateMapper {
    static final String RATE = "USERRATE:INFO";

    int countByExample(UserRateExample example);

    int deleteByExample(UserRateExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserRate record);

    int insertSelective(UserRate record);

    List<UserRate> selectByExample(UserRateExample example);

    UserRate selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserRate record, @Param("example") UserRateExample example);

    int updateByExample(@Param("record") UserRate record, @Param("example") UserRateExample example);

    int updateByPrimaryKeySelective(UserRate record);

    int updateByPrimaryKey(UserRate record);

    /**
     * <p>查询当前用户开启的代付费率</p>
     *
     * @param userId
     * @return
     */
    @Select("select * from alipay_user_rate where feeType = 2 and `switchs` = 1 and userId = #{userId} ")
    List<UserRate> findUserRateWitByUserId(@Param("userId") String userId);

    @Select("select * from alipay_user_rate where feeType = 1 and userId = #{userId} and `switchs` = 1 and payTypr = #{productAlipayScan} and status  = 1")
    UserRate findUserRate(@Param("userId")String userId,@Param("productAlipayScan") String productAlipayScan);

    /**
     * 通过用户查询产品的费率
     * @param userId
     * @param productCode
     * @return
     */
    @Select("select * from alipay_user_rate where userId = #{userId} and payTypr = #{payTypr} and channelId = #{channel}")
    UserRate findProductFeeBy(@Param("userId")String userId,@Param("payTypr") String productCode,@Param("channel")  String channel);


    @Select("select * from alipay_user_rate where id = #{id}")
    UserRate findFeeById(@Param("id")Integer id);

    @Select("select * from alipay_user_rate where userId = #{userId}")
    List<UserRate> findUserRateInfoByUserId(@Param("userId") String userId);

    /**
     * 查询码商入款费率
     * @return
     */
    @Select("select * from alipay_user_rate where feeType = 1 and userId =  #{userId}")
    UserRate findUserRateR(@Param("userId") String userId);


    @Update("update alipay_user_rate set fee = #{fee},payTypr=#{payTypr} where feeType = 1 and userId = #{userId} ")
    int updateRateR(@Param("userId") String userId, @Param("fee") String fee, @Param("payTypr") String payTypr);

    @Select("select * from alipay_user_rate where userId = #{userId} and payTypr = #{product} and channelId = #{channel}")
    UserRate findProductFeeByAll(@Param("userId") String userId, @Param("product") String product, @Param("channel") String channelId);


    /**
     * 根据ip查询费率数据，不做费率计算和条件判断，可以做长久缓存
     *
     * @param feeId
     * @return
     */
    @Cacheable(cacheNames = {RATE}, unless = "#result == null")
    @Select("select * from alipay_user_rate where id = #{id}")
    UserRate findRateFeeType(@Param("id") Integer id);

    /**
     * 查询费率，不管是否开关
     *
     * @param userId   账号
     * @param userType 用户类型
     * @param payTypr  支付类型
     * @param feeType  费率类型
     * @return
     */
    @Select("select * from alipay_user_rate where userId = #{userId} and userType = #{userType} and payTypr = #{payTypr} and feeType  = #{feeType}")
    UserRate findAgentChannelFee(@Param("userId") String userId, @Param("userType") Integer userType, @Param("payTypr") String payTypr, @Param("feeType") Integer feeType);

    @Select("select * from alipay_user_rate where feeType = 2 and `switchs` = 1 and userId = #{userId} limint 1 ")
    UserRate findUserRateWitByUserIdApp(String userId);



    @Select("select * from alipay_user_rate where userId = #{appId}  and payTypr = #{payTypr} and  `switchs` = 1  and feeType = 1 " )
    List<UserRate> findOpenFeeR(@Param("appId")  String appId, @Param("payTypr") String payTypr);
}