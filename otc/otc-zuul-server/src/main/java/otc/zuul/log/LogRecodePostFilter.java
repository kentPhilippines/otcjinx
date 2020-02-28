package otc.zuul.log;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import cn.hutool.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
 
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;
@Component
public class LogRecodePostFilter extends ZuulFilter { 
	Logger log = LoggerFactory.getLogger(LogRecodePostFilter.class);
	@Override
    public String filterType() {
    //fiterType,有pre、route、post、error四种类型，分别代表路由前、路由时
    //路由后以及异常时的过滤器
    return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
	    //排序，指定相同filterType时的执行顺序，越小越优先执行，这里指定顺序为路由过滤器的顺序-1，即在路由前执行
	      return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
	}
	
	@Override
	public boolean shouldFilter() {
	    return   false;
	}
	
	@Override
	public Object run() throws ZuulException {
	    return null;
    }


}
