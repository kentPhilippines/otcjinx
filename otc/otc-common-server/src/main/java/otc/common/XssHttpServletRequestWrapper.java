package otc.common;



import com.google.common.net.HttpHeaders;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerMapping;
import otc.util.XssUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;


public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * 对header处理
     * @param name
     * @return
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return XssUtil.cleanXSS(value);
    }

    /**
     * 对参数处理
     * @param name
     * @return
     */
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return XssUtil.cleanXSS(value);
    }

    /**
     * 对数值进行处理
     * @param name
     * @return
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapseValues = new String[length];
            for (int i = 0; i < length; i++) {
                escapseValues[i] = XssUtil.cleanXSS(values[i]);
            }
            return escapseValues;
        }
        return super.getParameterValues(name);
    }

    /**
     * 主要是针对HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE 获取pathvalue的时候把原来的pathvalue经过xss过滤掉
     */
    @Override
    public Object getAttribute(String name) {
        // 获取pathvalue的值
        if (HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE.equals(name)) {
            Map uriTemplateVars = (Map) super.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            if (Objects.isNull(uriTemplateVars)) {
                return uriTemplateVars;
            }
            Map newMap = new LinkedHashMap<>();
            uriTemplateVars.forEach((key, value) -> {
                if (value instanceof String) {
                    newMap.put(key, XssUtil.cleanXSS((String) value));
                } else {
                    newMap.put(key, value);

                }
            });
            return newMap;
        } else {
            return super.getAttribute(name);
        }
    }

    /**
     * 对json处理
     * @return
     * @throws IOException
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        // 非json类型，直接返回
        if (!isJsonRequest()) {
            return super.getInputStream();
        }

        List<String> lines = IOUtils.readLines(super.getInputStream(), UTF_8);
        String json = String.join("\n", lines);

        // 为空，直接返回
        if (StringUtils.isBlank(json)) {
            return super.getInputStream();
        }

        // xss过滤
        json = XssUtil.cleanXSS(json);
        final ByteArrayInputStream bis = new ByteArrayInputStream(json.getBytes(UTF_8));
        return new ServletInputStream() {
            @Override
            public boolean isFinished(){
                return true;
            }

            @Override
            public boolean isReady(){
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read(){
                return bis.read();
            }
        };
    }

    /**
     * 是否是Json请求
     */
    private boolean isJsonRequest() {
        String header = super.getHeader(HttpHeaders.CONTENT_TYPE);
        return MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(header) || MediaType.APPLICATION_JSON_UTF8_VALUE.equalsIgnoreCase(header);
    }
}