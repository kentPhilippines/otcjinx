package otc.otc.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.util.ObjectUtil;
import feign.Param;
import otc.bean.config.ManageConfig;
import otc.common.PayApiConstant;
import otc.result.Result;

@RestController
@RequestMapping(PayApiConstant.Config.CONFIG_API)
public class ConfigApi {
	/**
	 * <p>后台获取接口配置的接口</p>
	 * @param key			接口键
	 * @return
	 */
	@PostMapping(PayApiConstant.Config.CONFIG_API_GET_CONFIG_MANAGE)
	public Result getConfigAdmin(String key) {
		ManageConfig config = new ManageConfig();
		if(ObjectUtil.isNull(config))
			return Result.buildFailMessage("接口配置获取失败");
		return Result.buildSuccessResult(config);
	}

}
