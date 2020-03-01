package alipay.manage.util;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alipay.manage.bean.util.AreaIp;
import alipay.manage.service.LogService;
@Component
public class LogUtil {
	@Autowired
	LogService	logServiceImpl;
	@Autowired
	IsDealIpUtil isDealIpUtil;
	/**
	 * <p>码商专用日志记录</p>
	 * @param request
	 * @return
	 */
	public boolean addLog(HttpServletRequest request , String msg,String userId) {
		AreaIp areaIp = null;
		try {
			areaIp = isDealIpUtil.getAreaIp(request);
		} catch (NumberFormatException | UnsupportedEncodingException e) {
		}
		boolean addLog = logServiceImpl.addLog(userId,areaIp,msg);
		return addLog;
	}
	
	
	
	
	

}
