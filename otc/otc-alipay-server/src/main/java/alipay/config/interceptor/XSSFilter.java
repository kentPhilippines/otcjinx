package alipay.config.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import otc.common.XssHttpServletRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class XSSFilter implements Filter {
    /**
     * 排除链接
     */
    @Value("#{'${xss.excludes}'.split(',')}")
    private List<String> excludes;

    @Value("${xss.enabled}")
    private Boolean enabled;


    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request;
        /**
         * 判断是否进行xss过滤
         */
        if (!handleExcludeUrl(req)){
            request = new XssHttpServletRequestWrapper((HttpServletRequest) req);
        }else {
            request = (HttpServletRequest) req;
        }

        chain.doFilter(request, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }
    @Override
    public void destroy() {
    }

    /**
     * 判断当前路径是否需要过滤
     */
    private boolean handleExcludeUrl(ServletRequest request) {
        if (!enabled) {
            return true;
        }
        if (excludes == null || excludes.isEmpty() || StringUtils.isEmpty(excludes.get(0))) {
            return false;
        }
        HttpServletRequest req = (HttpServletRequest)request;
        String url = req.getServletPath();
        for (String pattern : excludes) {
            Pattern p = Pattern.compile("^" + pattern);
            Matcher m = p.matcher(url);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }
}