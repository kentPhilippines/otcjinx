package alipay.config.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义异常处理
 */
@Component
public class ExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
     //判断请求类型，是否为异常请求
        String ajax_header=request.getHeader("X-Requested-With");
        if(ajax_header!=null && ajax_header.equals("XMLHttpRequest")){
          //当前请求是异步请求
          //对异步请求的异常进行处理
          response.setContentType("application/json;charset=UTF-8");
          response.setCharacterEncoding("UTF-8");
          //创建结果对象
          Map<String,Object> result=new HashMap<>();
          result.put("success",false);
          result.put("message",ex.getMessage());
          ObjectMapper mapper=new ObjectMapper();
          PrintWriter writer=null;
          try {
              writer=response.getWriter();
              //将map集合转换成json数据后写出
              mapper.writeValue(writer,result);
              writer.flush();
          } catch (IOException e) {
              e.printStackTrace();
          }finally {
              if (writer!=null)
                  writer.close();
          }
        }else{
            //当请求是普通请求
            //对普通请求的异常的进行处理
          ModelAndView modelAndView=new ModelAndView();
          modelAndView.addObject("ex",ex);
          modelAndView.setViewName("common/error");
          return modelAndView;
        }
        return null;
    }
}
