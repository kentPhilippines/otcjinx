package otc.notfiy.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.http.HttpUtil;
import otc.common.PayApiConstant;
import otc.notfiy.util.HeartUtil;

@RestController
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API)
public class Api {
	Logger log = LoggerFactory.getLogger(Api.class);
	@Autowired HeartUtil heartUtil;
	@PostMapping("/heartbeat")
	@ResponseBody
	public boolean testHeartbeat(HttpServletRequest request,@RequestBody Map<Object, Object> postjson) {
		String ipAddr = HttpUtil.getClientIP(request);
		log.info("ip:"+ipAddr);
		log.info("<<<<<进入心跳检测");
		return heartUtil.testHeartbeat(postjson);
	}
	
	
	
	
	
	
	
	

}
