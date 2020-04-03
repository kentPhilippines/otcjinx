package otc.otc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import feign.Param;
import otc.bean.config.ManageConfig;
import otc.common.PayApiConstant;
import otc.otc.util.CacheConfigUtil;
import otc.result.Result;

@RestController
@RequestMapping(PayApiConstant.Config.CONFIG_API)
public class ConfigApi {
	Logger log= LoggerFactory.getLogger(ConfigApi.class);
	@Autowired CacheConfigUtil cacheConfigUtil;
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
	
	/**
	 * <p>根据系统和唯一键，获取配置数据</p>
	 * @param system				配置
	 * @param key					键
	 * @return
	 */
	@PostMapping(PayApiConstant.Config.CONFIG_API_GET_CONFIG_SYSTEM)
	public Result getconfig(  String system ,  String key) {
		if(StrUtil.isBlank(system) || StrUtil.isBlank(key))
			return Result.buildFailMessage("必传参数为空");
		Result getconfig = cacheConfigUtil.getconfig(system, key);
		ThreadUtil.execAsync(()->{
			log.info("【远程调用获取全局配置，当前获取配置服务："+system+"，当前获取配置键："+key+"，获取全局配置值为："+getconfig.getResult()+"】");
		});
		return getconfig;
	}
	
	
	
	
	
	
	
	
	

}
