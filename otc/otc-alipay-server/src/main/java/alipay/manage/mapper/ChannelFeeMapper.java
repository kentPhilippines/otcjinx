package alipay.manage.mapper;

import alipay.manage.bean.ChannelFee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;

@Mapper
public interface ChannelFeeMapper {
	static final String CHANNEL = "CHANNEL:INFO:FEE";

	/**
	 * <p>实现类查询</p>
	 *
	 * @param channelId
	 * @param feeType
	 * @return
	 */
	///@Cacheable(cacheNames = {CHANNEL}, unless = "#result == null")
	@Select("select * from alipay_chanel_fee where channelId = #{channelId} and productId = #{feeType}")
	ChannelFee findImpl(@Param("channelId") String channelId, @Param("feeType") String feeType);

	//@Cacheable(cacheNames = {CHANNEL}, unless = "#result == null")
	@Select("select * from alipay_chanel_fee where channelId = #{channelId} and productId = #{productId}")
	ChannelFee findChannelFee(@Param("channelId") String channelId, @Param("productId") String productId);

}
