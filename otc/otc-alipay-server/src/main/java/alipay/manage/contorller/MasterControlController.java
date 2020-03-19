package alipay.manage.contorller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import alipay.manage.api.configserver.ConfigServiceClientFeign;
import alipay.manage.bean.util.RegisterSetting;
import otc.bean.config.ConfigFile;
import otc.result.Result;

@Controller
@RequestMapping("/masterControl")
public class MasterControlController {
	@Autowired
	ConfigServiceClientFeign configServiceClientFeignImpl;
	private static Logger log= LoggerFactory.getLogger(MasterControlController.class);
	/**
	 * <p>获取网站标题</p>
	 * @return
	 */
//	@GetMapping("/getSystemSetting")
//	@ResponseBody
//	public Result getSystemSetting() {
//		Result config = configServiceClientFeignImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.TIBLE_LINK);
//		log.info("config配置：：：： " + config);
//		return Result.buildSuccessResult(config.getResult().toString());
//	}
	/**
	 * <p>获取页面配置</p>
	 * @return
	 */
	@GetMapping("/getRegisterSetting")
	@ResponseBody
	public Result getRegisterSetting() {
		RegisterSetting bean = new RegisterSetting();
		bean.setInviteCodeEffectiveDuration(50000000); 
		Result config = configServiceClientFeignImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.REGISTER_ENABLED);
		bean.setRegisterEnabled(config.getResult()!=null?true:false);//是否开放注册功能  config.getResult().toString().equals("1")?true:false
		bean.setInviteRegisterEnabled(false); 
		bean.setRegitserDefaultRebate(0.01);
		return Result.buildSuccessResult(bean);
	}
}