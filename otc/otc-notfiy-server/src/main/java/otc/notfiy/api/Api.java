package otc.notfiy.api;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import otc.common.PayApiConstant;
import otc.notfiy.util.AndroidUtil;
import otc.notfiy.util.HeartUtil;
import otc.result.Result;

@RestController
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API)
public class Api {
	private static final Log log = LogFactory.get();
	@Autowired HeartUtil heartUtil;
	@Autowired AndroidUtil androidUtil;
	@PostMapping("/heartbeat")
	@ResponseBody
	public boolean testHeartbeat(HttpServletRequest request,@RequestBody Map<Object, Object> postjson) {
		String ipAddr = HttpUtil.getClientIP(request);
		log.info("ip:"+ipAddr);
		log.info("<<<<<进入心跳检测");
		return heartUtil.testHeartbeat(postjson);
	}
	
	
	@PostMapping("/heartbeat")
	public void notfiyPay(@RequestBody Map<Object, Object> postjson,HttpServletRequest request, HttpServletResponse response) {
		log.info("【接收到手机监听的回调信息】");
		try {
			androidUtil.http2(postjson, request, response);
		} catch (IOException e) {
			log.info("【回调发送异常】");
		}
	
	}
	
	
	
	

}
