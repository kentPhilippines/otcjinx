package otc.api;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import otc.api.impl.AlipayServiceClienFeignHystrix;
import otc.bean.alipay.Medium;
import otc.common.PayApiConstant;

/**
 * <p>alipay数据服务【接口】</p>
 * @author K
 */
@FeignClient(value=PayApiConstant.Server.ALIPAY_SERVER, fallback = AlipayServiceClienFeignHystrix.class)
public interface AlipayServiceClienFeign {
	
	/**
	 * <p>查询当前可用的媒介账号</p>
	 * @param mediumType				媒介类型
	 * @param code						队列code
	 * @return
	 */
	@PostMapping(PayApiConstant.Alipay.MEDIUM_API+PayApiConstant.Alipay.FIND_MEDIUM_IS_DEAL)
	List<Medium> findIsDealMedium(@RequestParam("mediumType")String mediumType, @RequestParam("code")String code);
	
	/**
	 * <p>查询当前可用的媒介账号【所有】</p>
	 * @param mediumAlipay				媒介类型
	 * @return
	 */
	@PostMapping(PayApiConstant.Alipay.MEDIUM_API+PayApiConstant.Alipay.FIND_MEDIUM_IS_DEAL)
	List<Medium> findIsDealMedium(@RequestParam("mediumType")String mediumAlipay);
}