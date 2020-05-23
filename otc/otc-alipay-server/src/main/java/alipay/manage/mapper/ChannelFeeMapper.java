package alipay.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import alipay.manage.bean.ChannelFee;

@Mapper
public interface ChannelFeeMapper {
	/**
	 * <p>实现类查询</p>
	 * @param channelId
	 * @param feeType
	 * @return
	 */
	@Select("select * from alipay_chanel_fee where channelId = #{channelId} and productId = #{feeType}")
	ChannelFee findImpl(@Param("channelId") String channelId,@Param("feeType")  String feeType);

	@Select("select * from alipay_chanel_fee where channelId = #{channelId} and productId = #{productId}")
	ChannelFee findChannelFee(@Param("channelId") String channelId,@Param("productId")  String productId);
	
}
