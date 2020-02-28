package alipay.config.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import alipay.config.annotion.LogMonitor;
import cn.hutool.core.util.ObjectUtil;

@Component
public class MyInterceptor implements HandlerInterceptor {
    Logger log = LoggerFactory.getLogger(MyInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) 
            return true;
        HandlerMethod method = (HandlerMethod) handler;
        boolean hasLoginAnnotation = method.getMethod().isAnnotationPresent(LogMonitor.class);//是否存在登录注解
        if (!hasLoginAnnotation) 
            return true;
        LogMonitor loginRequired = method.getMethod().getAnnotation(LogMonitor.class);//获取登录注解
        if (!loginRequired.required()) //权限是否打开
            return true;
     //   if (ObjectUtil.isNull(user)) {
     //       response.sendRedirect("/login");
     //       return false;
   //     }
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object arg2, ModelAndView arg3)
            throws Exception {
        //	req.setAttribute("ctx", req.getContextPath());//前端全局变量
        resp.setContentType("text/html;charset=utf-8");
        resp.setCharacterEncoding("utf-8");
    //    log.info("MyInterceptor01--->postHandle()执行控制器之后且在渲染视图前调用此方法....");
    }
}