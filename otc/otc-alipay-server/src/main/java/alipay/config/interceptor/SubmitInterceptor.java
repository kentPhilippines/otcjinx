package alipay.config.interceptor;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

import alipay.config.annotion.LogMonitor;
import alipay.config.annotion.Submit;
import alipay.config.redis.RedisUtil;
import alipay.manage.bean.UserInfo;
import alipay.manage.util.SessionUtil;
import cn.hutool.core.util.ObjectUtil;
import otc.api.dealpay.Common;
import otc.common.RedisConstant;
@Component
public class SubmitInterceptor extends HandlerInterceptorAdapter {
	Logger log = LoggerFactory.getLogger(SubmitInterceptor.class);
	@Autowired SessionUtil sessionUtil;
	@Autowired RedisUtil redisUtil;
	@SuppressWarnings("null")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		  if (!(handler instanceof HandlerMethod)) 
	            return true;
	        HandlerMethod method = (HandlerMethod) handler;
	        boolean hasLoginAnnotation = method.getMethod().isAnnotationPresent(LogMonitor.class);//是否存在登录注解
	        boolean hasSubmit = method.getMethod().isAnnotationPresent(Submit.class);//是否存在登录注解
	        if (!hasLoginAnnotation) 
	            return true;
	        LogMonitor loginRequired = method.getMethod().getAnnotation(LogMonitor.class);//获取登录注解
	        if (!loginRequired.required()) //权限是否打开
	            return true;
	        UserInfo user = sessionUtil.getUser(request);
	        if (!ObjectUtil.isNull(user)) 
	            redisUtil.set(RedisConstant.User.LOGIN_PARENT + user.getUserId(), user.getUserId(), 60 * 5);
	        if (ObjectUtil.isNull(user)) {
	            response.sendRedirect("/login");
	            return false;
	        }
	        String name2 = method.getMethod().getName();
	        if (!hasSubmit) 
	            return true;
	        Submit annotation = method.getMethod().getAnnotation(Submit.class);//获取登录注解
	        if (!annotation.required()) //权限是否打开
	            return true;
	        String name = method.getMethod().getName();
	        log.info("【提交】"+name);
	        Object object = null;
	        if(redisUtil.hasKey(RedisConstant.User.SUBMIT_URL+name+user.getUserId())) 
	        	object = redisUtil.get(RedisConstant.User.SUBMIT_URL+name+user.getUserId());
	        if(ObjectUtil.isNotNull(object)) {
	        	Map<String, Object> result = new HashMap<>();
				result.put("success", false);
				result.put("message", "请不要频繁发起请求");
	        	ObjectMapper mapper = new ObjectMapper();
				PrintWriter writer = null;
				writer = response.getWriter();
				// 将map集合转换成json数据后写出
				mapper.writeValue(writer, result);
				writer.flush();
	        	return false;
	        }
	        redisUtil.set(RedisConstant.User.SUBMIT_URL+name+user.getUserId(), user.getUserId(), 120);
	        return true;
	}
	
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		super.postHandle(request, response, handler, modelAndView);
	}
	
}
