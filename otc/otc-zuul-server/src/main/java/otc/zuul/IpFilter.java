package otc.zuul;

import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
@Component
public class IpFilter extends ZuulFilter {
    private List<String> blackIpList = Arrays.asList("127.0.0.1");
    private static final Log log = LogFactory.get();
    public IpFilter() {
        super();
    }

    @Override
    public String filterType() {
        /**
         * pre：可以在请求被路由之前调用
         * route：在路由请求时候被调用
         * post：在route和error过滤器之后被调用
         * error：处理请求时发生错误时被调用
         */
        return "pre";
    }

    @Override
    public int filterOrder() {
        /**
         * 拦截器优先级,数值越小级别越高
         */
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        //判断过滤器是否生效
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String ip = HttpUtil.getClientIP(ctx.getRequest());
        // 在黑名单中禁用
        log.info("当前请求ip："+ip+"，当前ip访问的目标方法："+ctx.getRequest().getRequestURL());
       /* if (StringUtils.isNotBlank(ip) && !blackIpList.contains(ip)) {
            ctx.setSendZuulResponse(false);*//*拦截请求*//*
            ctx.setResponseBody(Result.buildFailMessage("非法ip").toJson() );//提示内容
            ctx.getResponse().setContentType("application/json; charset=utf-8");
            return null;
        }*/
        return null;
    }
}
